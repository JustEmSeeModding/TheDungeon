package net.emsee.thedungeon.block.custom.portal;

import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.block.entity.portal.DungeonPortalBlockEntity;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.dungeon.src.GlobalDungeonManager;
import net.emsee.thedungeon.utils.ModDungeonTeleportHandling;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
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

import java.util.List;

public abstract class DungeonPortal extends BaseEntityBlock implements IDungeonCarryItem {
    private static final long LOW_TIME_LOCK = 2400;

    public DungeonPortal(Properties properties) {
        super(properties.randomTicks());
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof DungeonPortalBlockEntity entity) {
            MinecraftServer server = level.getServer();
            if (player.isCreative() || GlobalDungeonManager.isOpen(server, getExitRank())) {
                if (timeCheck(player, server) || player.isCreative())
                    ModDungeonTeleportHandling.playerTeleportDungeon(player, entity.getExitID(server, this), getExitRank());
            } else {
                player.displayClientMessage(Component.translatable("message.thedungeon.dungeon_portal.dungeon_closed"), true);
            }
        } else {
            level.playLocalSound(pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1, 1, false);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(DungeonItem.DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide && level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            GlobalDungeonManager.addPortalLocation(level.getServer(), pos, DungeonRank.getClosestTo(pos));
        }
        if (!GlobalDungeonManager.isOpen(level.getServer(), getExitRank()) && level.getBlockEntity(pos) instanceof DungeonPortalBlockEntity entity)
            entity.resetID();
        super.randomTick(state, level, pos, random);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide && level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            if (this instanceof DungeonPortalExit)
                GlobalDungeonManager.addPortalLocation(level.getServer(), pos, DungeonRank.getClosestTo(pos));
            else
                level.setBlockAndUpdate(pos, ModBlocks.DUNGEON_PORTAL_EXIT.get().defaultBlockState());
        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }


    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock() && level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            GlobalDungeonManager.removePortalLocation(level.getServer(), pos, DungeonRank.getClosestTo(pos));
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }

    private boolean timeCheck(Player player, MinecraftServer server) {
        if (player.isCrouching()) return true;
        if (player.level().dimension() == ModDimensions.DUNGEON_LEVEL_KEY) return true;
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        long worldTime = server.overworld().getGameTime();
        long timeLeft = GameruleRegistry.getIntegerGamerule(server, ModGamerules.TICKS_BETWEEN_COLLAPSES) - (worldTime - saveData.GetLastExecutionTime());
        if (getExitRank() == saveData.getNextToCollapse() && timeLeft <= LOW_TIME_LOCK) {
            long secondsLeft = (long) Math.ceil(timeLeft / (20f));
            player.displayClientMessage(Component.translatable("announcement.thedungeon.low_time_teleport", secondsLeft).withStyle(ChatFormatting.RED).withStyle(ChatFormatting.UNDERLINE), true);
            return false;
        }
        return true;
    }

    public abstract DungeonRank getExitRank();
}
