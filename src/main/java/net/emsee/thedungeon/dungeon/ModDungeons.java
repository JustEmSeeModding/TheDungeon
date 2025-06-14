package net.emsee.thedungeon.dungeon;


import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.types.Dungeon;
import net.emsee.thedungeon.dungeon.types.GridDungeon;
import net.emsee.thedungeon.dungeon.roomCollections.collections.*;
import net.emsee.thedungeon.dungeon.util.DungeonRank;

import java.util.HashMap;
import java.util.Map;

public class ModDungeons {
    public static final Map<String, Dungeon> DUNGEONS = new HashMap<>();


    public static final Dungeon TEST = register(new GridDungeon(
            "dungeon.the_dungeon.test",
            DungeonRank.F,
            0,
            3,
            3,
            new TestGridRoomCollection())
            .setDepth(10)
            .setRoomEndChance(0));


    public static final Dungeon THE_LIBRARY = register(new GridDungeon(
            "dungeon.the_dungeon.library",
            DungeonRank.A,
            0, // disabled for now
            13,
            9,
            new LibraryGridRoomCollection())
            .setDepth(20)
            .setRoomEndChance(.03f)
            .setMaxFloorHeight(3)
            .setRoomPickMethod(GridDungeon.RoomGenerationPickMethod.RANDOM));

    public static final Dungeon CASTLE = register(new GridDungeon(
            "dungeon.the_dungeon.castle",
            DungeonRank.D,
            1,
            9,
            9,
            new CastleGridRoomCollection())
            .setDepth(25)
            .setRoomEndChance(.02f)
            .setMaxFloorHeight(7)
            .setRoomPickMethod(GridDungeon.RoomGenerationPickMethod.RANDOM));

    public static final Dungeon GOBLIN_CAVES = register(new GridDungeon(
            "dungeon.the_dungeon.goblin_caves",
            DungeonRank.F,
            1,
            11,
            11,
            new GoblinCavesGridRoomCollection())
            .setDepth(25)
            .setRoomEndChance(.01f)
            .setMaxFloorHeight(12)
            .setRoomPickMethod(GridDungeon.RoomGenerationPickMethod.RANDOM));

    protected static Dungeon register(Dungeon dungeon) {
        DebugLog.logInfo(DebugLog.DebugLevel.INSTANCE_SETUP,"Registering Dungeon :{}", dungeon);
        DUNGEONS.put(dungeon.getResourceName(), dungeon);
        GlobalDungeonManager.dungeons.put(dungeon, dungeon.getWeight());
        return dungeon;
    }

    public static Dungeon GetByResourceName(String resourceName) {
        return DUNGEONS.get(resourceName);
    }
}
