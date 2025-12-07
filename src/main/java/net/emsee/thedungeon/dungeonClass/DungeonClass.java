package net.emsee.thedungeon.dungeonClass;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.attachmentType.ModAttachmentTypes;
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
        String className = player.getData(ModAttachmentTypes.PLAYER_CLASS);
        DungeonClass toReturn = getByResourceName(className);
        if (toReturn == null) {
            DebugLog.logWarn(DebugLog.DebugType.WARNINGS, "Player:{}, dungeonClass returned null, saved string:{}, class does not exist or is not correctly linked", player, className);
            //return Classless.INSTANCE;
        }
        return toReturn;
    }
}
