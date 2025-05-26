package net.emsee.thedungeon.dungeon.connectionRules.fail;

import net.emsee.thedungeon.dungeon.connectionRules.FailRule;
import net.emsee.thedungeon.dungeon.room.GeneratedRoom;
import net.emsee.thedungeon.dungeon.room.GridRoomUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.function.Supplier;

public class WallFailRule extends FailRule {
    private final StructureProcessorList processorList = new StructureProcessorList(new ArrayList<>());
    private final int width;
    private final int height;
    private final int heightOffset;
    private final int blocksPerLoop;
    private final boolean offsetOut;
    private final Supplier<Block> block;

    int fillX;
    int fillY;
    int fillZ;

    int maxFillX;
    int maxFillZ;

    public WallFailRule(String tag, int width, int height, int heightOffset, boolean offsetOut, Supplier<Block> block) {
        super(tag);
        this.width = width;
        this.height = height;
        this.heightOffset = heightOffset;
        this.blocksPerLoop = 1;
        this.offsetOut = offsetOut;
        this.block = block;


        fillX = -width / 2;
        maxFillX = width / 2;
        fillZ = -width / 2;
        maxFillZ = width / 2;
        fillY = 0;
    }

    /**
     * Constructs a WallFailRule with specified dimensions, offset, block supplier, and blocks per loop.
     *
     * @param tag           identifier for the rule
     * @param width         width of the wall to generate
     * @param height        height of the wall to generate
     * @param heightOffset  vertical offset from the base position
     * @param offsetOut     whether to offset the wall outward from the connection
     * @param block         supplier providing the block type to place
     * @param blocksPerLoop number of blocks to place per fill operation
     */
    public WallFailRule(String tag, int width, int height, int heightOffset, boolean offsetOut, Supplier<Block> block, int blocksPerLoop) {
        super(tag);
        this.width = width;
        this.height = height;
        this.heightOffset = heightOffset;
        this.blocksPerLoop = blocksPerLoop;
        this.offsetOut = offsetOut;
        this.block = block;


        fillX = -width / 2;
        maxFillX = width / 2;
        fillZ = -width / 2;
        maxFillZ = width / 2;
        fillY = 0;
    }

    /**
     * Adds a structure processor to be applied during block placement and returns this rule for chaining.
     *
     * @param processor the structure processor to add
     * @return this rule instance with the processor added
     */
    public FailRule withStructureProcessor(StructureProcessor processor) {
        processorList.list().add(processor);
        return this;
    }

    /****
     * Applies the wall fail rule by placing blocks along the wall of a room, using the specified structure processors.
     *
     * Combines the provided and internal structure processors, determines the wall center based on the room and connection,
     * and fills the wall area with processed blocks if a valid wall center is found.
     *
     * @param room the generated room in which to apply the wall rule
     * @param connection the connection indicating which wall to fill
     * @param level the server level where blocks are placed
     * @param processors additional structure processors to apply during block placement
     */
    @Override
    public void ApplyFail(GeneratedRoom room, GridRoomUtils.Connection connection, ServerLevel level, StructureProcessorList processors) {
        StructureProcessorList finalProcessors = new StructureProcessorList(new ArrayList<>());
        finalProcessors.list().addAll(processors.list());
        finalProcessors.list().addAll(processorList.list());
        BlockPos wallCenter = findWallCenter(room, connection);
        if (wallCenter==null) {
            return;
        }
        Fill(wallCenter, level, connection, finalProcessors);
    }

    /****
     * Determines whether the wall block placement process has completed.
     *
     * @return true if all blocks up to the configured height have been placed; false otherwise
     */
    @Override
    public boolean isFinished() {
        return fillY>height-1;
    }

