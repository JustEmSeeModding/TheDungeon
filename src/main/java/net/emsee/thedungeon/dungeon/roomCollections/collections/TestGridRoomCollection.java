package net.emsee.thedungeon.dungeon.roomCollections.collections;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.room.GridRoomUtils;
import net.emsee.thedungeon.dungeon.room.GridRoom;
import net.emsee.thedungeon.dungeon.roomCollections.GridRoomCollection;
import net.minecraft.resources.ResourceLocation;

public final class TestGridRoomCollection extends GridRoomCollection {
        public TestGridRoomCollection() {
                super(3, 3);
                        this.setFallback(new GridRoom(3, 3).withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/fallback")))
                        .addRoom(new GridRoom(3, 3)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/x"))
                                .withWeight(1)
                                .horizontalConnections()
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoom(3, 3)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/i"))
                                .withWeight(1)
                                .addConnection(GridRoomUtils.Connection.north).addConnection(GridRoomUtils.Connection.south)
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoom(3, 3)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/t"))
                                .withWeight(1)
                                .addConnection(GridRoomUtils.Connection.north).addConnection(GridRoomUtils.Connection.south).addConnection(GridRoomUtils.Connection.west)
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoom(3, 3)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/d"))
                                .withWeight(1)
                                .addConnection(GridRoomUtils.Connection.down)
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoom(3, 3)
                                .setSize(3, 1)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/lr"))
                                .withWeight(1)
                                .horizontalConnections()
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoom(3, 3)
                                .setSize(3, 1)
                                .setHorizontalConnectionOffset(GridRoomUtils.Connection.west, 1, 0)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/lro"))
                                .withWeight(1)
                                .addConnection(GridRoomUtils.Connection.west)
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoom(3, 3)
                                .setSize(3, 1)
                                .setVerticalConnectionOffset(GridRoomUtils.Connection.up, 1, 0)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/lrou"))
                                .withWeight(1)
                                .addConnection(GridRoomUtils.Connection.up)
                                .horizontalConnections()
                                .doAllowRotation(true, true)
                        )
                        .addRoom(new GridRoom(3, 3)
                                .setSize(3, 1)
                                .setVerticalConnectionOffset(GridRoomUtils.Connection.down, -1, 0)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/lrod"))
                                .withWeight(1)
                                .addConnection(GridRoomUtils.Connection.down)
                                .doAllowRotation(true, true)
                        )
                        .addRoom(new GridRoom(3, 3)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/tag"))
                                .withWeight(1)
                                .setConnectionTag(GridRoomUtils.Connection.north, "stone")
                                .doAllowRotation()
                                .addConnection(GridRoomUtils.Connection.north)
                                .addConnection(GridRoomUtils.Connection.south)
                        );
        }

        @Override
        public GridRoomCollection getCopy() {
                return new TestGridRoomCollection();
        }
}
