package net.emsee.thedungeon.dungeon.registry.roomCollections.cleanup;

import net.emsee.thedungeon.dungeon.src.types.grid.room.GridRoomBasic;
import net.emsee.thedungeon.dungeon.src.types.grid.roomCollection.GridRoomCollection;

public final class CleanupGridRoomCollection extends GridRoomCollection {

    public CleanupGridRoomCollection() {
        super(47, 47);
        this.setFallback(GridRoomBasic.builder("cleanup/room",47, 47).build())
                .addRoom(GridRoomBasic.builder("cleanup/room",47, 47)
                        .withWeight(1)
                        .allConnections().build());
    }
}
