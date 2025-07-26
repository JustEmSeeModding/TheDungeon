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

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos offset, BlockPos pos,
                                                        StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo,
                                                        StructurePlaceSettings settings, @Nullable StructureTemplate template) {

        RandomSource random = settings.getRandom(relativeBlockInfo.pos());
        WeightedMap.Int<ReplaceInstance> allOptions = this.getReplacements().get(relativeBlockInfo.state().getBlock());

        final WeightedMap.Int<Supplier<BlockState>> options = new WeightedMap.Int<>();

        if (allOptions == null || allOptions.isEmpty()) return relativeBlockInfo;

        allOptions.forEach((instance, weight) -> {
            if (instance.test(level, offset, pos, blockInfo, relativeBlockInfo, settings, template))
                options.put(instance.stateSupplier, weight);
        });

        if (options.isEmpty()) return relativeBlockInfo;

        Supplier<BlockState> newStateSupplier = options.getRandom(random);
        if (newStateSupplier == null) return relativeBlockInfo;

        BlockState newBlockstate = newStateSupplier.get();
        if (newBlockstate == null) return relativeBlockInfo;

        BlockState oldBlockstate = relativeBlockInfo.state();
        newBlockstate = copyAllProperties(oldBlockstate, newBlockstate);

        return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), newBlockstate, relativeBlockInfo.nbt());
    }
}