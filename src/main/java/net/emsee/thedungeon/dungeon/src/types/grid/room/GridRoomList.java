package net.emsee.thedungeon.dungeon.src.types.grid.room;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/** A better way of storing rooms, allows editing all rooms at once for multiple variations of the same list */
public class GridRoomList extends ArrayList<AbstractGridRoom> {
    public GridRoomList addRoom(AbstractGridRoom room) {
        add(room);
        return this;
    }

    public GridRoomList applyToAll(Consumer<AbstractGridRoom> method) {
        forEach(method);
        return this;
    }

    public GridRoomList getCopy() {
        GridRoomList toReturn = new GridRoomList();
        toReturn.addAll(this);
        return toReturn;
    }

    public GridRoomList addRooms(List<AbstractGridRoom> rooms) {
        addAll(rooms);
        return this;
    }
}
