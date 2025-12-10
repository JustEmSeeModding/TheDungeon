package net.emsee.thedungeon.dungeonClass;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.attachmentType.ModAttachmentTypes;
import net.emsee.thedungeon.item.interfaces.IClassedItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public abstract class DungeonClass {
        public abstract boolean isItemForClass(IClassedItem item);

    public MutableComponent getTranslatable() {
        return Component.translatable(getResourceName());
    }
    public abstract String getResourceName();

    public void onApplyClass() {}
    public void onRemoveClass() {}

    public static DungeonClass getByPath(String name) {
        final DungeonClass[] toReturn = new DungeonClass[]{null};

        ModClasses.CLASSES.getEntries().forEach(c -> {
            if (Objects.equals(c.getId().getPath(), name)) {
                toReturn[0] = c.get();
            }
        });

        return toReturn[0];
    }

    public static DungeonClass getClassForPlayer(Player player) {
        String className = player.getData(ModAttachmentTypes.PLAYER_CLASS);
        DungeonClass toReturn = getByPath(className);
        if (toReturn == null) {
            DebugLog.logWarn(DebugLog.DebugType.WARNINGS, "Player:{}, dungeonClass returned null, saved string:{}, class does not exist or is not correctly linked", player, className);
            //return Classless.INSTANCE;
        }
        return toReturn;
    }
}
