package net.emsee.thedungeon.dungeon.src.mobSpawnRules.rules;

import net.emsee.thedungeon.dungeon.src.mobSpawnRules.MobSpawnRule;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GeneratedRoom;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.Rotation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

    public SpawnAt<T> withRotation(float rotation) {
        spawnRotation=rotation;
        return this;
    }


    @Override
    public List<UUID> spawn(ServerLevel level, BlockPos roomCenter, BlockPos minCorner, BlockPos maxCorner, Rotation roomRotation) {
        List<UUID> uuids = new ArrayList<>();
        RandomSource random = level.random;
        if (tryChance(random)) {
            int tempCount = count;
            while (tempCount > 0) {
                Entity spawned = entity.get().spawn(level, roomCenter.offset(pos.rotate(roomRotation)), MobSpawnType.STRUCTURE);
                if (spawned==null) return uuids;
                spawned.setYRot(spawnRotation+spawned.rotate(roomRotation));
                uuids.add(spawned.getUUID());
                tempCount--;
            }
        }
        return uuids;
    }

    protected boolean tryChance(RandomSource random) {
        return chance >= 1f || random.nextFloat() < chance;
    }
}
