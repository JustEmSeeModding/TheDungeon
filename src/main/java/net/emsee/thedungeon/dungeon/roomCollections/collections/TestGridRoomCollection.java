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
                                .addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.SOUTH)
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoom(3, 3)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/t"))
                                .withWeight(1)
                                .addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.SOUTH).addConnection(GridRoomUtils.Connection.WEST)
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoom(3, 3)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/d"))
                                .withWeight(1)
                                .addConnection(GridRoomUtils.Connection.DOWN)
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
                                .setHorizontalConnectionOffset(GridRoomUtils.Connection.WEST, 1, 0)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/lro"))
                                .withWeight(1)
                                .addConnection(GridRoomUtils.Connection.WEST)
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoom(3, 3)
                                .setSize(3, 1)
                                .setVerticalConnectionOffset(GridRoomUtils.Connection.UP, 1, 0)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/lrou"))
                                .withWeight(1)
                                .addConnection(GridRoomUtils.Connection.UP)
                                .horizontalConnections()
                                .doAllowRotation(true, true)
                        )
                        .addRoom(new GridRoom(3, 3)
                                .setSize(3, 1)
                                .setVerticalConnectionOffset(GridRoomUtils.Connection.DOWN, -1, 0)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/lrod"))
                                .withWeight(1)
                                .addConnection(GridRoomUtils.Connection.DOWN)
                                .doAllowRotation(true, true)
                        )
                        .addRoom(new GridRoom(3, 3)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/tag"))
                                .withWeight(1)
                                .setConnectionTag(GridRoomUtils.Connection.NORTH, "stone")
                                .doAllowRotation()
                                .addConnection(GridRoomUtils.Connection.NORTH)
                                .addConnection(GridRoomUtils.Connection.SOUTH)
                        );
        }

        @Override
        public GridRoomCollection getCopy() {
                return new TestGridRoomCollection();
        }
}
