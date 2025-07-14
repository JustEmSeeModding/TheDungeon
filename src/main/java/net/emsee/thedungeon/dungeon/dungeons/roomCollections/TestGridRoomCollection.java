package net.emsee.thedungeon.dungeon.dungeons.roomCollections;

import net.emsee.thedungeon.dungeon.src.room.GridRoomBasic;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.GridRoomCollection;

public final class TestGridRoomCollection extends GridRoomCollection {
        public TestGridRoomCollection() {
                super(3, 3);
                        this.setFallback(new GridRoomBasic("test/fallback",3, 3))
                        .addRoom(new GridRoomBasic("test/x",3, 3)
                                .withWeight(1)
                                .horizontalConnections()
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoomBasic("test/i", 3, 3)
                                .withWeight(1)
                                .addConnection(Connection.NORTH).addConnection(Connection.SOUTH)
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoomBasic("test/t",3, 3)
                                .withWeight(1)
                                .addConnection(Connection.NORTH).addConnection(Connection.SOUTH).addConnection(Connection.WEST)
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoomBasic("test/d",3, 3)
                                .withWeight(1)
                                .addConnection(Connection.DOWN)
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoomBasic("test/lr",3, 3)
                                .setSize(3, 1)
                                .withWeight(1)
                                .horizontalConnections()
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoomBasic("test/lro",3, 3)
                                .setSize(3, 1)
                                .setHorizontalConnectionOffset(Connection.WEST, 1, 0)
                                .withWeight(1)
                                .addConnection(Connection.WEST)
                                .doAllowRotation()
                        )
                        .addRoom(new GridRoomBasic("test/lrou",3, 3)
                                .setSize(3, 1)
                                .setVerticalConnectionOffset(Connection.UP, 1, 0)
                                .withWeight(1)
                                .addConnection(Connection.UP)
                                .horizontalConnections()
                                .doAllowRotation(true, true)
                        )
                        .addRoom(new GridRoomBasic("test/lrod",3, 3)
                                .setSize(3, 1)
                                .setVerticalConnectionOffset(Connection.DOWN, -1, 0)
                                .withWeight(1)
                                .addConnection(Connection.DOWN)
                                .doAllowRotation(true, true)
                        )
                        .addRoom(new GridRoomBasic("test/tag",3, 3)
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
