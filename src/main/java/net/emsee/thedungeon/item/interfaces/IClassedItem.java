package net.emsee.thedungeon.item.interfaces;

import net.emsee.thedungeon.dungeonClass.DungeonClass;
import net.emsee.thedungeon.dungeonClass.DungeonSubClass;
import net.emsee.thedungeon.item.DungeonItemRank;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public interface IClassedItem {
    @NotNull DeferredHolder<DungeonClass, ?>[] getLinkedClasses();
    @NotNull DeferredHolder<DungeonSubClass<?>, ?>[] getLinkedSubClasses();
    DungeonItemRank getItemRank();

    default boolean hasNoLinkedClasses() {
        return false;//getLinkedClasses().length == 0
                //&& getLinkedSubClasses().length == 0;
    }
}
