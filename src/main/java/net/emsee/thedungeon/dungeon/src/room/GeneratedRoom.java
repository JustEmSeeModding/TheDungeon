package net.emsee.thedungeon.dungeon.src.room;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.DungeonUtils;
import net.emsee.thedungeon.dungeon.src.generators.GridDungeonGenerator;
import net.emsee.thedungeon.dungeon.src.mobSpawnRules.MobSpawnRule;
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

    private Connection placedFrom;
    private BlockPos placedWorldPos;
    private Rotation placedRotation;
    private int placedArrayX;
    private int placedArrayY;
    private int placedArrayZ;
    private boolean generated;

    private final Map<Connection, Boolean> finishedConnections = new HashMap<>();

    /**
     * create a new GeneratedRoom
     */
    public static GeneratedRoom createInstance(AbstractGridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, int listPosX, int listPosY, int listPosZ, BlockPos worldPos, Rotation rotation, Random random) {
        if (toGenerate == null)
            throw new IllegalStateException("room to generate was NULL");


        while (toGenerate instanceof GridRoomGroup group) {
            toGenerate = group.getRandom(random);
        }

        GeneratedRoom toReturn = new GeneratedRoom(toGenerate, gridDungeonGenerator, listPosX, listPosY, listPosZ, worldPos, rotation, random);
        if (!toReturn.generated)
            throw new IllegalStateException("error with placing generating room");

        return toReturn;
    }

    /**
     * create a new GeneratedRoom
     */
    public static GeneratedRoom createInstance(AbstractGridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, int listPosX, int listPosY, int listPosZ, BlockPos worldPos, Rotation rotation, boolean skipBorderCheck, GridDungeonGenerator.Occupation occupation, boolean forcePlace, Random random) {
        if (toGenerate == null)
            throw new IllegalStateException("room to generate was NULL");

        while (toGenerate instanceof GridRoomGroup group) {
            toGenerate = group.getRandom(random);
        }

        GeneratedRoom toReturn = new GeneratedRoom(toGenerate, gridDungeonGenerator, listPosX, listPosY, listPosZ, worldPos, rotation, skipBorderCheck, occupation, forcePlace, random);

        if (!toReturn.generated)
            throw new IllegalStateException("error with placing generating room");

        return toReturn;
    }

    /**
     * create a new GeneratedRoom from a connection using its offsets
     */
    private static GeneratedRoom generateRoomFromConnection(AbstractGridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, int listPosX, int listPosY, int listPosZ, BlockPos worldPos, Rotation rotation, Connection fromConnection, Random random) {
        if (toGenerate == null)
            throw new IllegalStateException("room to generate was NULL");

        while (toGenerate instanceof GridRoomGroup group) {
            toGenerate = group.getRandom(random);
        }

        GeneratedRoom toReturn = new GeneratedRoom(toGenerate, gridDungeonGenerator, listPosX, listPosY, listPosZ, worldPos, rotation, fromConnection, random);

        if (!toReturn.generated)
            return null;

        return toReturn;
    }


    
    private GeneratedRoom(AbstractGridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, int listPosX, int listPosY, int listPosZ, BlockPos worldPos, Rotation rotation, Random random) {
        if (toGenerate == null)
            throw new IllegalStateException("room to generate was NULL");

        room = toGenerate.getCopy();
        generate(gridDungeonGenerator, listPosX, listPosY, listPosZ, worldPos, rotation, false, GridDungeonGenerator.Occupation.OCCUPIED, false);
    }

    
    private GeneratedRoom(AbstractGridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, int listPosX, int listPosY, int listPosZ, BlockPos worldPos, Rotation rotation, boolean skipBorderCheck, GridDungeonGenerator.Occupation occupation, boolean forcePlace, Random random) {
        if (toGenerate == null)
            throw new IllegalStateException("room to generate was NULL");

        room = toGenerate.getCopy();
        generate(gridDungeonGenerator, listPosX, listPosY, listPosZ, worldPos, rotation, skipBorderCheck, occupation, forcePlace);
    }

    private GeneratedRoom(AbstractGridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, int listPosX, int listPosY, int listPosZ, BlockPos worldPos, Rotation rotation, Connection fromConnection, Random random) {
        if (toGenerate == null)
            throw new IllegalStateException("room to generate was NULL");

        room = toGenerate.getCopy();
        generateRoomFrom(gridDungeonGenerator, listPosX, listPosY, listPosZ, worldPos, rotation, fromConnection);
    }

    private void generate(GridDungeonGenerator generator, int arrayX, int arrayY, int arrayZ, BlockPos worldPos, Rotation roomRotation, boolean skipBorderCheck, GridDungeonGenerator.Occupation newOccupation, boolean forcePlace) {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS,
                "{}: trying to generate", this);
        boolean occupation = checkOccupation(generator.getOccupationArray(), arrayX, arrayY, arrayZ, skipBorderCheck, roomRotation);
        if (!forcePlace && !occupation) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS,
                    "{}: trying to generate in occupied space or out of bounds, returning fail", this);
            return;
        }

        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: placing", this);

        setOccupation(generator.getOccupationArray(), arrayX, arrayY, arrayZ, newOccupation, roomRotation);

        placedWorldPos = worldPos;
        placedRotation = roomRotation;
        placedArrayX = arrayX;
        placedArrayY = arrayY;
        placedArrayZ = arrayZ;
        generated = true;

        DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: returning success", this);
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

    private void generateRoomFrom(GridDungeonGenerator generator, int arrayX, int arrayY, int arrayZ, BlockPos worldPos, Rotation roomRotation, Connection fromConnection) {
        placedFrom = fromConnection;

        int xOffset = 0;
        int yOffset = 0;
        int zOffset = 0;

        switch (fromConnection) {
            case NORTH -> zOffset = room.getRotatedNorthPlacementOffset(roomRotation) - 1;
            case EAST -> xOffset = -(room.getRotatedEastPlacementOffset(roomRotation) - 1);
            case SOUTH -> zOffset = -(room.getRotatedNorthPlacementOffset(roomRotation) - 1);
            case WEST -> xOffset = room.getRotatedEastPlacementOffset(roomRotation) - 1;
            case UP -> yOffset = -(room.getHeightScale() - 1);
        }

        Vec3i offsets = room.getConnectionPlaceOffsets(fromConnection, roomRotation);
        xOffset += offsets.getX();
        yOffset += offsets.getY();
        zOffset += offsets.getZ();

        BlockPos newWorldPos = worldPos.offset(new Vec3i(xOffset * room.getGridCellWidth(), yOffset * room.getGridCellWidth(), zOffset * room.getGridCellWidth()));
        int newArrayX = arrayX + xOffset;
        int newArrayY = arrayY + yOffset;
        int newArrayZ = arrayZ + zOffset;
        generate(generator, newArrayX, newArrayY, newArrayZ, newWorldPos, roomRotation, false, GridDungeonGenerator.Occupation.OCCUPIED, false);
    }

    private boolean checkOccupation(GridDungeonGenerator.Occupation[][][] occupationArray, int arrayX, int arrayY, int arrayZ, boolean skipBorderCheck, Rotation rotation) {
        for (int y = 0; y < room.getHeightScale(); y++) {
            int yOffset = arrayY + y;
            for (int x = -(room.getRotatedEastPlacementOffset(rotation) - 1); x <= (room.getRotatedEastPlacementOffset(rotation) - 1); x++) {
                int xOffset = arrayX + x;
                for (int z = -(room.getRotatedNorthPlacementOffset(rotation) - 1); z <= (room.getRotatedNorthPlacementOffset(rotation) - 1); z++) {
                    int zOffset = arrayZ + z;
                    try {
                        if (!(occupationArray[xOffset][yOffset][zOffset] == GridDungeonGenerator.Occupation.AVAILABLE))
                            return false;
                        // don't allow generation at the edge of the dungeon to prevent open ends
                        if (!skipBorderCheck && (
                                xOffset == 0 ||
                                        yOffset == 0 ||
                                        zOffset == 0 ||
                                        xOffset == occupationArray.length - 1 ||
                                        yOffset == occupationArray.length - 1 ||
                                        zOffset == occupationArray.length - 1)) {
                            return false;
                        }
                    } catch (Exception e) {
                        // in case of checking outside of bounds, return false
                        return false;
                    }
                }
            }
        }
        //TheDungeon.LOGGER.info("returned:"+true);
        return true;
    }


    private void setOccupation(GridDungeonGenerator.Occupation[][][] occupationArray, int arrayX, int arrayY, int arrayZ, GridDungeonGenerator.Occupation occupation, Rotation rotation) {
        for (int y = 0; y < room.getHeightScale(); y++) {
            int yOffset = arrayY + y;
            for (int x = -(room.getRotatedEastPlacementOffset(rotation) - 1); x <= (room.getRotatedEastPlacementOffset(rotation) - 1); x++) {
                int xOffset = arrayX + x;
                for (int z = -(room.getRotatedNorthPlacementOffset(rotation) - 1); z <= (room.getRotatedNorthPlacementOffset(rotation) - 1); z++) {
                    int zOffset = arrayZ + z;
                    occupationArray[xOffset][yOffset][zOffset] = occupation;
                }
            }
        }
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

        Vec3i offsets = getPlacedArrayOffset(connection);
        if (offsets == null) return null;

        if (placedArrayX + offsets.getX() < 0 || placedArrayZ + offsets.getZ() < 0 || placedArrayX + offsets.getX() > generator.getOccupationArray().length - 1 || placedArrayZ + offsets.getZ() > generator.getOccupationArray().length - 1 || placedWorldPos == null) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate new room out of bounds", this);
            generator.RoomConnectionFail(room.getConnectionTag(connection, placedRotation), this, connection, true, false);
            return null;
        }

        if (generator.getOccupationArray()[placedArrayX + offsets.getX()][placedArrayY + offsets.getY()][placedArrayZ + offsets.getZ()] != GridDungeonGenerator.Occupation.AVAILABLE) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate new room in already occupied space", this);
            generator.RoomConnectionFail(room.getConnectionTag(connection, placedRotation), this, connection, false, true);
            return null;
        }

        if (Math.abs(placedArrayY + offsets.getY() - generator.getDungeon().getDungeonDepth()) > generator.getDungeon().getMaxFloorHeightFromCenterOffset() ||
                (Math.abs(placedArrayY + offsets.getY() - generator.getDungeon().getDungeonDepth()) < 0 && generator.getDungeon().isDownGenerationDisabled())) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate room outside of floor limit, placing fallback", this);
            generator.RoomConnectionFail(room.getConnectionTag(connection, placedRotation), this, connection, true, false);
            return null;
        }

        float roomEndChance = generator.getDungeon().getRoomEndChance();
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

            GeneratedRoom generatedRoom = generateRoomFromConnection(targetGridRoom, generator, placedArrayX + offsets.getX(), placedArrayY + offsets.getY(), placedArrayZ + offsets.getZ(), placedWorldPos.offset(offsets.getX() * getGridCellWidth(), offsets.getY() * getGridCellHeight(), offsets.getZ() * getGridCellWidth()), randomRoomRotation, connection.getOpposite(), random);

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

        if (placedArrayY + room.getRotatedEastSizeScale(placedRotation) < 0 || placedArrayY + room.getRotatedNorthSizeScale(placedRotation) > generator.getOccupationArray().length - 1) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate new room out of bounds", this);
            generator.RoomConnectionFail(room.getConnectionTag(Connection.UP, placedRotation), this, Connection.UP, true, false);
            return null;
        }

        if (generator.getOccupationArray()[placedArrayX + xOffset][placedArrayY + room.getHeightScale()][placedArrayZ + zOffset] != GridDungeonGenerator.Occupation.AVAILABLE) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate new room in already occupied space", this);
            generator.RoomConnectionFail(room.getConnectionTag(Connection.UP, placedRotation), this, Connection.UP, false, true);
            return null;
        }

        if (Math.abs(placedArrayY + 1 - generator.getDungeon().getDungeonDepth()) > generator.getDungeon().getMaxFloorHeightFromCenterOffset()) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate room outside of floor limit, placing fallback", this);
            generator.RoomConnectionFail(room.getConnectionTag(Connection.UP, placedRotation), this, Connection.UP, true, false); //generator.PlaceFallbackAt(placedArrayX, placedArrayY + 1, placedArrayZ, placedWorldPos.above(Height()));
            return null;
        }

        float roomEndChance = generator.getDungeon().getRoomEndChance();
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
            GeneratedRoom generatedRoom = generateRoomFromConnection(targetGridRoom, generator, placedArrayX + xOffset, placedArrayY + room.getHeightScale(), placedArrayZ + zOffset, placedWorldPos.offset(xOffset * getGridCellWidth(), room.getHeightScale() * getGridCellHeight(), zOffset * getGridCellWidth()), randomRoomRotation, Connection.DOWN, random);

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

        if (placedArrayY - 1 < 0 || placedArrayY - 1 > generator.getOccupationArray().length - 1) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate new room out of bounds", this);
            generator.RoomConnectionFail(room.getConnectionTag(Connection.DOWN, placedRotation), this, Connection.DOWN, true, false);
            return null;
        }

        if (generator.getOccupationArray()[placedArrayX + xOffset][placedArrayY - 1][placedArrayZ + zOffset] != GridDungeonGenerator.Occupation.AVAILABLE) {
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate new room in already occupied space", this);
            generator.RoomConnectionFail(room.getConnectionTag(Connection.DOWN, placedRotation), this, Connection.DOWN, false, true);
            return null;
        }

        float roomEndChance = generator.getDungeon().getRoomEndChance();
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
            AbstractGridRoom targetGridRoom = generator.GetRandomRoomByConnection(Connection.UP, room.getConnectionTag(Connection.DOWN, placedRotation), random);

            if (generator.getDungeon().isDownGenerationDisabled() || Math.abs(placedArrayY - targetGridRoom.getHeightScale() - generator.getDungeon().getDungeonDepth()) > generator.getDungeon().getMaxFloorHeightFromCenterOffset()) {
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_TICKS_DETAILS, "{}: tried to generate room outside of floor limit", this);
                triesLeft--;
                continue;
            }

            Rotation randomRoomRotation = placedRotation;
            if (room.canUpDownRotate() && targetGridRoom.canUpDownRotate()) {
                List<Rotation> allowedRoomRotations = targetGridRoom.getAllowedRotations(Connection.UP, room.getConnectionTag(Connection.DOWN, placedRotation), generator.getConnectionRules());
                randomRoomRotation = ListAndArrayUtils.getRandomFromList(allowedRoomRotations, random);
            }
            GeneratedRoom generatedRoom = generateRoomFromConnection(targetGridRoom, generator, placedArrayX + xOffset, placedArrayY - 1, placedArrayZ + zOffset, placedWorldPos.offset(xOffset * getGridCellWidth(), -getGridCellHeight(), zOffset * getGridCellWidth()), randomRoomRotation, Connection.UP, random);
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

    public Vec3i getPlacedArrayOffset(Connection placedConnection) {
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

    public Vec3i getPlacedArrayPos() {
        return new Vec3i(placedArrayX, placedArrayY, placedArrayZ);
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
        return room.getCopy();
    }
}

