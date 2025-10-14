package net.emsee.thedungeon.dungeon.registry.roomCollections;

import net.emsee.thedungeon.dungeon.src.connectionRules.connection.CanConnectBothWays;
import net.emsee.thedungeon.dungeon.src.connectionRules.connection.CanConnectOneWay;
import net.emsee.thedungeon.dungeon.src.connectionRules.connection.CantConnectToSelf;
import net.emsee.thedungeon.dungeon.src.connectionRules.ConnectionRule;
import net.emsee.thedungeon.dungeon.src.connectionRules.fail.WallFailRule;
import net.emsee.thedungeon.dungeon.src.types.grid.room.AbstractGridRoom;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GridRoomBasic;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GridRoomMultiResource;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.mobSpawnRules.rules.SpawnAt;
import net.emsee.thedungeon.dungeon.src.types.roomCollection.GridRoomCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;

public final class CastleGridRoomCollection extends GridRoomCollection {
    public CastleGridRoomCollection() {
        super(9, 9);
        this.setFallback(GridRoomBasic.builder("castle/fallback",9, 9).build())
                .setStartingRoom(GridRoomBasic.builder("castle/mainhall/center",9, 9)
                        .setSizeHeight(3, 3, 4)
                        .horizontalConnections().setAllConnectionTags("main_hall")
                        .setOverrideEndChance(0).setGenerationPriority(10)
                        .addMobSpawnRule(new SpawnAt<>(() -> EntityType.ZOGLIN, new BlockPos(0, 3, 0))).build())

                .addRequiredRoom(4, 6, GridRoomBasic.builder("castle/portals/default",9, 9)
                        .withWeight(4)
                        .addConnection(Connection.NORTH).build())

                .addRoom(mainHallSegments.withWeight(3).build())
                .addRoom(GridRoomBasic.builder("castle/mainhall/hallway_entrance_one",9, 9)
                        .setSizeHeight(1, 3, 4)
                        .withWeight(2)
                        .horizontalConnections(2,0,2,1)
                        .doAllowRotation().setConnectionTag(Connection.NORTH, "main_hall")
                        .setConnectionTag(Connection.SOUTH, "main_hall")
                        .setConnectionTag(Connection.WEST, "main_hall_to_hall")
                        .setConnectionTag(Connection.EAST, "main_hall_to_hall")
                        .setOverrideEndChance(0)
                        .setGenerationPriority(10).build())
                .addRoom(GridRoomBasic.builder("castle/mainhall/hallway_entrance_two",9, 9)
                        .setSizeHeight(1, 3, 4).withWeight(1)
                        .horizontalConnections(2,1,2,1)
                        .doAllowRotation().setConnectionTag(Connection.NORTH, "main_hall")
                        .setConnectionTag(Connection.SOUTH, "main_hall")
                        .setConnectionTag(Connection.WEST, "main_hall_to_hall")
                        .setConnectionTag(Connection.EAST, "main_hall_to_hall")
                        .setOverrideEndChance(0)
                        .setGenerationPriority(10).build())

                .addRequiredRoom(2, GridRoomBasic.builder("castle/stairs/one",9, 9)
                        .setHeight(2)
                        .withWeight(10)
                        .addConnection(Connection.NORTH)
                        .addConnection(Connection.SOUTH)
                        .setAllConnectionTags("stair")
                        .setHorizontalConnectionOffset(Connection.SOUTH, 0, 1).build())
                .addRequiredRoom(2, GridRoomBasic.builder("castle/stairs/two",9, 9)
                        .setHeight(3)
                        .withWeight(10)
                        .addConnection(Connection.NORTH)
                        .addConnection(Connection.SOUTH)
                        .setAllConnectionTags("stair")
                        .setHorizontalConnectionOffset(Connection.SOUTH, 0, 2).build())

                .addRoom(hallsStraight.withWeight(10).build())
                .addRoom(hallsCorners.withWeight(3).build())

                .addTagRule(new CanConnectOneWay("main_hall_to_hall", ConnectionRule.DEFAULT_CONNECTION_TAG))
                .addTagRule(new CantConnectToSelf("main_hall_to_hall"))
                .addTagRule(new CanConnectOneWay("main_hall_to_hall", "stair"))
                .addTagRule(new CanConnectOneWay("stair", ConnectionRule.DEFAULT_CONNECTION_TAG))
                .addTagRule(new CantConnectToSelf("stair"))
                .addTagRule(new CanConnectBothWays("corner", ConnectionRule.DEFAULT_CONNECTION_TAG))
                .addTagRule(new WallFailRule("main_hall", 27,36,0,true, Blocks.ANDESITE::defaultBlockState, 27))
        ;

    }

    private static final AbstractGridRoom.Builder<?> hallsStraight = GridRoomMultiResource.builder(9, 9)
            .withResourceLocation("castle/halls/straight/default",10)
            .addConnection(Connection.NORTH)
            .addConnection(Connection.SOUTH)
            .doAllowRotation();

    private static final AbstractGridRoom.Builder<?> hallsCorners = GridRoomMultiResource.builder(9, 9)
            .withResourceLocation("castle/halls/corner/default",10)
            .addConnection(Connection.NORTH)
            .addConnection(Connection.WEST)
            .doAllowRotation()
            .setAllConnectionTags("corner");

    private static final AbstractGridRoom.Builder<?> mainHallSegments = GridRoomMultiResource.builder(9, 9)
            .withResourceLocation("castle/mainhall/default",10)
            .withResourceLocation("castle/mainhall/loose_flag",2)
            .withResourceLocation("castle/mainhall/ripped_flag",2)
            .withResourceLocation("castle/mainhall/fallen_flag",1)
            .withResourceLocation("castle/mainhall/cracked_wall",4)
            .setSizeHeight(1, 3, 4)
            .addConnection(Connection.NORTH)
            .addConnection(Connection.SOUTH)
            .doAllowRotation()
            .setAllConnectionTags("main_hall")
            .setOverrideEndChance(0)
            .setGenerationPriority(10);
}
