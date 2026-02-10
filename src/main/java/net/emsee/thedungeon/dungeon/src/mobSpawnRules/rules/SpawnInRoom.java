package net.emsee.thedungeon.dungeon.src.mobSpawnRules.rules;

import net.emsee.thedungeon.dungeon.src.mobSpawnRules.MobSpawnRule;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GeneratedRoom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SpawnInRoom<T extends Entity> extends MobSpawnRule {
    final Supplier<EntityType<T>> entity;
    final float chance;
    final int min;
    final int max;
    final Consumer<T> postSpawnFunction;
    final int maxRoomHeight;


    public SpawnInRoom(Supplier<EntityType<T>> entity) {
        this(entity, 1, 1, -1);
    }

    public SpawnInRoom(Supplier<EntityType<T>> entity, int min, int max, int maxRoomHeight) {
        this(entity, null, min, max, 1, maxRoomHeight);
    }

    public SpawnInRoom(Supplier<EntityType<T>> entity, int min, int max, float chance, int maxRoomHeight) {
        this(entity, null, min, max, chance, maxRoomHeight);
    }


    public SpawnInRoom(Supplier<EntityType<T>> entity, int min, int max) {
        this(entity, null, min, max, 1, -1);
    }

    public SpawnInRoom(Supplier<EntityType<T>> entity, int min, int max, float chance) {
        this(entity, null, min, max, chance, -1);
    }

    public SpawnInRoom(Supplier<EntityType<T>> entity, Consumer<T> postSpawnFunction, int min, int max, float chance) {
        this (entity, postSpawnFunction, min, max, chance, -1);
    }

    public SpawnInRoom(Supplier<EntityType<T>> entity, Consumer<T> postSpawnFunction, int min, int max, float chance, int maxRoomHeight) {
        this.entity = entity;
        this.chance = chance;
        this.min = min;
        this.max = max;
        this.postSpawnFunction = postSpawnFunction;
        this.maxRoomHeight = maxRoomHeight;
    }


    @Override
    public List<UUID> spawn(ServerLevel level, BlockPos roomCenter, BlockPos minCorner, BlockPos maxCorner, Rotation roomRotation) {
        List<UUID> uuids = new ArrayList<>();
        if (maxRoomHeight>=0) {
            int height = maxCorner.getY() - minCorner.getY();
            int clampedHeight  = Math.min(height,maxRoomHeight);

            maxCorner = maxCorner.atY(minCorner.above(clampedHeight).getY());
        }

        RandomSource random = level.random;
        if (tryChance(random)) {
            int count = getAmountForSpawns(random);
            Iterable<BlockPos> positions = BlockPos.randomBetweenClosed(random, count, minCorner.getX(), minCorner.getY(), minCorner.getZ(), maxCorner.getX(), maxCorner.getY(), maxCorner.getZ());

            for (BlockPos pos : positions) {
                T spawned = entity.get().spawn(level, roomCenter, MobSpawnType.STRUCTURE);
                BlockPos finalPos = findClosestValidSpawn(pos, level, spawned, minCorner, maxCorner);
                if (spawned!=null) {
                    if (finalPos != null)
                        spawned.setPos(finalPos.getBottomCenter());
                    if (postSpawnFunction != null) {
                        postSpawnFunction.accept(spawned);
                    }
                    uuids.add(spawned.getUUID());
                }

            }
        }
        return uuids;
    }

    protected boolean tryChance(RandomSource random) {
        return chance >= 1f || random.nextFloat() < chance;
    }

    protected int getAmountForSpawns(RandomSource random) {
        return random.nextInt(min, max+1);
    }

    protected BlockPos findClosestValidSpawn(BlockPos chosenPos, Level level, Entity entity, BlockPos minCorner, BlockPos maxCorner) {
        int minX = Math.min(minCorner.getX(), maxCorner.getX());
        int maxX = Math.max(minCorner.getX(), maxCorner.getX());
        int minY = Math.min(minCorner.getY(), maxCorner.getY());
        int maxY = Math.max(minCorner.getY(), maxCorner.getY())-1;
        int minZ = Math.min(minCorner.getZ(), maxCorner.getZ());
        int maxZ = Math.max(minCorner.getZ(), maxCorner.getZ());

        BlockPos closestPos = null;
        double closestDistanceSq = Double.MAX_VALUE;

        for (
                int x = minX;
                x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = minY; y <= maxY; y++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    if (isValidSpawnPosition(currentPos, level, entity)) {
                        double distanceSq = currentPos.distSqr(chosenPos);
                        if (distanceSq < closestDistanceSq) {
                            closestPos = currentPos;
                            closestDistanceSq = distanceSq;
                        }
                    }
                }
            }
        }

        return closestPos;
    }

    protected static boolean isValidSpawnPosition(BlockPos pos, Level level, Entity entity) {
        if (entity==null) return false;
        for (int i = 0; i < entity.getBbHeight(); i++) {
            BlockState posState = level.getBlockState(pos.above(i));
            if (!posState.isAir()) {
                return false;
            }
        }

        BlockPos groundPos = pos.below();
        BlockState groundState = level.getBlockState(groundPos);
        return groundState.isFaceSturdy(level, groundPos, Direction.UP);
    }
}

