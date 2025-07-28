package net.emsee.thedungeon.dungeon.src.mobSpawnRules.rules;

import net.emsee.thedungeon.dungeon.src.mobSpawnRules.MobSpawnRule;
import net.emsee.thedungeon.dungeon.src.room.GeneratedRoom;
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

import java.util.function.Supplier;

public class SpawnInRoom<T extends Entity> extends MobSpawnRule {
    final Supplier<EntityType<T>> entity;
    final float chance;
    final int min;
    final int max;


    public SpawnInRoom(Supplier<EntityType<T>> entity) {
        this(entity, 1, 1, 1);
    }

    public SpawnInRoom(Supplier<EntityType<T>> entity, int min, int max, float chance) {
        this.entity = entity;
        this.chance = chance;
        this.min = min;
        this.max = max;
    }


    @Override
    public void spawn(ServerLevel level, GeneratedRoom room) {
        BlockPos roomCenter = room.getPlacedWorldPos();
        Rotation roomRotation = room.getPlacedWorldRotation();

        BlockPos cornerOne = room.getRoom().getMinCorner(roomCenter, roomRotation);
        BlockPos cornerTwo = room.getRoom().getMaxCorner(roomCenter, roomRotation);

        RandomSource random = level.random;
        if (chance == 1 || chance > random.nextFloat()) {
            int count = random.nextInt(min, max+1);
            Iterable<BlockPos> positions = BlockPos.randomBetweenClosed(random, count, cornerOne.getX(), cornerOne.getY(), cornerOne.getZ(), cornerTwo.getX(), cornerTwo.getY(), cornerTwo.getZ());

            for (BlockPos pos : positions) {
                Entity spawned = entity.get().spawn(level, roomCenter, MobSpawnType.STRUCTURE);
                BlockPos finalPos = findClosestValidSpawn(pos, level, spawned, cornerOne, cornerTwo);
                if (finalPos!= null && spawned!=null)
                    spawned.setPos(finalPos.getBottomCenter());
            }
        }
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

