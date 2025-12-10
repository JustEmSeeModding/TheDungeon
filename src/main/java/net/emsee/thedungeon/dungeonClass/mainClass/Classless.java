package net.emsee.thedungeon.dungeonClass.mainClass;

import net.emsee.thedungeon.dungeonClass.DungeonClass;
import net.emsee.thedungeon.item.DungeonItemRank;
import net.emsee.thedungeon.item.interfaces.IClassedItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Arrays;

public class Classless extends DungeonClass {
    @Override
    public boolean isItemForClass(IClassedItem item) {
        return item.getLinkedClasses().length==0 ||
                Arrays.stream(item.getLinkedClasses()).anyMatch(r -> r.get() instanceof Classless) ||
                item.getItemRank() == DungeonItemRank.F;
    }

    @Override
    public String getResourceName() {
        return "thedungeon.class.classless";
    }
}
