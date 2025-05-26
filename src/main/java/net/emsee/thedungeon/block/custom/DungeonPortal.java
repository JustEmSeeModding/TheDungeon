package net.emsee.thedungeon.block.custom;

import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.entity.DungeonPortalBlockEntity;
import net.emsee.thedungeon.dungeon.GlobalDungeonManager;
import net.emsee.thedungeon.events.ModDungeonDimensionEvents;
import net.emsee.thedungeon.item.custom.DungeonItem;
import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DungeonPortal extends BaseEntityBlock implements IDungeonCarryItem {
    public static final MapCodec<DungeonPortal> CODEC = simpleCodec(DungeonPortal::new);

    public DungeonPortal(Properties properties) {
        super(properties.randomTicks());
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof DungeonPortalBlockEntity entity) {
            MinecraftServer server = level.getServer();
            if (player.isCreative() || GlobalDungeonManager.isOpen(server)) {
                ModDungeonDimensionEvents.PlayerTeleportDungeon(player, entity.getExitID(server));
            } else {
                player.displayClientMessage(Component.translatable("message.thedungeon.dungeon_portal.dungeon_closed"), false);
            }
        } else {
            level.playLocalSound(pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1,1,false);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(DungeonItem.DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new DungeonPortalBlockEntity(blockPos, blockState);
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!level.isClientSide && level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            GlobalDungeonManager.addPortalLocation(level.getServer(), pos);
        }
        super.randomTick(state, level, pos, random);
    }

    @Override
    protected void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide && level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            GlobalDungeonManager.addPortalLocation(level.getServer(), pos);
        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }


    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock() && level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            GlobalDungeonManager.removePortalLocation(level.getServer(), pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
