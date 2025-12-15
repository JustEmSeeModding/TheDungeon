package net.emsee.thedungeon.dungeonClass;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.attachmentType.ModAttachmentTypes;
import net.emsee.thedungeon.item.interfaces.IClassedItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public abstract class DungeonSubClass<C extends DungeonClass> {
    public abstract boolean isItemForClass(IClassedItem item);
    public MutableComponent getTranslatable() {
        return Component.translatable(getResourceName());
    }
    public abstract String getResourceName();

    public abstract boolean isSubclassOf(DungeonClass dungeonClass);

    public void onApplyClass() {}
    public void onRemoveClass() {}
}
