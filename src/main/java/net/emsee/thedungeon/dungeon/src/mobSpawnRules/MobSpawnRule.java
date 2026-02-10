package net.emsee.thedungeon.dungeon.src.mobSpawnRules;

import net.emsee.thedungeon.dungeon.src.types.grid.room.GeneratedRoom;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;

import java.util.List;
import java.util.UUID;

/**
 * mod spawn rules for spawning mobs in rooms
 */
public abstract class MobSpawnRule {
    /**
     * spawn the mobs
     * @return a list of spawned mob UUISs
     */
    public final List<UUID> spawn(ServerLevel level, GeneratedRoom room) {
        BlockPos roomCenter = room.getPlacedWorldPos();
        Rotation roomRotation = room.getPlacedWorldRotation();

        BlockPos cornerOne = room.getRoom().getMinCorner(roomCenter, roomRotation);
        BlockPos cornerTwo = room.getRoom().getMaxCorner(roomCenter, roomRotation);

        return spawn(level, roomCenter, cornerOne, cornerTwo, roomRotation);
    }

    /**
     * spawn the mobs
     * @return a list of spawned mob UUISs
     */
    public abstract List<UUID> spawn(ServerLevel level, BlockPos roomCenter, BlockPos minCorner, BlockPos maxCorner, Rotation roomRotation);
}
