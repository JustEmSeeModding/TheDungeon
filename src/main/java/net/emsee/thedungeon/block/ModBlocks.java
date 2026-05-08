package net.emsee.thedungeon.block;

import com.mojang.blaze3d.systems.TimerQuery;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.custom.*;
import net.emsee.thedungeon.block.custom.dungeonBlockCopies.*;
import net.emsee.thedungeon.block.custom.fightRoom.TestFightRoomBlock;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.item.interfaces.ICanTakeItemToDungeon;
import net.emsee.thedungeon.simpleRegisterGroup.SimpleBlockGroup;
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

    public static final SimpleBlockGroup.ItemBlockAndOreAndDeepslateOre PYRITE_BLOCKS = new SimpleBlockGroup.ItemBlockAndOreAndDeepslateOre("pyrite",
            () -> new DungeonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK)),
             () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE), Blocks.STONE::defaultBlockState),
             () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_GOLD_ORE),Blocks.DEEPSLATE::defaultBlockState));
    public static final DeferredBlock<SimpleMinableBlock> INFUSED_DIRT = registerBlock("infused_dirt",
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT), Blocks.DIRT::defaultBlockState));

    public static final DeferredBlock<InfusedGrassBlock> INFUSED_GRASS_BLOCK = registerBlock("infused_grass_block",
            () -> new InfusedGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)));

    public static final DeferredBlock<SimpleMinableBlock> INFUSED_CLAY = registerBlock("infused_clay",
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY), Blocks.CLAY::defaultBlockState));

    public static final DeferredBlock<InfusedColoredFallingBlock> INFUSED_SAND = registerBlock("infused_sand",
            () -> new InfusedColoredFallingBlock(new ColorRGBA(14406560), BlockBehaviour.Properties.ofFullCopy(Blocks.SAND), Blocks.SAND));

    public static final DeferredBlock<InfusedColoredFallingBlock> INFUSED_GRAVEL = registerBlock("infused_gravel",
            () -> new InfusedColoredFallingBlock(new ColorRGBA(-8356741), BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL), Blocks.GRAVEL));

    public static final DeferredBlock<SimpleMinableBlock> INFUSED_STONE = registerBlock("infused_stone",
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE), Blocks.STONE::defaultBlockState));

    public static final DeferredBlock<SimpleMinableBlock> INFUSED_DEEPSLATE = registerBlock("infused_deepslate",
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE), Blocks.DEEPSLATE::defaultBlockState));

    public static final DeferredBlock<SimpleMinableBlock> INFUSED_STONE_BRICKS = registerBlock("infused_stone_bricks",
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS), Blocks.STONE_BRICKS::defaultBlockState));

    public static final DeferredBlock<SimpleMinableBlock> INFUSED_NETHERRACK = registerBlock("infused_netherrack",
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERRACK), Blocks.NETHERRACK::defaultBlockState));

    public static final DeferredBlock<InfusedSoulSandBlock> INFUSED_SOUL_SAND = registerBlock("infused_soul_sand",
            () -> new InfusedSoulSandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SAND)));

    public static final DeferredBlock<SimpleMinableBlock> INFUSED_SOUL_SOIL = registerBlock("infused_soul_soil",
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SOIL), Blocks.SOUL_SOIL::defaultBlockState));

    public static final DeferredBlock<SimpleMinableBlock> INFUSED_END_STONE = registerBlock("infused_end_stone",
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE), Blocks.END_STONE::defaultBlockState));

    public static final DeferredBlock<SimpleMinableBlock> INFUSED_END_STONE_BRICKS = registerBlock("infused_end_stone_bricks",
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE_BRICKS), Blocks.END_STONE_BRICKS::defaultBlockState));

    public static final DeferredBlock<InfusedGlassBlock> INFUSED_GLASS = registerBlock("infused_glass",
            () -> new InfusedGlassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_STAINED_GLASS)));

    public static final DeferredBlock<DungeonThreadBlock> INFUSED_THREAD = registerBlock("infused_thread",
            () -> new DungeonThreadBlock(Blocks.TRIPWIRE_HOOK, BlockBehaviour.Properties.ofFullCopy(Blocks.TRIPWIRE)));


    public static final SimpleBlockGroup.CrystalBlockAndClusterGroup<DungeonAmethystBlock, BuddingRoselithBlock> ROSELITH_CRYSTAL_GROUP = new SimpleBlockGroup.CrystalBlockAndClusterGroup<>(
            "roselith",
            () -> new DungeonAmethystBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)),
            () -> new BuddingRoselithBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BUDDING_AMETHYST).noLootTable()));

    public static final SimpleBlockGroup.CrystalBlockAndClusterGroup<DungeonAmethystBlock, BuddingGarnetoreBlock> GARNETORE_CRYSTAL_GROUP = new SimpleBlockGroup.CrystalBlockAndClusterGroup<>(
            "garnetore",
            () -> new DungeonAmethystBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)),
            () -> new BuddingGarnetoreBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BUDDING_AMETHYST).noLootTable()));

    public static final SimpleBlockGroup.CrystalBlockAndClusterGroup<DungeonAmethystBlock, BuddingVerdantiteBlock> VERDANTITE_CRYSTAL_GROUP = new SimpleBlockGroup.CrystalBlockAndClusterGroup<>(
            "verdantite",
            () -> new DungeonAmethystBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)),
            () -> new BuddingVerdantiteBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BUDDING_AMETHYST).noLootTable()));

    public static final SimpleBlockGroup.CrystalBlockAndClusterGroup<DungeonAmethystBlock, BuddingLumaniteBlock> LUMANITE_CRYSTAL_GROUP = new SimpleBlockGroup.CrystalBlockAndClusterGroup<>(
            "lumanite",
            () -> new DungeonAmethystBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)),
            () -> new BuddingLumaniteBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BUDDING_AMETHYST).noLootTable()));

    public static final DeferredBlock<GoblinForgeBlock> GOBLIN_FORGE = registerBlock("goblin_forge",
            () -> new GoblinForgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLAST_FURNACE)));

    public static final DeferredBlock<TestFightRoomBlock> TEST_FIGHT = registerBlock("test_fight",
            () -> new TestFightRoomBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));

    public static final SimpleBlockGroup.ItemBlockAndRawAndOreAndDeepslateOre GILDREAN = new SimpleBlockGroup.ItemBlockAndRawAndOreAndDeepslateOre("gildrean",
            () -> new DungeonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK)),
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_GOLD_BLOCK), Blocks.BEDROCK::defaultBlockState),
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK), Blocks.STONE::defaultBlockState),
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_GOLD_ORE), Blocks.DEEPSLATE::defaultBlockState));

    public static final DeferredBlock<SimpleMinableBlock> INGILDERD_BLACKSTONE = registerBlock("ingilderd_blackstone",
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GILDED_BLACKSTONE), Blocks.BLACKSTONE::defaultBlockState));

    public static final SimpleBlockGroup.ItemBlockAndRawAndOre INFERNAL_TIN = new SimpleBlockGroup.ItemBlockAndRawAndOre(
            "infernal_tin",
            () -> new DungeonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)),
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK), Blocks.MAGMA_BLOCK::defaultBlockState),
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE), Blocks.BLACKSTONE::defaultBlockState)
    );
    public static final SimpleBlockGroup.ItemBlockAndRawAndOre ARCTIC_IRON = new SimpleBlockGroup.ItemBlockAndRawAndOre(
            "arctic_iron",
            () -> new DungeonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)),
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK), Blocks.BLUE_ICE::defaultBlockState),
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_ICE), Blocks.BLUE_ICE::defaultBlockState)
    );
    public static final SimpleBlockGroup.ItemBlockAndRawAndOreAndDeepslateOre LAVINTINE = new SimpleBlockGroup.ItemBlockAndRawAndOreAndDeepslateOre(
            "lavintine",
            () -> new DungeonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)),
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK), Blocks.BEDROCK::defaultBlockState),
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE), Blocks.STONE::defaultBlockState),
            () -> new SimpleMinableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE), Blocks.DEEPSLATE::defaultBlockState)
    );

    public static final DeferredBlock<DungeonBlock> KOBALT_BLOCK = registerBlock("kobalt_block",
            () -> new DungeonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)));

    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().rarity(ICanTakeItemToDungeon.DUNGEON_ITEM_RARITY)
                //1.21.4 + .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, name)))
        ));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
