package net.emsee.thedungeon.dungeon.dungeon.generator;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.GlobalDungeonManager;
import net.emsee.thedungeon.dungeon.connectionRules.FailInstance;
import net.emsee.thedungeon.dungeon.connectionRules.FailRule;
import net.emsee.thedungeon.dungeon.dungeon.type.GridDungeon;
import net.emsee.thedungeon.dungeon.connectionRules.ConnectionRule;
import net.emsee.thedungeon.dungeon.room.GeneratedRoom;
import net.emsee.thedungeon.dungeon.room.GridRoomUtils;
import net.emsee.thedungeon.dungeon.room.GridRoom;
import net.emsee.thedungeon.dungeon.roomCollections.GridRoomCollection;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;

import java.util.*;

import static net.emsee.thedungeon.TheDungeon.LOGGER;
import static net.emsee.thedungeon.dungeon.dungeon.Dungeon.PriorityCleanup;

public class GridDungeonGenerator {

    private enum GenerationTask {
        UNSTARTED,
        CALCULATING,
        CHECK_REQUIREMENTS,
        FILLING_UNOCCUPIED,
        PLACING_ROOMS,
        FILLING_WITH_MOBS,
        DONE
    }

    private GenerationTask currentTask = GenerationTask.UNSTARTED;

    private final Random random;
    private long seed;
    private final Occupation[][][] occupationArray;
    private final GridDungeon dungeon;
    private GridRoomCollection collection;
    private final BlockPos worldPos;
    private final Queue<GeneratedRoom> todoRooms = new LinkedList<>();
    private final Queue<Object> toPlaceInstances = new LinkedList<>();
    private final Queue<GeneratedRoom> toSpawnMobsRooms = new LinkedList<>();

    private int lastFillX = 0;
    private int lastFillY = 0;
    private int lastFillZ = 0;

    public GridRoomCollection GetCollection() {
        return collection.getCopy();
    }

    public void SetCollection(GridRoomCollection gridRoomCollection) {
        collection = gridRoomCollection;
    }

    public GridRoomCollection GetCollectionSource() {
        return collection;
    }

    public List<ConnectionRule> getConnectionRules() {
        return collection.getConnectionRules();
    }

    public enum Occupation {
        available,
        occupied,
    }

    public GridDungeonGenerator(GridDungeon dungeon) {
        this(dungeon, new Random().nextLong());
    }

    public GridDungeonGenerator(GridDungeon dungeon, long seed) {
        this(dungeon, new Random(seed));
        this.seed = seed;
    }

    private GridDungeonGenerator(GridDungeon dungeon, Random random) {
        this.random = random;
        this.dungeon = dungeon;
        collection = dungeon.getRoomCollection();
        this.worldPos = dungeon.getRank().getCenterPos();
        int listCentreOffset = dungeon.GetDungeonDepth();
        int arraySize = (dungeon.GetDungeonDepth() * 2) + 1;
        occupationArray = new Occupation[arraySize][arraySize][arraySize];

        for (Occupation[][] occupationArrayRow : occupationArray) {
            for (Occupation[] occupationArrayCol : occupationArrayRow) {
                Arrays.fill(occupationArrayCol, Occupation.available);
            }
        }

        GridRoom startingRoom = dungeon.GetStaringRoom();
        if (startingRoom == null)
            startingRoom = dungeon.getRoomCollection().getRandomRoom(random);
        if (startingRoom == null) if (TheDungeon.debugMode.is(TheDungeon.DebugMode.IMPORTANT_ONLY)) LOGGER.error("could not find starting room");
        GeneratedRoom generatedStartingRoom = GeneratedRoom.generateRoom(startingRoom, this, listCentreOffset, listCentreOffset, listCentreOffset, worldPos, GridRoomUtils.getRandomRotation(random), random);
        assert generatedStartingRoom != null;
        todoRooms.add(generatedStartingRoom);
        collection.updatePlacedRequirements(startingRoom);
        toPlaceInstances.add(generatedStartingRoom);
        if (generatedStartingRoom.hasMobSpawns()) {
            toSpawnMobsRooms.add(generatedStartingRoom);
        }
    }


    public void step(ServerLevel serverLevel) {

        if (currentTask == GenerationTask.UNSTARTED) {
            currentTask = GenerationTask.CALCULATING;
            GlobalDungeonManager.KillAllInDungeon(serverLevel.getServer());
        } else if (currentTask == GenerationTask.CALCULATING) calculationStep(serverLevel.getServer());
        else if (currentTask == GenerationTask.CHECK_REQUIREMENTS) checkRequirementStep(serverLevel.getServer());
        else if (currentTask == GenerationTask.FILLING_UNOCCUPIED) fillUnoccupiedStep(serverLevel.getServer());
        else if (currentTask == GenerationTask.PLACING_ROOMS) placeRoomStep(serverLevel);
        else if (currentTask == GenerationTask.FILLING_WITH_MOBS) fillWithMobsStep(serverLevel);

    }

