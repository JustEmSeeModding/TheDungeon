package net.emsee.thedungeon.block.custom.portal;

import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.block.entity.portal.DungeonPortalBlockEntity;
import net.emsee.thedungeon.dungeon.util.DungeonRank;
import net.emsee.thedungeon.dungeon.GlobalDungeonManager;
import net.emsee.thedungeon.events.ModDungeonCalledEvents;
import net.emsee.thedungeon.item.custom.DungeonItem;
import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.emsee.thedungeon.worldSaveData.DungeonSaveData;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class DungeonPortal extends BaseEntityBlock implements IDungeonCarryItem {
    private final long timeLeftWarning = 2400;

    public DungeonPortal(Properties properties) {
        super(properties.randomTicks());
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof DungeonPortalBlockEntity entity) {
            MinecraftServer server = level.getServer();
            if (player.isCreative() || GlobalDungeonManager.isOpen(server, getExitRank())) {
                if (timeCheck(player, server) || player.isCrouching() || player.isCreative())
                    ModDungeonCalledEvents.playerTeleportDungeon(player, entity.getExitID(server, this), getExitRank());
            } else {
                player.displayClientMessage(Component.translatable("message.thedungeon.dungeon_portal.dungeon_closed"), true);
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
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!level.isClientSide && level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            GlobalDungeonManager.addPortalLocation(level.getServer(), pos, DungeonRank.getClosestTo(pos));
        }
        super.randomTick(state, level, pos, random);
    }

    @Override
    protected void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide && level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            if (this instanceof DungeonPortalExit)
                GlobalDungeonManager.addPortalLocation(level.getServer(), pos, DungeonRank.getClosestTo(pos));
            else
                level.setBlockAndUpdate(pos, ModBlocks.DUNGEON_PORTAL_EXIT.get().defaultBlockState());
        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }


    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock() && level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            GlobalDungeonManager.removePortalLocation(level.getServer(), pos, DungeonRank.getClosestTo(pos));
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public int getLightEmission(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return 15;
    }

    private boolean timeCheck(Player player, MinecraftServer server) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        long worldTime = server.overworld().getGameTime();
        long timeLeft = saveData.getTickInterval() - (worldTime - saveData.GetLastExecutionTime());
        if (getExitRank() == saveData.getNextToCollapse() && timeLeft <= timeLeftWarning) {
            long secondsLeft = (long) Math.ceil(timeLeft / (20f));
            player.displayClientMessage(Component.translatable("announcement.thedungeon.low_time_teleport", secondsLeft).withStyle(ChatFormatting.RED).withStyle(ChatFormatting.UNDERLINE),true);
            return false;
        }
        return true;
    }

    public abstract DungeonRank getExitRank();
}
