package net.emsee.thedungeon.item.custom;


import net.emsee.thedungeon.dungeon.GlobalDungeonManager;
import net.emsee.thedungeon.dungeon.util.DungeonRank;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.emsee.thedungeon.worldSaveData.DungeonSaveData;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DungeonClock extends DungeonItem implements IDungeonCarryItem {
    public DungeonClock(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide) {
            viewTime(player, level.getServer());
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    public static void viewTime(Player player, MinecraftServer server) {
        //if (player instanceof AbstractClientPlayer clientPlayer)
        if(!GameruleRegistry.getBooleanGamerule(server, ModGamerules.AUTO_DUNGEON_CYCLING)){
            player.displayClientMessage(Component.translatable("message.thedungeon.cycling_disabled"),true);
            return;
        }
        long worldTime = server.overworld().getGameTime();
        DungeonSaveData saveData = DungeonSaveData.Get(server);

        DungeonRank nextRank = saveData.getNextToCollapse();

        long timeLeft = -((worldTime - saveData.GetLastExecutionTime()) - saveData.getTickInterval());
        long secondsLeft = (long) Math.ceil(timeLeft / (20f));
        long minutesLeft = (long) Math.floor(secondsLeft / (60f));

        if (secondsLeft <= 60) {
            player.displayClientMessage(Component.translatable("message.thedungeon.view_time_seconds", nextRank.getName(),secondsLeft),true);
        } else {
            player.displayClientMessage(Component.translatable("message.thedungeon.view_time_minutes", nextRank.getName(),minutesLeft, secondsLeft - minutesLeft * 60),true);
        }
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        return this.use(context.getLevel(), Objects.requireNonNull(context.getPlayer()), context.getHand()).getResult();
    }
}
