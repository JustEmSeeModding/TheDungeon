package net.emsee.thedungeon.dungeon.src.types.grid.array;

import net.emsee.thedungeon.dungeon.src.types.grid.room.AbstractGridRoom;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GeneratedRoom;

public class EmptyGridCell extends GridCell{
    @Override
    public boolean isOccupied() {
        return false;
    }

    @Override
    public boolean isReplaceAllowed() {
        return true;
    }

    @Override
    public boolean hasRoom() {
        return false;
    }

    @Override
    public GeneratedRoom getRoom() {
        return null;
    }
}
