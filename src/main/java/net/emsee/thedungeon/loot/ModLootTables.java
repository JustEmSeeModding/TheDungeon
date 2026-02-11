package net.emsee.thedungeon.loot;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModLootTables {

    public static final ResourceKey<LootTable> HOB_GOBLIN_FIGHTER =
            ResourceKey.create(Registries.LOOT_TABLE,
                    TheDungeon.defaultResourceLocation("entities/hob_goblin/fighter"));

    public static final ResourceKey<LootTable> HOB_GOBLIN_FORGER =
            ResourceKey.create(Registries.LOOT_TABLE,
                    TheDungeon.defaultResourceLocation("entities/hob_goblin/forger"));

    public static final ResourceKey<LootTable> HOB_GOBLIN_SCAVENGER =
            ResourceKey.create(Registries.LOOT_TABLE,
                    TheDungeon.defaultResourceLocation("entities/hob_goblin/scavenger"));

    public static final ResourceKey<LootTable> HOB_GOBLIN_MINER =
            ResourceKey.create(Registries.LOOT_TABLE,
                    TheDungeon.defaultResourceLocation("entities/hob_goblin/miner"));

    public static final ResourceKey<LootTable> CRYSTAL_GOLEM_ROSELITH =
            ResourceKey.create(Registries.LOOT_TABLE,
                    TheDungeon.defaultResourceLocation("entities/crystal_golem/rose_quartz"));

    public static final ResourceKey<LootTable> CRYSTAL_GOLEM_GARNETORE =
            ResourceKey.create(Registries.LOOT_TABLE,
                    TheDungeon.defaultResourceLocation("entities/hob_goblin/garnetore"));

    public static final ResourceKey<LootTable> CRYSTAL_GOLEM_VERDANTITE =
            ResourceKey.create(Registries.LOOT_TABLE,
                    TheDungeon.defaultResourceLocation("entities/hob_goblin/verdantite"));

    public static final ResourceKey<LootTable> CRYSTAL_GOLEM_LUMANITE =
            ResourceKey.create(Registries.LOOT_TABLE,
                    TheDungeon.defaultResourceLocation("entities/hob_goblin/lumanite"));

    public static final ResourceKey<LootTable> CRYSTAL_GOLEM_AMETHYST =
            ResourceKey.create(Registries.LOOT_TABLE,
                    TheDungeon.defaultResourceLocation("entities/hob_goblin/amethyst"));

    public static final ResourceKey<LootTable> CRYSTAL_GOLEM_DIAMOND =
            ResourceKey.create(Registries.LOOT_TABLE,
                    TheDungeon.defaultResourceLocation("entities/hob_goblin/diamond"));

    public static final ResourceKey<LootTable> CRYSTAL_GOLEM_EMERALD =
            ResourceKey.create(Registries.LOOT_TABLE,
                    TheDungeon.defaultResourceLocation("entities/hob_goblin/emerald"));
}
