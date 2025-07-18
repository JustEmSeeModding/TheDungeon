package net.emsee.thedungeon.structureProcessor;

import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public abstract class BlobsReplacementProcessor extends BasicReplacementProcessor{
    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        RandomSource randomsource = settings.getRandom(relativeBlockInfo.pos());
        WeightedMap.Int<Supplier<BlockState>> options = this.getReplacements().get(relativeBlockInfo.state().getBlock());
        if (options == null)
            return relativeBlockInfo;
        BlockState newBlockstate = options.getRandom(randomsource).get();
        if (newBlockstate == null) {
            return relativeBlockInfo;
        } else {
            BlockState oldBlockstate = relativeBlockInfo.state();
            newBlockstate = copyAllProperties(oldBlockstate, newBlockstate);

            return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), newBlockstate, relativeBlockInfo.nbt());
        }
    }
}
