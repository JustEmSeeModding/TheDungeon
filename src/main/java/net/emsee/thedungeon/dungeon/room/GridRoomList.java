package net.emsee.thedungeon.dungeon.room;

import java.util.ArrayList;
import java.util.function.Consumer;

/** a better way of soring rooms, allows to edit all rooms at once for multiple variations of the same list */
public class GridRoomList extends ArrayList<GridRoom> {
    public GridRoomList addRoom(GridRoom room) {
        add(room);
        return this;
    }

    public GridRoomList applyToAll(Consumer<GridRoom> method) {
        forEach(method);
        return this;
    }

    public GridRoomList getCopy() {
        GridRoomList toReturn = new GridRoomList();
        for(GridRoom room : this)
            toReturn.add(room.getCopy());
        return toReturn;
    }
}
