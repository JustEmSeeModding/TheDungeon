package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.attachmentType.ModAttachmentTypes;
import net.emsee.thedungeon.attachmentType.PreDeathTotemInventorySave;
import net.emsee.thedungeon.dungeonClass.DungeonClass;
import net.emsee.thedungeon.dungeonClass.DungeonSubClass;
import net.emsee.thedungeon.item.DungeonItemRank;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class SoulBoundTotem extends DungeonCurio{
    private static final boolean WORKS_IN_INVENTORY = false;


    public SoulBoundTotem(Properties properties, DungeonItemRank rank, DeferredHolder<DungeonClass, ?>[] classes, DeferredHolder<DungeonSubClass<?>, ?>[] subClasses) {
        super(properties, rank, classes, subClasses);
    }

    public static boolean hasOrHadInInventoryOnDeath(Player player) {
        if (player.getPersistentData().getBoolean("hasOrHadSoulBoundTotem")) {
            return true;
        }
        return hasInInventory(player);
    }

    protected static Predicate<ItemStack> totemValidation(Player player) {
        return (stack) -> {
            if (stack.isEmpty()) return false;
            if (!(stack.getItem() instanceof SoulBoundTotem totem)) return false;
            return totem.validateUsability(player);
        };
    }

    public static boolean hasInInventory(Player player) {
        if (WORKS_IN_INVENTORY && player.getInventory().contains(totemValidation(player))) {
            addPlayerData(player);
            return true;
        }

        Optional<ICuriosItemHandler> originalCurioInventoryO = CuriosApi.getCuriosInventory(player);
        if (originalCurioInventoryO.isEmpty()) {
            return false;
        }
        ICuriosItemHandler originalCurioInventory = originalCurioInventoryO.get();
        if (originalCurioInventory.isEquipped(totemValidation(player))) {
            addPlayerData(player);
            return true;
        }
        return false;
    }

    private boolean validateUsability(Player player) {
        return true;
    }

    public static void addPlayerData(Player player) {
        player.getPersistentData().putBoolean("hasOrHadSoulBoundTotem", true);
    }

    public static void copyPlayerInventory(Player original, Player clone) {
        PreDeathTotemInventorySave invSave = original.getData(ModAttachmentTypes.PRE_DEATH_TOTEM_INVENTORY_SAVE);
        invSave.loadInventoryToPlayer(clone);
    }


    public static void copyPlayerCurios(Player original, Player clone) {
        PreDeathTotemInventorySave invSave = original.getData(ModAttachmentTypes.PRE_DEATH_TOTEM_INVENTORY_SAVE);
        invSave.loadCuriosToPlayer(clone);
    }

    public static boolean useFirstTotem(Player player) {
        Optional<ICuriosItemHandler> curioInventoryO = CuriosApi.getCuriosInventory(player);
        if (curioInventoryO.isPresent()) {
            ICuriosItemHandler curioInventory = curioInventoryO.get();
            IItemHandlerModifiable itemHandlerModifiable = curioInventory.getEquippedCurios();
            for (int i = 0; i < itemHandlerModifiable.getSlots(); i++) {
                ItemStack stack = itemHandlerModifiable.getStackInSlot(i);
                if (totemValidation(player).test(stack)) {
                    damageOrDestroy(stack);
                    //itemHandlerModifiable.setStackInSlot(i,stack);
                    return true;
                }
            }
        }

        AtomicBoolean found = new AtomicBoolean(false);

        if (WORKS_IN_INVENTORY) {
            if(player.getInventory().contains(stack -> stack.getItem() instanceof SoulBoundTotem)) {
                player.getInventory().items.forEach(stack -> {
                    if (found.get()) return;
                    if (totemValidation(player).test(stack)) {
                        damageOrDestroy(stack);
                        found.set(true);
                    }
                });
            }
        }
        return found.get();
    }

    private static void damageOrDestroy(ItemStack stack) {
        DebugLog.logInfo(DebugLog.DebugType.WARNINGS, "damaging Totem");
        if (stack.isDamageableItem()) {
            stack.setDamageValue(stack.getDamageValue() - 1);
            if (stack.getDamageValue() == 0)
                stack.shrink(1);
        }
        else
            stack.shrink(1);
    }
}
