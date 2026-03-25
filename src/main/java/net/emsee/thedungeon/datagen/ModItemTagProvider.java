package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.item.interfaces.ICanTakeItemToDungeon;
import net.emsee.thedungeon.utils.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosTags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class ModItemTagProvider extends ItemTagsProvider {

    ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, TheDungeon.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        addICanTakeItemToDungeonToTag();
        tag(ItemTags.DIRT)
                .add(ModBlocks.INFUSED_DIRT.get().asItem())
                .add(ModBlocks.INFUSED_GRASS_BLOCK.get().asItem());
        tag(ItemTags.SAND).add(ModBlocks.INFUSED_SAND.get().asItem());
        tag(ItemTags.STONE_BRICKS).add(ModBlocks.INFUSED_STONE_BRICKS.get().asItem());
        tag(ItemTags.SOUL_FIRE_BASE_BLOCKS)
                .add(ModBlocks.INFUSED_SOUL_SAND.get().asItem())
                .add(ModBlocks.INFUSED_SOUL_SOIL.get().asItem());

        tag(CuriosTags.createItemTag("trinket"))
                .add(ModItems.TEST_BELT.get())
                .add(ModItems.SOUL_BOUND_TOTEM.get());

        tag(CuriosTags.createItemTag("effigy"))
                .add(ModItems.HEAVY_EFFIGY.get());

        tag(ItemTags.TRIMMABLE_ARMOR)
                .addAll(ModItems.SCHOLAR_ARMOR_SET.getAllAsResourceKey());
    }



    private void addICanTakeItemToDungeonToTag() {
        List<ResourceKey<Item>> items = new ArrayList<>();
        List<ResourceKey<Blocks>> blocks = new ArrayList<>();
        getAllKnownModItemKeys().forEach(item -> {
            if (item instanceof ICanTakeItemToDungeon)
                tag(ModTags.Items.CAN_CARRY_TO_DUNGEON).add(item);
        });
        getAllKnownModBlockKeys().forEach(block -> {
            if (block instanceof ICanTakeItemToDungeon)
                    tag(ModTags.Items.CAN_CARRY_TO_DUNGEON).add(block.asItem());
        });

    }

    private Stream<? extends Item> getAllKnownModItemKeys() {
        return ModItems.ITEMS.getEntries().stream()
                .map(DeferredHolder::value)
                .filter(item -> item != Items.AIR);
    }

    private Stream<? extends Block> getAllKnownModBlockKeys() {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(DeferredHolder::value)
                .filter(block -> block != Blocks.AIR);
    }
}