    /**
     * Calculates the central world position for placing a wall based on the room's location and the specified connection direction.
     *
     * Returns null if the connection is vertical (up or down) or if the connection offset is unavailable.
     *
     * @param room the generated room for which to find the wall center
     * @param connection the direction of the wall relative to the room
     * @return the block position representing the wall's center, or null if not applicable
     */
    private BlockPos findWallCenter(GeneratedRoom room, GridRoomUtils.Connection connection) {
        BlockPos roomCenter = room.getPlacedWorldPos();
        Vec3i connectionArrayOffset = room.getPlacedArrayOffset(connection);
        if (connection == GridRoomUtils.Connection.up) {
            return null;
        }
        if (connection == GridRoomUtils.Connection.down) {
            return null;
        }
        if (connectionArrayOffset==null) return null;
        int unitXOffset = 0;
        int unitZOffset = 0;
        switch (connection) {
            case north -> unitZOffset = 1;
            case east -> unitXOffset = -1;
            case south -> unitZOffset = -1;
            case west -> unitXOffset = 1;
        }
        return roomCenter.offset((connectionArrayOffset.getX()*room.getGridWidth() + unitXOffset * (room.getGridWidth()/2+(offsetOut?0:1))), connectionArrayOffset.getY()*room.getGridHeight() + heightOffset, connectionArrayOffset.getZ()*room.getGridWidth() + unitZOffset * (room.getGridWidth()/2+(offsetOut?0:1)));

    }

    /**
     * Places a batch of blocks along the wall at the specified center position, applying structure processors to each block.
     *
     * Iterates up to the configured number of blocks per loop, incrementally filling the wall in the direction of the connection.
     * For each block, applies all provided structure processors before placement. Stops filling if the configured wall height is reached.
     *
     * @param wallCenter the central position of the wall where filling begins
     * @param connection the direction of the wall relative to the room
     * @param processors the list of structure processors to apply to each block before placement
     */
    public void Fill(BlockPos wallCenter, ServerLevel level, GridRoomUtils.Connection connection, StructureProcessorList processors) {
        int i = blocksPerLoop;
        while (i > 0) {
            if (fillY > height - 1) return;
            switch (connection) {
                case north, south -> {
                    fillZ = 0;
                    maxFillZ = 0;
                }
                case east, west -> {
                    fillX = 0;
                    maxFillX = 0;
                }

            }

            BlockPos placePos = wallCenter.offset(fillX, fillY, fillZ);
            level.setBlockAndUpdate(placePos, processBlockForPlacement(level,placePos, block.get().defaultBlockState(),processors));
            switch (connection) {
                case north, south -> {
                    fillX++;
                    if (fillX > maxFillX) {
                        fillX = -width / 2;
                        fillY++;
                    }
                }
                case east, west -> {
                    fillZ++;
                    if (fillZ > maxFillZ) {
                        fillZ = -width / 2;
                        fillY++;
                    }
                }
            }
            i--;
        }
    }

    /**
     * Applies a list of structure processors to a block before placement and returns the resulting block state.
     *
     * If any processor modifies the block, the processed state is returned; otherwise, the original state is used.
     *
     * @param globalPos the world position where the block will be placed
     * @param initialState the original block state to process
     * @param processors the list of structure processors to apply
     * @return the processed block state after applying all processors, or the original state if unchanged
     */
    private BlockState processBlockForPlacement(ServerLevel level, BlockPos globalPos, BlockState initialState, StructureProcessorList processors) {
        StructureTemplate template = new StructureTemplate();

        // Create a StructureBlockInfo for the block
        StructureTemplate.StructureBlockInfo blockInfo = new StructureTemplate.StructureBlockInfo(
                globalPos,
                initialState,
                null
        );

        StructurePlaceSettings placementData = new StructurePlaceSettings();
        for (StructureProcessor processor : processors.list()) {
            blockInfo = processor.processBlock(level, new BlockPos(0,0,0), globalPos, blockInfo, blockInfo, new StructurePlaceSettings());
            placementData.addProcessor(processor);
        }
        // Return the processed block state (or fallback to initial state)
        return blockInfo != null ? blockInfo.state() : initialState;
    }

    /**
     * Indicates that fallback block placement should be halted after this rule is applied.
     *
     * @return true, signaling to stop any further fallback placement attempts
     */
    @Override
    public boolean stopFallbackPlacement() {
        return true;
    }

    /**
     * Creates a copy of this WallFailRule, including all configuration parameters and structure processors.
     *
     * @return a new WallFailRule instance with identical settings and processors
     */
    @Override
    public FailRule getCopy() {
        WallFailRule toReturn = new WallFailRule(tag, width, height, heightOffset, offsetOut, block, blocksPerLoop);
        for (StructureProcessor processor : processorList.list())
            toReturn.withStructureProcessor(processor);
        return toReturn;
    }
}
