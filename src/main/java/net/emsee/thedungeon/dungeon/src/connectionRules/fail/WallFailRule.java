package net.emsee.thedungeon.dungeon.src.connectionRules.fail;

import net.emsee.thedungeon.dungeon.src.connectionRules.FailRule;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.room.GeneratedRoom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
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

    private int fillX;
    private int fillY;
    private int fillZ;

    private int maxFillX;
    private int maxFillZ;

    private boolean exitMarkedObstructed;

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
        fillY = height - 1;
    }


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
        fillY = height - 1;
    }


    public FailRule withStructureProcessor(StructureProcessor processor) {
        processorList.list().add(processor);
        return this;
    }


    @Override
    public void applyFailTick(GeneratedRoom room, Connection connection, ServerLevel level, StructureProcessorList processors, boolean wouldPlaceFallback, boolean exitObstructed) {
        exitMarkedObstructed = exitObstructed;
        if (offsetOut && exitObstructed) return;
        StructureProcessorList finalProcessors = new StructureProcessorList(new ArrayList<>());
        finalProcessors.list().addAll(processors.list());
        finalProcessors.list().addAll(processorList.list());
        BlockPos wallCenter = findWallCenter(room, connection);
        if (wallCenter==null) return;
        Fill(wallCenter, level, connection, finalProcessors);
    }


    @Override
    public boolean isFinished() {
        return fillY<0 || offsetOut && exitMarkedObstructed;
    }


    private BlockPos findWallCenter(GeneratedRoom room, Connection connection) {
        BlockPos roomCenter = room.getPlacedWorldPos();
        Vec3i connectionArrayOffset = room.getPlacedArrayOffset(connection);
        if (connection == Connection.UP) {
            return null;
        }
        if (connection == Connection.DOWN) {
            return null;
        }
        if (connectionArrayOffset==null) return null;
        int unitXOffset = 0;
        int unitZOffset = 0;
        switch (connection) {
            case NORTH -> unitZOffset = 1;
            case EAST -> unitXOffset = -1;
            case SOUTH -> unitZOffset = -1;
            case WEST -> unitXOffset = 1;
        }
        return roomCenter.offset((connectionArrayOffset.getX()*room.getGridCellWidth() + unitXOffset * (room.getGridCellWidth()/2+(offsetOut?0:1))), connectionArrayOffset.getY()*room.getGridCellHeight() + heightOffset, connectionArrayOffset.getZ()*room.getGridCellWidth() + unitZOffset * (room.getGridCellWidth()/2+(offsetOut?0:1)));

    }


    private void Fill(BlockPos wallCenter, ServerLevel level, Connection connection, StructureProcessorList processors) {
        int i = blocksPerLoop;
        while (i > 0) {
            if (fillY < 0) return;
            switch (connection) {
                case NORTH, SOUTH -> {
                    fillZ = 0;
                    maxFillZ = 0;
                }
                case EAST, WEST -> {
                    fillX = 0;
                    maxFillX = 0;
                }

            }

            BlockPos placePos = wallCenter.offset(fillX, fillY, fillZ);
            level.setBlockAndUpdate(placePos, processBlockForPlacement(level,placePos, block.get().defaultBlockState(),processors));
            switch (connection) {
                case NORTH, SOUTH -> {
                    fillX++;
                    if (fillX > maxFillX) {
                        fillX = -width / 2;
                        fillY--;
                    }
                }
                case EAST, WEST -> {
                    fillZ++;
                    if (fillZ > maxFillZ) {
                        fillZ = -width / 2;
                        fillY--;
                    }
                }
            }
            i--;
        }
    }

    private BlockState processBlockForPlacement(ServerLevel level, BlockPos globalPos, BlockState initialState, StructureProcessorList processors) {
        if (processors.list().isEmpty()) return initialState;
        // Create a StructureBlockInfo for the block
        StructureTemplate.StructureBlockInfo blockInfo = new StructureTemplate.StructureBlockInfo(
                globalPos,
                initialState,
                null
        );

        for (StructureProcessor processor : processors.list()) {
            assert blockInfo != null;
            blockInfo = processor.processBlock(level, new BlockPos(0,0,0), globalPos, blockInfo, blockInfo, new StructurePlaceSettings());
        }
        // Return the processed block state (or fallback to initial state)
        return blockInfo != null ? blockInfo.state() : initialState;
    }

    /**
     * Indicates that fallback block placement should be halted after this rule is applied.
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
