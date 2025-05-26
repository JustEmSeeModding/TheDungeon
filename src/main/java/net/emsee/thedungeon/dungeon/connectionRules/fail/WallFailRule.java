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

    public FailRule withStructureProcessor(StructureProcessor processor) {
        processorList.list().add(processor);
        return this;
    }

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

    @Override
    public boolean isFinished() {
        return fillY>height-1;
    }

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

    @Override
    public boolean stopFallbackPlacement() {
        return true;
    }

    @Override
    public FailRule getCopy() {
        WallFailRule toReturn = new WallFailRule(tag, width, height, heightOffset, offsetOut, block, blocksPerLoop);
        for (StructureProcessor processor : processorList.list())
            toReturn.withStructureProcessor(processor);
        return toReturn;
    }
}
