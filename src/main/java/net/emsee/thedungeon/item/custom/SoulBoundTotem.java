package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeonClass.DungeonClass;
import net.emsee.thedungeon.dungeonClass.DungeonSubClass;
import net.emsee.thedungeon.item.DungeonItemRank;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.registries.DeferredHolder;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class SoulBoundTotem extends DungeonCurio{
    private static final boolean WORKS_IN_INVENTORY = false;


    public SoulBoundTotem(Properties properties, DungeonItemRank rank, DeferredHolder<DungeonClass, ?>[] classes, DeferredHolder<DungeonSubClass<?>, ?>[] subClasses) {
        super(properties, rank, classes, subClasses);
    }

    public static boolean hasOrHadInInventory(Player player) {
        if (player.getPersistentData().getBoolean("hasOrHadSoulBoundTotem")) {
            return true;
        }
        return hasInInventory(player);
    }


    public static boolean hasInInventory(Player player) {
        if (WORKS_IN_INVENTORY && player.getInventory().contains(stack -> stack.getItem() instanceof SoulBoundTotem)){
            addPlayerData(player);
            return true;
        }

        Optional<ICuriosItemHandler> originalCurioInventoryO = CuriosApi.getCuriosInventory(player);
        if (originalCurioInventoryO.isEmpty()) {
            return false;
        }
        ICuriosItemHandler originalCurioInventory = originalCurioInventoryO.get();
        if (originalCurioInventory.isEquipped(stack -> stack.getItem() instanceof SoulBoundTotem)) {
            addPlayerData(player);
            return true;
        }
        return false;
    }

    public static void addPlayerData(Player player) {
        player.getPersistentData().putBoolean("hasOrHadSoulBoundTotem", true);
    }

    public static void copyPlayerInventory(Player original, Player clone) {
        /*for (int i = 0; i < original.getInventory().getContainerSize(); i++) {
            clone.getInventory().setItem(i, original.getInventory().getItem(i));
        }*/
        DebugLog.logInfo(DebugLog.DebugType.WARNINGS, "copyINV");
        clone.getInventory().replaceWith(original.getInventory());

        CuriosApi.getCuriosInventory(original).ifPresent(originalHandler -> {
            CuriosApi.getCuriosInventory(clone).ifPresent(cloneHandler -> {
                cloneHandler.setCurios(originalHandler.getCurios());
            });
        });
    }

    public static void removeFirstTotem(Player player) {
        AtomicBoolean found = new AtomicBoolean(false);
        if (WORKS_IN_INVENTORY) {
            if(player.getInventory().contains(stack -> stack.getItem() instanceof SoulBoundTotem)) {
                player.getInventory().items.forEach(stack -> {
                    if (found.get()) return;
                    if (stack.getItem() instanceof SoulBoundTotem && !stack.isEmpty()) {
                        stack.shrink(1);
                        found.set(true);
                    }
                });
            }
        }
        if (found.get()) return;

        Optional<ICuriosItemHandler> curioInventoryO = CuriosApi.getCuriosInventory(player);
        if (curioInventoryO.isEmpty()) {
            return;
        }
        ICuriosItemHandler curioInventory = curioInventoryO.get();
        IItemHandlerModifiable itemHandlerModifiable = curioInventory.getEquippedCurios();
        for (int i =0; i< itemHandlerModifiable.getSlots(); i++) {
            ItemStack stack =itemHandlerModifiable.getStackInSlot(i);
            if (stack.getItem() instanceof SoulBoundTotem && !stack.isEmpty()) {
                stack.shrink(1);
                //itemHandlerModifiable.setStackInSlot(i,stack);
                return;
            }
        }
    }
}
