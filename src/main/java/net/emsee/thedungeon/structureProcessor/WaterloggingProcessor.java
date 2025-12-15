package net.emsee.thedungeon.structureProcessor;

import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets.BlackstoneToDeepslateProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;

public class WaterloggingProcessor  extends StructureProcessor {
    public static final WaterloggingProcessor INSTANCE = new WaterloggingProcessor();

    public static final MapCodec<WaterloggingProcessor> CODEC = MapCodec.unit(() -> INSTANCE);


    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos offset, BlockPos pos,
                                                        StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo,
                                                        StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        final BlockState oldState = relativeBlockInfo.state();
        final Block block = oldState.getBlock();
        BlockState newBlockstate = relativeBlockInfo.state();

        /*if (block instanceof SimpleWaterloggedBlock) {
            newBlockstate = newBlockstate.setValue(BlockStateProperties.WATERLOGGED, true);
        }
        else*/
        if (oldState.hasProperty(BlockStateProperties.WATERLOGGED)) {
            newBlockstate = newBlockstate.setValue(BlockStateProperties.WATERLOGGED, true);
        }
        else if(block.defaultBlockState().isAir()) {
            newBlockstate = Blocks.WATER.defaultBlockState();
        }

        return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), newBlockstate, relativeBlockInfo.nbt());
    }
}
