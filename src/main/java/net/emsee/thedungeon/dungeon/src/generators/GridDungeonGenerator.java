package net.emsee.thedungeon.dungeon.src.generators;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.src.GlobalDungeonManager;
import net.emsee.thedungeon.dungeon.src.connectionRules.FailRule;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.DungeonUtils;
import net.emsee.thedungeon.dungeon.src.types.GridDungeon;
import net.emsee.thedungeon.dungeon.src.connectionRules.ConnectionRule;
import net.emsee.thedungeon.dungeon.src.room.GeneratedRoom;
import net.emsee.thedungeon.dungeon.src.room.AbstractGridRoom;
import net.emsee.thedungeon.dungeon.src.GridRoomCollection;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;

import java.util.*;

public class GridDungeonGenerator {


    public enum GenerationTask {
        UN_STARTED,
        CALCULATING,
        CHECK_REQUIREMENTS,
        FILLING_UNOCCUPIED,
        PLACING_ROOMS,
        POST_PROCESSING_ROOMS,
        FILLING_WITH_MOBS,
        DONE
    }

    private GenerationTask currentTask = GenerationTask.UN_STARTED;

    private long seed;
    private final Random random;
    private final Occupation[][][] occupationArray;
    private final GridDungeon dungeon;
    private final GridRoomCollection collection;
    private final BlockPos placedCenterPos;
    private final Queue<GeneratedRoom> todoRooms = new LinkedList<>();
    private final Queue<Object> toPlaceInstances = new LinkedList<>();
    private final Queue<GeneratedRoom> toPostProcessRooms = new LinkedList<>();
    private final Queue<GeneratedRoom> toSpawnMobsRooms = new LinkedList<>();

    private int lastFallbackFillX = 0;
    private int lastFallbackFillY = 0;
    private int lastFallbackFillZ = 0;


    public List<ConnectionRule> getConnectionRules() {
        return new ArrayList<>(collection.getConnectionRules());
    }

    public enum Occupation {
        AVAILABLE,
        OCCUPIED,
    }

    public GridDungeonGenerator(GridDungeon dungeon, long seed) {
        this(dungeon, new Random(seed));
        this.seed = seed;
    }

    private void addRoomToLists(GeneratedRoom room) {
        todoRooms.add(room);
        toPlaceInstances.add(room);
        if (room.hasMobSpawns())
            toSpawnMobsRooms.add(room);
        if (room.hasPostProcessing())
            toPostProcessRooms.add(room);
    }

    private GridDungeonGenerator(GridDungeon dungeon, Random random) {
        this.random = random;
        this.dungeon = dungeon;
        this.collection = dungeon.getRoomCollection();
        this.placedCenterPos = dungeon.getCenterPos();
        int listCentreOffset = dungeon.getDungeonDepth();
        int arraySize = (dungeon.getDungeonDepth() * 2) + 1;
        this.occupationArray = new Occupation[arraySize][arraySize][arraySize];

        for (Occupation[][] occupationArrayRow : this.occupationArray) {
            for (Occupation[] occupationArrayCol : occupationArrayRow) {
                Arrays.fill(occupationArrayCol, Occupation.AVAILABLE);
            }
        }

        // select the starting room
        AbstractGridRoom startingRoom = dungeon.getStaringRoom();
        if (startingRoom == null)
            startingRoom = dungeon.getRoomCollection().getRandomRoom(random);
        if (startingRoom == null)
            throw new IllegalStateException("error finding dungeon starting room");

        // create a GeneratedRoom from the room
        GeneratedRoom generatedStartingRoom = GeneratedRoom.createInstance(startingRoom, this, listCentreOffset, listCentreOffset, listCentreOffset, placedCenterPos, DungeonUtils.getRandomRotation(random), random);

        // add the room to all the lists
        collection.updatePlacedRequirements(startingRoom);
        addRoomToLists(generatedStartingRoom);
    }


