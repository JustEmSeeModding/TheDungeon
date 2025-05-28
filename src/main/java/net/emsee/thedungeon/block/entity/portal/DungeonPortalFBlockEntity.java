package net.emsee.thedungeon.block.entity.portal;

import net.emsee.thedungeon.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DungeonPortalFBlockEntity extends DungeonPortalBlockEntity{
    public DungeonPortalFBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DUDGEON_PORTAL_BLOCKENTITY_F.get(), pos, blockState);
    }
}
