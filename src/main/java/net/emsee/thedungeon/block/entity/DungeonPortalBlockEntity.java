package net.emsee.thedungeon.block.entity;

import net.emsee.thedungeon.dungeon.GlobalDungeonManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DungeonPortalBlockEntity extends BlockEntity {
    private int exitPortalID = -1;

    public DungeonPortalBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DUDGEON_PORTAL_BLOCKENTITY.get(), pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        tag.putInt("exitID", exitPortalID);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        exitPortalID = tag.getInt("exitID");
        super.loadAdditional(tag, registries);
    }

    public int getExitID(MinecraftServer server) {
        if (exitPortalID < 0)
            exitPortalID = GlobalDungeonManager.giveRandomPortalID(server);
        return exitPortalID;
    }
}
