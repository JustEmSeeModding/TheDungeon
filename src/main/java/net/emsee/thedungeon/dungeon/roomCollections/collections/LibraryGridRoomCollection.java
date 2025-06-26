package net.emsee.thedungeon.dungeon.roomCollections.collections;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.room.MultiResourceGridRoom;
import net.emsee.thedungeon.dungeon.util.Connection;
import net.emsee.thedungeon.dungeon.room.GridRoom;
import net.emsee.thedungeon.dungeon.room.GridRoomGroup;
import net.emsee.thedungeon.dungeon.roomCollections.GridRoomCollection;
import net.minecraft.resources.ResourceLocation;

public final class LibraryGridRoomCollection extends GridRoomCollection {
    public LibraryGridRoomCollection() {
        super(13, 9);
        this.setFallback(new GridRoom(13, 9).withResourceLocation("library/fallback"))
                .setStartingRoom(new GridRoom(13, 9).withResourceLocation("library/crosses/default").horizontalConnections())
                .addRoom(new GridRoom(13, 9).withWeight(3).withResourceLocation("library/stairs/lower_north").addConnection(Connection.UP).addConnection(Connection.NORTH).doAllowRotation(true, false))
                .addRoom(new GridRoom(13, 9).withWeight(2).withResourceLocation("library/stairs/upper_north").addConnection(Connection.DOWN).addConnection(Connection.NORTH).doAllowRotation(true, false))
                .addRoom(new GridRoom(13, 9).withWeight(3).withResourceLocation("library/stairs/lower_west").addConnection(Connection.UP).addConnection(Connection.WEST).doAllowRotation(true, false))
                .addRoom(new GridRoom(13, 9).withWeight(2).withResourceLocation("library/stairs/upper_west").addConnection(Connection.DOWN).addConnection(Connection.WEST).doAllowRotation(true, false))
                .addRoom(new GridRoom(13, 9).withWeight(4).withResourceLocation("library/big/plaza").setSizeHeight(3, 3, 1).horizontalConnections())
                .addRoom(straights.withWeight(7))
                .addRoom(corners.withWeight(5))
                .addRoom(t.withWeight(10))
                .addRoom(crosses.withWeight(15))
                .addRoom(smallRooms.withWeight(3))

                .addRequiredRoom(1, 1, new GridRoom(13, 9).withWeight(4).setGenerationPriority(1).withResourceLocation("library/big/portal").horizontalConnections().setSizeHeight(3, 3, 1))
                .addRequiredRoom(3, 5, new GridRoom(13, 9).withWeight(4).setGenerationPriority(1).withResourceLocation("library/crosses/portal").horizontalConnections());
    }

    static GridRoom straights = new MultiResourceGridRoom(13, 9)
            .withResourceLocation("library/straights/default",10)
            .withResourceLocation("library/straights/benches",2)
            .withResourceLocation("library/straights/missing_shelf",5)
            .withResourceLocation("library/straights/no_books",5)
            .addConnection(Connection.NORTH)
            .addConnection(Connection.SOUTH)
            .doAllowRotation();

    static GridRoom corners = new MultiResourceGridRoom(13, 9)
            .withResourceLocation("library/corners/default",10)
            .withResourceLocation("library/corners/no_books",5)
            .withResourceLocation("library/corners/benches",2)
            .withResourceLocation("library/corners/natural",2)
            .addConnection(Connection.NORTH)
            .addConnection(Connection.EAST)
            .doAllowRotation();

    static GridRoom t = new MultiResourceGridRoom(13, 9)
            .withResourceLocation("library/t/default",10)
            .withResourceLocation("library/t/no_books",5)
            .withResourceLocation("library/t/wall_art",2)
            .addConnection(Connection.NORTH)
            .addConnection(Connection.EAST)
            .addConnection(Connection.SOUTH)
            .doAllowRotation();

    static GridRoom crosses = new MultiResourceGridRoom(13, 9)
            .withResourceLocation("library/crosses/default",10)
            .withResourceLocation("library/crosses/pillar",3)
            .withResourceLocation("library/crosses/no_books",5)
            .withResourceLocation("library/crosses/tree",1)
            .withResourceLocation("library/crosses/aquarium",1)
            .horizontalConnections();

    static GridRoomGroup smallRooms = ((GridRoomGroup) new GridRoomGroup(13, 9).addConnection(Connection.NORTH).doAllowRotation())
            .addRoom(new GridRoom(13, 9).withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "library/rooms/enchanting")).addConnection(Connection.NORTH).doAllowRotation())
            .addRoom(new GridRoom(13, 9).withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "library/rooms/smithing")).addConnection(Connection.NORTH).doAllowRotation());


    @Override
    public GridRoomCollection getCopy() {
        return new LibraryGridRoomCollection();
    }
}
