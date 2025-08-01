package net.emsee.thedungeon.dungeon.registry.roomCollections;

import net.emsee.thedungeon.dungeon.src.connectionRules.connection.CanConnectBothWays;
import net.emsee.thedungeon.dungeon.src.connectionRules.connection.CanConnectOneWay;
import net.emsee.thedungeon.dungeon.src.connectionRules.connection.CantConnectToSelf;
import net.emsee.thedungeon.dungeon.src.connectionRules.ConnectionRule;
import net.emsee.thedungeon.dungeon.src.connectionRules.fail.WallFailRule;
import net.emsee.thedungeon.dungeon.src.room.AbstractGridRoom;
import net.emsee.thedungeon.dungeon.src.room.GridRoomBasic;
import net.emsee.thedungeon.dungeon.src.room.GridRoomMultiResource;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.mobSpawnRules.rules.SpawnAt;
import net.emsee.thedungeon.dungeon.src.GridRoomCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;

public final class CastleGridRoomCollection extends GridRoomCollection {
    public CastleGridRoomCollection() {
        super(9, 9);
        this.setFallback(new GridRoomBasic("castle/fallback",9, 9))
                .setStartingRoom(new GridRoomBasic("castle/mainhall/center",9, 9)
                        .setSizeHeight(3, 3, 4)
                        .horizontalConnections().setAllConnectionTags("main_hall")
                        .setOverrideEndChance(0).setGenerationPriority(10)
                        .addMobSpawnRule(new SpawnAt<>(() -> EntityType.ZOGLIN, new BlockPos(0, 3, 0))))

                .addRequiredRoom(4, 6, new GridRoomBasic("castle/portals/default",9, 9)
                        .withWeight(4)
                        .addConnection(Connection.NORTH))

                .addRoom(mainHallSegments.withWeight(3))
                .addRoom(new GridRoomBasic("castle/mainhall/hallway_entrance_one",9, 9)
                        .setSizeHeight(1, 3, 4)
                        .withWeight(2)
                        .horizontalConnections(2,0,2,1)
                        .doAllowRotation().setConnectionTag(Connection.NORTH, "main_hall")
                        .setConnectionTag(Connection.SOUTH, "main_hall")
                        .setConnectionTag(Connection.WEST, "main_hall_to_hall")
                        .setConnectionTag(Connection.EAST, "main_hall_to_hall")
                        .setOverrideEndChance(0)
                        .setGenerationPriority(10))
                .addRoom(new GridRoomBasic("castle/mainhall/hallway_entrance_two",9, 9)
                        .setSizeHeight(1, 3, 4).withWeight(1)
                        .horizontalConnections(2,1,2,1)
                        .doAllowRotation().setConnectionTag(Connection.NORTH, "main_hall")
                        .setConnectionTag(Connection.SOUTH, "main_hall")
                        .setConnectionTag(Connection.WEST, "main_hall_to_hall")
                        .setConnectionTag(Connection.EAST, "main_hall_to_hall")
                        .setOverrideEndChance(0)
                        .setGenerationPriority(10))

                .addRequiredRoom(2, new GridRoomBasic("castle/stairs/one",9, 9)
                        .setHeight(2)
                        .withWeight(10)
                        .addConnection(Connection.NORTH)
                        .addConnection(Connection.SOUTH)
                        .setAllConnectionTags("stair")
                        .setHorizontalConnectionOffset(Connection.SOUTH, 0, 1))
                .addRequiredRoom(2, new GridRoomBasic("castle/stairs/two",9, 9)
                        .setHeight(3)
                        .withWeight(10)
                        .addConnection(Connection.NORTH)
                        .addConnection(Connection.SOUTH)
                        .setAllConnectionTags("stair")
                        .setHorizontalConnectionOffset(Connection.SOUTH, 0, 2))

                .addRoom(hallsStraight.withWeight(10))
                .addRoom(hallsCorners.withWeight(3))

                .addTagRule(new CanConnectOneWay("main_hall_to_hall", ConnectionRule.DEFAULT_CONNECTION_TAG))
                .addTagRule(new CantConnectToSelf("main_hall_to_hall"))
                .addTagRule(new CanConnectOneWay("main_hall_to_hall", "stair"))
                .addTagRule(new CanConnectOneWay("stair", ConnectionRule.DEFAULT_CONNECTION_TAG))
                .addTagRule(new CantConnectToSelf("stair"))
                .addTagRule(new CanConnectBothWays("corner", ConnectionRule.DEFAULT_CONNECTION_TAG))
                .addTagRule(new WallFailRule("main_hall", 27,36,0,true, Blocks.ANDESITE::defaultBlockState, 27))
        ;

    }

    private static final AbstractGridRoom hallsStraight = new GridRoomMultiResource(9, 9)
            .withResourceLocation("castle/halls/straight/default",10)
            .addConnection(Connection.NORTH)
            .addConnection(Connection.SOUTH)
            .doAllowRotation();

    private static final AbstractGridRoom hallsCorners = new GridRoomMultiResource(9, 9)
            .withResourceLocation("castle/halls/corner/default",10)
            .addConnection(Connection.NORTH)
            .addConnection(Connection.WEST)
            .doAllowRotation()
            .setAllConnectionTags("corner");

    private static final AbstractGridRoom mainHallSegments = new GridRoomMultiResource(9, 9)
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


    @Override
    public GridRoomCollection getCopy() {
        return new CastleGridRoomCollection();
    }
}
