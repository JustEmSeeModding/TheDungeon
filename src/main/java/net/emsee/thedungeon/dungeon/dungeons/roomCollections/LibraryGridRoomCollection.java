package net.emsee.thedungeon.dungeon.dungeons.roomCollections;

import net.emsee.thedungeon.dungeon.src.room.AbstractGridRoom;
import net.emsee.thedungeon.dungeon.src.room.GridRoomBasic;
import net.emsee.thedungeon.dungeon.src.room.GridRoomMultiResource;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.room.GridRoomGroup;
import net.emsee.thedungeon.dungeon.src.GridRoomCollection;
import net.emsee.thedungeon.structureProcessor.library.DefaultLibraryProcessor;

public final class LibraryGridRoomCollection extends GridRoomCollection {
    public LibraryGridRoomCollection() {
        super(13, 9);
        this.setFallback(new GridRoomBasic("library/fallback",13, 9))
                .setStartingRoom(new GridRoomBasic("library/crosses/default",13, 9).horizontalConnections())
                .addRoom(new GridRoomBasic("library/stairs/lower_north",13, 9).withWeight(3).addConnection(Connection.UP).addConnection(Connection.NORTH).doAllowRotation(true, false))
                .addRoom(new GridRoomBasic("library/stairs/upper_north",13, 9).withWeight(2).addConnection(Connection.DOWN).addConnection(Connection.NORTH).doAllowRotation(true, false))
                .addRoom(new GridRoomBasic("library/stairs/lower_west",13, 9).withWeight(3).addConnection(Connection.UP).addConnection(Connection.WEST).doAllowRotation(true, false))
                .addRoom(new GridRoomBasic("library/stairs/upper_west",13, 9).withWeight(2).addConnection(Connection.DOWN).addConnection(Connection.WEST).doAllowRotation(true, false))
                .addRoom(new GridRoomBasic("library/big/plaza",13, 9).withWeight(4).setSizeHeight(3, 3, 1).horizontalConnections())
                .addRoom(straights.withWeight(7))
                .addRoom(corners.withWeight(5))
                .addRoom(t.withWeight(10))
                .addRoom(crosses.withWeight(15))
                .addRoom(smallRooms.withWeight(3))

                .addRequiredRoom(1, 1, new GridRoomBasic("library/big/portal",13, 9).withWeight(4).setGenerationPriority(1).horizontalConnections().setSizeHeight(3, 3, 1))
                .addRequiredRoom(3, 5, new GridRoomBasic("library/crosses/portal",13, 9).withWeight(4).setGenerationPriority(1).horizontalConnections())
                .withStructureProcessor(DefaultLibraryProcessor.INSTANCE);
    }

    static AbstractGridRoom straights = new GridRoomMultiResource(13, 9)
            .withResourceLocation("library/straights/default",10)
            .withResourceLocation("library/straights/benches",2)
            .withResourceLocation("library/straights/missing_shelf",5)
            .withResourceLocation("library/straights/no_books",5)
            .addConnection(Connection.NORTH)
            .addConnection(Connection.SOUTH)
            .doAllowRotation();

    static AbstractGridRoom corners = new GridRoomMultiResource(13, 9)
            .withResourceLocation("library/corners/default",10)
            .withResourceLocation("library/corners/no_books",5)
            .withResourceLocation("library/corners/benches",2)
            .withResourceLocation("library/corners/natural",2)
            .addConnection(Connection.NORTH)
            .addConnection(Connection.EAST)
            .doAllowRotation();

    static AbstractGridRoom t = new GridRoomMultiResource(13, 9)
            .withResourceLocation("library/t/default",10)
            .withResourceLocation("library/t/no_books",5)
            .withResourceLocation("library/t/wall_art",2)
            .addConnection(Connection.NORTH)
            .addConnection(Connection.EAST)
            .addConnection(Connection.SOUTH)
            .doAllowRotation();

    static AbstractGridRoom crosses = new GridRoomMultiResource(13, 9)
            .withResourceLocation("library/crosses/default",10)
            .withResourceLocation("library/crosses/pillar",3)
            .withResourceLocation("library/crosses/no_books",5)
            .withResourceLocation("library/crosses/tree",1)
            .withResourceLocation("library/crosses/aquarium",1)
            .horizontalConnections();

    static GridRoomGroup smallRooms = ((GridRoomGroup) new GridRoomGroup(13, 9).addConnection(Connection.NORTH).doAllowRotation())
            .addRoom(new GridRoomBasic("library/rooms/enchanting",13, 9).addConnection(Connection.NORTH).doAllowRotation())
            .addRoom(new GridRoomBasic("library/rooms/smithing",13, 9).addConnection(Connection.NORTH).doAllowRotation());


    @Override
    public GridRoomCollection getCopy() {
        return new LibraryGridRoomCollection();
    }
}
