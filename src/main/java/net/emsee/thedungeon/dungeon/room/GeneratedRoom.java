package net.emsee.thedungeon.dungeon.room;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.dungeon.generator.GridDungeonGenerator;
import net.emsee.thedungeon.dungeon.mobSpawnRules.MobSpawnRules;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.utils.StructureUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.*;

import static net.emsee.thedungeon.TheDungeon.LOGGER;

public final class GeneratedRoom {
    static final int maxRandomRoomTries = 10;

    private final GridRoom room;

    private BlockPos placedWorldPos;
    private Rotation placedRotation;
    private int placedArrayX;
    private int placedArrayY;
    private int placedArrayZ;
    private boolean generated;

    private final Random storedRandom;

    private final Map<GridRoomUtils.Connection, Boolean> finishedConnections = new HashMap<>();

    public static GeneratedRoom generateRoom(GridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, int listPosX, int listPosY, int listPosZ, BlockPos worldPos, Rotation rotation, Random random) {
        while (toGenerate instanceof GridRoomGroup group) {
            toGenerate = group.getRandom(random);
        }

        GeneratedRoom toReturn = new GeneratedRoom(toGenerate, gridDungeonGenerator, listPosX, listPosY, listPosZ, worldPos, rotation, random);
        if (!toReturn.generated) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) LOGGER.error("{}: error with placing generating room, returning null", toReturn);
            return null;
        }
        return toReturn;
    }

    public static GeneratedRoom generateRoom(GridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, int listPosX, int listPosY, int listPosZ, BlockPos worldPos, Rotation rotation, boolean skipBorderCheck, GridDungeonGenerator.Occupation occupation, boolean forcePlace, Random random) {
        while (toGenerate instanceof GridRoomGroup group) {
            toGenerate = group.getRandom(random);
        }

        GeneratedRoom toReturn = new GeneratedRoom(toGenerate, gridDungeonGenerator, listPosX, listPosY, listPosZ, worldPos, rotation, skipBorderCheck, occupation, forcePlace, random);
        if (!toReturn.generated) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) LOGGER.error("{}: error with generating room, returning null", toReturn);
            return null;
        }
        return toReturn;
    }

    public static GeneratedRoom generateRoomFromConnection(GridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, int listPosX, int listPosY, int listPosZ, BlockPos worldPos, Rotation rotation, GridRoomUtils.Connection fromConnection, Random random) {
        while (toGenerate instanceof GridRoomGroup group) {
            toGenerate = group.getRandom(random);
        }

        GeneratedRoom toReturn = new GeneratedRoom(toGenerate, gridDungeonGenerator, listPosX, listPosY, listPosZ, worldPos, rotation, fromConnection, random);
        if (!toReturn.generated) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) LOGGER.error("{}: error with placing generating room, returning null", toReturn);
            return null;
        }
        return toReturn;
    }


    /**
     * only use for rooms you are 100% sure need to be in that place
     */
    private GeneratedRoom(GridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, int listPosX, int listPosY, int listPosZ, BlockPos worldPos, Rotation rotation, Random random) {
        storedRandom = random;
        if (toGenerate != null) {
            room = toGenerate.getCopy();
            generate(gridDungeonGenerator, listPosX, listPosY, listPosZ, worldPos, rotation, false, GridDungeonGenerator.Occupation.occupied, false);
            return;
        }
        room = null;
    }

    /**
     * only use for rooms you are 100% sure need to be in that place
     */
    private GeneratedRoom(GridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, int listPosX, int listPosY, int listPosZ, BlockPos worldPos, Rotation rotation, boolean skipBorderCheck, GridDungeonGenerator.Occupation occupation, boolean forcePlace, Random random) {
        storedRandom = random;
        if (toGenerate != null) {
            room = toGenerate.getCopy();
            generate(gridDungeonGenerator, listPosX, listPosY, listPosZ, worldPos, rotation, skipBorderCheck, occupation, forcePlace);
            return;
        }
        LOGGER.error("{}: room is null", this);
        room = null;
    }

    private GeneratedRoom(GridRoom toGenerate, GridDungeonGenerator gridDungeonGenerator, int listPosX, int listPosY, int listPosZ, BlockPos worldPos, Rotation rotation, GridRoomUtils.Connection fromConnection, Random random) {
        storedRandom = random;
        if (toGenerate != null) {
            room = toGenerate.getCopy();
            generateRoomFrom(gridDungeonGenerator, listPosX, listPosY, listPosZ, worldPos, rotation, fromConnection);
            return;
        }
        room = null;
    }

    private int width() {
        return room.getGridWidth();
    }

    private int height() {
        return room.getGridHeight();
    }

    private void generate(GridDungeonGenerator generator, int arrayX, int arrayY, int arrayZ, BlockPos worldPos, Rotation roomRotation, boolean skipBorderCheck, GridDungeonGenerator.Occupation newOccupation, boolean forcePlace) {
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) LOGGER.info("{}: trying to generate", this);
        boolean occupation = checkOccupation(generator.getOccupationArray(), arrayX, arrayY, arrayZ, skipBorderCheck, roomRotation);
        if (!forcePlace && !occupation) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: trying to generate in occupied space or out of bounds, returning fail", this);
            return;
        }

        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) LOGGER.info("{}: placing", this);

        setOccupation(generator.getOccupationArray(), arrayX, arrayY, arrayZ, newOccupation, roomRotation);

        placedWorldPos = worldPos;
        placedRotation = roomRotation;
        placedArrayX = arrayX;
        placedArrayY = arrayY;
        placedArrayZ = arrayZ;
        generated = true;

        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) LOGGER.info("{}: returning success", this);
    }

    public void finalizePlacement(ServerLevel serverLevel, StructureProcessorList processors, Random random) {
        StructureProcessorList finalProcessors = new StructureProcessorList(new ArrayList<>());
        finalProcessors.list().addAll(room.getStructureProcessors().list());
        finalProcessors.list().addAll(processors.list());
        placeTemplate(serverLevel, placedWorldPos, placedRotation, finalProcessors, random);
    }

    private void placeTemplate(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList processors, Random random) {
        StructureTemplate template = StructureUtils.getTemplate(serverLevel, room.getResourceLocation(random));
        if (template == null) {
            LOGGER.error("from {}: template was null", this);
            return;
        }
        if (centre == null) {
            LOGGER.error("from {}: Placement position was null", this);
            return;
        }
        if (roomRotation == null) {
            LOGGER.error("from {}: Placement rotation was null", this);
            return;
        }

        BlockPos origin = centre.subtract(new Vec3i(Math.round((room.getGridWidth()) * room.getRotatedEastSizeScale(Rotation.NONE) / 2f) - 1, 0, Math.round((room.getGridWidth()) * room.getRotatedNorthSizeScale(Rotation.NONE) / 2f) - 1));
        BlockPos minCorner = centre.subtract(new Vec3i(room.getGridWidth() * room.getMaxSizeScale(), 0, room.getGridWidth() * room.getMaxSizeScale()));
        BlockPos maxCorner = centre.offset(new Vec3i(room.getGridWidth() * room.getMaxSizeScale(), room.getGridHeight() * room.getHeightScale(), room.getGridWidth() * room.getMaxSizeScale()));
        RandomSource rand = RandomSource.create(serverLevel.dimension().location().hashCode() + Math.round(Math.random() * 1000));
        BoundingBox mbb = BoundingBox.fromCorners(minCorner, maxCorner);

        StructurePlaceSettings placement = new StructurePlaceSettings()
                .setRandom(rand)
                .addProcessor(JigsawReplacementProcessor.INSTANCE)
                //.addProcessor()
                .setRotationPivot(new BlockPos(room.getGridWidth() * room.getRotatedEastSizeScale(Rotation.NONE) / 2, 0, room.getGridWidth() * room.getRotatedNorthSizeScale(Rotation.NONE) / 2))
                .setRotation(roomRotation)
                .setBoundingBox(mbb)
                .setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING);

        //new RuleProcessor(ImmutableList.of(new ProcessorRule(new RandomBlockStateMatchTest(Blocks.AIR.defaultBlockState(),.1f), AlwaysTrueTest.INSTANCE, ModBlocks.DUNGEON_MOD_SPAWNER.get().defaultBlockState())))

        for (StructureProcessor processor : processors.list()) {
            placement.addProcessor(processor);
        }

        template.placeInWorld(serverLevel, origin, origin, placement, rand, Block.UPDATE_ALL);
    }

    private void generateRoomFrom(GridDungeonGenerator generator, int arrayX, int arrayY, int arrayZ, BlockPos worldPos, Rotation roomRotation, GridRoomUtils.Connection fromConnection) {
        int xOffset = 0;
        int yOffset = 0;
        int zOffset = 0;

        switch (fromConnection) {
            case north -> zOffset = room.getRotatedNorthPlacementOffset(roomRotation) - 1;
            case east -> xOffset = -(room.getRotatedEastPlacementOffset(roomRotation) - 1);
            case south -> zOffset = -(room.getRotatedNorthPlacementOffset(roomRotation) - 1);
            case west -> xOffset = room.getRotatedEastPlacementOffset(roomRotation) - 1;
            case up -> yOffset = -(room.getHeightScale() - 1);
        }

        Vec3i offsets = room.getPlaceOffsets(fromConnection, roomRotation);
        xOffset += offsets.getX();
        yOffset += offsets.getY();
        zOffset += offsets.getZ();

        BlockPos newWorldPos = worldPos.offset(new Vec3i(xOffset * room.getGridWidth(), yOffset * room.getGridWidth(), zOffset * room.getGridWidth()));
        int newArrayX = arrayX + xOffset;
        int newArrayY = arrayY + yOffset;
        int newArrayZ = arrayZ + zOffset;
        generate(generator, newArrayX, newArrayY, newArrayZ, newWorldPos, roomRotation, false, GridDungeonGenerator.Occupation.occupied, false);
    }

    private boolean checkOccupation(GridDungeonGenerator.Occupation[][][] occupationArray, int arrayX, int arrayY, int arrayZ, boolean skipBorderCheck, Rotation rotation) {
        for (int y = 0; y < room.getHeightScale(); y++) {
            int yOffset = arrayY + y;
            for (int x = -(room.getRotatedEastPlacementOffset(rotation) - 1); x <= (room.getRotatedEastPlacementOffset(rotation) - 1); x++) {
                int xOffset = arrayX + x;
                for (int z = -(room.getRotatedNorthPlacementOffset(rotation) - 1); z <= (room.getRotatedNorthPlacementOffset(rotation) - 1); z++) {
                    int zOffset = arrayZ + z;
                    try {
                        if (!(occupationArray[xOffset][yOffset][zOffset] == GridDungeonGenerator.Occupation.available))
                            return false;

                        // don't allow generation at the edge
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
                        //TheDungeon.LOGGER.info("out of list returned:"+false);
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
                    try {
                        occupationArray[xOffset][yOffset][zOffset] = occupation;
                    } catch (Exception e) {
                        LOGGER.error(e.getLocalizedMessage());
                        return;
                    }
                }
            }
        }
    }

    public GeneratedRoom generateConnections(GridDungeonGenerator generator, Random random) {
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
            LOGGER.info("{}: generating connected rooms", this);

        Map<GridRoomUtils.Connection, Integer> placedRoomConnections = GridRoomUtils.getRotatedConnections(room.getConnections(), placedRotation);
        final GeneratedRoom[] newRoom = {null};

        //Priority Map
        Map<Runnable, Integer> tasks = new HashMap<>();

        if (!finishedConnections.getOrDefault(GridRoomUtils.Connection.north, false))
            tasks.put(() -> {
                newRoom[0] = (generateHorizontalRoom(generator, placedRoomConnections, GridRoomUtils.Connection.north, random));
                finishedConnections.put(GridRoomUtils.Connection.north, true);
            }, placedRoomConnections.getOrDefault(GridRoomUtils.Connection.north, 0));
        if (!finishedConnections.getOrDefault(GridRoomUtils.Connection.east, false))
            tasks.put(() -> {
                newRoom[0] = (generateHorizontalRoom(generator, placedRoomConnections, GridRoomUtils.Connection.east, random));
                finishedConnections.put(GridRoomUtils.Connection.east, true);
            }, placedRoomConnections.getOrDefault(GridRoomUtils.Connection.east, 0));
        if (!finishedConnections.getOrDefault(GridRoomUtils.Connection.south, false))
            tasks.put(() -> {
                newRoom[0] = (generateHorizontalRoom(generator, placedRoomConnections, GridRoomUtils.Connection.south, random));
                finishedConnections.put(GridRoomUtils.Connection.south, true);
            }, placedRoomConnections.getOrDefault(GridRoomUtils.Connection.south, 0));
        if (!finishedConnections.getOrDefault(GridRoomUtils.Connection.west, false))
            tasks.put(() -> {
                newRoom[0] = (generateHorizontalRoom(generator, placedRoomConnections, GridRoomUtils.Connection.west, random));
                finishedConnections.put(GridRoomUtils.Connection.west, true);
            }, placedRoomConnections.getOrDefault(GridRoomUtils.Connection.west, 0));
        if (!finishedConnections.getOrDefault(GridRoomUtils.Connection.up, false))
            tasks.put(() -> {
                newRoom[0] = (generateUpRoom(generator, placedRoomConnections, random));
                finishedConnections.put(GridRoomUtils.Connection.up, true);
            }, placedRoomConnections.getOrDefault(GridRoomUtils.Connection.up, 0));
        if (!finishedConnections.getOrDefault(GridRoomUtils.Connection.down, false))
            tasks.put(() -> {
                newRoom[0] = (generateDownRoom(generator, placedRoomConnections, random));
                finishedConnections.put(GridRoomUtils.Connection.down, true);
            }, placedRoomConnections.getOrDefault(GridRoomUtils.Connection.down, 0));


        Runnable task = ListAndArrayUtils.getRandomFromPriorityMap(tasks, random, 1);
        if (task == null) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: finished generating all connections for room", this);
            finishedConnections.put(GridRoomUtils.Connection.north, true);
            finishedConnections.put(GridRoomUtils.Connection.east, true);
            finishedConnections.put(GridRoomUtils.Connection.south, true);
            finishedConnections.put(GridRoomUtils.Connection.west, true);
            finishedConnections.put(GridRoomUtils.Connection.up, true);
            finishedConnections.put(GridRoomUtils.Connection.down, true);
            return null;
        }
        task.run();

        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
            LOGGER.info("{}: finished generating connected room", this);

        return newRoom[0];
    }

    public boolean generatedAllConnections() {
        return
                finishedConnections.getOrDefault(GridRoomUtils.Connection.north, false) &&
                        finishedConnections.getOrDefault(GridRoomUtils.Connection.east, false) &&
                        finishedConnections.getOrDefault(GridRoomUtils.Connection.south, false) &&
                        finishedConnections.getOrDefault(GridRoomUtils.Connection.west, false) &&
                        finishedConnections.getOrDefault(GridRoomUtils.Connection.up, false) &&
                        finishedConnections.getOrDefault(GridRoomUtils.Connection.down, false);
    }

    private GeneratedRoom generateHorizontalRoom(GridDungeonGenerator generator, Map<GridRoomUtils.Connection, Integer> connections, GridRoomUtils.Connection
            connection, Random random) {
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
            LOGGER.info("{}: generating room at {}", this, connection);
        if (connections.get(connection) <= 0) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: room does not have connection", this);
            return null;
        }

        Vec3i offsets = getPlacedArrayOffset(connection);
        if (offsets == null) return null;

        if (placedArrayX + offsets.getX() < 0 || placedArrayZ + offsets.getZ() < 0 || placedArrayX + offsets.getX() > generator.getOccupationArray().length - 1 || placedArrayZ + offsets.getZ() > generator.getOccupationArray().length - 1 || placedWorldPos == null) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: tried to generate room out of bounds", this);
            return null;
        }

        if (generator.getOccupationArray()[placedArrayX + offsets.getX()][placedArrayY + offsets.getY()][placedArrayZ + offsets.getZ()] != GridDungeonGenerator.Occupation.available) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: tried to generate room in occupied space", this);
            return null;
        }

        if (Math.abs(placedArrayY + offsets.getY() - generator.getDungeon().GetDungeonDepth()) > generator.getDungeon().GetMaxFloorHeightFromCenterOffset() ||
                (Math.abs(placedArrayY + offsets.getY() - generator.getDungeon().GetDungeonDepth()) < 0 && generator.getDungeon().IsDownGenerationDisabled())) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: tried to generate room outside of floor limit, placing fallback", this);
            generator.RoomConnectionFail(room.getConnectionTag(connection, placedRotation), this, connection);
            return null;
        }

        float roomEndChance = generator.getDungeon().GetRoomEndChance();
        if (IsOverrideEndChance()) {
            roomEndChance = GetOverrideEndChance();
        }

        if (random.nextFloat() <= roomEndChance) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: room ended, placing fallback", this);
            generator.RoomConnectionFail(room.getConnectionTag(connection, placedRotation), this, connection);
            return null;
        }

        int triesLeft = maxRandomRoomTries;

        while (triesLeft>0) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: trying placement, tries left: {}", this, triesLeft);

            GridRoom targetGridRoom = generator.GetRandomRoomByConnection(GridRoomUtils.getOppositeConnection(connection), room.getConnectionTag(connection, placedRotation), random);
            if (targetGridRoom == null) {
                if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                    LOGGER.info("{}: found no new room", this);
                triesLeft--;
                continue;
            }
            List<Rotation> allowedRoomRotations = targetGridRoom.getAllowedRotations(GridRoomUtils.getOppositeConnection(connection), room.getConnectionTag(connection, placedRotation), generator.getConnectionRules());
            Rotation randomRoomRotation = ListAndArrayUtils.getRandomFromList(allowedRoomRotations, random);

            GeneratedRoom generatedRoom = generateRoomFromConnection(targetGridRoom, generator, placedArrayX + offsets.getX(), placedArrayY + offsets.getY(), placedArrayZ + offsets.getZ(), placedWorldPos.offset(offsets.getX() * width(), offsets.getY() * height(), offsets.getZ() * width()), randomRoomRotation, GridRoomUtils.getOppositeConnection(connection), random);

            if (generatedRoom == null || !generatedRoom.generated) {
                if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                    LOGGER.info("{}: placement returned failure", this);
                triesLeft--;
                continue;
            }

            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: placement success, placed ({})", this, targetGridRoom);
            return generatedRoom;
        }
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
            LOGGER.info("{}: out of tries, placing fallback", this);
        generator.RoomConnectionFail(room.getConnectionTag(connection, placedRotation), this, connection);
        return null;
    }


    private GeneratedRoom generateUpRoom(GridDungeonGenerator generator, Map<GridRoomUtils.Connection, Integer> connections, Random random) {
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
            LOGGER.info("{}: generating room at {}", this, GridRoomUtils.Connection.up);
        if (connections.get(GridRoomUtils.Connection.up) <= 0) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: room does not have connection", this);
            return null;
        }

        Vec3i offsets = room.getPlaceOffsets(GridRoomUtils.Connection.up, placedRotation);
        int xOffset = -offsets.getX();
        int zOffset = -offsets.getZ();

        if (placedArrayY + room.getRotatedEastSizeScale(placedRotation) < 0 || placedArrayY + room.getRotatedNorthSizeScale(placedRotation) > generator.getOccupationArray().length - 1) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: tried to generate room out of bounds", this);
            return null;
        }

        if (generator.getOccupationArray()[placedArrayX + xOffset][placedArrayY + room.getHeightScale()][placedArrayZ + zOffset] != GridDungeonGenerator.Occupation.available) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: tried to generate room in occupied space", this);
            return null;
        }

        if (Math.abs(placedArrayY + 1 - generator.getDungeon().GetDungeonDepth()) > generator.getDungeon().GetMaxFloorHeightFromCenterOffset()) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: tried to generate room outside of floor limit, placing fallback", this);
            generator.RoomConnectionFail(room.getConnectionTag(GridRoomUtils.Connection.up, placedRotation), this, GridRoomUtils.Connection.up);
            //generator.PlaceFallbackAt(placedArrayX, placedArrayY + 1, placedArrayZ, placedWorldPos.above(Height()));
            return null;
        }

        float roomEndChance = generator.getDungeon().GetRoomEndChance();
        if (IsOverrideEndChance()) {
            roomEndChance = GetOverrideEndChance();
        }

        if (random.nextFloat() <= roomEndChance) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: room ended, placing fallback", this);
            generator.RoomConnectionFail(room.getConnectionTag(GridRoomUtils.Connection.up, placedRotation), this, GridRoomUtils.Connection.up);
            return null;
        }

        int triesLeft = maxRandomRoomTries;

        while (triesLeft > 0) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: trying placement, tries left: {}", this, triesLeft);

            GridRoom targetGridRoom = generator.GetRandomRoomByConnection(GridRoomUtils.Connection.down, room.getConnectionTag(GridRoomUtils.Connection.up, placedRotation), random);
            if (targetGridRoom == null) {
                if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                    LOGGER.info("{}: found no new room", this);
                triesLeft--;
                continue;
            }

            Rotation randomRoomRotation;
            if (room.canUpDownRotate() && targetGridRoom.canUpDownRotate()) {
                List<Rotation> allowedRoomRotations = targetGridRoom.getAllowedRotations(GridRoomUtils.Connection.down, room.getConnectionTag(GridRoomUtils.Connection.up, placedRotation), generator.getConnectionRules());
                randomRoomRotation = ListAndArrayUtils.getRandomFromList(allowedRoomRotations, random);
            } else randomRoomRotation = placedRotation;
            GeneratedRoom generatedRoom = generateRoomFromConnection(targetGridRoom, generator, placedArrayX + xOffset, placedArrayY + room.getHeightScale(), placedArrayZ + zOffset, placedWorldPos.offset(xOffset * width(), room.getHeightScale() * height(), zOffset * width()), randomRoomRotation, GridRoomUtils.Connection.down, random);

            if (generatedRoom == null || !generatedRoom.generated) {
                if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                    LOGGER.info("{}: placement returned failure", this);
                triesLeft--;
                continue;
            }

            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: placement success, placed ({})", this, targetGridRoom);
            return generatedRoom;
        }

        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
            LOGGER.info("{}: out of tries, placing fallback", this);
        generator.RoomConnectionFail(room.getConnectionTag(GridRoomUtils.Connection.up, placedRotation), this, GridRoomUtils.Connection.up);
        return null;

    }


    private GeneratedRoom generateDownRoom(GridDungeonGenerator generator, Map<GridRoomUtils.Connection, Integer> connections, Random random) {
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
            LOGGER.info("{}: generating room at {}", this, GridRoomUtils.Connection.down);
        if (connections.get(GridRoomUtils.Connection.down)<=0) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: room does not have connection", this);
            return null;
        }

        Vec3i offsets = room.getPlaceOffsets(GridRoomUtils.Connection.down, placedRotation);
        int xOffset = -offsets.getX();
        int zOffset = -offsets.getZ();

        if (placedArrayY - 1 < 0 || placedArrayY - 1 > generator.getOccupationArray().length - 1) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: tried to generate room out of bounds", this);
            return null;
        }

        if (generator.getOccupationArray()[placedArrayX + xOffset][placedArrayY - 1][placedArrayZ + zOffset] != GridDungeonGenerator.Occupation.available) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) LOGGER.info("{}: tried to generate room in occupied space", this);
            return null;
        }

        float roomEndChance = generator.getDungeon().GetRoomEndChance();
        if (IsOverrideEndChance()) {
            roomEndChance = GetOverrideEndChance();
        }

        if (random.nextFloat() > roomEndChance) {
            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: room ended, placing fallback", this);
            generator.RoomConnectionFail(room.getConnectionTag(GridRoomUtils.Connection.down, placedRotation), this, GridRoomUtils.Connection.down);
            return null;
        }


        int triesLeft = maxRandomRoomTries;

        while (triesLeft > 0) {
            GridRoom targetGridRoom = generator.GetRandomRoomByConnection(GridRoomUtils.Connection.up, room.getConnectionTag(GridRoomUtils.Connection.down, placedRotation), random);

            if (generator.getDungeon().IsDownGenerationDisabled() || Math.abs(placedArrayY - targetGridRoom.getHeightScale() - generator.getDungeon().GetDungeonDepth()) > generator.getDungeon().GetMaxFloorHeightFromCenterOffset()) {
                if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                    LOGGER.info("{}: tried to generate room outside of floor limit", this);
                triesLeft--;
                continue;
            }

            Rotation randomRoomRotation = placedRotation;
            if (room.canUpDownRotate() && targetGridRoom.canUpDownRotate()) {
                List<Rotation> allowedRoomRotations = targetGridRoom.getAllowedRotations(GridRoomUtils.Connection.up, room.getConnectionTag(GridRoomUtils.Connection.down, placedRotation), generator.getConnectionRules());
                randomRoomRotation = ListAndArrayUtils.getRandomFromList(allowedRoomRotations, random);
            }
            GeneratedRoom generatedRoom = generateRoomFromConnection(targetGridRoom, generator, placedArrayX + xOffset, placedArrayY - 1, placedArrayZ + zOffset, placedWorldPos.offset(xOffset * width(), -height(), zOffset * width()), randomRoomRotation, GridRoomUtils.Connection.up, random);
            if (generatedRoom == null || !generatedRoom.generated) {
                if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                    LOGGER.info("{}: placement returned failure", this);
                triesLeft--;
                continue;
            }

            if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
                LOGGER.info("{}: placement success, placed ({})", this, targetGridRoom);
            return generatedRoom;
        }
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL))
            LOGGER.info("{}: out of tries, placing fallback", this);
        generator.RoomConnectionFail(room.getConnectionTag(GridRoomUtils.Connection.down, placedRotation), this, GridRoomUtils.Connection.down);
        return null;
    }

    @Override
    public String toString() {
        return "GeneratedRoom:" + room;
    }

    public int getPriority() {
        return room.getGenerationPriority();
    }

    public boolean IsOverrideEndChance() {
        return room.hasOverrideEndChance();
    }

    public float GetOverrideEndChance() {
        return room.getOverrideEndChance();
    }

    public void spawnMobs(ServerLevel level) {
        for (MobSpawnRules rule : room.getSpawnRules()) {
            rule.spawn(level, placedWorldPos, placedRotation);
        }
    }

    public boolean hasMobSpawns() {
        return room.hasMobSpawns();
    }

    public Vec3i getPlacedArrayOffset(GridRoomUtils.Connection placedConnection) {
        Map<GridRoomUtils.Connection, Integer> connections = GridRoomUtils.getRotatedConnections(room.getConnections(), placedRotation);
        if (connections.get(placedConnection)<=0) return null;

        int unitXOffset = 0;
        int yOffset = 0;
        int unitZOffset = 0;
        switch (placedConnection) {
            case north -> unitZOffset = -1;
            case east -> unitXOffset = 1;
            case south -> unitZOffset = 1;
            case west -> unitXOffset = -1;
            case up -> yOffset = room.getHeightScale();
            case down -> yOffset = -1;
        }

        int xOffset = unitXOffset * room.getRotatedEastPlacementOffset(placedRotation);
        int zOffset = unitZOffset * room.getRotatedNorthPlacementOffset(placedRotation);

        Vec3i offsets = room.getPlaceOffsets(placedConnection, placedRotation);
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

    public int getGridWidth() {
        return room.gridWidth;
    }

    public int getGridHeight() {
        return room.gridHeight;
    }

    public GridRoom getRoom() {
        return room.getCopy();
    }
}

