package net.emsee.thedungeon.dungeon.room;

import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.minecraft.world.level.block.Rotation;

import java.util.*;

public final class GridRoomUtils {
    public enum Connection {
        NORTH, EAST, SOUTH, WEST, UP, DOWN
    }

    /**
     * returns the connection rotated
     */
    public static Connection getRotatedConnection(Connection connection, Rotation roomRotation) {
        if (roomRotation == null) return null;
        if (connection == Connection.UP || connection == Connection.DOWN) return connection;

        Connection[][] connectionRotations = {
                {Connection.NORTH, Connection.EAST, Connection.SOUTH, Connection.WEST}, // none
                {Connection.WEST, Connection.NORTH, Connection.EAST, Connection.SOUTH}, //+90
                {Connection.SOUTH, Connection.WEST, Connection.NORTH, Connection.EAST}, //180
                {Connection.EAST, Connection.SOUTH, Connection.WEST, Connection.NORTH}  //-90
        };

        int connectionIndex = -1;
        switch (connection) {
            case NORTH -> connectionIndex = 0;
            case EAST -> connectionIndex = 1;
            case SOUTH -> connectionIndex = 2;
            case WEST -> connectionIndex = 3;
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
     * returns the weighted connection map rotated
     */
    public static Map<Connection, Integer> getRotatedConnections(Map<Connection, Integer> connections, Rotation roomRotation) {
        if (roomRotation == null) return null;
        if (roomRotation == Rotation.NONE) return connections;
        Map<Connection, Integer> toReturn = new HashMap<>();
        for (Connection connection : connections.keySet()) {
            toReturn.put(getRotatedConnection(connection, roomRotation), connections.get(connection));
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
            toReturn.put(getRotatedConnection(connection, roomRotation), tags.get(connection));
        }
        return toReturn;
    }

    static Connection getOppositeConnection(Connection connection) {
        switch (connection) {
            case NORTH -> {
                return Connection.SOUTH;
            }
            case EAST -> {
                return Connection.WEST;
            }
            case SOUTH -> {
                return Connection.NORTH;
            }
            case WEST -> {
                return Connection.EAST;
            }
            case UP -> {
                return Connection.DOWN;
            }
            case DOWN -> {
                return Connection.UP;
            }
        }
        throw new IllegalStateException("connection should not exist: " + connection);
    }


    public static Rotation getRandomRotation(Random random) {
        return ListAndArrayUtils.getRandomFromList(Arrays.stream(Rotation.values()).toList(), random);
    }
}
