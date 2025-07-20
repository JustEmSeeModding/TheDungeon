package net.emsee.thedungeon.dungeon.src;

import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.utils.PriorityMap;
import net.minecraft.world.level.block.Rotation;

import java.util.*;

public final class DungeonUtils {
        // gets the opposite of the rotation
    public static Rotation getInvertedRotation(Rotation rotation) {
        return switch (rotation) {
            case NONE -> Rotation.NONE;
            case CLOCKWISE_90 -> Rotation.COUNTERCLOCKWISE_90;
            case COUNTERCLOCKWISE_90 -> Rotation.CLOCKWISE_90;
            case CLOCKWISE_180 -> Rotation.CLOCKWISE_180;
        };
    }

    /**
     * returns the priority connection map rotated
     */
    public static PriorityMap<Connection> getRotatedConnectionMap(PriorityMap<Connection> connections, Rotation roomRotation) {
        if (roomRotation == null) return null;
        if (roomRotation == Rotation.NONE) return connections;
        PriorityMap<Connection> toReturn = new PriorityMap<>();
        for (Connection connection : connections.keySet()) {
            toReturn.put(connection.getRotated(roomRotation), connections.get(connection));
        }
        return toReturn;
    }

    /**
     * returns the tag map rotated
     */
    public static Map<Connection, String> getRotatedTags(Map<Connection, String> tags, Rotation roomRotation) {
        if (roomRotation == null) return null;
        if (roomRotation == Rotation.NONE) return tags;
        Map<Connection, String> toReturn = new HashMap<>();
        for (Connection connection : tags.keySet()) {
            toReturn.put(connection.getRotated(roomRotation), tags.get(connection));
        }
        return toReturn;
    }

    public static Rotation getRandomRotation(Random random) {
        return ListAndArrayUtils.getRandomFromList(Arrays.stream(Rotation.values()).toList(), random);
    }

}
