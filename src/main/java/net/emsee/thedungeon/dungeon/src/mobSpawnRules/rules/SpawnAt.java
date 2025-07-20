package net.emsee.thedungeon.dungeon.src.mobSpawnRules.rules;

import net.emsee.thedungeon.dungeon.src.mobSpawnRules.MobSpawnRule;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.Rotation;

import java.util.function.Supplier;

public class SpawnAt<T extends Entity> extends MobSpawnRule {
    final Supplier<EntityType<T>> entity;
    final BlockPos pos;
    final int count;
    final float chance;
    float spawnRotation;

    public SpawnAt(Supplier<EntityType<T>> entity, BlockPos pos) {
        this(entity, pos, 1, 1);
    }

    public SpawnAt(Supplier<EntityType<T>> entity, BlockPos pos, int count, float chance) {
        this.entity = entity;
        this.pos = pos;
        this.count = count;
        this.chance = chance;
    }

    public SpawnAt withRotation(float rotation) {
        spawnRotation=rotation;
        return this;
    }


    @Override
    public void spawn(ServerLevel level, BlockPos roomCenter, Rotation roomRotation) {
        RandomSource random = level.random;
        if (chance == 1 || chance > random.nextFloat()) {
            int tempCount = count;
            while (tempCount > 0) {
                Entity spawned = entity.get().spawn(level, roomCenter.offset(pos.rotate(roomRotation)), MobSpawnType.STRUCTURE);
                assert spawned != null;
                spawned.setYRot(spawnRotation+spawned.rotate(roomRotation));
                tempCount--;
            }
        }
    }
}
