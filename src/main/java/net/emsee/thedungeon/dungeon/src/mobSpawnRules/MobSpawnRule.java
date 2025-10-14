package net.emsee.thedungeon.dungeon.src.mobSpawnRules;

import net.emsee.thedungeon.dungeon.src.types.grid.room.GeneratedRoom;
import net.minecraft.server.level.ServerLevel;

/**
 * mod spawn rules for spawning mobs in rooms
 */
public abstract class MobSpawnRule {
    /**
     * spawn the mobs
     */
    public abstract void spawn(ServerLevel level, GeneratedRoom room);
}
