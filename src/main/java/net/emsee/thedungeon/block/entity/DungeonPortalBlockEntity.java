package net.emsee.thedungeon.block.entity;

import net.emsee.thedungeon.dungeon.GlobalDungeonManager;
import net.emsee.thedungeon.dungeon.dungeon.Dungeon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DungeonPortalBlockEntity extends BlockEntity {
    private int exitPortalID = -1;
    private Dungeon.DungeonRank exitRank = Dungeon.DungeonRank.F;

    public DungeonPortalBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DUDGEON_PORTAL_BLOCKENTITY.get(), pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        tag.putInt("exitID", exitPortalID);
        tag.putString("exitRank", exitRank.getName());
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        exitPortalID = tag.getInt("exitID");
        exitRank = Dungeon.DungeonRank.getByName(tag.getString("exitRank"));
        super.loadAdditional(tag, registries);
    }

    public int getExitID(MinecraftServer server) {
        if (exitPortalID < 0)
            exitPortalID = GlobalDungeonManager.giveRandomPortalID(server, exitRank);
        return exitPortalID;
    }

    public Dungeon.DungeonRank getExitRank(MinecraftServer server) {
        return exitRank;
    }
}
