package net.emsee.thedungeon.dungeonClass;

import net.emsee.thedungeon.item.DungeonItemRank;
import net.emsee.thedungeon.item.interfaces.IClassedItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Arrays;

public class TankClass extends DungeonClass{
    public static TankClass INSTANCE = new TankClass();

    @Override
    public boolean isItemForClass(IClassedItem item) {
        return item.getLinkedClasses().length==0||
                Arrays.stream(item.getLinkedClasses()).anyMatch(r -> r instanceof TankClass);
    }

    @Override
    public MutableComponent getTranslatable() {
        return Component.translatable(getResourceName());
    }

    @Override
    public String getResourceName() {
        return "thedungeon.class.tank";
    }
}
