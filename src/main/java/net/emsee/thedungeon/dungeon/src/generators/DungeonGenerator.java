package net.emsee.thedungeon.dungeon.src.generators;

import net.emsee.thedungeon.dungeon.src.types.Dungeon;
import net.minecraft.server.level.ServerLevel;

public abstract class DungeonGenerator<T extends Dungeon> {
    /**
     * called each tick to generate the dungeon
     */
    public void step(ServerLevel level) {}
}
