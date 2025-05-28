package net.emsee.thedungeon.block.entity.portal;

import net.emsee.thedungeon.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DungeonPortalEBlockEntity extends DungeonPortalBlockEntity{
    public DungeonPortalEBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DUDGEON_PORTAL_BLOCKENTITY_E.get(), pos, blockState);
    }
}
