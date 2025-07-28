package net.emsee.thedungeon.dungeon.registry.roomCollections.cleanup;

import net.emsee.thedungeon.dungeon.src.room.GridRoomBasic;
import net.emsee.thedungeon.dungeon.src.GridRoomCollection;

public final class CleanupGridRoomCollection extends GridRoomCollection {

    public CleanupGridRoomCollection() {
        super(47, 47);
        this.setFallback(new GridRoomBasic("cleanup/room",47, 47))
                .addRoom(new GridRoomBasic("cleanup/room",47, 47)
                        .withWeight(1)
                        .allConnections());
    }

    @Override
    public GridRoomCollection getCopy(){
        return new CleanupGridRoomCollection();
    }
}
