package net.emsee.thedungeon.dungeon.src.types.grid.room;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/** A better way of storing rooms, allows editing all rooms at once for multiple variations of the same list */
public class GridRoomList {
    protected final ArrayList<AbstractGridRoom> rooms;

    private GridRoomList(ArrayList<AbstractGridRoom> rooms) {
        this.rooms = rooms;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<AbstractGridRoom> getList() {
        return new ArrayList<>(rooms);
    }

    public static class Builder {
        private final ArrayList<AbstractGridRoom.Builder<?>> list = new ArrayList<>();

        public GridRoomList build() {
            ArrayList<AbstractGridRoom> newList = new ArrayList<>();
            list.forEach(builder -> {
                newList.add(builder.build());
            });
            return new GridRoomList(newList);
        }

        public Builder addRoom(AbstractGridRoom.Builder<?> room) {
            list.add(room);
            return this;
        }

        public Builder applyToAll(Consumer<AbstractGridRoom.Builder<?>> method) {
            list.forEach(method);
            return this;
        }

        public Builder addRooms(List<AbstractGridRoom.Builder<?>> newRooms) {
            list.addAll(newRooms);
            return this;
        }

        public Builder addRooms(Builder otherBuilder) {
            list.addAll(otherBuilder.list);
            return this;
        }
    }
}
