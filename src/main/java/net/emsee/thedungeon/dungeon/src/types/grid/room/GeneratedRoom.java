package net.emsee.thedungeon.dungeon.src.types.grid.room;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.DungeonUtils;
import net.emsee.thedungeon.dungeon.src.generators.GridDungeonGenerator;
import net.emsee.thedungeon.dungeon.src.mobSpawnRules.MobSpawnRule;
import net.emsee.thedungeon.dungeon.src.types.grid.array.GridArray;
import net.emsee.thedungeon.dungeon.src.types.grid.array.GridCell;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.utils.PriorityMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.*;

public final class GeneratedRoom {
    static final int maxRandomRoomTries = 10;

    private final AbstractGridRoom room;

    private final Connection placedFrom;
    private final BlockPos placedWorldPos;
    private final Rotation placedRotation;
    private final Vec3i placedGridPos;
    private boolean generated;

    private final Map<Connection, Boolean> finishedConnections = new HashMap<>();

    /**
     * create a new GeneratedRoom
     */
    public static GeneratedRoom createInstance(AbstractGridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, Vec3i arrayPos, BlockPos worldPos, Rotation rotation, Random random) {
        if (toGenerate == null)
            throw new IllegalStateException("room to generate was NULL");


        while (toGenerate instanceof GridRoomGroup group) {
            toGenerate = group.getRandom(random);
        }

        GeneratedRoom toReturn = new GeneratedRoom(toGenerate, gridDungeonGenerator,arrayPos, worldPos, rotation, random);
        if (!toReturn.generated)
            return null;//throw new IllegalStateException("error with placing generating room");

        return toReturn;
    }

    /**
     * create a new GeneratedRoom
     */
    public static GeneratedRoom createInstance(AbstractGridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, Vec3i arrayPos, BlockPos worldPos, Rotation rotation, boolean skipBorderCheck, boolean allowReplace, boolean forcePlace, Random random) {
        if (toGenerate == null)
            throw new IllegalStateException("room to generate was NULL");

        while (toGenerate instanceof GridRoomGroup group) {
            toGenerate = group.getRandom(random);
        }

        GeneratedRoom toReturn = new GeneratedRoom(toGenerate, gridDungeonGenerator, arrayPos, worldPos, rotation, skipBorderCheck, allowReplace, forcePlace, random);

        if (!toReturn.generated)
            return null;//throw new IllegalStateException("error with placing generating room");

        return toReturn;
    }

    /**
     * create a new GeneratedRoom from a connection using its offsets
     */
    private static GeneratedRoom createInstanceFromConnection(AbstractGridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, Vec3i arrayPos, BlockPos worldPos, Rotation rotation, Connection fromConnection, Random random) {
        if (toGenerate == null)
            throw new IllegalStateException("room to generate was NULL");

        while (toGenerate instanceof GridRoomGroup group) {
            toGenerate = group.getRandom(random);
        }

        int xOffset = 0;
        int yOffset = 0;
        int zOffset = 0;

        switch (fromConnection) {
            case NORTH -> zOffset = toGenerate.getRotatedNorthPlacementOffset(rotation) - 1;
            case EAST -> xOffset = -(toGenerate.getRotatedEastPlacementOffset(rotation) - 1);
            case SOUTH -> zOffset = -(toGenerate.getRotatedNorthPlacementOffset(rotation) - 1);
            case WEST -> xOffset = toGenerate.getRotatedEastPlacementOffset(rotation) - 1;
            case UP -> yOffset = -(toGenerate.getHeightScale() - 1);
        }

        Vec3i offsets = toGenerate.getConnectionPlaceOffsets(fromConnection, rotation);
        xOffset += offsets.getX();
        yOffset += offsets.getY();
        zOffset += offsets.getZ();

        BlockPos newWorldPos = worldPos.offset(new Vec3i(xOffset * toGenerate.getGridCellWidth(), yOffset * toGenerate.getGridCellWidth(), zOffset * toGenerate.getGridCellWidth()));
        Vec3i newArrayPos = arrayPos.offset(xOffset, yOffset, zOffset);

        GeneratedRoom toReturn = new GeneratedRoom(toGenerate, gridDungeonGenerator, newArrayPos, newWorldPos, rotation, fromConnection, random);

        if (!toReturn.generated)
            return null;
        return toReturn;
    }

    
    private GeneratedRoom(AbstractGridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, Vec3i arrayPos, BlockPos worldPos, Rotation rotation, Random random) {
        if (toGenerate == null)
            throw new IllegalStateException("room to generate was NULL");

        room = toGenerate;
        placedFrom = null;
        placedWorldPos = worldPos;
        placedRotation = rotation;
        placedGridPos = arrayPos;
        generate(gridDungeonGenerator, worldPos, rotation, false, false, false);
    }

    
    private GeneratedRoom(AbstractGridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, Vec3i arrayPos, BlockPos worldPos, Rotation rotation, boolean skipBorderCheck, boolean allowReplace, boolean forcePlace, Random random) {
        if (toGenerate == null)
            throw new IllegalStateException("room to generate was NULL");

        room = toGenerate;
        placedFrom = null;
        placedWorldPos = worldPos;
        placedRotation = rotation;
        placedGridPos = arrayPos;
        generate(gridDungeonGenerator, worldPos, rotation, skipBorderCheck, allowReplace, forcePlace);
    }

