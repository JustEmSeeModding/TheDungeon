package net.emsee.thedungeon.block.custom.portal;

import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.entity.portal.DungeonPortalBlockEntity;
import net.emsee.thedungeon.block.entity.ModBlockEntities;
import net.emsee.thedungeon.block.entity.portal.DungeonPortalCBlockEntity;
import net.emsee.thedungeon.dungeon.dungeon.Dungeon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DungeonPortalC extends DungeonPortal{
    public static final MapCodec<DungeonPortal> CODEC = simpleCodec(DungeonPortalC::new);

    public DungeonPortalC(Properties properties) {
        super(properties);
    }

    @Override
    public Dungeon.DungeonRank getExitRank() {
        return Dungeon.DungeonRank.C;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new DungeonPortalCBlockEntity(blockPos, blockState);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
