package net.emsee.thedungeon.dungeonClass.mainClass;

import net.emsee.thedungeon.dungeonClass.DungeonClass;
import net.emsee.thedungeon.item.interfaces.IClassedItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Arrays;

public class TankClass extends DungeonClass {
    @Override
    public boolean isItemForClass(IClassedItem item) {
        return item.hasNoLinkedClasses()||
                Arrays.stream(item.getLinkedClasses()).anyMatch(r -> r.get() instanceof TankClass);
    }

    @Override
    public String getResourceName() {
        return "thedungeon.class.tank";
    }
}
