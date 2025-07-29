package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.component.ModDataComponentTypes;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.emsee.thedungeon.worldSaveData.DungeonSaveData;
import net.emsee.thedungeon.worldSaveData.NBT.DungeonNBTData;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DungeonClock extends DungeonItem implements IDungeonCarryItem {
    public DungeonClock(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

/*    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (level instanceof ServerLevel serverLevel) {
            long time = DungeonSaveData.Get(serverLevel.getServer()).getTimeLeft(serverLevel.getServer());
            time = (time + 600) / 1200 * 1200; //round up to next multiple of 1200
            stack.set(ModDataComponentTypes.CLOCK_SAVED_TIME, time);
            stack.set(ModDataComponentTypes.CLOCK_SAVED_IN_RANK, DungeonRank.getClosestTo(entity.blockPosition()) == DungeonSaveData.Get(serverLevel.getServer()).getNextToCollapse());
        }
    }*/

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide) {
            viewTime(player, level.getServer());
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    public static void viewTime(Player player, MinecraftServer server) {
        if(!GameruleRegistry.getBooleanGamerule(server, ModGamerules.AUTO_DUNGEON_CYCLING)){
            player.displayClientMessage(Component.translatable("message.thedungeon.cycling_disabled"),true);
            return;
        }

        DungeonSaveData saveData = DungeonSaveData.Get(server);

        DungeonRank nextRank = saveData.getNextToCollapse();

        long timeLeft = saveData.getTimeLeft();
        long secondsLeft = (long) Math.ceil(timeLeft / (20f));
        long minutesLeft = (long) Math.floor(secondsLeft / (60f));

        if (secondsLeft <= 60) {
            player.displayClientMessage(Component.translatable("message.thedungeon.view_time_seconds", nextRank.getName(),secondsLeft),true);
        } else {
            player.displayClientMessage(Component.translatable("message.thedungeon.view_time_minutes", nextRank.getName(),minutesLeft, secondsLeft - minutesLeft * 60),true);
        }
    }

    /*public static long getTimeLeft(ItemStack itemStack) {
        if (itemStack.get(ModDataComponentTypes.CLOCK_SAVED_TIME)!=null) {
            return itemStack.get(ModDataComponentTypes.CLOCK_SAVED_TIME);
        }
        return 0;
    }*/

    public static boolean isInNextToCollapseOrOutside(LivingEntity livingEntity, ClientLevel clientLevel) {
        if (clientLevel.dimension() != ModDimensions.DUNGEON_LEVEL_KEY) return true;
        if (DungeonSaveData.GetClient() == null) return false;
        if (livingEntity==null) return true;
        return DungeonRank.getClosestTo(livingEntity.blockPosition()) == DungeonSaveData.GetClient().nextToCollapse();
    }

    /*public static boolean isInNextToCollapseOrOutside(ItemStack itemStack, ClientLevel clientLevel) {
        if (clientLevel.dimension() != ModDimensions.DUNGEON_LEVEL_KEY) return true;
        if (itemStack.get(ModDataComponentTypes.CLOCK_SAVED_IN_RANK)!=null)
            return itemStack.get(ModDataComponentTypes.CLOCK_SAVED_IN_RANK);
        return false;
    }*/

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        return this.use(context.getLevel(), Objects.requireNonNull(context.getPlayer()), context.getHand()).getResult();
    }
}
