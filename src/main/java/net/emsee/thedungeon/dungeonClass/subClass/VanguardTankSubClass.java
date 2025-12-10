package net.emsee.thedungeon.dungeonClass.subClass;

import net.emsee.thedungeon.dungeonClass.DungeonClass;
import net.emsee.thedungeon.dungeonClass.DungeonSubClass;
import net.emsee.thedungeon.dungeonClass.mainClass.TankClass;
import net.emsee.thedungeon.item.interfaces.IClassedItem;

import java.util.Arrays;

public class VanguardTankSubClass extends DungeonSubClass<TankClass> {
    @Override
    public boolean isItemForClass(IClassedItem item) {
        return item.getLinkedSubClasses().length==0||
                Arrays.stream(item.getLinkedSubClasses()).anyMatch(r -> r.get() instanceof VanguardTankSubClass);
    }

    @Override
    public String getResourceName() {
        return "thedungeon.subclass.tank.vanguard";
    }

    @Override
    public boolean isSubclassOf(DungeonClass dungeonClass) {
        return false;
    }
}
