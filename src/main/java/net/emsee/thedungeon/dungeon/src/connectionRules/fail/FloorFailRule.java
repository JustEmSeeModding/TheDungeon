package net.emsee.thedungeon.dungeon.src.connectionRules.fail;

import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.connectionRules.FailRule;
import net.emsee.thedungeon.dungeon.src.room.GeneratedRoom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;  
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.function.Supplier;

public class FloorFailRule extends FailRule {
    private final StructureProcessorList processorList = new StructureProcessorList(new ArrayList<>());
    private final int width;
    private final int blocksPerLoop;
    private final boolean offsetOut;
    private final Supplier<BlockState> block;

    private int fillX;
    private int fillZ;

    private int maxFillX;
    private int maxFillZ;

    private boolean exitMarkedObstructed;

    public FloorFailRule(String tag, int width, boolean offsetOut, Supplier<BlockState> block) {
        super(tag, false, true);
        this.width = width;
        this.blocksPerLoop = 1;
        this.offsetOut = offsetOut;
        this.block = block;


        fillX = -width / 2;
        maxFillX = width / 2;
        fillZ = -width / 2;
        maxFillZ = width / 2;
    }


    public FloorFailRule(String tag, int width, boolean offsetOut, Supplier<BlockState> block, int blocksPerLoop) {
        super(tag, false, true);
        this.width = width;
        this.blocksPerLoop = blocksPerLoop;
        this.offsetOut = offsetOut;
        this.block = block;


        fillX = -width / 2;
        maxFillX = width / 2;
        fillZ = -width / 2;
        maxFillZ = width / 2;
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
        BlockPos floorCenter = findFloorCenter(room, connection);
        if (floorCenter==null) return;
        Fill(floorCenter, level, connection, finalProcessors);
    }


    @Override
    public boolean isFinished() {
        return fillZ>maxFillZ || offsetOut && exitMarkedObstructed;
    }


    private BlockPos findFloorCenter(GeneratedRoom room, Connection connection) {
        BlockPos roomCenter = room.getPlacedWorldPos();
        Vec3i connectionArrayOffset = room.getPlacedArrayOffset(connection);
        if (connectionArrayOffset == null) return null;
        if (connection == Connection.UP) {
            return roomCenter.offset(
                    connectionArrayOffset.getX() * room.getGridCellWidth(),
                    connectionArrayOffset.getY() * room.getGridCellHeight() - (offsetOut ? 0 : 1),
                    connectionArrayOffset.getZ() * room.getGridCellWidth()
            );
        }
        if (connection == Connection.DOWN) {
            return roomCenter.offset(
                    connectionArrayOffset.getX() * room.getGridCellWidth(),
                    connectionArrayOffset.getY() * room.getGridCellHeight() + room.getGridCellHeight() - (offsetOut ? 1 : 0),
                    connectionArrayOffset.getZ() * room.getGridCellWidth()
            );
        }
        return null;
    }


    private void Fill(BlockPos wallCenter, ServerLevel level, Connection connection, StructureProcessorList processors) {
        int i = blocksPerLoop;
        while (i > 0) {
            if (fillZ > maxFillZ) return;
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

            BlockPos placePos = wallCenter.offset(fillX, 0, fillZ);
            level.setBlockAndUpdate(placePos, processBlockForPlacement(level, placePos, block.get(), processors));

            fillX++;
            if (fillX > maxFillX) {
                fillX = -width / 2;
                fillZ++;
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
            blockInfo = processor.process(level, new BlockPos(0,0,0), globalPos, blockInfo, blockInfo, new StructurePlaceSettings(),null);
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
        FloorFailRule toReturn = new FloorFailRule(tag, width, offsetOut, block, blocksPerLoop);
        for (StructureProcessor processor : processorList.list())
            toReturn.withStructureProcessor(processor);
        return toReturn;
    }
}
