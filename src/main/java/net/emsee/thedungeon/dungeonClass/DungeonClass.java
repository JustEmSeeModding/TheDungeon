package net.emsee.thedungeon.dungeonClass;

import net.emsee.thedungeon.attachmentType.ModAttachmentTypes;
import net.emsee.thedungeon.component.ModDataComponentTypes;
import net.emsee.thedungeon.item.interfaces.IClassedItem;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

public abstract class DungeonClass {
    private static final HashMap<String, DungeonClass> namedInstances = new HashMap<>();

    public abstract boolean isItemForClass(IClassedItem item);
    public abstract MutableComponent getTranslatable();
    public abstract String getResourceName();

    public static void createNamedInstances() {
        namedInstances.clear();
        namedInstances.put(Classless.INSTANCE.getResourceName(), Classless.INSTANCE);
        namedInstances.put(TankClass.INSTANCE.getResourceName(), TankClass.INSTANCE);
    }

    public static DungeonClass getByResourceName(String name) {
        return namedInstances.get(name);
    }

    public static DungeonClass getClassForPlayer(Player player) {
        DungeonClass toReturn = getByResourceName(player.getData(ModAttachmentTypes.PLAYER_CLASS));
        if (toReturn == null) return Classless.INSTANCE;
        return toReturn;
    }
}
