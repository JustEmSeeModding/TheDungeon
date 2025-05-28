package net.emsee.thedungeon.block;



import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.custom.portal.*;
import net.emsee.thedungeon.block.custom.DungeonTripWireBlock;
import net.emsee.thedungeon.block.custom.InfusedGlassBlock;
import net.emsee.thedungeon.block.custom.UnstableDungeonPortal;
import net.emsee.thedungeon.dungeon.dungeon.Dungeon;
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

    public static final DeferredBlock<DungeonPortal> DUNGEON_PORTAL_F = registerBlock("dungeon_portal_f",
            () -> new DungeonPortalF(BlockBehaviour.Properties.of()
                    .isValidSpawn(BlockUtils::NeverValidSpawn).destroyTime(40).explosionResistance(100).requiresCorrectToolForDrops().lightLevel((light)-> 14)));

    public static final DeferredBlock<DungeonPortal> DUNGEON_PORTAL_E = registerBlock("dungeon_portal_e",
            () -> new DungeonPortalE(BlockBehaviour.Properties.of()
                    .isValidSpawn(BlockUtils::NeverValidSpawn).destroyTime(40).explosionResistance(100).requiresCorrectToolForDrops().lightLevel((light)-> 14)));

    public static final DeferredBlock<DungeonPortal> DUNGEON_PORTAL_D = registerBlock("dungeon_portal_d",
            () -> new DungeonPortalD(BlockBehaviour.Properties.of()
                    .isValidSpawn(BlockUtils::NeverValidSpawn).destroyTime(40).explosionResistance(100).requiresCorrectToolForDrops().lightLevel((light)-> 14)));

    public static final DeferredBlock<DungeonPortal> DUNGEON_PORTAL_C = registerBlock("dungeon_portal_c",
            () -> new DungeonPortalC(BlockBehaviour.Properties.of()
                    .isValidSpawn(BlockUtils::NeverValidSpawn).destroyTime(40).explosionResistance(100).requiresCorrectToolForDrops().lightLevel((light)-> 14)));

    public static final DeferredBlock<DungeonPortal> DUNGEON_PORTAL_B = registerBlock("dungeon_portal_b",
            () -> new DungeonPortalB(BlockBehaviour.Properties.of()
                    .isValidSpawn(BlockUtils::NeverValidSpawn).destroyTime(40).explosionResistance(100).requiresCorrectToolForDrops().lightLevel((light)-> 14)));

    public static final DeferredBlock<DungeonPortal> DUNGEON_PORTAL_A = registerBlock("dungeon_portal_a",
            () -> new DungeonPortalA(BlockBehaviour.Properties.of()
                    .isValidSpawn(BlockUtils::NeverValidSpawn).destroyTime(40).explosionResistance(100).requiresCorrectToolForDrops().lightLevel((light)-> 14)));

    public static final DeferredBlock<DungeonPortal> DUNGEON_PORTAL_S = registerBlock("dungeon_portal_s",
            () -> new DungeonPortalS(BlockBehaviour.Properties.of()
                    .isValidSpawn(BlockUtils::NeverValidSpawn).destroyTime(40).explosionResistance(100).requiresCorrectToolForDrops().lightLevel((light)-> 14)));

    public static final DeferredBlock<DungeonPortal> DUNGEON_PORTAL_SS = registerBlock("dungeon_portal_ss",
            () -> new DungeonPortalSS(BlockBehaviour.Properties.of()
                    .isValidSpawn(BlockUtils::NeverValidSpawn).destroyTime(40).explosionResistance(100).requiresCorrectToolForDrops().lightLevel((light)-> 14)));

    public static final DeferredBlock<DungeonPortal> DUNGEON_PORTAL_EXIT = registerBlock("dungeon_portal_exit",
            () -> new DungeonPortalExit(BlockBehaviour.Properties.of()
                    .isValidSpawn(BlockUtils::NeverValidSpawn).destroyTime(40).explosionResistance(100).requiresCorrectToolForDrops().lightLevel((light)-> 14)));



    public static final DeferredBlock<UnstableDungeonPortal> DUNGEON_PORTAL_UNSTABLE = registerBlock("dungeon_portal_unstable",
            () -> new UnstableDungeonPortal(BlockBehaviour.Properties.of()
                    .isValidSpawn(BlockUtils::NeverValidSpawn).destroyTime(40).explosionResistance(100).requiresCorrectToolForDrops().lightLevel((light)-> 14)));

    public static final DeferredBlock<Block> INFUSED_DIRT = registerBlock("infused_dirt",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)));

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

    public static final DeferredBlock<DungeonTripWireBlock> INFUSED_THREAD = registerBlock("infused_thread",
            () -> new DungeonTripWireBlock(Blocks.TRIPWIRE_HOOK, BlockBehaviour.Properties.ofFullCopy(Blocks.TRIPWIRE)));


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
