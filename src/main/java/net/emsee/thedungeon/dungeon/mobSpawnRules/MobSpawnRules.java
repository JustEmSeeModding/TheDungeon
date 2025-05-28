package net.emsee.thedungeon.dungeon.mobSpawnRules;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;

import java.util.Random;

/**
 * mod spawn rules for spawning mobs in rooms
 */
public abstract class MobSpawnRules {
    /**
     * spawn the mobs
     */
    public abstract void spawn(ServerLevel level, BlockPos roomCenter, Rotation roomRotation);
}
