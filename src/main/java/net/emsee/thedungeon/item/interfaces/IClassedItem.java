package net.emsee.thedungeon.item.interfaces;

import net.emsee.thedungeon.dungeonClass.DungeonClass;
import net.emsee.thedungeon.item.DungeonItemRank;

public interface IClassedItem {
    DungeonClass[] getLinkedClasses();
    DungeonItemRank getItemRank();
}
