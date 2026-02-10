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

    public static final DeferredBlock<AmethystBlock> ROSE_QUARTZ_BLOCK = registerBlock("rose_quartz_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)));

    public static final DeferredBlock<BuddingRoseQuartzBlock> BUDDING_ROSE_QUARTZ = registerBlock("budding_rose_quartz",
            () -> new BuddingRoseQuartzBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BUDDING_AMETHYST)));

    public static final DeferredBlock<AmethystClusterBlock> ROSE_QUARTZ_CLUSTER = registerBlock("rose_quartz_cluster",
            () -> new AmethystClusterBlock(7.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER)));

    public static final DeferredBlock<AmethystClusterBlock> LARGE_ROSE_QUARTZ_BUD = registerBlock("large_rose_quartz_bud",
            () -> new AmethystClusterBlock(5.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_AMETHYST_BUD)));

    public static final DeferredBlock<AmethystClusterBlock> MEDIUM_ROSE_QUARTZ_BUD = registerBlock("medium_rose_quartz_bud",
            () -> new AmethystClusterBlock(4.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD)));

    public static final DeferredBlock<AmethystClusterBlock> SMALL_ROSE_QUARTZ_BUD = registerBlock("small_rose_quartz_bud",
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
