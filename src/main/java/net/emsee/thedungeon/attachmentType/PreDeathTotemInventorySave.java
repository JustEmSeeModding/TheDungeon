package net.emsee.thedungeon.attachmentType;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PreDeathTotemInventorySave {
    private final Map<Integer, ItemStack> inventory;
    private final Map<String, Map<Integer, ItemStack>> curios;

    private static final Codec<Map<Integer, ItemStack>> SLOT_MAP_CODEC =
            Codec.unboundedMap(Codec.INT, ItemStack.CODEC);

    private static final Codec<Map<String, Map<Integer, ItemStack>>> CURIOS_CODEC =
            Codec.unboundedMap(Codec.STRING, SLOT_MAP_CODEC);

    public static final Codec<PreDeathTotemInventorySave> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    SLOT_MAP_CODEC.fieldOf("inventory").forGetter(PreDeathTotemInventorySave::getInventory),

                    CURIOS_CODEC.fieldOf("curios").forGetter(PreDeathTotemInventorySave::getCurios)
            ).apply(instance, PreDeathTotemInventorySave::newFromSerialized));

    private static PreDeathTotemInventorySave newFromSerialized(Map<Integer, ItemStack> inventoryMap, Map<String, Map<Integer, ItemStack>> curioMap) {
        return new PreDeathTotemInventorySave(inventoryMap, curioMap);
    }

    public Map<Integer, ItemStack> getInventory() {
        return inventory;
    }

    public Map<String, Map<Integer, ItemStack>> getCurios() {
        return curios;
    }


    public PreDeathTotemInventorySave(Player player) {
        if (player == null) {
            inventory = null;
            curios = null;
            return;
        }
        inventory = serializeInventorySave(player.getInventory());
        Optional<ICuriosItemHandler> optHandler = CuriosApi.getCuriosInventory(player);
        ICuriosItemHandler handler = optHandler.orElse(null);
        if (handler != null)
            curios = serializeCurioSave(handler);
        else
            curios = null;
    }

    private PreDeathTotemInventorySave(Map<Integer, ItemStack> inventory, Map<String, Map<Integer, ItemStack>> curios) {
        this.inventory = inventory;
        this.curios = curios;
    }

    private Map<Integer, ItemStack> serializeInventorySave(Inventory inventory) {
        Map<Integer, ItemStack> toReturn = new HashMap<>();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                toReturn.put(i, stack);
            }
        }
        return toReturn;
    }

    private static Map<String, Map<Integer, ItemStack>> serializeCurioSave(ICuriosItemHandler itemHandler) {
        Map<String, Map<Integer, ItemStack>> toReturn = new HashMap<>();
        for (Map.Entry<String, ICurioStacksHandler> entry : itemHandler.getCurios().entrySet()) {
            Map<Integer, ItemStack> slotMap = new HashMap<>();
            ICurioStacksHandler handler = entry.getValue();

            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStacks().getStackInSlot(i).copy();
                if (!stack.isEmpty()) {
                    slotMap.put(i, stack);
                }
            }

            if (!slotMap.isEmpty()) {
                toReturn.put(entry.getKey(), slotMap);
            }
        }
        return toReturn;
    }

    public void loadAllToPlayer(Player player) {
        loadInventoryToPlayer(player);
        loadCuriosToPlayer(player);
    }

    public void loadInventoryToPlayer(Player player) {
        Inventory inv = player.getInventory();

        for (Map.Entry<Integer, ItemStack> entry : inventory.entrySet()) {
            int slot = entry.getKey();
            if (!inv.getItem(slot).isEmpty()) {
                player.drop(inv.getItem(slot), true);
            }
            inv.setItem(slot, entry.getValue());
        }
    }

    public void loadCuriosToPlayer(Player player) {
        if (curios != null)
            CuriosApi.getCuriosInventory(player).ifPresent(handler ->
                    curios.forEach((id, slotData) ->
                            slotData.forEach((slotId, stack) ->
                                    handler.setEquippedCurio(id, slotId, stack))));
    }
}
