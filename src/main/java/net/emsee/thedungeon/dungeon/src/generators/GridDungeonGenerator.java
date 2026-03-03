package net.emsee.thedungeon.dungeon.src.generators;

import net.emsee.thedungeon.Config;
import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.registry.DungeonBiome;
import net.emsee.thedungeon.dungeon.src.Biome.GridDungeonBiomeRegistry;
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
import net.emsee.thedungeon.dungeon.src.types.grid.roomCollection.GridRoomCollectionInstance;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.worldSaveData.DungeonSaveData;
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
        REGISTERING_PORTAL_LOCATIONS,
        SAVING_ADDITIONAL,
        DONE
    }

    protected GenerationTask currentTask = GenerationTask.UN_STARTED;

    protected final long seed;
    protected final Random random;
    protected final GridArray occupationArray;
    protected final GridDungeonInstance dungeon;
    protected final GridDungeonBiomeRegistry biomeRegistry;
    protected final GridRoomCollectionInstance collection;
    protected final Queue<GeneratedRoom> todoRooms = new LinkedList<>();
    protected final Queue<GeneratedRoom> toPlaceRooms = new LinkedList<>();
    protected final Queue<FailRule.Instance> toPlaceFailRules = new LinkedList<>();
    protected final Queue<GeneratedRoom> toPostProcessRooms = new LinkedList<>();
    protected final Queue<GeneratedRoom> toSpawnMobsRooms = new LinkedList<>();
    protected final Queue<GeneratedRoom> toRegisterPortals = new LinkedList<>();

    protected final ServerLevel cashedServerLevel;

    protected int requirementStepInterval = 0;
    protected final int STEP_INTERVAL_BETWEEN_FORCED_REQUIREMENT_TRIES;


    public List<ConnectionRule> getConnectionRules() {
        return new ArrayList<>(dungeon.getRoomCollection().getRaw().getConnectionRules());
    }

    protected void addRoomToLists(GeneratedRoom room) {
        todoRooms.add(room);
        toPlaceRooms.add(room);
        if (room.hasMobSpawns())
            toSpawnMobsRooms.add(room);
        if (room.hasPostProcessing() || dungeon.getRoomCollection().getRaw().hasPostProcessing())
            toPostProcessRooms.add(room);
        if (room.hasPortalPosition()) {
            toRegisterPortals.add(room);
        }
    }

    public GridDungeonGenerator(GridDungeonInstance dungeon, long seed, ServerLevel serverLevel) {
        this.seed = seed;
        this.random = new Random(seed);
        this.dungeon = dungeon;
        this.collection = dungeon.getRoomCollection();
        this.occupationArray = new GridArray(dungeon.getRaw().getDungeonDepth(), dungeon.getRaw().getMaxFloorHeight(), !dungeon.getRaw().isDownGenerationDisabled());
        this.biomeRegistry = new GridDungeonBiomeRegistry(collection.getRaw().getGridCellWidth(),collection.getRaw().getGridCellHeight(), dungeon.getRaw().getCenterPos());
        this.STEP_INTERVAL_BETWEEN_FORCED_REQUIREMENT_TRIES = dungeon.getRaw().getStepIntervalBetweenForcesRequirementTries();

        this.cashedServerLevel = serverLevel;

        // select the starting room
        AbstractGridRoom startingRoom = dungeon.getRaw().getStartingRoom();
        if (startingRoom == null)
            startingRoom = dungeon.getRoomCollection().getRandomRoom(random, new AbstractGridRoom.PlacementPredicateData(serverLevel, dungeon.getRaw().getCenterPos(), this));
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
        else if (currentTask == GenerationTask.REGISTERING_PORTAL_LOCATIONS) registerPortalLocations(serverLevel);
        else if (currentTask == GenerationTask.SAVING_ADDITIONAL) saveAdditional(serverLevel);
    }

    protected void calculationStep(MinecraftServer server) {
        for (int i = 0; i < Config.CALCULATOR_STEPS_PER_TICK.getAsInt(); i++) {
            if (todoRooms.isEmpty()) {
                // if done start next task
                currentTask = GenerationTask.CHECK_REQUIREMENTS;
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"calculation complete, checking requirements:");
                return;
            }

            List<GeneratedRoom> nextGenerationOptions = getPossibleRoomSelection();

            // selects a room to generate its connection according to the dungeon rules
            final GeneratedRoom roomToDo = switch (dungeon.getRaw().getRoomPickMethod()) {
                case LAST -> nextGenerationOptions.getLast();
                case RANDOM -> ListAndArrayUtils.getRandomFromList(nextGenerationOptions, random);
                default -> nextGenerationOptions.getFirst();
            };
            if (roomToDo == null)
                throw new IllegalStateException("the room to generate its next connection was NULL");

            // starts calculating the next placement
            GeneratedRoom newRoom = roomToDo.generateConnection(this, random);
            if (roomToDo.generatedAllConnections())
                todoRooms.remove(roomToDo);
            if (newRoom != null) {
                // if placement was success add the room to all required lists
                boolean wasRequirement = collection.updatePlacedRequirements(newRoom.getRoom());
                if (wasRequirement) {
                    requirementStepInterval=0;
                }
                addRoomToLists(newRoom);
            }
            requirementStepInterval++;
        }
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS,"Dungeon calculation tick completed");
    }

    /**
     * gets all rooms that are allowed to generate its connections based on priority
     */
    protected List<GeneratedRoom> getPossibleRoomSelection() {
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


    protected void checkRequirementStep(MinecraftServer server) {
        if (!collection.requiredRoomsDone()) {
            // if not all rooms were generated, discard this generator and start a new one with seed+1
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Not All Required Rooms where generated, regenerating with Seed+1");
            dungeon.generateSeeded(server,GetSeed() + 1);
            return;
        }
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"All Required Rooms where generated");
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Filling Unoccupied Placements");

        // start the next task
        currentTask = GenerationTask.FILLING_UNOCCUPIED;
    }

    protected void fillUnoccupiedStep(MinecraftServer server) {
        if (!dungeon.getRaw().shouldFillWithFallback()) {
            // if the dungeon has this setting disabled, start the next task
            currentTask = GenerationTask.VERIFY_FAIL_RULES;
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Fallback Fill Disabled, Starting Fail Rule Verification");
            return;
        }
        for (int i = 0; i < Config.CALCULATOR_STEPS_PER_TICK.getAsInt(); i++) {
            if (FillUnoccupied()) {
                // if all placements have been calculated, start the next step
                currentTask = GenerationTask.VERIFY_FAIL_RULES;
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Fallback Placing Finished, Starting Fail Rule Verification");
            }
        }
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS,"Fallback Placing Tick Complete");
    }

    protected int lastFallbackFillX = 0;
    protected int lastFallbackFillY = 0;
    protected int lastFallbackFillZ = 0;

    /**
     * fills the dungeon with fallback
     * returns true if done
     */
    protected boolean FillUnoccupied() {
        int depth = dungeon.getRaw().getDungeonDepth();
        int maxFloorHeight = dungeon.getRaw().getMaxFloorHeightFromCenterOffset()+1;
        int gridCellWidth = collection.getRaw().getGridCellWidth();
        int gridCellHeight = collection.getRaw().getGridCellHeight();
        BlockPos centerPos = dungeon.getRaw().getCenterPos();

        // Determine Y bounds based on whether down generation is disabled
        int minY = dungeon.getRaw().isDownGenerationDisabled() ? 0 : -Math.min(depth, maxFloorHeight);
        int maxY = Math.min(depth, maxFloorHeight);

        // Resume from last position
        for (int x = lastFallbackFillX - depth; x <= depth; x++) {
            for (int y = (x == lastFallbackFillX - depth ? lastFallbackFillY : minY); y <= maxY; y++) {
                for (int z = (x == lastFallbackFillX - depth && y == lastFallbackFillY ? lastFallbackFillZ : -depth); z <= depth; z++) {
                    Vec3i arrayPos = new Vec3i(x, y, z);

                    // Check if cell is inside grid bounds and empty
                    if (occupationArray.isInsideGrid(arrayPos, true) &&
                            occupationArray.isCellEmptyAt(arrayPos)) {

                        // Calculate world position
                        BlockPos worldPos = centerPos.offset(
                                x * gridCellWidth,
                                y * gridCellHeight,
                                z * gridCellWidth
                        );

                        // Place fallback at this position
                        PlaceFallbackAt(arrayPos, worldPos);

                        // Save progress and return false to continue next tick
                        lastFallbackFillX = x + depth;
                        lastFallbackFillY = y;
                        lastFallbackFillZ = z;
                        return false;
                    }
                }
                lastFallbackFillZ = -depth;
            }
            lastFallbackFillY = minY;
        }

        // Reset progress counters and return true when done
        lastFallbackFillX = 0;
        lastFallbackFillY = 0;
        lastFallbackFillZ = 0;
        return true;
    }

    protected void verifyFailRulesStep() {
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

    protected void placeRoomStep(ServerLevel serverLevel) {
        for (int i = 0; i < Config.PLACER_STEPS_PER_TICK.getAsInt(); i++) {
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

    protected void placeFailRuleStep(ServerLevel serverLevel) {
        for (int i = 0; i < Config.PLACER_STEPS_PER_TICK.getAsInt(); i++) {
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

    protected void postProcessRoomsStep(ServerLevel serverLevel) {
        for (int i = 0; i < Config.POST_PROCESSOR_STEPS_PER_TICK.getAsInt(); i++) {
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

    protected void fillWithMobsStep(ServerLevel serverLevel) {
        for (int i = 0; i < Config.SPAWNER_STEPS_PER_TICK.getAsInt(); i++) {
            if (toSpawnMobsRooms.isEmpty()) {
                // if all mobs spawned, start the next task
                currentTask = GenerationTask.REGISTERING_PORTAL_LOCATIONS;
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

    protected void registerPortalLocations(ServerLevel level) {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS,"Registering Portal Locations");

        while (!toRegisterPortals.isEmpty()) {
            GeneratedRoom toRegisterPortal = toRegisterPortals.remove();
            toRegisterPortal.registerPortal(level.getServer(), dungeon.getRank());
        }

        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS,"Registered Portal Locations");
        currentTask = GenerationTask.SAVING_ADDITIONAL;
    }

    protected void saveAdditional(ServerLevel serverLevel) {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Saving Additional Data");

        DungeonSaveData saveData = DungeonSaveData.Get(serverLevel.getServer());

        saveData.setBiomeRegistry(dungeon.getRank(), biomeRegistry);

        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Saving Complete");
        currentTask = GenerationTask.DONE;
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

    public void setBiomeAt(Vec3i cellPos, DungeonBiome biome) {
        biomeRegistry.setBiomeAt(cellPos, biome);
    }

    public void setBiomeAt(Vec3i from, Vec3i to, DungeonBiome biome) {
        for (int y = from.getY(); y <= to.getY(); y++) {
            for (int x = from.getX(); x <= to.getX(); x++) {
                for (int z = from.getZ(); z <= to.getZ(); z++) {
                    setBiomeAt(new Vec3i(x,y,z), biome);
                }
            }
        }
    }

    public GridArray getOccupationArray() {
        return occupationArray;
    }

    public GridDungeonInstance getDungeon() {
        return dungeon;
    }

    public AbstractGridRoom GetRandomRoomByConnection(Connection connection, String fromTag, Random random, Vec3i originArrayPos) {
        return collection.getRandomRoomByConnection(connection, fromTag, collection.getRaw().getConnectionRules(), random, new AbstractGridRoom.PlacementPredicateData(cashedServerLevel, originArrayPos, this));
    }

    public AbstractGridRoom GetRandomRequiredRoomByConnection(Connection connection, String fromTag, Random random, Vec3i originArrayPos) {
        return collection.getRandomRequiredRoomByConnection(connection, fromTag, collection.getRaw().getConnectionRules(), random, new AbstractGridRoom.PlacementPredicateData(cashedServerLevel, originArrayPos, this));
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

    public boolean hasToTryRequiredRoom() {
        if (STEP_INTERVAL_BETWEEN_FORCED_REQUIREMENT_TRIES <= 0) return false;
        return requirementStepInterval >= STEP_INTERVAL_BETWEEN_FORCED_REQUIREMENT_TRIES;
    }
}
