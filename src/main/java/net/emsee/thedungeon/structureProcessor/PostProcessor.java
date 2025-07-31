package net.emsee.thedungeon.structureProcessor;

import net.emsee.thedungeon.utils.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;

/**
 *  post-processors are custom used in the dungeon generators.
 * <p>
 *  use these when you want to have the structure placed as this calculates.
 *  <p>
 *  can still be used as a normal processor in some cases, but results are not guaranteed.
 */
public interface PostProcessor {
    BlockUtils.ForEachMethod getMethod();

    /**
     * skipping blocks hopefully adds some performance
     */
    default boolean skipBlockForProcessing(LevelReader level, BlockPos pos, BlockState state) {
        return level.getMaxBuildHeight() > pos.getY() || level.getMinBuildHeight() < pos.getY();
    }
}
