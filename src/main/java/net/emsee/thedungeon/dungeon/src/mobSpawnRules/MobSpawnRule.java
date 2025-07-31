package net.emsee.thedungeon.dungeon.src.mobSpawnRules;

import net.emsee.thedungeon.dungeon.src.room.GeneratedRoom;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;

/**
 * mod spawn rules for spawning mobs in rooms
 */
public abstract class MobSpawnRule {
    /**
     * spawn the mobs
     */
    public abstract void spawn(ServerLevel level, GeneratedRoom room);
}
