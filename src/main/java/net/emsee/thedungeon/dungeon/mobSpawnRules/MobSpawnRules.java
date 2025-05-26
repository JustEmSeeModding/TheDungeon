package net.emsee.thedungeon.dungeon.mobSpawnRules;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;

import java.util.Random;

public abstract class MobSpawnRules {
    public abstract void spawn(ServerLevel level, BlockPos roomCenter, Rotation roomRotation);
}
