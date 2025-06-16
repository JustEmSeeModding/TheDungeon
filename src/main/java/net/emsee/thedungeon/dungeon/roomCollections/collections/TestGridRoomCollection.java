package net.emsee.thedungeon.dungeon.roomCollections.collections;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.util.Connection;
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
                                .addConnection(Connection.NORTH).addConnection(Connection.SOUTH)
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoom(3, 3)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/t"))
                                .withWeight(1)
                                .addConnection(Connection.NORTH).addConnection(Connection.SOUTH).addConnection(Connection.WEST)
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoom(3, 3)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/d"))
                                .withWeight(1)
                                .addConnection(Connection.DOWN)
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
                                .setHorizontalConnectionOffset(Connection.WEST, 1, 0)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/lro"))
                                .withWeight(1)
                                .addConnection(Connection.WEST)
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoom(3, 3)
                                .setSize(3, 1)
                                .setVerticalConnectionOffset(Connection.UP, 1, 0)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/lrou"))
                                .withWeight(1)
                                .addConnection(Connection.UP)
                                .horizontalConnections()
                                .doAllowRotation(true, true)
                        )
                        .addRoom(new GridRoom(3, 3)
                                .setSize(3, 1)
                                .setVerticalConnectionOffset(Connection.DOWN, -1, 0)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/lrod"))
                                .withWeight(1)
                                .addConnection(Connection.DOWN)
                                .doAllowRotation(true, true)
                        )
                        .addRoom(new GridRoom(3, 3)
                                .withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "test/tag"))
                                .withWeight(1)
                                .setConnectionTag(Connection.NORTH, "stone")
                                .doAllowRotation()
                                .addConnection(Connection.NORTH)
                                .addConnection(Connection.SOUTH)
                        );
        }

        @Override
        public GridRoomCollection getCopy() {
                return new TestGridRoomCollection();
        }
}
