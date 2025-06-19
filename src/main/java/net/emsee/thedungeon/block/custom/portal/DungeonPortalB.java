package net.emsee.thedungeon.block.custom.portal;

import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.entity.portal.DungeonPortalBBlockEntity;
import net.emsee.thedungeon.dungeon.util.DungeonRank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DungeonPortalB extends DungeonPortal{
    public static final MapCodec<DungeonPortal> CODEC = simpleCodec(DungeonPortalB::new);

    public DungeonPortalB(Properties properties) {
        super(properties);
    }

    @Override
    public DungeonRank getExitRank() {
        return DungeonRank.B;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new DungeonPortalBBlockEntity(blockPos, blockState);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
