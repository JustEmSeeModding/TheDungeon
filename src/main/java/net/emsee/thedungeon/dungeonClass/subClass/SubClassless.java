package net.emsee.thedungeon.dungeonClass.subClass;


import net.emsee.thedungeon.dungeonClass.DungeonClass;
import net.emsee.thedungeon.dungeonClass.DungeonSubClass;
import net.emsee.thedungeon.item.interfaces.IClassedItem;

import java.util.Arrays;

public class SubClassless extends DungeonSubClass<DungeonClass> {
    @Override
    public boolean isItemForClass(IClassedItem item) {
        return item.getLinkedSubClasses().length==0||
                Arrays.stream(item.getLinkedSubClasses()).anyMatch(r -> r.get() instanceof SubClassless);
    }

    @Override
    public String getResourceName() {
        return "thedungeon.subclass.classless";
    }

    @Override
    public boolean isSubclassOf(DungeonClass dungeonClass) {
        return dungeonClass != null;
    }
}
