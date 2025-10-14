package net.emsee.thedungeon.dungeon.src.generators;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.src.GlobalDungeonManager;
import net.emsee.thedungeon.dungeon.src.connectionRules.FailRule;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.DungeonUtils;
import net.emsee.thedungeon.dungeon.src.types.grid.GridDungeonInstance;
import net.emsee.thedungeon.dungeon.src.types.grid.array.GridArray;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GridRoomEmpty;
import net.emsee.thedungeon.dungeon.src.types.grid.GridDungeon;
import net.emsee.thedungeon.dungeon.src.connectionRules.ConnectionRule;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GeneratedRoom;
import net.emsee.thedungeon.dungeon.src.types.grid.room.AbstractGridRoom;
import net.emsee.thedungeon.dungeon.src.types.roomCollection.GridRoomCollectionInstance;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;

import java.util.*;

public class GridDungeonGenerator extends DungeonGenerator<GridDungeon> {
    public enum GenerationTask {
        UN_STARTED,
        CALCULATING,
        CHECK_REQUIREMENTS,
        FILLING_UNOCCUPIED,
        VERIFY_FAIL_RULES,
        PLACING_ROOMS,
        PLACING_FAIL_RULES,
        POST_PROCESSING_ROOMS,
        FILLING_WITH_MOBS,
        DONE
    }

    private GenerationTask currentTask = GenerationTask.UN_STARTED;

    private final long seed;
    private final Random random;
    private final GridArray occupationArray;
    private final GridDungeonInstance dungeon;
    private final GridRoomCollectionInstance collection;
    private final Queue<GeneratedRoom> todoRooms = new LinkedList<>();
    private final Queue<GeneratedRoom> toPlaceRooms = new LinkedList<>();
    private final Queue<FailRule.Instance> toPlaceFailRules = new LinkedList<>();
    private final Queue<GeneratedRoom> toPostProcessRooms = new LinkedList<>();
    private final Queue<GeneratedRoom> toSpawnMobsRooms = new LinkedList<>();


    public List<ConnectionRule> getConnectionRules() {
        return new ArrayList<>(dungeon.getRoomCollection().getRaw().getConnectionRules());
    }

    private void addRoomToLists(GeneratedRoom room) {
        todoRooms.add(room);
        toPlaceRooms.add(room);
        if (room.hasMobSpawns())
            toSpawnMobsRooms.add(room);
        if (room.hasPostProcessing() || dungeon.getRoomCollection().getRaw().hasPostProcessing())
            toPostProcessRooms.add(room);
    }

    public GridDungeonGenerator(GridDungeonInstance dungeon, long seed) {
        this.seed = seed;
        this.random = new Random(seed);
        this.dungeon = dungeon;
        this.collection = dungeon.getRoomCollection();
        this.occupationArray = new GridArray(dungeon.getRaw().getDungeonDepth(), dungeon.getRaw().getMaxFloorHeight(), !dungeon.getRaw().isDownGenerationDisabled());

        // select the starting room
        AbstractGridRoom startingRoom = dungeon.getRaw().getStaringRoom();
        if (startingRoom == null)
            startingRoom = dungeon.getRoomCollection().getRandomRoom(random);
        if (startingRoom == null)
            throw new IllegalStateException("error finding dungeon starting room");

        // create a GeneratedRoom from the room
        GeneratedRoom generatedStartingRoom = GeneratedRoom.createInstance(startingRoom, this, new Vec3i(0,0,0), dungeon.getRaw().getCenterPos(), DungeonUtils.getRandomRotation(random), random);

        // add the room to all the lists
        collection.updatePlacedRequirements(startingRoom);
        addRoomToLists(generatedStartingRoom);
    }



    @Override
    public void step(ServerLevel serverLevel) {
        if (currentTask == GenerationTask.UN_STARTED) {
            currentTask = GenerationTask.CALCULATING;
            GlobalDungeonManager.killAllInDungeon(serverLevel.getServer(), dungeon.getRank());
        } else if (currentTask == GenerationTask.CALCULATING) calculationStep(serverLevel.getServer());
        else if (currentTask == GenerationTask.CHECK_REQUIREMENTS) checkRequirementStep(serverLevel.getServer());
        else if (currentTask == GenerationTask.FILLING_UNOCCUPIED) fillUnoccupiedStep(serverLevel.getServer());
        else if (currentTask == GenerationTask.VERIFY_FAIL_RULES) verifyFailRulesStep();
        else if (currentTask == GenerationTask.PLACING_ROOMS) placeRoomStep(serverLevel);
        else if (currentTask == GenerationTask.PLACING_FAIL_RULES) placeFailRuleStep(serverLevel);
        else if (currentTask == GenerationTask.POST_PROCESSING_ROOMS) postProcessRoomsStep(serverLevel);
        else if (currentTask == GenerationTask.FILLING_WITH_MOBS) fillWithMobsStep(serverLevel);
    }