    private GeneratedRoom(AbstractGridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, Vec3i arrayPos, BlockPos worldPos, Rotation rotation, Connection fromConnection, Random random) {
        if (toGenerate == null)
            throw new IllegalStateException("room to generate was NULL");

        room = toGenerate;
        placedFrom = fromConnection;
        placedWorldPos = worldPos;
        placedRotation = rotation;
        placedGridPos = arrayPos;
        generate(gridDungeonGenerator, worldPos, rotation, false, false, false);
    }

    private void generate(GridDungeonGenerator generator, BlockPos worldPos, Rotation roomRotation, boolean skipBorderCheck, boolean allowReplace, boolean forcePlace) {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS,
                "{}: trying to generate", this);

        boolean occupationFree = checkOccupation(generator.getOccupationArray(), skipBorderCheck, roomRotation);
        if (!forcePlace && !occupationFree) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS,
                    "{}: trying to generate in occupied space or out of bounds, returning fail", this);
            return;
        }

        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: placing", this);

        GridCell parent = generator.getOccupationArray().insertRoomAt(this, placedGridPos, false);

        Vec3i min = new Vec3i(-(room.getRotatedEastPlacementOffset(placedRotation) - 1),0, -(room.getRotatedNorthPlacementOffset(placedRotation) - 1)).offset(placedGridPos);
        Vec3i max = new Vec3i((room.getRotatedEastPlacementOffset(placedRotation) - 1), room.getHeightScale(), (room.getRotatedNorthPlacementOffset(placedRotation) - 1)).offset(placedGridPos);
        generator.getOccupationArray().insertChildren(parent, min, max, allowReplace);

        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: returning success", this);
        generated = true;
    }

    public void finalizePlacement(ServerLevel serverLevel, StructureProcessorList collectionProcessors , Random random) {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: finalizing placement", this);
        StructureProcessorList finalProcessors = new StructureProcessorList(new ArrayList<>());
        finalProcessors.list().addAll(room.getStructureProcessors().list());
        if (!room.doSkipCollectionProcessors())
            finalProcessors.list().addAll(collectionProcessors.list());
        room.placeFeature(serverLevel, placedWorldPos, placedRotation, finalProcessors, random);
    }

    public void finalizePostProcessing(ServerLevel serverLevel , StructureProcessorList collectionPostProcessors, Random random) {
        StructureProcessorList finalPostProcessors = new StructureProcessorList(new ArrayList<>());
        finalPostProcessors.list().addAll(room.getStructurePostProcessors().list());
        if (!room.doSkipCollectionPostProcessors())
            finalPostProcessors.list().addAll(collectionPostProcessors.list());
        room.postProcess(serverLevel, placedWorldPos, placedRotation, finalPostProcessors, random);
    }

    public boolean hasPostProcessing() {
        return room.hasPostProcessing();
    }

    private boolean checkOccupation(GridArray occupationArray, boolean skipBorderCheck, Rotation rotation) {
        for (int y = 0; y < room.getHeightScale(); y++) {
            for (int x = -(room.getRotatedEastPlacementOffset(rotation) - 1); x <= (room.getRotatedEastPlacementOffset(rotation) - 1); x++) {
                for (int z = -(room.getRotatedNorthPlacementOffset(rotation) - 1); z <= (room.getRotatedNorthPlacementOffset(rotation) - 1); z++) {
                    if (occupationArray.isCellOccupiedAt(placedGridPos.offset(x,y,z)))
                        return false;
                    // out of bounds
                    if (!occupationArray.isInsideGrid(placedGridPos.offset(x,y,z), false)) {
                        return false;
                    }
                    // don't allow generation at the edge of the dungeon to prevent open ends
                    if (!skipBorderCheck && occupationArray.isAtBorder(placedGridPos.offset(x,y,z), false)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Generates one room from one of the connections of this one
     */
    public GeneratedRoom generateConnection(GridDungeonGenerator generator, Random random) {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: generating connected room", this);

        PriorityMap<Connection> placedRoomConnections = DungeonUtils.getRotatedConnectionMap(room.getConnections(), placedRotation);

        // use an array to store a single data value as Lambda's can only use final values
        final GeneratedRoom[] newRoom = {null};

        //Priority Task Map
        PriorityMap<Runnable> tasks = new PriorityMap<>();

        if (!finishedConnections.getOrDefault(Connection.NORTH, false) && placedFrom != Connection.NORTH)
            tasks.put(() -> {
                newRoom[0] = (generateHorizontalRoom(generator, placedRoomConnections, Connection.NORTH, random));
                finishedConnections.put(Connection.NORTH, true);
            }, placedRoomConnections.getOrDefault(Connection.NORTH, 0));
        if (!finishedConnections.getOrDefault(Connection.EAST, false) && placedFrom != Connection.EAST)
            tasks.put(() -> {
                newRoom[0] = (generateHorizontalRoom(generator, placedRoomConnections, Connection.EAST, random));
                finishedConnections.put(Connection.EAST, true);
            }, placedRoomConnections.getOrDefault(Connection.EAST, 0));
        if (!finishedConnections.getOrDefault(Connection.SOUTH, false) && placedFrom != Connection.SOUTH)
            tasks.put(() -> {
                newRoom[0] = (generateHorizontalRoom(generator, placedRoomConnections, Connection.SOUTH, random));
                finishedConnections.put(Connection.SOUTH, true);
            }, placedRoomConnections.getOrDefault(Connection.SOUTH, 0));
        if (!finishedConnections.getOrDefault(Connection.WEST, false) && placedFrom != Connection.WEST)
            tasks.put(() -> {
                newRoom[0] = (generateHorizontalRoom(generator, placedRoomConnections, Connection.WEST, random));
                finishedConnections.put(Connection.WEST, true);
            }, placedRoomConnections.getOrDefault(Connection.WEST, 0));
        if (!finishedConnections.getOrDefault(Connection.UP, false) && placedFrom != Connection.UP)
            tasks.put(() -> {
                newRoom[0] = (generateUpRoom(generator, placedRoomConnections, random));
                finishedConnections.put(Connection.UP, true);
            }, placedRoomConnections.getOrDefault(Connection.UP, 0));
        if (!finishedConnections.getOrDefault(Connection.DOWN, false) && placedFrom != Connection.DOWN)
            tasks.put(() -> {
                newRoom[0] = (generateDownRoom(generator, placedRoomConnections, random));
                finishedConnections.put(Connection.DOWN, true);
            }, placedRoomConnections.getOrDefault(Connection.DOWN, 0));

        Runnable task = tasks.getRandom(random, 1);

        // in case there are no tasks left to preform mark as done
        if (task == null) {
            finishedConnections.put(Connection.NORTH, true);
            finishedConnections.put(Connection.EAST, true);
            finishedConnections.put(Connection.SOUTH, true);
            finishedConnections.put(Connection.WEST, true);
            finishedConnections.put(Connection.UP, true);
            finishedConnections.put(Connection.DOWN, true);
            return null;
        }

        // run the task
        task.run();

        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: finished generating connected room", this);

        // return the newly generated room
        return newRoom[0];
    }

    /**
     * returns true if this room has finished generating all its connections
     */
    public boolean generatedAllConnections() {
        return
                finishedConnections.getOrDefault(Connection.NORTH, false) &&
                        finishedConnections.getOrDefault(Connection.EAST, false) &&
                        finishedConnections.getOrDefault(Connection.SOUTH, false) &&
                        finishedConnections.getOrDefault(Connection.WEST, false) &&
                        finishedConnections.getOrDefault(Connection.UP, false) &&
                        finishedConnections.getOrDefault(Connection.DOWN, false);
    }

    /**
     * places a new room on a HORIZONTAL connection, returns NULL on fail
     */
    private GeneratedRoom generateHorizontalRoom(GridDungeonGenerator generator, PriorityMap<Connection> connections, Connection connection, Random random) {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: generating room at {}", this, connection);
        if (connections.get(connection) <= 0) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: room does not have this connection, skipping", this);
            return null;
        }

        Vec3i offsets = getPlacementConnectionArrayOffset(connection);
        if (offsets == null) return null;

        if (!generator.getOccupationArray().isInsideGrid(placedGridPos.offset(offsets), true)) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate new room out of bounds", this);
            generator.RoomConnectionFail(room.getConnectionTag(connection, placedRotation), this, connection, true, false);
            return null;
        }

        if (generator.getOccupationArray().isCellOccupiedAt(placedGridPos.offset(offsets))) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate new room in already occupied space", this);
            generator.RoomConnectionFail(room.getConnectionTag(connection, placedRotation), this, connection, false, true);
            return null;
        }

        float roomEndChance = generator.getDungeon().getRaw().getRoomEndChance();
        if (hasOverrideEndChance()) {
            roomEndChance = getOverrideEndChance();
        }

        if (random.nextFloat() <= roomEndChance) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: room ended", this);
            generator.RoomConnectionFail(room.getConnectionTag(connection, placedRotation), this, connection, true, false);
            return null;
        }

        int triesLeft = maxRandomRoomTries;

        while (triesLeft > 0) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: trying placement, tries left: {}", this, triesLeft);

            AbstractGridRoom targetGridRoom = generator.GetRandomRoomByConnection(connection.getOpposite(), room.getConnectionTag(connection, placedRotation), random);
            if (targetGridRoom == null) {
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: found no new room for connection", this);
                triesLeft--;
                continue;
            }
            List<Rotation> allowedRoomRotations = targetGridRoom.getAllowedRotations(connection.getOpposite(), room.getConnectionTag(connection, placedRotation), generator.getConnectionRules());
            Rotation randomRoomRotation = ListAndArrayUtils.getRandomFromList(allowedRoomRotations, random);

            GeneratedRoom generatedRoom = createInstanceFromConnection(targetGridRoom, generator, placedGridPos.offset(offsets), placedWorldPos.offset(offsets.getX() * getGridCellWidth(), offsets.getY() * getGridCellHeight(), offsets.getZ() * getGridCellWidth()), randomRoomRotation, connection.getOpposite(), random);

            if (generatedRoom == null || !generatedRoom.generated) {
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: new room placement returned failure", this);
                triesLeft--;
                continue;
            }

            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: placement success, placed ({})", this, targetGridRoom);
            return generatedRoom;
        }
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: out of tries, failed", this);
        generator.RoomConnectionFail(room.getConnectionTag(connection, placedRotation), this, connection, true, false);
        return null;
    }


    /**
     * places a new room on an UPWARDS connection, returns NULL on fail
     */
    private GeneratedRoom generateUpRoom(GridDungeonGenerator generator, PriorityMap<Connection> connections, Random random) {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: generating room at {}", this, Connection.UP);
        if (connections.get(Connection.UP) <= 0) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: room does not have connection", this);
            return null;
        }

        Vec3i offsets = room.getConnectionPlaceOffsets(Connection.UP, placedRotation);
        int xOffset = -offsets.getX();
        int zOffset = -offsets.getZ();

        if (placedGridPos.getX() + room.getRotatedEastSizeScale(placedRotation) < 0 || placedGridPos.getY() + room.getRotatedNorthSizeScale(placedRotation) > generator.getOccupationArray().getDepth()) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate new room out of bounds", this);
            generator.RoomConnectionFail(room.getConnectionTag(Connection.UP, placedRotation), this, Connection.UP, true, false);
            return null;
        }

        if (generator.getOccupationArray().isCellOccupiedAt(placedGridPos.offset(xOffset, room.getHeightScale(), zOffset))) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate new room in already occupied space", this);
            generator.RoomConnectionFail(room.getConnectionTag(Connection.UP, placedRotation), this, Connection.UP, false, true);
            return null;
        }

        if (!generator.getOccupationArray().isInsideFloorLimit(placedWorldPos.offset(xOffset, room.getHeightScale(), zOffset))) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate room outside of floor limit, placing fallback", this);
            generator.RoomConnectionFail(room.getConnectionTag(Connection.UP, placedRotation), this, Connection.UP, true, false); //generator.PlaceFallbackAt(placedArrayX, placedArrayY + 1, placedArrayZ, placedWorldPos.above(Height()));
            return null;
        }

        float roomEndChance = generator.getDungeon().getRaw().getRoomEndChance();
        if (hasOverrideEndChance()) {
            roomEndChance = getOverrideEndChance();
        }

        if (random.nextFloat() <= roomEndChance) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: room ended, placing fallback", this);
            generator.RoomConnectionFail(room.getConnectionTag(Connection.UP, placedRotation), this, Connection.UP, true, false);
            return null;
        }

        int triesLeft = maxRandomRoomTries;

        while (triesLeft > 0) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: trying placement, tries left: {}", this, triesLeft);

            AbstractGridRoom targetGridRoom = generator.GetRandomRoomByConnection(Connection.DOWN, room.getConnectionTag(Connection.UP, placedRotation), random);
            if (targetGridRoom == null) {
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: found no new room", this);
                triesLeft--;
                continue;
            }

            Rotation randomRoomRotation;
            if (room.canUpDownRotate() && targetGridRoom.canUpDownRotate()) {
                List<Rotation> allowedRoomRotations = targetGridRoom.getAllowedRotations(Connection.DOWN, room.getConnectionTag(Connection.UP, placedRotation), generator.getConnectionRules());
                randomRoomRotation = ListAndArrayUtils.getRandomFromList(allowedRoomRotations, random);
            } else randomRoomRotation = placedRotation;
            GeneratedRoom generatedRoom = createInstanceFromConnection(targetGridRoom, generator, placedGridPos.offset(xOffset, room.getHeightScale(),  zOffset), placedWorldPos.offset(xOffset * getGridCellWidth(), room.getHeightScale() * getGridCellHeight(), zOffset * getGridCellWidth()), randomRoomRotation, Connection.DOWN, random);

            if (generatedRoom == null || !generatedRoom.generated) {
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: placement returned failure", this);
                triesLeft--;
                continue;
            }

            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: placement success, placed ({})", this, targetGridRoom);
            return generatedRoom;
        }

        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: out of tries failed", this);
        generator.RoomConnectionFail(room.getConnectionTag(Connection.UP, placedRotation), this, Connection.UP, true, false);
        return null;

    }


    private GeneratedRoom generateDownRoom(GridDungeonGenerator generator, PriorityMap<Connection> connections, Random random) {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: generating room at {}", this, Connection.DOWN);
        if (connections.get(Connection.DOWN) <= 0) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: room does not have connection", this);
            return null;
        }

        Vec3i offsets = room.getConnectionPlaceOffsets(Connection.DOWN, placedRotation);
        int xOffset = -offsets.getX();
        int zOffset = -offsets.getZ();

        if (!generator.getOccupationArray().isInsideGrid(placedGridPos.offset(0,-1,0), false)) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate new room out of bounds", this);
            generator.RoomConnectionFail(room.getConnectionTag(Connection.DOWN, placedRotation), this, Connection.DOWN, true, false);
            return null;
        }

        if (generator.getOccupationArray().isCellOccupiedAt(placedGridPos.offset(xOffset, -1, zOffset))) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate new room in already occupied space", this);
            generator.RoomConnectionFail(room.getConnectionTag(Connection.DOWN, placedRotation), this, Connection.DOWN, false, true);
            return null;
        }

        if (!generator.getOccupationArray().isInsideFloorLimit(placedWorldPos.offset(xOffset, -1, zOffset))) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate room outside of floor limit, placing fallback", this);
            generator.RoomConnectionFail(room.getConnectionTag(Connection.UP, placedRotation), this, Connection.UP, true, false); //generator.PlaceFallbackAt(placedArrayX, placedArrayY + 1, placedArrayZ, placedWorldPos.above(Height()));
            return null;
        }

        float roomEndChance = generator.getDungeon().getRaw().getRoomEndChance();
        if (hasOverrideEndChance()) {
            roomEndChance = getOverrideEndChance();
        }

        if (random.nextFloat() <= roomEndChance) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: room ended, placing fallback", this);
            generator.RoomConnectionFail(room.getConnectionTag(Connection.DOWN, placedRotation), this, Connection.DOWN, true, false);
            return null;
        }


        int triesLeft = maxRandomRoomTries;

        while (triesLeft > 0) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: trying placement, tries left: {}", this, triesLeft);
            AbstractGridRoom targetGridRoom = generator.GetRandomRoomByConnection(Connection.UP, room.getConnectionTag(Connection.DOWN, placedRotation), random);

            if (generator.getDungeon().getRaw().isDownGenerationDisabled() || !generator.getOccupationArray().isInsideFloorLimit(placedGridPos.offset(0,-targetGridRoom.getHeightScale(),0))) {
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate room outside of floor limit", this);
                triesLeft--;
                continue;
            }

            Rotation randomRoomRotation = placedRotation;
            if (room.canUpDownRotate() && targetGridRoom.canUpDownRotate()) {
                List<Rotation> allowedRoomRotations = targetGridRoom.getAllowedRotations(Connection.UP, room.getConnectionTag(Connection.DOWN, placedRotation), generator.getConnectionRules());
                randomRoomRotation = ListAndArrayUtils.getRandomFromList(allowedRoomRotations, random);
            }
            GeneratedRoom generatedRoom = createInstanceFromConnection(targetGridRoom, generator, placedGridPos.offset(xOffset, - 1, zOffset), placedWorldPos.offset(xOffset * getGridCellWidth(), -getGridCellHeight(), zOffset * getGridCellWidth()), randomRoomRotation, Connection.UP, random);
            if (generatedRoom == null || !generatedRoom.generated) {
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: placement returned failure", this);
                triesLeft--;
                continue;
            }

            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: placement success, placed ({})", this, targetGridRoom);
            return generatedRoom;
        }
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: out of tries, placing fallback", this);
        generator.RoomConnectionFail(room.getConnectionTag(Connection.DOWN, placedRotation), this, Connection.DOWN, true, false);
        return null;
    }


    @Override
    public String toString() {
        return "GeneratedRoom:" + room;
    }

    public int getPriority() {
        return room.getGenerationPriority();
    }

    public boolean hasOverrideEndChance() {
        return room.hasOverrideEndChance();
    }

    public float getOverrideEndChance() {
        return room.getOverrideEndChance();
    }

    /**
     * spawns the mobs in the room
     */
    public void spawnMobs(ServerLevel level) {
        for (MobSpawnRule rule : room.getSpawnRules()) {
            rule.spawn(level, this);
        }
    }

    public boolean hasMobSpawns() {
        return room.hasMobSpawns();
    }

    /**
     * the connected array position
     */
    public Vec3i getPlacementConnectionArrayOffset(Connection placedConnection) {
        PriorityMap<Connection> connections = DungeonUtils.getRotatedConnectionMap(room.getConnections(), placedRotation);
        if (connections.get(placedConnection) <= 0) return null;

        int unitXOffset = 0;
        int yOffset = 0;
        int unitZOffset = 0;
        switch (placedConnection) {
            case NORTH -> unitZOffset = -1;
            case EAST -> unitXOffset = 1;
            case SOUTH -> unitZOffset = 1;
            case WEST -> unitXOffset = -1;
            case UP -> yOffset = room.getHeightScale();
            case DOWN -> yOffset = -1;
        }

        int xOffset = unitXOffset * room.getRotatedEastPlacementOffset(placedRotation);
        int zOffset = unitZOffset * room.getRotatedNorthPlacementOffset(placedRotation);

        Vec3i offsets = room.getConnectionPlaceOffsets(placedConnection, placedRotation);
        xOffset -= offsets.getX();
        yOffset -= offsets.getY();
        zOffset -= offsets.getZ();

        return new Vec3i(xOffset, yOffset, zOffset);
    }

    /**
     * array position where the conection is from
     */
    public Vec3i getPlacementConnectionArrayOffsetInset(Connection placedConnection) {
        PriorityMap<Connection> connections = DungeonUtils.getRotatedConnectionMap(room.getConnections(), placedRotation);
        if (connections.get(placedConnection) <= 0) return null;

        int unitXOffset = 0;
        int yOffset = 0;
        int unitZOffset = 0;
        switch (placedConnection) {
            case NORTH -> unitZOffset = -1;
            case EAST -> unitXOffset = 1;
            case SOUTH -> unitZOffset = 1;
            case WEST -> unitXOffset = -1;
            case UP -> yOffset = room.getHeightScale()-1;
        }

        int xOffset = unitXOffset * (room.getRotatedEastPlacementOffset(placedRotation)-1);
        int zOffset = unitZOffset * (room.getRotatedNorthPlacementOffset(placedRotation)-1);

        Vec3i offsets = room.getConnectionPlaceOffsets(placedConnection, placedRotation);
        xOffset -= offsets.getX();
        yOffset -= offsets.getY();
        zOffset -= offsets.getZ();

        return new Vec3i(xOffset, yOffset, zOffset);
    }

    public Vec3i getPlacedArrayPos() {
        return placedGridPos;
    }

    public BlockPos getPlacedWorldPos() {
        return placedWorldPos;
    }

    public Rotation getPlacedWorldRotation() {
        return placedRotation;
    }

    public int getGridCellWidth() {
        return room.getGridCellWidth();
    }

    public int getGridCellHeight() {
        return room.getGridCellHeight();
    }

    public AbstractGridRoom getRoom() {
        return room;
    }

    public Vec3i getConnectedCellPos(Connection connection) {
        Vec3i offsets = getPlacementConnectionArrayOffset(connection);
        if (offsets==null) return null;
        return placedGridPos.offset(offsets);
    }

    public Vec3i getConnectedCellPosInset(Connection connection) {
        Vec3i offsets = getPlacementConnectionArrayOffsetInset(connection);
        if (offsets==null) return null;
        return placedGridPos.offset(offsets);
    }


    public boolean hasConnectionAt(Vec3i arrayPos, Connection has) {
        Vec3i thisArrayPos = getConnectedCellPosInset(has);
        if (arrayPos==null||thisArrayPos==null) return false;
        return thisArrayPos.equals(arrayPos);
    }
}

