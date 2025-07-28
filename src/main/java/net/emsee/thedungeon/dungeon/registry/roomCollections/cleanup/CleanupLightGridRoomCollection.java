package net.emsee.thedungeon.dungeon.registry.roomCollections.cleanup;

import net.emsee.thedungeon.dungeon.src.GridRoomCollection;
import net.emsee.thedungeon.dungeon.src.room.GridRoomBasic;
import net.emsee.thedungeon.dungeon.src.room.GridRoomEmpty;
import net.emsee.thedungeon.structureProcessor.cleanup.CleanIllegalBlocksProcessor;

public final class CleanupLightGridRoomCollection extends GridRoomCollection {
    public CleanupLightGridRoomCollection() {
        super(101, 101);
        this.setFallback(new GridRoomEmpty(101, 101)
                        .withStructurePostProcessor(CleanIllegalBlocksProcessor.INSTANCE))
                .addRoom(new GridRoomEmpty(101, 101)
                        .withWeight(1)
                        .allConnections()
                        .withStructurePostProcessor(CleanIllegalBlocksProcessor.INSTANCE));
    }

    @Override
    public GridRoomCollection getCopy(){
        return new CleanupLightGridRoomCollection();
    }
}
