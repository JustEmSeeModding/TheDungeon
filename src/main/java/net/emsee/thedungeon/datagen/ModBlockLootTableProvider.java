package net.emsee.thedungeon.datagen;


import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.simpleRegisterGroup.SimpleBlockGroup;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
        dropSelf(ModBlocks.CATALYST_F.get());
        dropSelf(ModBlocks.CATALYST_E.get());
        dropSelf(ModBlocks.CATALYST_D.get());
        dropSelf(ModBlocks.CATALYST_C.get());
        dropSelf(ModBlocks.CATALYST_B.get());
        dropSelf(ModBlocks.CATALYST_A.get());
        dropSelf(ModBlocks.CATALYST_S.get());
        dropSelf(ModBlocks.CATALYST_SS.get());
        dropOther(ModBlocks.CATALYST_BROKEN.get(), ModItems.SHATTERED_CATALYST_CORE);

        blockAndOreGroupDrops(ModBlocks.PYRITE_BLOCKS,ModItems.PYRITE, 2f, 3f);
        blockAndOreGroupDrops(ModBlocks.GILDREAN,ModItems.GILDREAN.RAW, 1f, 2f);
        oreDrops(ModBlocks.INGILDERD_BLACKSTONE.get(), ModItems.GILDREAN.NUGGET, 3f, 8f);
        blockAndOreGroupDrops(ModBlocks.INFERNAL_TIN,ModItems.INFERNAL_TIN.RAW, 2f, 3f);
        blockAndOreGroupDrops(ModBlocks.ARCTIC_IRON,ModItems.ARCTIC_IRON.RAW, 2f, 5f);
        blockAndOreGroupDrops(ModBlocks.LAVINTINE,ModItems.LAVINTINE.RAW, 1f, 2f);
        dropSelf(ModBlocks.KOBALT_BLOCK.get());


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

        crystalBlockAndClusterDrops(ModBlocks.ROSELITH_CRYSTAL_GROUP , ModItems.ROSELITH_CRYSTAL);
        crystalBlockAndClusterDrops(ModBlocks.GARNETORE_CRYSTAL_GROUP , ModItems.GARNETORE_PIECE);
        crystalBlockAndClusterDrops(ModBlocks.VERDANTITE_CRYSTAL_GROUP , ModItems.VERDANTITE_CHUNK);
        crystalBlockAndClusterDrops(ModBlocks.LUMANITE_CRYSTAL_GROUP , ModItems.LUMANITE_FRAGMENT);

        dropSelf(ModBlocks.GOBLIN_FORGE.get());

        dropSelf(ModBlocks.TEST_FIGHT.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(Holder::value)
                .filter(block -> block != net.minecraft.world.level.block.Blocks.AIR)
                ::iterator;
    }

    private void simpleItemDropWithSilk(Block block, ItemLike defaultDrop) {
        add(block, createSingleItemTableWithSilkTouch(block, defaultDrop));
    }

    private void simpleItemDropSelfOnlyWithSilk(Block block) {
        add(block, createSilkTouchOnlyTable(block));

    }

    private void oreDrops(Block block, ItemLike item, float min, float max) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        add(block, createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE))))));
    }

    private void oreDrops(SimpleBlockGroup.WithOres group, ItemLike item, float min, float max) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        group.getAllOres().forEach(ore ->
                add(ore.get(), createSilkTouchDispatchTable(ore.get(), this.applyExplosionDecay(ore.get(), LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))))));
    }

    private <G extends SimpleBlockGroup & SimpleBlockGroup.WithOres> void blockAndOreGroupDrops(G group, ItemLike item, float min, float max) {
        if (group instanceof SimpleBlockGroup.WithPackedItemBlock withBase)dropSelf(withBase.getPackedItemBlock().get());
        if (group instanceof SimpleBlockGroup.WithRawBlock withRaw)dropSelf(withRaw.getRawBlock().get());
        oreDrops(group, item, min, max);
    }
    private void crystalBlockAndClusterDrops(SimpleBlockGroup.CrystalBlockAndClusterGroup<?,?> group, ItemLike item) {
        dropSelf(group.BLOCK.get());
        crystalClusterDrops(group.CLUSTERS, item);
    }

    private void crystalClusterDrops(SimpleBlockGroup.CrystalClusterGroup group, ItemLike item) {
        oreDrops(group.CLUSTER.get(), item, 1f, 2f);
        simpleItemDropSelfOnlyWithSilk(group.LARGE_BUD.get());
        simpleItemDropSelfOnlyWithSilk(group.MEDIUM_BUD.get());
        simpleItemDropSelfOnlyWithSilk(group.SMALL_BUD.get());
    }
}
