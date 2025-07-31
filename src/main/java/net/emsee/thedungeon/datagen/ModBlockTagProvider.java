package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public final class ModBlockTagProvider extends BlockTagsProvider {
    ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, TheDungeon.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.PYRITE_ORE.get())
                .add(ModBlocks.DEEPSLATE_PYRITE_ORE.get())
        ;

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.DUNGEON_PORTAL_F.get())
                .add(ModBlocks.DUNGEON_PORTAL_E.get())
                .add(ModBlocks.DUNGEON_PORTAL_D.get())
                .add(ModBlocks.DUNGEON_PORTAL_C.get())
                .add(ModBlocks.DUNGEON_PORTAL_B.get())
                .add(ModBlocks.DUNGEON_PORTAL_A.get())
                .add(ModBlocks.DUNGEON_PORTAL_S.get())
                .add(ModBlocks.DUNGEON_PORTAL_SS.get())
                .add(ModBlocks.DUNGEON_PORTAL_UNSTABLE.get())
        ;

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.DUNGEON_PORTAL_F.get())
                .add(ModBlocks.DUNGEON_PORTAL_E.get())
                .add(ModBlocks.DUNGEON_PORTAL_D.get())
                .add(ModBlocks.DUNGEON_PORTAL_C.get())
                .add(ModBlocks.DUNGEON_PORTAL_B.get())
                .add(ModBlocks.DUNGEON_PORTAL_A.get())
                .add(ModBlocks.DUNGEON_PORTAL_S.get())
                .add(ModBlocks.DUNGEON_PORTAL_SS.get())
                .add(ModBlocks.DUNGEON_PORTAL_UNSTABLE.get())
                .add(ModBlocks.PYRITE_ORE.get())
                .add(ModBlocks.DEEPSLATE_PYRITE_ORE.get())
                .add(ModBlocks.INFUSED_STONE.get())
                .add(ModBlocks.INFUSED_DEEPSLATE.get())
                .add(ModBlocks.INFUSED_STONE_BRICKS.get())
                .add(ModBlocks.INFUSED_NETHERRACK.get())
                .add(ModBlocks.INFUSED_END_STONE.get())
                .add(ModBlocks.INFUSED_END_STONE_BRICKS.get())
                .add(ModBlocks.INFUSED_GLASS.get())
        ;

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.INFUSED_CLAY.get())
                .add(ModBlocks.INFUSED_DIRT.get())
                .add(ModBlocks.INFUSED_GRASS_BLOCK.get())
                .add(ModBlocks.INFUSED_SAND.get())
                .add(ModBlocks.INFUSED_GRAVEL.get())
                .add(ModBlocks.INFUSED_SOUL_SAND.get())
                .add(ModBlocks.INFUSED_SOUL_SOIL.get())
        ;

        tag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(ModBlocks.INFUSED_GRAVEL.get())
        ;

        tag(BlockTags.ENDERMAN_HOLDABLE)
                .add(ModBlocks.INFUSED_CLAY.get())
                .add(ModBlocks.INFUSED_SAND.get())
                .add(ModBlocks.INFUSED_GRAVEL.get())
        ;

        tag(BlockTags.SCULK_REPLACEABLE)
                .add(ModBlocks.INFUSED_CLAY.get())
                .add(ModBlocks.INFUSED_SAND.get())
                .add(ModBlocks.INFUSED_GRAVEL.get())
                .add(ModBlocks.INFUSED_NETHERRACK.get())
                .add(ModBlocks.INFUSED_SOUL_SAND.get())
                .add(ModBlocks.INFUSED_SOUL_SOIL.get())
                .add(ModBlocks.INFUSED_END_STONE.get())
        ;

        tag(BlockTags.AZALEA_GROWS_ON)
                .add(ModBlocks.INFUSED_GRAVEL.get())
        ;

        tag(BlockTags.GOATS_SPAWNABLE_ON)
                .add(ModBlocks.INFUSED_GRAVEL.get())
                .add(ModBlocks.INFUSED_STONE.get())
                .add(ModBlocks.INFUSED_GRASS_BLOCK.get())
        ;

        tag(BlockTags.DIRT)
                .add(ModBlocks.INFUSED_DIRT.get())
                .add(ModBlocks.INFUSED_GRASS_BLOCK.get())
        ;

        tag(BlockTags.AXOLOTLS_SPAWNABLE_ON).add(ModBlocks.INFUSED_CLAY.get());
        tag(BlockTags.SMALL_DRIPLEAF_PLACEABLE).add(ModBlocks.INFUSED_CLAY.get());
        tag(BlockTags.LUSH_GROUND_REPLACEABLE).add(ModBlocks.INFUSED_CLAY.get());

        tag(BlockTags.SAND).add(ModBlocks.INFUSED_SAND.get());
        tag(BlockTags.RABBITS_SPAWNABLE_ON)
                .add(ModBlocks.INFUSED_SAND.get())
                .add(ModBlocks.INFUSED_GRASS_BLOCK.get())
        ;

        tag(BlockTags.SNIFFER_DIGGABLE_BLOCK)
                .add(ModBlocks.INFUSED_DIRT.get())
                .add(ModBlocks.INFUSED_GRASS_BLOCK.get())
        ;

        tag(BlockTags.BASE_STONE_OVERWORLD)
                .add(ModBlocks.INFUSED_STONE.get())
                .add(ModBlocks.INFUSED_DEEPSLATE.get())
        ;

        tag(BlockTags.FROGS_SPAWNABLE_ON).add(ModBlocks.INFUSED_GRASS_BLOCK.get());
        tag(BlockTags.FOXES_SPAWNABLE_ON).add(ModBlocks.INFUSED_GRASS_BLOCK.get());
        tag(BlockTags.ANIMALS_SPAWNABLE_ON).add(ModBlocks.INFUSED_GRASS_BLOCK.get());
        tag(BlockTags.VALID_SPAWN).add(ModBlocks.INFUSED_GRASS_BLOCK.get());
        tag(BlockTags.WOLVES_SPAWNABLE_ON).add(ModBlocks.INFUSED_GRASS_BLOCK.get());
        tag(BlockTags.ARMADILLO_SPAWNABLE_ON).add(ModBlocks.INFUSED_GRASS_BLOCK.get());
        tag(BlockTags.PARROTS_SPAWNABLE_ON).add(ModBlocks.INFUSED_GRASS_BLOCK.get());

        tag(BlockTags.SNAPS_GOAT_HORN).add(ModBlocks.INFUSED_STONE.get());

        tag(BlockTags.STONE_BRICKS).add(ModBlocks.INFUSED_STONE_BRICKS.get());

        tag(BlockTags.BASE_STONE_NETHER).add(ModBlocks.INFUSED_NETHERRACK.get());
        tag(BlockTags.INFINIBURN_END).add(ModBlocks.INFUSED_NETHERRACK.get());
        tag(BlockTags.INFINIBURN_OVERWORLD).add(ModBlocks.INFUSED_NETHERRACK.get());
        tag(BlockTags.INFINIBURN_NETHER).add(ModBlocks.INFUSED_NETHERRACK.get());

        tag(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON).add(ModBlocks.INFUSED_SOUL_SAND.get());

        tag(BlockTags.SOUL_FIRE_BASE_BLOCKS)
                .add(ModBlocks.INFUSED_SOUL_SAND.get())
                .add(ModBlocks.INFUSED_SOUL_SOIL.get())
        ;

        tag(BlockTags.SOUL_SPEED_BLOCKS)
                .add(ModBlocks.INFUSED_SOUL_SAND.get())
                .add(ModBlocks.INFUSED_SOUL_SOIL.get())
        ;

        tag(BlockTags.WITHER_SUMMON_BASE_BLOCKS)
                .add(ModBlocks.INFUSED_SOUL_SAND.get())
                .add(ModBlocks.INFUSED_SOUL_SOIL.get())
        ;

        tag(BlockTags.WALL_POST_OVERRIDE).add(ModBlocks.INFUSED_THREAD.get());

        tag(BlockTags.DRAGON_IMMUNE)
                .add(ModBlocks.DUNGEON_PORTAL_F.get())
                .add(ModBlocks.DUNGEON_PORTAL_UNSTABLE.get())
                .add(ModBlocks.INFUSED_END_STONE.get())
        ;


        //tag(BlockTags.FALL_DAMAGE_RESETTING).add(ModBlocks.INFUSED_COBWEB.get());
    }
}
