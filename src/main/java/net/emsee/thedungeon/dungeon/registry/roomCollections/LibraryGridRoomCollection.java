package net.emsee.thedungeon.dungeon.registry.roomCollections;

import net.emsee.thedungeon.dungeon.src.connectionRules.ConnectionRule;
import net.emsee.thedungeon.dungeon.src.connectionRules.fail.FloorFailRule;
import net.emsee.thedungeon.dungeon.src.types.grid.room.AbstractGridRoom;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GridRoomBasic;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GridRoomMultiResource;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GridRoomGroup;
import net.emsee.thedungeon.dungeon.src.types.grid.roomCollection.GridRoomCollection;
import net.emsee.thedungeon.structureProcessor.library.DefaultLibraryProcessor;
import net.minecraft.world.level.block.Blocks;

public final class LibraryGridRoomCollection extends GridRoomCollection {
    public LibraryGridRoomCollection() {
        super(13, 9);
        this.setFallback(GridRoomBasic.builder("library/fallback",13, 9).build())
                .setStartingRoom(GridRoomBasic.builder("library/crosses/default",13, 9).horizontalConnections().build())
                .addRoom(GridRoomBasic.builder("library/stairs/lower_north",13, 9).withWeight(3).addConnection(Connection.UP).addConnection(Connection.NORTH).doAllowRotation(true, false).build())
                .addRoom(GridRoomBasic.builder("library/stairs/upper_north",13, 9).withWeight(2).addConnection(Connection.DOWN).addConnection(Connection.NORTH).doAllowRotation(true, false).build())
                .addRoom(GridRoomBasic.builder("library/stairs/lower_west",13, 9).withWeight(3).addConnection(Connection.UP).addConnection(Connection.WEST).doAllowRotation(true, false).build())
                .addRoom(GridRoomBasic.builder("library/stairs/upper_west",13, 9).withWeight(2).addConnection(Connection.DOWN).addConnection(Connection.WEST).doAllowRotation(true, false).build())
                .addRoom(GridRoomBasic.builder("library/big/plaza",13, 9).withWeight(4).setSizeHeight(3, 3, 1).horizontalConnections().build())
                .addRoom(straights.withWeight(7).build())
                .addRoom(corners.withWeight(5).build())
                .addRoom(t.withWeight(10).build())
                .addRoom(crosses.withWeight(15).build())
                .addRoom(smallRooms.withWeight(3).build())

                .addRequiredRoom(1, 1, GridRoomBasic.builder("library/big/portal",13, 9).withWeight(4).setGenerationPriority(1).horizontalConnections().setSizeHeight(3, 3, 1).build())
                .addRequiredRoom(3, 5, GridRoomBasic.builder("library/crosses/portal",13, 9).withWeight(4).setGenerationPriority(1).horizontalConnections().build())
                .withStructureProcessor(DefaultLibraryProcessor.INSTANCE)

                .addTagRule(new FloorFailRule(ConnectionRule.DEFAULT_CONNECTION_TAG, 13, false, Blocks.OAK_PLANKS::defaultBlockState, 169));
    }

    static AbstractGridRoom.Builder<?,?> straights = GridRoomMultiResource.builder(13, 9)
            .withResourceLocation("library/straights/default",10)
            .withResourceLocation("library/straights/benches",2)
            .withResourceLocation("library/straights/missing_shelf",5)
            .withResourceLocation("library/straights/no_books",5)
            .addConnection(Connection.NORTH)
            .addConnection(Connection.SOUTH)
            .doAllowRotation();

    static AbstractGridRoom.Builder<?,?> corners = GridRoomMultiResource.builder(13, 9)
            .withResourceLocation("library/corners/default",10)
            .withResourceLocation("library/corners/no_books",5)
            .withResourceLocation("library/corners/benches",2)
            .withResourceLocation("library/corners/natural",2)
            .addConnection(Connection.NORTH)
            .addConnection(Connection.EAST)
            .doAllowRotation();

    static AbstractGridRoom.Builder<?,?> t = GridRoomMultiResource.builder(13, 9)
            .withResourceLocation("library/t/default",10)
            .withResourceLocation("library/t/no_books",5)
            .withResourceLocation("library/t/wall_art",2)
            .addConnection(Connection.NORTH)
            .addConnection(Connection.EAST)
            .addConnection(Connection.SOUTH)
            .doAllowRotation();

    static AbstractGridRoom.Builder<?,?> crosses = GridRoomMultiResource.builder(13, 9)
            .withResourceLocation("library/crosses/default",10)
            .withResourceLocation("library/crosses/pillar",3)
            .withResourceLocation("library/crosses/no_books",5)
            .withResourceLocation("library/crosses/tree",1)
            .withResourceLocation("library/crosses/aquarium",1)
            .horizontalConnections();

    static AbstractGridRoom.Builder<?,?> smallRooms = GridRoomGroup.builder(13, 9).addConnection(Connection.NORTH).doAllowRotation()
            .addRoom(GridRoomBasic.builder("library/rooms/enchanting",13, 9).addConnection(Connection.NORTH).doAllowRotation())
            .addRoom(GridRoomBasic.builder("library/rooms/smithing",13, 9).addConnection(Connection.NORTH).doAllowRotation());
}
