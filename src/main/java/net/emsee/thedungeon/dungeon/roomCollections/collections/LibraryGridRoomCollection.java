package net.emsee.thedungeon.dungeon.roomCollections.collections;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.room.GridRoomUtils;
import net.emsee.thedungeon.dungeon.room.GridRoom;
import net.emsee.thedungeon.dungeon.room.GridRoomGroup;
import net.emsee.thedungeon.dungeon.roomCollections.GridRoomCollection;
import net.minecraft.resources.ResourceLocation;

public final class LibraryGridRoomCollection extends GridRoomCollection {
    public LibraryGridRoomCollection() {
        super(13, 9);
        this.setFallback(new GridRoom(13, 9).withResourceLocation("library/fallback"))
                .setStartingRoom(new GridRoom(13, 9).withResourceLocation("library/crosses/default").horizontalConnections())
                .addRoom(new GridRoom(13, 9).withWeight(3).withResourceLocation("library/stairs/lower_north").addConnection(GridRoomUtils.Connection.UP).addConnection(GridRoomUtils.Connection.NORTH).doAllowRotation(true, false))
                .addRoom(new GridRoom(13, 9).withWeight(2).withResourceLocation("library/stairs/upper_north").addConnection(GridRoomUtils.Connection.DOWN).addConnection(GridRoomUtils.Connection.NORTH).doAllowRotation(true, false))
                .addRoom(new GridRoom(13, 9).withWeight(3).withResourceLocation("library/stairs/lower_west").addConnection(GridRoomUtils.Connection.UP).addConnection(GridRoomUtils.Connection.WEST).doAllowRotation(true, false))
                .addRoom(new GridRoom(13, 9).withWeight(2).withResourceLocation("library/stairs/upper_west").addConnection(GridRoomUtils.Connection.DOWN).addConnection(GridRoomUtils.Connection.WEST).doAllowRotation(true, false))
                .addRoom(new GridRoom(13, 9).withWeight(4).withResourceLocation("library/big/plaza").setSizeHeight(3, 3, 1).horizontalConnections())
                .addRoom(straights.withWeight(7))
                .addRoom(corners.withWeight(5))
                .addRoom(t.withWeight(10))
                .addRoom(crosses.withWeight(15))
                .addRoom(rooms.withWeight(3))

                .addRequiredRoom(1, 1, new GridRoom(13, 9).withWeight(4).setGenerationPriority(1).withResourceLocation("library/big/portal").horizontalConnections().setSizeHeight(3, 3, 1))
                .addRequiredRoom(3, 5, new GridRoom(13, 9).withWeight(4).setGenerationPriority(1).withResourceLocation("library/crosses/portal").horizontalConnections());
    }

    static GridRoom straights = ((GridRoomGroup) new GridRoomGroup(13, 9).addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.SOUTH).doAllowRotation())
            .addRoom(new GridRoom(13, 9).withWeight(10).withResourceLocation("library/straights/default").doAllowRotation().addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.SOUTH))
            .addRoom(new GridRoom(13, 9).withWeight(2).withResourceLocation("library/straights/benches").doAllowRotation().addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.SOUTH))
            .addRoom(new GridRoom(13, 9).withWeight(5).withResourceLocation("library/straights/missing_shelf").doAllowRotation().addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.SOUTH))
            .addRoom(new GridRoom(13, 9).withWeight(5).withResourceLocation("library/straights/no_books").doAllowRotation().addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.SOUTH));

    static GridRoom corners = ((GridRoomGroup) new GridRoomGroup(13, 9).addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.EAST).doAllowRotation())
            .addRoom(new GridRoom(13, 9).withWeight(10).withResourceLocation("library/corners/default").addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.EAST).doAllowRotation())
            .addRoom(new GridRoom(13, 9).withWeight(5).withResourceLocation("library/corners/no_books").addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.EAST).doAllowRotation())
            .addRoom(new GridRoom(13, 9).withWeight(2).withResourceLocation("library/corners/benches").addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.EAST).doAllowRotation())
            .addRoom(new GridRoom(13, 9).withWeight(2).withResourceLocation("library/corners/natural").addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.EAST).doAllowRotation());

    static GridRoomGroup t = ((GridRoomGroup) new GridRoomGroup(13, 9).addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.EAST).addConnection(GridRoomUtils.Connection.SOUTH).doAllowRotation())
            .addRoom(new GridRoom(13, 9).withWeight(10).withResourceLocation("library/t/default").addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.EAST).addConnection(GridRoomUtils.Connection.SOUTH).doAllowRotation())
            .addRoom(new GridRoom(13, 9).withWeight(5).withResourceLocation("library/t/no_books").addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.EAST).addConnection(GridRoomUtils.Connection.SOUTH).doAllowRotation())
            .addRoom(new GridRoom(13, 9).withWeight(1).withResourceLocation("library/t/wall_art").addConnection(GridRoomUtils.Connection.NORTH).addConnection(GridRoomUtils.Connection.EAST).addConnection(GridRoomUtils.Connection.SOUTH).doAllowRotation());

    static GridRoomGroup crosses = ((GridRoomGroup) new GridRoomGroup(13, 9).horizontalConnections())
            .addRoom(new GridRoom(13, 9).withWeight(10).withResourceLocation("library/crosses/default").horizontalConnections())
            .addRoom(new GridRoom(13, 9).withWeight(3).withResourceLocation("library/crosses/pillar").horizontalConnections())
            .addRoom(new GridRoom(13, 9).withWeight(5).withResourceLocation("library/crosses/no_books").horizontalConnections())
            .addRoom(new GridRoom(13, 9).withWeight(1).withResourceLocation("library/crosses/tree").horizontalConnections().doAllowRotation())
            .addRoom(new GridRoom(13, 9).withWeight(1).withResourceLocation("library/crosses/aquarium").horizontalConnections().doAllowRotation());


    static GridRoomGroup rooms = ((GridRoomGroup) new GridRoomGroup(13, 9).addConnection(GridRoomUtils.Connection.NORTH).doAllowRotation())
            .addRoom(new GridRoom(13, 9).withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "library/rooms/enchanting")).addConnection(GridRoomUtils.Connection.NORTH).doAllowRotation())
            .addRoom(new GridRoom(13, 9).withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "library/rooms/smithing")).addConnection(GridRoomUtils.Connection.NORTH).doAllowRotation());


    @Override
    public GridRoomCollection getCopy() {
        return new LibraryGridRoomCollection();
    }
}
