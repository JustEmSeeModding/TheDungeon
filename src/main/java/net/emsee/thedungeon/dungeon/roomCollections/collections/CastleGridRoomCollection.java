package net.emsee.thedungeon.dungeon.roomCollections.collections;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.connectionRules.connection.CanConnectBothWays;
import net.emsee.thedungeon.dungeon.connectionRules.connection.CanConnectOneWay;
import net.emsee.thedungeon.dungeon.connectionRules.connection.CantConnectToSelf;
import net.emsee.thedungeon.dungeon.connectionRules.ConnectionRule;
import net.emsee.thedungeon.dungeon.connectionRules.fail.WallFailRule;
import net.emsee.thedungeon.dungeon.util.Connection;
import net.emsee.thedungeon.dungeon.room.GridRoom;
import net.emsee.thedungeon.dungeon.room.GridRoomGroup;
import net.emsee.thedungeon.dungeon.mobSpawnRules.rules.SpawnAt;
import net.emsee.thedungeon.dungeon.roomCollections.GridRoomCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;

public final class CastleGridRoomCollection extends GridRoomCollection {
    public CastleGridRoomCollection() {
        super(9, 9);
        this.setFallback(new GridRoom(9, 9).withResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "castle/fallback")))
                .setStartingRoom(new GridRoom(9, 9)
                        .setSizeHeight(3, 3, 4)
                        .withResourceLocation("castle/mainhall/center")
                        .horizontalConnections().setAllConnectionTags("main_hall")
                        .setOverrideEndChance(0).setGenerationPriority(10)
                        .addMobSpawnRule(new SpawnAt<>(() -> EntityType.ZOGLIN, new BlockPos(0, 3, 0))))

                .addRequiredRoom(4, 6, new GridRoom(9, 9)
                        .withWeight(4)
                        .withResourceLocation("castle/portals/default")
                        .addConnection(Connection.NORTH))

                .addRoom(mainHallSegments.withWeight(3))
                .addRoom(new GridRoom(9, 9)
                        .setSizeHeight(1, 3, 4)
                        .withWeight(2)
                        .withResourceLocation("castle/mainhall/hallway_entrance_one")
                        .horizontalConnections(2,0,2,1)
                        .doAllowRotation().setConnectionTag(Connection.NORTH, "main_hall")
                        .setConnectionTag(Connection.SOUTH, "main_hall")
                        .setConnectionTag(Connection.WEST, "main_hall_to_hall")
                        .setConnectionTag(Connection.EAST, "main_hall_to_hall")
                        .setOverrideEndChance(0)
                        .setGenerationPriority(10))
                .addRoom(new GridRoom(9, 9)
                        .setSizeHeight(1, 3, 4).withWeight(1)
                        .withResourceLocation("castle/mainhall/hallway_entrance_two")
                        .horizontalConnections(2,1,2,1)
                        .doAllowRotation().setConnectionTag(Connection.NORTH, "main_hall")
                        .setConnectionTag(Connection.SOUTH, "main_hall")
                        .setConnectionTag(Connection.WEST, "main_hall_to_hall")
                        .setConnectionTag(Connection.EAST, "main_hall_to_hall")
                        .setOverrideEndChance(0)
                        .setGenerationPriority(10))

                .addRequiredRoom(2, new GridRoom(9, 9)
                        .setHeight(2)
                        .withWeight(10)
                        .withResourceLocation("castle/stairs/one")
                        .addConnection(Connection.NORTH)
                        .addConnection(Connection.SOUTH)
                        .setAllConnectionTags("stair")
                        .setHorizontalConnectionOffset(Connection.SOUTH, 0, 1))
                .addRequiredRoom(2, new GridRoom(9, 9)
                        .setHeight(3)
                        .withWeight(10)
                        .withResourceLocation("castle/stairs/two")
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
                .addTagRule(new WallFailRule("main_hall", 27,36,0,true, () -> Blocks.ANDESITE, 27))
        ;

    }

    private static final GridRoom hallsStraight = ((GridRoomGroup) new GridRoomGroup(9, 9).addConnection(Connection.NORTH).addConnection(Connection.SOUTH).doAllowRotation())
            .addRoom(new GridRoom(9, 9)
                    .withWeight(10).withResourceLocation("castle/halls/straight/default")
                    .doAllowRotation()
                    .addConnection(Connection.NORTH)
                    .addConnection(Connection.SOUTH));

    private static final GridRoom hallsCorners = ((GridRoomGroup) new GridRoomGroup(9, 9).addConnection(Connection.NORTH).addConnection(Connection.WEST).doAllowRotation().setAllConnectionTags("corner"))
            .addRoom(new GridRoom(9, 9)
                    .withWeight(10).withResourceLocation("castle/halls/corner/default")
                    .addConnection(Connection.NORTH)
                    .addConnection(Connection.WEST)
                    .doAllowRotation().setAllConnectionTags("corner"));

    private static final GridRoom mainHallSegments = ((GridRoomGroup) new GridRoomGroup(9, 9).setSizeHeight(1, 3, 4).addConnection(Connection.NORTH).addConnection(Connection.SOUTH).doAllowRotation().setAllConnectionTags("main_hall").setGenerationPriority(10).setOverrideEndChance(0))
            .addRoom(new GridRoom(9, 9)
                    .setSizeHeight(1, 3, 4)
                    .withWeight(10)
                    .withResourceLocation("castle/mainhall/default")
                    .addConnection(Connection.NORTH)
                    .addConnection(Connection.SOUTH)
                    .doAllowRotation()
                    .setAllConnectionTags("main_hall")
                    .setOverrideEndChance(0)
                    .setGenerationPriority(10))
            .addRoom(new GridRoom(9, 9)
                    .setSizeHeight(1, 3, 4)
                    .withWeight(2)
                    .withResourceLocation("castle/mainhall/loose_flag")
                    .addConnection(Connection.NORTH)
                    .addConnection(Connection.SOUTH)
                    .doAllowRotation()
                    .setAllConnectionTags("main_hall")
                    .setOverrideEndChance(0)
                    .setGenerationPriority(10))
            .addRoom(new GridRoom(9, 9)
                    .setSizeHeight(1, 3, 4)
                    .withWeight(2)
                    .withResourceLocation("castle/mainhall/ripped_flag")
                    .addConnection(Connection.NORTH)
                    .addConnection(Connection.SOUTH)
                    .doAllowRotation().setAllConnectionTags("main_hall")
                    .setOverrideEndChance(0)
                    .setGenerationPriority(10))
            .addRoom(new GridRoom(9, 9)
                    .setSizeHeight(1, 3, 4)
                    .withWeight(2)
                    .withResourceLocation("castle/mainhall/fallen_flag")
                    .addConnection(Connection.NORTH)
                    .addConnection(Connection.SOUTH)
                    .doAllowRotation().setAllConnectionTags("main_hall")
                    .setOverrideEndChance(0)
                    .setGenerationPriority(10))
            .addRoom(new GridRoom(9, 9)
                    .setSizeHeight(1, 3, 4)
                    .withWeight(2)
                    .withResourceLocation("castle/mainhall/cracked_wall")
                    .addConnection(Connection.NORTH)
                    .addConnection(Connection.SOUTH)
                    .doAllowRotation().setAllConnectionTags("main_hall")
                    .setOverrideEndChance(0)
                    .setGenerationPriority(10));


    @Override
    public GridRoomCollection getCopy() {
        return new CastleGridRoomCollection();
    }
}
