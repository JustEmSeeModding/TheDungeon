package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public final class ModItemTagProvider extends ItemTagsProvider {

    ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, TheDungeon.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.DIRT).add(ModBlocks.INFUSED_DIRT.get().asItem());
        tag(ItemTags.SAND).add(ModBlocks.INFUSED_SAND.get().asItem());
        tag(ItemTags.STONE_BRICKS).add(ModBlocks.INFUSED_STONE_BRICKS.get().asItem());
        tag(ItemTags.SOUL_FIRE_BASE_BLOCKS)
                .add(ModBlocks.INFUSED_SOUL_SAND.get().asItem())
                .add(ModBlocks.INFUSED_SOUL_SOIL.get().asItem())
        ;
    }
}
