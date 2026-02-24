package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.datagen.ModCuriosDataProvider;
import net.emsee.thedungeon.dungeonClass.DungeonClass;
import net.emsee.thedungeon.dungeonClass.DungeonSubClass;
import net.emsee.thedungeon.dungeonClass.ModClasses;
import net.emsee.thedungeon.dungeonClass.ModSubClasses;
import net.emsee.thedungeon.item.DungeonItemRank;
import net.emsee.thedungeon.mobEffect.ModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public class EffigyCurio extends DungeonCurio {
    public EffigyCurio(Properties properties, DungeonItemRank rank, @NotNull DeferredHolder<DungeonClass, ?> dungeonClass, @Nullable DeferredHolder<DungeonSubClass<?>, ?> subClass) {
        super(properties, rank, new DeferredHolder[]{dungeonClass}, (subClass == null || !subClass.isBound()) ? new DeferredHolder[]{} : new DeferredHolder[]{subClass});
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        super.onEquip(slotContext, prevStack, stack);
        if (!(slotContext.entity() instanceof Player player)) return;
        ModClasses.setClassForPlayer(player, classes[0]);
        if (subClasses.length == 1) {
            DeferredHolder<DungeonSubClass<?>, ?> subClass = subClasses[0];
            if (subClass != null)
                ModSubClasses.setClassForPlayer(player, subClasses[0]);
        }
        player.addEffect(new MobEffectInstance(ModMobEffects.EFFIGY_LOCKED, getEffigySwapCooldown(), 0, true, false));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        super.onUnequip(slotContext, newStack, stack);
        if (!(slotContext.entity() instanceof Player player)) {
            return;
        }
        ModClasses.removeClassForPlayer(player);
        ModSubClasses.removeClassForPlayer(player);
    }

    public int getEffigySwapCooldown() {
        return 12000;
    }

    public static void keepEffigyInInventory(Player original, Player clone) {
        CuriosApi.getCuriosInventory(original).ifPresent(originalHandler -> {
            CuriosApi.getCuriosInventory(clone).ifPresent(cloneHandler -> {
                ICurioStacksHandler originalStacks = originalHandler.getCurios().get(ModCuriosDataProvider.EFFIGY_IDENTIFIER);
                ICurioStacksHandler cloneStacks = cloneHandler.getCurios().get(ModCuriosDataProvider.EFFIGY_IDENTIFIER);

                if (originalStacks != null && cloneStacks != null) {
                    ItemStack stackToKeep = originalStacks.getStacks().getStackInSlot(0).copy();
                    cloneStacks.getStacks().setStackInSlot(0, stackToKeep);
                }
            });
        });
    }
}
