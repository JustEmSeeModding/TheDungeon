package net.emsee.thedungeon.block;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.custom.*;
import net.emsee.thedungeon.block.custom.fightRoom.TestFightRoomBlock;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.utils.BlockUtils;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(TheDungeon.MOD_ID);

    public static final DeferredBlock<DungeonPortal> DUNGEON_PORTAL = registerBlock("dungeon_portal",
            () -> new DungeonPortal(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK)
                    .isValidSpawn(BlockUtils::never).noOcclusion().lightLevel((light) -> 14)));

    public static final DeferredBlock<DungeonCatalystBlock> CATALYST_F = registerBlock("catalyst_f",
            () -> new DungeonCatalystBlock(DungeonRank.F,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .isValidSpawn(BlockUtils::never)));

    public static final DeferredBlock<DungeonCatalystBlock> CATALYST_E = registerBlock("catalyst_e",
            () -> new DungeonCatalystBlock(DungeonRank.E,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .isValidSpawn(BlockUtils::never)));

    public static final DeferredBlock<DungeonCatalystBlock> CATALYST_D = registerBlock("catalyst_d",
            () -> new DungeonCatalystBlock(DungeonRank.D,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .isValidSpawn(BlockUtils::never)));

    public static final DeferredBlock<DungeonCatalystBlock> CATALYST_C = registerBlock("catalyst_c",
            () -> new DungeonCatalystBlock(DungeonRank.C,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .isValidSpawn(BlockUtils::never)));

    public static final DeferredBlock<DungeonCatalystBlock> CATALYST_B = registerBlock("catalyst_b",
            () -> new DungeonCatalystBlock(DungeonRank.B,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .isValidSpawn(BlockUtils::never)));

    public static final DeferredBlock<DungeonCatalystBlock> CATALYST_A = registerBlock("catalyst_a",
            () -> new DungeonCatalystBlock(DungeonRank.A,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .isValidSpawn(BlockUtils::never)));

    public static final DeferredBlock<DungeonCatalystBlock> CATALYST_S = registerBlock("catalyst_s",
            () -> new DungeonCatalystBlock(DungeonRank.S,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .isValidSpawn(BlockUtils::never)));

    public static final DeferredBlock<DungeonCatalystBlock> CATALYST_SS = registerBlock("catalyst_ss",
            () -> new DungeonCatalystBlock(DungeonRank.SS,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .isValidSpawn(BlockUtils::never)));

    public static final DeferredBlock<DungeonBlock> CATALYST_BROKEN = registerBlock("catalyst_broken",
            () -> new DungeonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .isValidSpawn(BlockUtils::never)));

    public static final DeferredBlock<Block> PYRITE_ORE = registerBlock("pyrite_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE)));

    public static final DeferredBlock<Block> DEEPSLATE_PYRITE_ORE = registerBlock("deepslate_pyrite_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE)));

    public static final DeferredBlock<Block> INFUSED_DIRT = registerBlock("infused_dirt",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)));

    public static final DeferredBlock<GrassBlock> INFUSED_GRASS_BLOCK = registerBlock("infused_grass_block",
            () -> new GrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)));

    public static final DeferredBlock<Block> INFUSED_CLAY = registerBlock("infused_clay",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY)));

    public static final DeferredBlock<ColoredFallingBlock> INFUSED_SAND = registerBlock("infused_sand",
            () -> new ColoredFallingBlock(new ColorRGBA(14406560), BlockBehaviour.Properties.ofFullCopy(Blocks.SAND)));

    public static final DeferredBlock<ColoredFallingBlock> INFUSED_GRAVEL = registerBlock("infused_gravel",
            () -> new ColoredFallingBlock(new ColorRGBA(-8356741), BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));

    public static final DeferredBlock<Block> INFUSED_STONE = registerBlock("infused_stone",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));

    public static final DeferredBlock<Block> INFUSED_DEEPSLATE = registerBlock("infused_deepslate",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)));

    public static final DeferredBlock<Block> INFUSED_STONE_BRICKS = registerBlock("infused_stone_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));

    public static final DeferredBlock<Block> INFUSED_NETHERRACK = registerBlock("infused_netherrack",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERRACK)));

    public static final DeferredBlock<SoulSandBlock> INFUSED_SOUL_SAND = registerBlock("infused_soul_sand",
            () -> new SoulSandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SAND)));

    public static final DeferredBlock<Block> INFUSED_SOUL_SOIL = registerBlock("infused_soul_soil",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SOIL)));

    public static final DeferredBlock<Block> INFUSED_END_STONE = registerBlock("infused_end_stone",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));

    public static final DeferredBlock<Block> INFUSED_END_STONE_BRICKS = registerBlock("infused_end_stone_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE_BRICKS)));

    public static final DeferredBlock<InfusedGlassBlock> INFUSED_GLASS = registerBlock("infused_glass",
            () -> new InfusedGlassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_STAINED_GLASS)));

    public static final DeferredBlock<DungeonThreadBlock> INFUSED_THREAD = registerBlock("infused_thread",
            () -> new DungeonThreadBlock(Blocks.TRIPWIRE_HOOK, BlockBehaviour.Properties.ofFullCopy(Blocks.TRIPWIRE)));

    public static final DeferredBlock<AmethystBlock> ROSELITH_BLOCK = registerBlock("roselith_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)));

    public static final DeferredBlock<BuddingRoselithBlock> BUDDING_ROSELITH = registerBlock("budding_roselith",
            () -> new BuddingRoselithBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BUDDING_AMETHYST).noLootTable()));

    public static final DeferredBlock<AmethystClusterBlock> ROSELITH_CLUSTER = registerBlock("roselith_cluster",
            () -> new AmethystClusterBlock(7.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER)));

    public static final DeferredBlock<AmethystClusterBlock> LARGE_ROSELITH_BUD = registerBlock("large_roselith_bud",
            () -> new AmethystClusterBlock(5.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_AMETHYST_BUD)));

    public static final DeferredBlock<AmethystClusterBlock> MEDIUM_ROSELITH_BUD = registerBlock("medium_roselith_bud",
            () -> new AmethystClusterBlock(4.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD)));

    public static final DeferredBlock<AmethystClusterBlock> SMALL_ROSELITH_BUD = registerBlock("small_roselith_bud",
            () -> new AmethystClusterBlock(3.0F, 4.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD)));

    public static final DeferredBlock<AmethystBlock> GARNETORE_BLOCK = registerBlock("garnetore_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)));

    public static final DeferredBlock<BuddingGarnetoreBlock> BUDDING_GARNETORE = registerBlock("budding_garnetore",
            () -> new BuddingGarnetoreBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BUDDING_AMETHYST).noLootTable()));

    public static final DeferredBlock<AmethystClusterBlock> GARNETORE_CLUSTER = registerBlock("garnetore_cluster",
            () -> new AmethystClusterBlock(7.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER)));

    public static final DeferredBlock<AmethystClusterBlock> LARGE_GARNETORE_BUD = registerBlock("large_garnetore_bud",
            () -> new AmethystClusterBlock(5.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_AMETHYST_BUD)));

    public static final DeferredBlock<AmethystClusterBlock> MEDIUM_GARNETORE_BUD = registerBlock("medium_garnetore_bud",
            () -> new AmethystClusterBlock(4.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD)));

    public static final DeferredBlock<AmethystClusterBlock> SMALL_GARNETORE_BUD = registerBlock("small_garnetore_bud",
            () -> new AmethystClusterBlock(3.0F, 4.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD)));

    public static final DeferredBlock<AmethystBlock> VERDATITE_BLOCK = registerBlock("verdantite_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)));

    public static final DeferredBlock<BuddingVerdantiteBlock> BUDDING_VERDATITE = registerBlock("budding_verdantite",
            () -> new BuddingVerdantiteBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BUDDING_AMETHYST).noLootTable()));

    public static final DeferredBlock<AmethystClusterBlock> VERDATITE_CLUSTER = registerBlock("verdantite_cluster",
            () -> new AmethystClusterBlock(7.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER)));

    public static final DeferredBlock<AmethystClusterBlock> LARGE_VERDATITE_BUD = registerBlock("large_verdantite_bud",
            () -> new AmethystClusterBlock(5.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_AMETHYST_BUD)));

    public static final DeferredBlock<AmethystClusterBlock> MEDIUM_VERDATITE_BUD = registerBlock("medium_verdantite_bud",
            () -> new AmethystClusterBlock(4.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD)));

    public static final DeferredBlock<AmethystClusterBlock> SMALL_VERDATITE_BUD = registerBlock("small_verdantite_bud",
            () -> new AmethystClusterBlock(3.0F, 4.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD)));

    public static final DeferredBlock<AmethystBlock> LUMANITE_BLOCK = registerBlock("lumanite_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)));

    public static final DeferredBlock<BuddingLumaniteBlock> BUDDING_LUMANITE = registerBlock("budding_lumanite",
            () -> new BuddingLumaniteBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BUDDING_AMETHYST).noLootTable()));

    public static final DeferredBlock<AmethystClusterBlock> LUMANITE_CLUSTER = registerBlock("lumanite_cluster",
            () -> new AmethystClusterBlock(7.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER)));

    public static final DeferredBlock<AmethystClusterBlock> LARGE_LUMANITE_BUD = registerBlock("large_lumanite_bud",
            () -> new AmethystClusterBlock(5.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_AMETHYST_BUD)));

    public static final DeferredBlock<AmethystClusterBlock> MEDIUM_LUMANITE_BUD = registerBlock("medium_lumanite_bud",
            () -> new AmethystClusterBlock(4.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD)));

    public static final DeferredBlock<AmethystClusterBlock> SMALL_LUMANITE_BUD = registerBlock("small_lumanite_bud",
            () -> new AmethystClusterBlock(3.0F, 4.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD)));

    public static final DeferredBlock<GoblinForgeBlock> GOBLIN_FORGE = registerBlock("goblin_forge",
            () -> new GoblinForgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLAST_FURNACE)));

    public static final DeferredBlock<TestFightRoomBlock> TEST_FIGHT = registerBlock("test_fight",
            () -> new TestFightRoomBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()
                //1.21.4 + .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, name)))
        ));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
