package net.emsee.thedungeon.dungeon.src.types.grid.array;

import net.emsee.thedungeon.dungeon.src.types.grid.room.GeneratedRoom;

public class OccupiedGridCell extends GridCell{
    final GeneratedRoom placedRoom;
    final boolean allowReplace;

    public OccupiedGridCell (GeneratedRoom room) {
        this(room, false);
    }


    public OccupiedGridCell (GeneratedRoom room, boolean allowReplace) {
        if (room == null) throw new IllegalArgumentException("OccupiedGridCell requires non-null room");
        this.placedRoom = room;
        this.allowReplace=allowReplace;
    }

    @Override
    public boolean isOccupied() {
        return true;
    }

    @Override
    public boolean isReplaceAllowed() {
        return allowReplace;
    }

    @Override
    public boolean hasRoom() {
        return true;
    }

    @Override
    public GeneratedRoom getRoom() {
        return placedRoom;
    }
}