    private void calculationStep(MinecraftServer server) {
        for (int i = 0; i < GameruleRegistry.getIntegerGamerule(server, ModGamerules.CALCULATOR_STEPS_PER_TICK); i++) {
            if (todoRooms.isEmpty()) {
                currentTask = GenerationTask.CHECK_REQUIREMENTS;
                if (TheDungeon.debugMode.is(TheDungeon.DebugMode.GENERIC)) LOGGER.info("calculation complete, checking requirements:");
                return;
            }

            List<GeneratedRoom> nextGenerationOptions = getPossibleRoomSelection();

            GeneratedRoom room;
            switch (dungeon.getRoomPickMethod()) {
                case LAST -> room = nextGenerationOptions.getLast();
                case RANDOM -> room = ListAndArrayUtils.getRandomFromList(nextGenerationOptions, random);
                default -> room = nextGenerationOptions.getFirst();
            }
            if (room == null) return;
            GeneratedRoom newRoom = room.generateConnections(this, random);
            if (room.generatedAllConnections())
                todoRooms.remove(room);
            if (newRoom != null) {
                todoRooms.add(newRoom);
                collection.updatePlacedRequirements(newRoom.getRoom());
                toPlaceInstances.add(newRoom);
                if (newRoom.hasMobSpawns()) {
                    toSpawnMobsRooms.add(newRoom);
                }
            }
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) LOGGER.info("Dungeon calculation step completed");
        }
    }

    private List<GeneratedRoom> getPossibleRoomSelection() {
        List<GeneratedRoom> toReturn = new ArrayList<>();

        //StringBuilder sBuilder = new StringBuilder("{");

        int maxPriority = 0;
        for (GeneratedRoom generatedRoom : todoRooms) {
            //sBuilder.append("[").append(generatedRoom).append("-=-").append(generatedRoom.getPriority()).append("],");
            if (generatedRoom.getPriority() == maxPriority)
                toReturn.add(generatedRoom);
            else if (generatedRoom.getPriority() > maxPriority) {
                maxPriority = generatedRoom.getPriority();
                toReturn = new ArrayList<>();
                toReturn.add(generatedRoom);
            }
        }
        //sBuilder.append("}");
        //LOGGER.info("FunnyMap:"+sBuilder);
        //LOGGER.info("finalList:"+toReturn);
        return toReturn;
    }

    private void checkRequirementStep(MinecraftServer server) {
        if (!collection.requiredRoomsDone()) {
            LOGGER.info("Not All Required Rooms where generated, regenerating with Seed+1");
            //generator = new GridDungeonGenerator(this, level, generatedPos, generator.GetSeed()+1);
            if (GameruleRegistry.getBooleanGamerule(server, ModGamerules.DUNGEON_CLEAN_ON_REGEN))
                PriorityCleanup(server);
            dungeon.Generate(worldPos, GetSeed() + 1);
            return;
        }
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.GENERIC)) LOGGER.info("All Required Rooms where generated, starting placement");
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.GENERIC)) LOGGER.info("Filling Unoccupied Placements");

        currentTask = GenerationTask.FILLING_UNOCCUPIED;
        lastFillY = Math.max(dungeon.GetDungeonDepth() - dungeon.GetMaxFloorHeightFromCenterOffset() - 1, 0);
    }

    private void fillUnoccupiedStep(MinecraftServer server) {
        if (!dungeon.shouldFillWithFallback()) {
            currentTask = GenerationTask.PLACING_ROOMS;
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.GENERIC)) LOGGER.info("Fallback Fill Disabled, Starting Room Placement");
            return;
        }
        for (int i = 0; i < GameruleRegistry.getIntegerGamerule(server, ModGamerules.CALCULATOR_STEPS_PER_TICK); i++) {
            if (FillUnoccupied()) {
                currentTask = GenerationTask.PLACING_ROOMS;
                if (TheDungeon.debugMode.is(TheDungeon.DebugMode.GENERIC)) LOGGER.info("Fallback Placing Finished, Starting Room Placement");
            }
        }
    }

    private void placeRoomStep(ServerLevel serverLevel) {
        for (int i = 0; i < GameruleRegistry.getIntegerGamerule(serverLevel.getServer(), ModGamerules.PLACER_STEPS_PER_TICK); i++) {
            if (toPlaceInstances.isEmpty()) {
                GlobalDungeonManager.KillAllInDungeon(serverLevel.getServer());
                if (TheDungeon.debugMode.is(TheDungeon.DebugMode.GENERIC)) LOGGER.info("All Rooms Placed, Starting Mob Spawning");
                currentTask = GenerationTask.FILLING_WITH_MOBS;
                return;
            }
            Object toPlace = toPlaceInstances.peek();
            if (toPlace instanceof GeneratedRoom room) {
                room.finalizePlacement(serverLevel, collection.getStructureProcessors(), random);
                toPlaceInstances.remove();
            }
            else if (toPlace instanceof FailInstance failInstance) {
                failInstance.finalize(serverLevel, collection.getStructureProcessors());
                if (failInstance.isFinished())
                    toPlaceInstances.remove();
            } else {
                if (TheDungeon.debugMode.is(TheDungeon.DebugMode.IMPORTANT_ONLY))
                    LOGGER.warn("Disallowed object in ToPlace List: {} - removing", toPlace);
                toPlaceInstances.remove();
            }
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) LOGGER.info("Room place step complete");
        }
    }

    private void fillWithMobsStep(ServerLevel serverLevel) {
        for (int i = 0; i < GameruleRegistry.getIntegerGamerule(serverLevel.getServer(), ModGamerules.SPAWNER_STEPS_PER_TICK); i++) {
            if (toSpawnMobsRooms.isEmpty()) {
                currentTask = GenerationTask.DONE;
                if (TheDungeon.debugMode.is(TheDungeon.DebugMode.GENERIC)) LOGGER.info("Mob Spawning Complete");
                return;
            }

            GeneratedRoom toSpawnMobs = toSpawnMobsRooms.remove();
            toSpawnMobs.spawnMobs(serverLevel);
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) LOGGER.info("Mob spawn step complete");
        }
    }

    public boolean FillUnoccupied() {
        for (int x = lastFillX; x < occupationArray.length; x++) {
            for (int y = lastFillY; y < Math.min(dungeon.GetDungeonDepth() + dungeon.GetMaxFloorHeightFromCenterOffset() + 2, occupationArray.length); y++) {
                for (int z = lastFillZ; z < occupationArray.length; z++) {
                    Occupation instance = occupationArray[x][y][z];
                    if (instance == Occupation.available) {
                        PlaceFallbackAt(x, y, z, worldPos.offset(new Vec3i(dungeon.GetRoomWidth() * (x - dungeon.GetDungeonDepth()), dungeon.GetRoomHeight() * (y - dungeon.GetDungeonDepth()), dungeon.GetRoomWidth() * (z - dungeon.GetDungeonDepth()))), Occupation.occupied);
                        lastFillX = x;
                        lastFillY = y;
                        lastFillZ = z;
                        return false;
                    }
                }
                lastFillZ = 0;
            }
            lastFillY = Math.max(dungeon.GetDungeonDepth() - dungeon.GetMaxFloorHeightFromCenterOffset() - 1, 0);
        }
        lastFillX = 0;
        return true;
    }

    public void RoomConnectionFail(String tag, GeneratedRoom room, GridRoomUtils.Connection from, boolean placeFallback, boolean exitObstructed) {
        boolean doPlaceFallback = placeFallback;
        for (FailRule failRule : collection.getFailRules()) {
            if (failRule.match(tag)) {
                toPlaceInstances.add(new FailInstance(failRule,room,from,placeFallback,exitObstructed));
                if (failRule.stopFallbackPlacement())
                    doPlaceFallback=false;
                break;
            }
        }
        if (doPlaceFallback) {
            Vec3i offset = room.getPlacedArrayOffset(from);
            if (offset!= null) {
                Vec3i pos = room.getPlacedArrayPos().offset(offset);
                PlaceFallbackAt(pos.getX(), pos.getY(), pos.getZ(), room.getPlacedWorldPos().offset(new Vec3i(offset.getX() * collection.getWidth(), offset.getY() * collection.getHeight(), offset.getZ() * collection.getWidth())));
            }
        }
    }

    private void PlaceFallbackAt(int arrayX, int arrayY, int arrayZ, BlockPos worldPos) {
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) LOGGER.info("placing fallback");
        if (collection.getFallback()==null) {return;}
        GeneratedRoom fallback = GeneratedRoom.generateRoom(collection.getFallback(), this, arrayX, arrayY, arrayZ, worldPos, Rotation.NONE, true, Occupation.available, true, random);
        toPlaceInstances.add(fallback);
        //return fallback;
    }

    public void PlaceFallbackAt(int arrayX, int arrayY, int arrayZ, BlockPos worldPos, Occupation occupation) {
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) LOGGER.info("placing fallback");
        if (collection.getFallback()==null) {return;}
        GeneratedRoom fallback = GeneratedRoom.generateRoom(collection.getFallback(), this, arrayX, arrayY, arrayZ, worldPos, Rotation.NONE, true, occupation, true, random);
        toPlaceInstances.add(fallback);
        //return fallback;
    }

    public Occupation[][][] getOccupationArray() {
        return occupationArray;
    }

    public GridDungeon getDungeon() {
        return dungeon;
    }

    public GridRoom GetRandomRoomByConnection(GridRoomUtils.Connection connection, String fromTag, Random random) {
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
}