    /**
     * called each tick to generate the dungeon
     */
    public void step(ServerLevel serverLevel) {
        if (currentTask == GenerationTask.UN_STARTED) {
            currentTask = GenerationTask.CALCULATING;
            GlobalDungeonManager.killAllInDungeon(serverLevel.getServer(), dungeon.getRank());
        } else if (currentTask == GenerationTask.CALCULATING) calculationStep(serverLevel.getServer());
        else if (currentTask == GenerationTask.CHECK_REQUIREMENTS) checkRequirementStep(serverLevel.getServer());
        else if (currentTask == GenerationTask.FILLING_UNOCCUPIED) fillUnoccupiedStep(serverLevel.getServer());
        else if (currentTask == GenerationTask.PLACING_ROOMS) placeRoomStep(serverLevel);
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
            switch (dungeon.getRoomPickMethod()) {
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
            if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.DUNGEON_CLEAN_ON_REGEN))
                GlobalDungeonManager.priorityCleanup(server, dungeon.getRank());
            dungeon.generateSeeded(GetSeed() + 1);
            return;
        }
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"All Required Rooms where generated");
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Filling Unoccupied Placements");

        // start the next task
        currentTask = GenerationTask.FILLING_UNOCCUPIED;
        lastFallbackFillY = Math.max(dungeon.getDungeonDepth() - dungeon.getMaxFloorHeightFromCenterOffset() - 1, 0);
    }

    private void fillUnoccupiedStep(MinecraftServer server) {
        if (!dungeon.shouldFillWithFallback()) {
            // if the dungeon has this setting disabled, start the next task
            currentTask = GenerationTask.PLACING_ROOMS;
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Fallback Fill Disabled, Starting Room Placement");
            return;
        }
        for (int i = 0; i < GameruleRegistry.getIntegerGamerule(server, ModGamerules.CALCULATOR_STEPS_PER_TICK); i++) {
            if (FillUnoccupied()) {
                // if all placements have been calculated, start the next step
                currentTask = GenerationTask.PLACING_ROOMS;
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"Fallback Placing Finished, Starting Room Placement");
            }
        }
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS,"Fallback Placing Tick Complete");
    }

    /**
     * fills the dungeon with fallback
     * returns true if done
     */
    private boolean FillUnoccupied() {
        for (int x = lastFallbackFillX; x < occupationArray.length; x++) {
            for (int y = lastFallbackFillY; y < Math.min(dungeon.getDungeonDepth() + dungeon.getMaxFloorHeightFromCenterOffset() + 2, occupationArray.length); y++) {
                for (int z = lastFallbackFillZ; z < occupationArray.length; z++) {
                    Occupation instance = occupationArray[x][y][z];
                    if (instance == Occupation.AVAILABLE) {
                        PlaceFallbackAt(x, y, z, placedCenterPos.offset(new Vec3i(dungeon.getGridCellWidth() * (x - dungeon.getDungeonDepth()), dungeon.getGridCellHeight() * (y - dungeon.getDungeonDepth()), dungeon.getGridCellWidth() * (z - dungeon.getDungeonDepth()))), Occupation.OCCUPIED);
                        lastFallbackFillX = x;
                        lastFallbackFillY = y;
                        lastFallbackFillZ = z;
                        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS,"Occupation Fill Tick Complete");
                        return false;
                    }
                }
                lastFallbackFillZ = 0;
            }
            lastFallbackFillY = Math.max(dungeon.getDungeonDepth() - dungeon.getMaxFloorHeightFromCenterOffset() - 1, 0);
        }
        lastFallbackFillX = 0;
        return true;
    }

    private void placeRoomStep(ServerLevel serverLevel) {
        for (int i = 0; i < GameruleRegistry.getIntegerGamerule(serverLevel.getServer(), ModGamerules.PLACER_STEPS_PER_TICK); i++) {
            if (toPlaceInstances.isEmpty()) {
                // if done start next task
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS,"All Rooms Placed, Starting Post Processing");
                currentTask = GenerationTask.POST_PROCESSING_ROOMS;
                return;
            }
            // get the object to place in the world
            Object toPlace = toPlaceInstances.peek();
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS,"to Place:" + toPlace);
            if (toPlace instanceof GeneratedRoom room) {
                // if it's a room, place it down
                room.finalizePlacement(serverLevel, collection.getStructureProcessors(), random);
                //serverLevel.getBiomeManager().
                toPlaceInstances.remove();
            }
            else if (toPlace instanceof FailRule.Instance failInstance) {
                // if it's a fail rule place it tick it end check if it's done
                failInstance.finalizeTick(serverLevel, collection.getStructureProcessors());
                if (failInstance.isFinished())
                    toPlaceInstances.remove();
            }
            else {
                // other objects should not exist this would be a bug
                DebugLog.logWarn(DebugLog.DebugType.WARNINGS,"Disallowed object in ToPlace List: {} - removing", toPlace);
                toPlaceInstances.remove();
            }
        }
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS,"Room place tick complete");
    }

    private void postProcessRoomsStep(ServerLevel serverLevel) {
        for (int i = 0; i < GameruleRegistry.getIntegerGamerule(serverLevel.getServer(), ModGamerules.PLACER_STEPS_PER_TICK); i++) {
            if (toPostProcessRooms.isEmpty()) {
                // if done start next task
                GlobalDungeonManager.killAllInDungeon(serverLevel.getServer(), dungeon.getRank());
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "All Rooms Post Processed, Starting Mob Spawning");
                currentTask = GenerationTask.FILLING_WITH_MOBS;
                return;
            }
            GeneratedRoom toProcess = toPostProcessRooms.remove();
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS,"to Process:" + toProcess);
            toProcess.finalizePostProcessing(serverLevel, collection.getStructurePostProcessors(), random);
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
        for (FailRule failRule : collection.getFailRules()) {
            if (failRule.match(tag, from)) {
                toPlaceInstances.add(new FailRule.Instance(failRule,room,from,placeFallback,exitObstructed));
                if (failRule.stopFallbackPlacement())
                    doPlaceFallback=false;
                break;
            }
        }
        if (doPlaceFallback) {
            Vec3i offset = room.getPlacedArrayOffset(from);
            if (offset!= null) {
                Vec3i pos = room.getPlacedArrayPos().offset(offset);
                PlaceFallbackAt(pos.getX(), pos.getY(), pos.getZ(), room.getPlacedWorldPos().offset(new Vec3i(offset.getX() * collection.getGridCellWidth(), offset.getY() * collection.getGridCellHeight(), offset.getZ() * collection.getGridCellWidth())));
            }
        }
    }

    private void PlaceFallbackAt(int arrayX, int arrayY, int arrayZ, BlockPos worldPos) {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS,"placing fallback");
        if (collection.getFallback()==null) {return;}
        GeneratedRoom fallback = GeneratedRoom.createInstance(collection.getFallback(), this, arrayX, arrayY, arrayZ, worldPos, Rotation.NONE, true, Occupation.AVAILABLE, true, random);
        toPlaceInstances.add(fallback);
        if (fallback.hasPostProcessing())
            toPostProcessRooms.add(fallback);
        //return fallback;
    }

    public void PlaceFallbackAt(int arrayX, int arrayY, int arrayZ, BlockPos worldPos, Occupation occupation) {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS,"placing fallback");
        if (collection.getFallback()==null) {return;}
        GeneratedRoom fallback = GeneratedRoom.createInstance(collection.getFallback(), this, arrayX, arrayY, arrayZ, worldPos, Rotation.NONE, true, occupation, true, random);
        toPlaceInstances.add(fallback);
        if (fallback.hasPostProcessing())
            toPostProcessRooms.add(fallback);
        //return fallback;
    }

    public Occupation[][][] getOccupationArray() {
        return occupationArray;
    }

    public GridDungeon getDungeon() {
        return dungeon;
    }

    public AbstractGridRoom GetRandomRoomByConnection(Connection connection, String fromTag, Random random) {
        return collection.getRandomRoomByConnection(connection, fromTag, collection.getConnectionRules(), random);
    }

    public boolean isDone() {
        return currentTask == GenerationTask.DONE;
    }

    public long GetSeed() {
        return seed;
    }

    public void SetSeed(long seed) {
        this.seed = seed;
        random.setSeed(seed);
    }

    public GenerationTask currentStep() {
        return currentTask;
    }
}