    private void calculationStep(MinecraftServer server) {
        for (int i = 0; i < GameruleRegistry.getIntegerGamerule(server, ModGamerules.CALCULATOR_STEPS_PER_TICK); i++) {
            if (todoRooms.isEmpty()) {
                // if done start next task
                currentTask = GenerationTask.CHECK_REQUIREMENTS;
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"calculation complete, checking requirements:");
                return;
            }

            List<GeneratedRoom> nextGenerationOptions = getPossibleRoomSelection();

            final GeneratedRoom roomToDo;

            // selects a room to generate its connection according to the dungeon rules
            switch (dungeon.getRaw().getRoomPickMethod()) {
                case LAST -> roomToDo = nextGenerationOptions.getLast();
                case RANDOM -> roomToDo = ListAndArrayUtils.getRandomFromList(nextGenerationOptions, random);
                default -> roomToDo = nextGenerationOptions.getFirst();
            }
            if (roomToDo == null)
                throw new IllegalStateException("the room to generate its next connection was NULL");

            // starts calculating the next placement
            GeneratedRoom newRoom = roomToDo.generateConnection(this, random);
            if (roomToDo.generatedAllConnections())
                todoRooms.remove(roomToDo);
            if (newRoom != null) {
                // if placement was success add the room to all required lists
                collection.updatePlacedRequirements(newRoom.getRoom());
                addRoomToLists(newRoom);
            }
        }
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS,"Dungeon calculation tick completed");
    }

    /**
     * gets all rooms that are allowed to generate its connections based on priority
     */
    private List<GeneratedRoom> getPossibleRoomSelection() {
        List<GeneratedRoom> toReturn = new ArrayList<>();
        int maxPriority = 0;
        for (GeneratedRoom generatedRoom : todoRooms) {
            if (generatedRoom.getPriority() == maxPriority)
                toReturn.add(generatedRoom);
            else if (generatedRoom.getPriority() > maxPriority) {
                maxPriority = generatedRoom.getPriority();
                toReturn = new ArrayList<>();
                toReturn.add(generatedRoom);
            }
        }
        return toReturn;
    }

    private void checkRequirementStep(MinecraftServer server) {
        if (!collection.requiredRoomsDone()) {
            // if not all rooms were generated, discard this generator and start a new one with seed+1
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Not All Required Rooms where generated, regenerating with Seed+1");
            dungeon.generateSeeded(GetSeed() + 1);
            return;
        }
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"All Required Rooms where generated");
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Filling Unoccupied Placements");

        // start the next task
        currentTask = GenerationTask.FILLING_UNOCCUPIED;
    }

    private void fillUnoccupiedStep(MinecraftServer server) {
        if (!dungeon.getRaw().shouldFillWithFallback()) {
            // if the dungeon has this setting disabled, start the next task
            currentTask = GenerationTask.VERIFY_FAIL_RULES;
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Fallback Fill Disabled, Starting Fail Rule Verification");
            return;
        }
        for (int i = 0; i < GameruleRegistry.getIntegerGamerule(server, ModGamerules.CALCULATOR_STEPS_PER_TICK); i++) {
            if (FillUnoccupied()) {
                // if all placements have been calculated, start the next step
                currentTask = GenerationTask.VERIFY_FAIL_RULES;
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Fallback Placing Finished, Starting Fail Rule Verification");
            }
        }
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS,"Fallback Placing Tick Complete");
    }

    /**
     * fills the dungeon with fallback
     * returns true if done
     */
    //TODO remake
    private boolean FillUnoccupied() {
        return true;
    }

    private void verifyFailRulesStep() {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "Verifying Fail Rules");
        Queue<FailRule.Instance> newFailRules = new LinkedList<>();
        toPlaceFailRules.forEach(instance -> {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "Verifying rule: {}", instance);
            if (instance.verifyPlacement(this)) {
                newFailRules.add(instance);
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "Verifying success");
            }
            else DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "Verifying failure, removing");
        });
        toPlaceFailRules.clear();
        toPlaceFailRules.addAll(newFailRules);
        currentTask = GenerationTask.PLACING_ROOMS;
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "Fail Rules Verified, Starting Room Placement");
    }

    private void placeRoomStep(ServerLevel serverLevel) {
        for (int i = 0; i < GameruleRegistry.getIntegerGamerule(serverLevel.getServer(), ModGamerules.PLACER_STEPS_PER_TICK); i++) {
            if (toPlaceRooms.isEmpty()) {
                // if done start next task
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "All Rooms Placed, Fail Rule Placement");
                currentTask = GenerationTask.PLACING_FAIL_RULES;
                return;
            }
            // get the object to place in the world
            GeneratedRoom toPlace = toPlaceRooms.peek();
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "to Place:" + toPlace);

            // place it down
            toPlace.finalizePlacement(serverLevel, collection.getRaw().getStructureProcessors(), random);
            toPlaceRooms.remove();
            if (toPlace.getRoom() instanceof GridRoomEmpty)
                i--;

        }
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS,"Room place tick complete");
    }

    private void placeFailRuleStep(ServerLevel serverLevel) {
        for (int i = 0; i < GameruleRegistry.getIntegerGamerule(serverLevel.getServer(), ModGamerules.PLACER_STEPS_PER_TICK); i++) {
            if (toPlaceFailRules.isEmpty()) {
                // if done start next task
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "All Fail Rules Placed, Starting Post Processing");
                currentTask = GenerationTask.POST_PROCESSING_ROOMS;
                return;
            }
            FailRule.Instance toPlace = toPlaceFailRules.peek();
            // place it tick it end check if it's done
            toPlace.finalizeTick(serverLevel, collection.getRaw().getStructureProcessors());
            if (toPlace.isFinished())
                toPlaceFailRules.remove();
        }
    }

    private void postProcessRoomsStep(ServerLevel serverLevel) {
        for (int i = 0; i < GameruleRegistry.getIntegerGamerule(serverLevel.getServer(), ModGamerules.POST_PROCESSOR_STEPS_PER_TICK); i++) {
            if (toPostProcessRooms.isEmpty()) {
                // if done start next task
                GlobalDungeonManager.killAllInDungeon(serverLevel.getServer(), dungeon.getRank());
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "All Rooms Post Processed, Starting Mob Spawning");
                currentTask = GenerationTask.FILLING_WITH_MOBS;
                return;
            }
            GeneratedRoom toProcess = toPostProcessRooms.remove();
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS,"to Process:" + toProcess);
            toProcess.finalizePostProcessing(serverLevel, collection.getRaw().getStructurePostProcessors(), random);
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS,"Post Process tick complete");
        }
    }

    private void fillWithMobsStep(ServerLevel serverLevel) {
        for (int i = 0; i < GameruleRegistry.getIntegerGamerule(serverLevel.getServer(), ModGamerules.SPAWNER_STEPS_PER_TICK); i++) {
            if (toSpawnMobsRooms.isEmpty()) {
                // if all mobs spawned, start the next task
                currentTask = GenerationTask.DONE;
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Mob Spawning Complete");
                return;
            }
            // spawn the mobs
            GeneratedRoom toSpawnMobs = toSpawnMobsRooms.remove();
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS,"Spawning Mobs for {}", toSpawnMobs);
            toSpawnMobs.spawnMobs(serverLevel);
        }
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS,"Mob spawn tick complete");
    }


    public void RoomConnectionFail(String tag, GeneratedRoom room, Connection from, boolean placeFallback, boolean exitObstructed) {
        boolean doPlaceFallback = placeFallback;
        for (FailRule failRule : collection.getRaw().getFailRules()) {
            if (failRule.match(tag, from)) {
                toPlaceFailRules.add(new FailRule.Instance(failRule,room,from,placeFallback,exitObstructed));
                if (failRule.stopFallbackPlacement())
                    doPlaceFallback=false;
                break;
            }
        }
        if (doPlaceFallback) {
            Vec3i offset = room.getPlacementConnectionArrayOffset(from);
            if (offset!= null) {
                Vec3i pos = room.getPlacedArrayPos().offset(offset);
                PlaceFallbackAt(pos, room.getPlacedWorldPos().offset(new Vec3i(offset.getX() * collection.getRaw().getGridCellWidth(), offset.getY() * collection.getRaw().getGridCellHeight(), offset.getZ() * collection.getRaw().getGridCellWidth())));
            }
        }
    }

    public void PlaceFallbackAt(Vec3i arrayPos, BlockPos worldPos) {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS,"placing fallback");
        if (collection.getRaw().getFallback()==null) {return;}
        GeneratedRoom fallback = GeneratedRoom.createInstance(collection.getRaw().getFallback(), this, arrayPos, worldPos, Rotation.NONE, true, true, false, random);
        if (fallback == null) {
            DebugLog.logError(DebugLog.DebugType.WARNINGS, "Error placing Fallback");
            return;
        }
        toPlaceRooms.add(fallback);
        if (fallback.hasPostProcessing() || collection.getRaw().hasPostProcessing())
            toPostProcessRooms.add(fallback);
        //return fallback;
    }

    public GridArray getOccupationArray() {
        return occupationArray;
    }

    public GridDungeonInstance getDungeon() {
        return dungeon;
    }

    public AbstractGridRoom GetRandomRoomByConnection(Connection connection, String fromTag, Random random) {
        return collection.getRandomRoomByConnection(connection, fromTag, collection.getRaw().getConnectionRules(), random);
    }

    public boolean isDone() {
        return currentTask == GenerationTask.DONE;
    }

    public long GetSeed() {
        return seed;
    }

    public GenerationTask currentStep() {
        return currentTask;
    }
}
