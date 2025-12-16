package net.emsee.thedungeon.dungeon.registry.roomCollections.cleanup;

import net.emsee.thedungeon.dungeon.src.types.grid.roomCollection.GridRoomCollection;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GridRoomEmpty;
import net.emsee.thedungeon.structureProcessor.cleanup.CleanIllegalBlocksProcessor;

public final class CleanupLightGridRoomCollection extends GridRoomCollection {
    public CleanupLightGridRoomCollection() {
        super(51,51);
        this.setFallback(GridRoomEmpty.builder(51, 51)
                        .withStructurePostProcessor(CleanIllegalBlocksProcessor.INSTANCE).build())
                .addRoom(GridRoomEmpty.builder(51, 51)
                        .withWeight(1)
                        .allConnections()
                        .withStructurePostProcessor(CleanIllegalBlocksProcessor.INSTANCE).build());
    }
}
