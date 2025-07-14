package net.emsee.thedungeon.dungeon.src;

import net.minecraft.world.level.block.Rotation;

public enum Connection {
    NORTH, EAST, SOUTH, WEST, UP, DOWN;

    /**
     * returns the connection rotated
     */
    public Connection getRotated(Rotation rotation) {
        if (rotation == null) return null;
        if (this == Connection.UP || this == Connection.DOWN) return this;

        Connection[][] connectionRotations = {
                {Connection.NORTH, Connection.EAST, Connection.SOUTH, Connection.WEST}, // none
                {Connection.WEST, Connection.NORTH, Connection.EAST, Connection.SOUTH}, //+90
                {Connection.SOUTH, Connection.WEST, Connection.NORTH, Connection.EAST}, //180
                {Connection.EAST, Connection.SOUTH, Connection.WEST, Connection.NORTH}  //-90
        };

        int connectionIndex = -1;
        switch (this) {
            case NORTH -> connectionIndex = 0;
            case EAST -> connectionIndex = 1;
            case SOUTH -> connectionIndex = 2;
            case WEST -> connectionIndex = 3;
        }

        int rotationIndex = -1;
        switch (rotation) {
            case NONE -> rotationIndex = 0;
            case COUNTERCLOCKWISE_90 -> rotationIndex = 1;
            case CLOCKWISE_180 -> rotationIndex = 2;
            case CLOCKWISE_90 -> rotationIndex = 3;
        }

        if (connectionIndex == -1 || rotationIndex == -1) return null;
        return connectionRotations[rotationIndex][connectionIndex];
    }

    public Connection getOpposite() {
        Connection toReturn = null;
        switch (this) {
            case NORTH -> toReturn = SOUTH;
            case EAST -> toReturn = WEST;
            case SOUTH -> toReturn = NORTH;
            case WEST -> toReturn = EAST;
            case UP -> toReturn = DOWN;
            case DOWN -> toReturn = UP;

        }
        if (toReturn == null)
            throw new IllegalStateException("connection should not exist: " + this);
        return toReturn;
    }

    public boolean isHorizontal() {
        return
                this == NORTH ||
                        this == EAST ||
                        this == SOUTH ||
                        this == WEST;
    }
    public boolean isVertical() {
        return
                this == UP ||
                        this == DOWN;
    }
}
