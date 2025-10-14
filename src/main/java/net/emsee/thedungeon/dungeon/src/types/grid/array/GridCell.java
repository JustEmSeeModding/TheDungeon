package net.emsee.thedungeon.dungeon.src.types.grid.array;

import net.emsee.thedungeon.dungeon.src.types.grid.room.AbstractGridRoom;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GeneratedRoom;

public abstract class GridCell {
    public abstract boolean isOccupied();
    public abstract boolean isReplaceAllowed();
    public abstract boolean hasRoom();
    public abstract GeneratedRoom getRoom();
}
