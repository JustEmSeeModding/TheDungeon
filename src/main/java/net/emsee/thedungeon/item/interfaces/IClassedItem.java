package net.emsee.thedungeon.item.interfaces;

import net.emsee.thedungeon.dungeonClass.DungeonClass;
import net.emsee.thedungeon.dungeonClass.DungeonSubClass;
import net.emsee.thedungeon.item.DungeonItemRank;
import net.neoforged.neoforge.registries.DeferredHolder;

public interface IClassedItem {
    DeferredHolder<DungeonClass, ?>[] getLinkedClasses();
    DeferredHolder<DungeonSubClass<?>, ?>[] getLinkedSubClasses();
    DungeonItemRank getItemRank();

}
