package net.emsee.thedungeon.dungeon.registry;


import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.registry.roomCollections.CastleGridRoomCollection;
import net.emsee.thedungeon.dungeon.registry.roomCollections.GoblinCavesGridRoomCollection;
import net.emsee.thedungeon.dungeon.registry.roomCollections.LibraryGridRoomCollection;
import net.emsee.thedungeon.dungeon.registry.roomCollections.TestGridRoomCollection;
import net.emsee.thedungeon.dungeon.src.types.Dungeon;
import net.emsee.thedungeon.dungeon.src.types.grid.GridDungeon;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class ModDungeons {
    public static final ResourceKey<Registry<Dungeon<?,?>>> DUNGEON_REGISTRY_KEY = ResourceKey.createRegistryKey(TheDungeon.defaultResourceLocation("dungeons"));

    public static final Registry<Dungeon<?,?>> DUNGEON_REGISTRY =
            new RegistryBuilder<>(DUNGEON_REGISTRY_KEY)
                    .sync(true)
                    .defaultKey(TheDungeon.defaultResourceLocation("empty"))
                    .create();

    public static final DeferredRegister<Dungeon<?,?>> DUNGEONS =
            DeferredRegister.create(DUNGEON_REGISTRY,TheDungeon.MOD_ID);

    public static final DeferredHolder<Dungeon<?,?>, GridDungeon> TEST = register("test", ()-> new GridDungeon(
            "dungeon.the_dungeon.test",
            DungeonRank.F,
            0,
            3,
            3,
            new TestGridRoomCollection())
            .setDepth(10)
            .setRoomEndChance(0)
    );


    public static final DeferredHolder<Dungeon<?,?>, GridDungeon> THE_LIBRARY = register("library", ()->new GridDungeon(
            "dungeon.the_dungeon.library",
            DungeonRank.SS,
            0 /* disabled for now as this will not be in first release*/,
            13,
            9,
            new LibraryGridRoomCollection())
            .setDepth(20)
            .setRoomEndChance(.03f)
            .setMaxFloorHeight(3)
            .setRoomPickMethod(GridDungeon.RoomGenerationPickMethod.RANDOM));

    public static final DeferredHolder<Dungeon<?,?>, GridDungeon> CASTLE = register("castle", ()->new GridDungeon(
            "dungeon.the_dungeon.castle",
            DungeonRank.E,
            1,
            9,
            9,
            new CastleGridRoomCollection())
            .setDepth(25)
            .setRoomEndChance(.02f)
            .setMaxFloorHeight(7)
            .setRoomPickMethod(GridDungeon.RoomGenerationPickMethod.RANDOM));

    public static final DeferredHolder<Dungeon<?,?>, GridDungeon> GOBLIN_CAVES = register("goblin_caves", ()->new GridDungeon(
            "dungeon.the_dungeon.goblin_caves",
            DungeonRank.F,
            1,
            11,
            11,
            new GoblinCavesGridRoomCollection())
            .setDepth(20)
            .setRoomEndChance(.005f)
            .setMaxFloorHeight(5)
            .setRoomPickMethod(GridDungeon.RoomGenerationPickMethod.RANDOM)
            .setStepIntervalBetweenForcesRequirementTries(30));

    protected static <T extends Dungeon<?,?>> DeferredHolder<Dungeon<?,?>, T> register(String name, Supplier<T> dungeon) {
        DebugLog.logInfo(DebugLog.DebugType.INSTANCE_SETUP, "Registering Dungeon :{}", name);
        return DUNGEONS.register(name, dungeon);
    }

    public static void register(IEventBus eventBus) {
        DUNGEONS.register(eventBus);
        ModCleanupDungeons.register(eventBus);
    }

    public static Dungeon<?,?> getByName(String name) {
        return DUNGEON_REGISTRY.get(TheDungeon.defaultResourceLocation(name));
    }
}
