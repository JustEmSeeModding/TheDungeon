package net.emsee.thedungeon.block.custom.portal;

import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.entity.portal.DungeonPortalExitBlockEntity;
import net.emsee.thedungeon.dungeon.util.DungeonRank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DungeonPortalExit extends DungeonPortal{
    public static final MapCodec<DungeonPortal> CODEC = simpleCodec(DungeonPortalExit::new);

    public DungeonPortalExit(Properties properties) {
        super(properties);
    }

    @Override
    public DungeonRank getExitRank() {
        return DungeonRank.F;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new DungeonPortalExitBlockEntity(blockPos, blockState);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
