package net.emsee.thedungeon.datagen;


import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.block.custom.GoblinForgeBlock;
import net.emsee.thedungeon.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public final class ModBlockLootTableProvider extends BlockLootSubProvider {
    ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.CATALIST_F.get());
        dropSelf(ModBlocks.CATALIST_E.get());
        dropSelf(ModBlocks.CATALIST_D.get());
        dropSelf(ModBlocks.CATALIST_C.get());
        dropSelf(ModBlocks.CATALIST_B.get());
        dropSelf(ModBlocks.CATALIST_A.get());
        dropSelf(ModBlocks.CATALIST_S.get());
        dropSelf(ModBlocks.CATALIST_SS.get());
        dropOther(ModBlocks.CATALIST_BROKEN.get(), ModItems.SHATTERED_CATALIST_CORE);

        oreDrops(ModBlocks.PYRITE_ORE.get(), ModItems.PYRITE, 2f, 3f);
        oreDrops(ModBlocks.DEEPSLATE_PYRITE_ORE.get(), ModItems.PYRITE, 2f, 3f);

        simpleItemDropWithSilk(ModBlocks.INFUSED_DIRT.get(), ModItems.DUNGEON_ESSENCE_SHARD);
        simpleItemDropWithSilk(ModBlocks.INFUSED_GRASS_BLOCK.get(), ModItems.DUNGEON_ESSENCE_SHARD);
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
        dropSelf(ModBlocks.ROSE_QUARTZ_BLOCK.get());
        dropSelf(ModBlocks.BUDDING_ROSE_QUARTZ.get());
        dropSelf(ModBlocks.ROSE_QUARTZ_CLUSTER.get());
        simpleItemDropSelfWithSilk(ModBlocks.LARGE_ROSE_QUARTZ_BUD.get());
        simpleItemDropSelfWithSilk(ModBlocks.MEDIUM_ROSE_QUARTZ_BUD.get());
        simpleItemDropSelfWithSilk(ModBlocks.SMALL_ROSE_QUARTZ_BUD.get());

        dropSelf(ModBlocks.GOBLIN_FORGE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(Holder::value)
                .filter(block -> block != net.minecraft.world.level.block.Blocks.AIR)
                ::iterator;
    }

    private void simpleItemDropWithSilk(Block block, ItemLike item) {
        add(block, createSingleItemTableWithSilkTouch(block, item));
    }

    private void simpleItemDropSelfWithSilk(Block block) {
        simpleItemDropWithSilk(block, block);
    }

    private void oreDrops(Block block, ItemLike item, float min, float max) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        add(block, createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE))))));
    }
}
