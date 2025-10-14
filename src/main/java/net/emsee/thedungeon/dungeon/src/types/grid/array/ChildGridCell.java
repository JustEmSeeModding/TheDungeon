package net.emsee.thedungeon.dungeon.src.types.grid.array;

import net.emsee.thedungeon.dungeon.src.types.grid.room.AbstractGridRoom;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GeneratedRoom;

public class ChildGridCell extends GridCell{
    private final GridCell parent;

    public ChildGridCell(GridCell parent) {
        this.parent = parent;
    }

    @Override
    public boolean isOccupied() {
        return parent.isOccupied();
    }

    @Override
    public boolean isReplaceAllowed() {
        return parent.isReplaceAllowed();
    }

    @Override
    public boolean hasRoom() {
        return parent.hasRoom();
    }

    @Override
    public GeneratedRoom getRoom() {
        return parent.getRoom();
    }
}
