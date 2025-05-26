package net.emsee.thedungeon.dungeon.roomCollections.collections;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.room.GridRoom;
import net.emsee.thedungeon.dungeon.roomCollections.GridRoomCollection;
import net.minecraft.resources.ResourceLocation;

public final class CleanupGridRoomCollection extends GridRoomCollection {

    public CleanupGridRoomCollection() {
        super(47, 47);
        this.setFallback(new GridRoom(47, 47).withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "cleanup/room")))
                .addRoom(new GridRoom(47, 47).
                        withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "cleanup/room")).
                        withWeight(1).
                        allConnections());
    }

    @Override
    public GridRoomCollection getCopy(){
        return new CleanupGridRoomCollection();
    }
}
