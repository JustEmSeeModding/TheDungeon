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

import java.util.function.Supplier;

public class SpawnInBox<T extends Entity> extends MobSpawnRule {
    final Supplier<EntityType<T>> entity;
    final BlockPos cornerOne;
    final BlockPos cornerTwo;
    final float chance;
    final int min;
    final int max;


    public SpawnInBox(Supplier<EntityType<T>> entity, BlockPos cornerOne, BlockPos cornerTwo) {
        this(entity, cornerOne, cornerTwo, 1, 1, 1);
    }

    public SpawnInBox(Supplier<EntityType<T>> entity, BlockPos cornerOne, BlockPos cornerTwo, int min, int max, float chance) {
        this.entity = entity;
        this.cornerOne = cornerOne;
        this.cornerTwo = cornerTwo;
        this.chance = chance;
        this.min = min;
        this.max = max;
    }


    @Override
    public void spawn(ServerLevel level, GeneratedRoom room) {
        BlockPos roomCenter = room.getPlacedWorldPos();
        Rotation roomRotation = room.getPlacedWorldRotation();

        RandomSource random = level.random;
        int count = random.nextInt(min, max+1);
        if (chance == 1 || chance > random.nextFloat()) {
            Iterable<BlockPos> positions = BlockPos.randomBetweenClosed(random, count, cornerOne.getX(), cornerOne.getY(), cornerOne.getZ(), cornerTwo.getX(), cornerTwo.getY(), cornerTwo.getZ());

            for (BlockPos pos : positions) {
                Entity spawned = entity.get().spawn(level, roomCenter, MobSpawnType.STRUCTURE);
                BlockPos finalPos = findClosestValidSpawn(roomCenter.offset(pos.rotate(roomRotation)), roomCenter, roomRotation, level, spawned);
                if (finalPos!= null && spawned!=null)
                    spawned.setPos(finalPos.getBottomCenter());
            }
        }
    }

    protected BlockPos findClosestValidSpawn(BlockPos chosenPos, BlockPos roomCenter, Rotation rotation, Level world, Entity entity) {
        BlockPos finalCornerOne = roomCenter.offset(cornerOne.rotate(rotation));
        BlockPos finalCornerTwo = roomCenter.offset(cornerTwo.rotate(rotation));
        int minX = Math.min(finalCornerOne.getX(), finalCornerTwo.getX());
        int maxX = Math.max(finalCornerOne.getX(), finalCornerTwo.getX());
        int minY = Math.min(finalCornerOne.getY(), finalCornerTwo.getY());
        int maxY = Math.max(finalCornerOne.getY(), finalCornerTwo.getY())-1;
        int minZ = Math.min(finalCornerOne.getZ(), finalCornerTwo.getZ());
        int maxZ = Math.max(finalCornerOne.getZ(), finalCornerTwo.getZ());

        BlockPos closestPos = null;
        double closestDistanceSq = Double.MAX_VALUE;

        for (
                int x = minX;
                x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = minY; y <= maxY; y++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    if (isValidSpawnPosition(currentPos, world, entity)) {
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
