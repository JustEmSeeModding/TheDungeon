package net.emsee.thedungeon.structureProcessor;

import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public abstract class BlockPalletReplacementProcessor extends AbstractReplacementProcessor {
    protected abstract Map<Block, WeightedMap.Int<Supplier<BlockState>>> getReplacements();

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos offset, BlockPos pos,
                                                        StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo,
                                                        StructurePlaceSettings settings, @Nullable StructureTemplate template) {

        RandomSource random = settings.getRandom(relativeBlockInfo.pos());
        WeightedMap.Int<Supplier<BlockState>> options = this.getReplacements().get(relativeBlockInfo.state().getBlock());

        if (options == null) return relativeBlockInfo;

        Supplier<BlockState> newStateSupplier = options.getRandom(random);
        if (newStateSupplier == null) return relativeBlockInfo;

        BlockState newBlockstate = newStateSupplier.get();
        if (newBlockstate == null) return relativeBlockInfo;

        BlockState oldBlockstate = relativeBlockInfo.state();
        newBlockstate = copyAllProperties(oldBlockstate, newBlockstate);

        return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), newBlockstate, relativeBlockInfo.nbt());
    }
}