package net.emsee.thedungeon.dungeon.room;

import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.minecraft.world.level.block.Rotation;

import java.util.*;

public final class GridRoomUtils {

    public enum Connection {
        north, east, south, west, up, down
    }

    public static Connection getRotatedConnection(Connection connection, Rotation roomRotation) {
        if (roomRotation == null) return null;
        if (connection == Connection.up || connection == Connection.down) return connection;

        Connection[][] connectionRotations = {
                {Connection.north, Connection.east, Connection.south, Connection.west},
                {Connection.west, Connection.north, Connection.east, Connection.south},
                {Connection.south, Connection.west, Connection.north, Connection.east},
                {Connection.east, Connection.south, Connection.west, Connection.north}
        };

        int connectionIndex = -1;
        switch (connection) {
            case north -> connectionIndex = 0;
            case east -> connectionIndex = 1;
            case south -> connectionIndex = 2;
            case west -> connectionIndex = 3;
        }

        int rotationIndex = -1;
        switch (roomRotation) {
            case NONE -> rotationIndex = 0;
            case COUNTERCLOCKWISE_90 -> rotationIndex = 1;
            case CLOCKWISE_180 -> rotationIndex = 2;
            case CLOCKWISE_90 -> rotationIndex = 3;
        }

        if (connectionIndex == -1 || rotationIndex == -1) return null;
        return connectionRotations[rotationIndex][connectionIndex];
    }

    public static Rotation getInvertedRotation(Rotation rotation) {
        return switch (rotation) {
            case NONE -> Rotation.NONE;
            case CLOCKWISE_90 -> Rotation.COUNTERCLOCKWISE_90;
            case COUNTERCLOCKWISE_90 -> Rotation.CLOCKWISE_90;
            case CLOCKWISE_180 -> Rotation.CLOCKWISE_180;
        };
    }

    public static Map<Connection, Integer> getRotatedConnections(Map<Connection, Integer> connections, Rotation roomRotation) {
        if (roomRotation == null) return null;
        if (roomRotation == Rotation.NONE) return connections;
        Map<Connection, Integer> toReturn = new HashMap<>();
        for (Connection connection : connections.keySet()) {
            toReturn.put(getRotatedConnection(connection, roomRotation), connections.get(connection));
        }
        return toReturn;
    }

    public static Map<Connection, String> getRotatedTags(Map<Connection, String> tags, Rotation roomRotation) {
        if (roomRotation == null) return null;
        if (roomRotation == Rotation.NONE) return tags;
        Map<Connection, String> toReturn = new HashMap<>();
        for (Connection connection : tags.keySet()) {
            toReturn.put(getRotatedConnection(connection, roomRotation), tags.get(connection));
        }
        return toReturn;
    }

    public static String GetRotatedTag(Map<Connection, String> tags, Connection connection, Rotation roomRotation) {
        return getRotatedTags(tags, roomRotation).get(connection);
    }

    static Connection getOppositeConnection(Connection connection) {
        switch (connection) {
            case north -> {
                return Connection.south;
            }
            case east -> {
                return Connection.west;
            }
            case south -> {
                return Connection.north;
            }
            case west -> {
                return Connection.east;
            }
            case up -> {
                return Connection.down;
            }
            case down -> {
                return Connection.up;
            }
        }
        return null;
    }

    public static Rotation getRandomRotation(Random random) {
        return ListAndArrayUtils.getRandomFromList(Arrays.stream(Rotation.values()).toList(), random);
    }
}
