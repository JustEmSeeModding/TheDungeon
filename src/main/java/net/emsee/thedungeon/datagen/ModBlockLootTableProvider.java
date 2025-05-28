package net.emsee.thedungeon.datagen;


import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public final class ModBlockLootTableProvider extends BlockLootSubProvider {
    ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        TheDungeon.LOGGER.info("Generating loot tables for blocks...");
        dropSelf(ModBlocks.DUNGEON_PORTAL_F.get());
        dropSelf(ModBlocks.DUNGEON_PORTAL_E.get());
        dropSelf(ModBlocks.DUNGEON_PORTAL_D.get());
        dropSelf(ModBlocks.DUNGEON_PORTAL_C.get());
        dropSelf(ModBlocks.DUNGEON_PORTAL_B.get());
        dropSelf(ModBlocks.DUNGEON_PORTAL_A.get());
        dropSelf(ModBlocks.DUNGEON_PORTAL_S.get());
        dropSelf(ModBlocks.DUNGEON_PORTAL_SS.get());
        dropSelf(ModBlocks.DUNGEON_PORTAL_EXIT.get());
        dropOther(ModBlocks.DUNGEON_PORTAL_UNSTABLE.get(), ModItems.SHATTERED_PORTAL_CORE);

        simpleItemDropWithSilk(ModBlocks.INFUSED_DIRT.get(), ModItems.DUNGEON_ESSENCE_SHARD);
        simpleItemDropWithSilk(ModBlocks.INFUSED_SAND.get(), ModItems.DUNGEON_ESSENCE_SHARD);
        simpleItemDropWithSilk(ModBlocks.INFUSED_GRAVEL.get(), ModItems.DUNGEON_ESSENCE_SHARD);

        simpleItemDropWithSilk(ModBlocks.INFUSED_CLAY.get(), ModItems.DUNGEON_ESSENCE_SHARD);
        simpleItemDropWithSilk(ModBlocks.INFUSED_STONE.get(), ModItems.DUNGEON_ESSENCE_SHARD);
        simpleItemDropWithSilk(ModBlocks.INFUSED_DEEPSLATE.get(), ModItems.DUNGEON_ESSENCE_SHARD);
        simpleItemDropWithSilk(ModBlocks.INFUSED_STONE_BRICKS.get(), ModItems.DUNGEON_ESSENCE_SHARD);

        simpleItemDropWithSilk(ModBlocks.INFUSED_NETHERRACK.get(), ModItems.DUNGEON_ESSENCE_SHARD);
        simpleItemDropWithSilk(ModBlocks.INFUSED_SOUL_SAND.get(), ModItems.DUNGEON_ESSENCE_SHARD);
        simpleItemDropWithSilk(ModBlocks.INFUSED_SOUL_SOIL.get(), ModItems.DUNGEON_ESSENCE_SHARD);

        simpleItemDropWithSilk(ModBlocks.INFUSED_END_STONE.get(), ModItems.DUNGEON_ESSENCE_SHARD);
        simpleItemDropWithSilk(ModBlocks.INFUSED_END_STONE_BRICKS.get(), ModItems.DUNGEON_ESSENCE_SHARD);

        simpleItemDropWithSilk(ModBlocks.INFUSED_GLASS.get(), ModItems.DUNGEON_ESSENCE_SHARD);
        dropSelf(ModBlocks.INFUSED_THREAD.get());
        //dropOther(ModBlocks.INFUSED_COBWEB.get(), ModItems.INFUSED_THREAD);
        //add([BLOCK], block -> createOreDrop([BLOCK]), [ITEM])
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }

    private void simpleItemDropWithSilk(Block block, ItemLike item) {
        add(block, createSingleItemTableWithSilkTouch(block, item));
    }
}
