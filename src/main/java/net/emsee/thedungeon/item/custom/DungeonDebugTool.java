package net.emsee.thedungeon.item.custom;


import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.component.ModDataComponentTypes;
import net.emsee.thedungeon.dungeon.GlobalDungeonManager;
import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DungeonDebugTool extends DungeonItem implements IDungeonCarryItem {
    public DungeonDebugTool(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    //private int selectedDungeonID = 0;

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        int selectedDungeonID = 0;
        if (itemstack.get(ModDataComponentTypes.ITEM_SAVED_DUNGEON_ID.get()) != null) {
            selectedDungeonID= itemstack.get(ModDataComponentTypes.ITEM_SAVED_DUNGEON_ID.get());
        }
        if (player.isCreative()) {
            if (!level.isClientSide) {
                if (player.isCrouching()) {
                    if (++selectedDungeonID >= GlobalDungeonManager.getDungeonCount()) {
                        selectedDungeonID = 0;
                    }

                    DebugLog.logInfo(DebugLog.DebugLevel.GENERIC,"Selected Dungeon Using Tool : ({})={}", selectedDungeonID, Objects.requireNonNull(GlobalDungeonManager.getDungeonByID(selectedDungeonID)).getResourceName());
                    player.sendSystemMessage(Component.translatable("item.thedungeon.dungeon_debug_tool.select", Objects.requireNonNull(GlobalDungeonManager.getDungeonByID(selectedDungeonID)).getResourceName()));

                } else {
                    if (!GlobalDungeonManager.isGenerating(level.getServer())) {
                        GlobalDungeonManager.generateDungeonFromTool(level.getServer(), selectedDungeonID);
                        player.sendSystemMessage(Component.translatable("item.thedungeon.dungeon_debug_tool.use"));
                    } else {
                        player.sendSystemMessage(Component.translatable("item.thedungeon.dungeon_debug_tool.use_busy"));
                    }
                }
            }
        }
        itemstack.set(ModDataComponentTypes.ITEM_SAVED_DUNGEON_ID, selectedDungeonID);
        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public boolean onEntitySwing(@NotNull ItemStack stack, LivingEntity entity, @NotNull InteractionHand hand) {
        if (!entity.level().isClientSide) {
            GlobalDungeonManager.sendManualGenerationTick(entity.getServer());
        }

        return super.onEntitySwing(stack, entity, hand);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        return this.use(context.getLevel(), Objects.requireNonNull(context.getPlayer()), context.getHand()).getResult();
    }
}
