package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.block.custom.interfaces.IDungeonPickaxeMinable;
import net.emsee.thedungeon.utils.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class ModBlockTagProvider extends BlockTagsProvider {
    ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, TheDungeon.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        addIDungeonPickaxeMinableToTag();
        tag(ModTags.Blocks.DUNGEON_TOOL_MINABLE)
                .add(Blocks.VINE)
                .add(Blocks.HANGING_ROOTS)
                .add(Blocks.GLOW_LICHEN)
                .add(Blocks.SMALL_AMETHYST_BUD)
                .add(Blocks.MEDIUM_AMETHYST_BUD)
                .add(Blocks.LARGE_AMETHYST_BUD)
                .add(Blocks.SNOW)
                .add(Blocks.MOSS_CARPET)
                .add(Blocks.OAK_LEAVES)
                .add(Blocks.SPRUCE_LEAVES)
                .add(Blocks.BIRCH_LEAVES)
                .add(Blocks.JUNGLE_LEAVES)
                .add(Blocks.ACACIA_LEAVES)
                .add(Blocks.DARK_OAK_LEAVES)
                .add(Blocks.MANGROVE_LEAVES)
                .add(Blocks.CHERRY_LEAVES)
                .add(Blocks.AZALEA_LEAVES)
                .add(Blocks.FLOWERING_AZALEA_LEAVES)
                .add(Blocks.AMETHYST_CLUSTER);

        tag(BlockTags.NEEDS_STONE_TOOL)
                .addAll(ModBlocks.PYRITE_BLOCKS.getAllAsResourceKey())
                .addAll(ModBlocks.ARCTIC_IRON.getAllAsResourceKey())
        ;


        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.DUNGEON_PORTAL.get())
                .add(ModBlocks.CATALYST_F.get())
                .add(ModBlocks.CATALYST_E.get())
                .add(ModBlocks.CATALYST_D.get())
                .add(ModBlocks.CATALYST_C.get())
                .add(ModBlocks.CATALYST_B.get())
                .add(ModBlocks.CATALYST_A.get())
                .add(ModBlocks.CATALYST_S.get())
                .add(ModBlocks.CATALYST_SS.get())
                .add(ModBlocks.CATALYST_BROKEN.get())
                .add(ModBlocks.INGILDERD_BLACKSTONE.get())
                .addAll(ModBlocks.GILDREAN.getAllAsResourceKey())
                .addAll(ModBlocks.INFERNAL_TIN.getAllAsResourceKey())
        ;

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .addAll(ModBlocks.LAVINTINE.getAllAsResourceKey())
        ;

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.DUNGEON_PORTAL.get())
                .add(ModBlocks.INFUSED_STONE.get())
                .add(ModBlocks.INFUSED_DEEPSLATE.get())
                .add(ModBlocks.INFUSED_STONE_BRICKS.get())
                .add(ModBlocks.INFUSED_NETHERRACK.get())
                .add(ModBlocks.INFUSED_END_STONE.get())
                .add(ModBlocks.INFUSED_END_STONE_BRICKS.get())
                .add(ModBlocks.INFUSED_GLASS.get())

                .add(ModBlocks.CATALYST_F.get())
                .add(ModBlocks.CATALYST_E.get())
                .add(ModBlocks.CATALYST_D.get())
                .add(ModBlocks.CATALYST_C.get())
                .add(ModBlocks.CATALYST_B.get())
                .add(ModBlocks.CATALYST_A.get())
                .add(ModBlocks.CATALYST_S.get())
                .add(ModBlocks.CATALYST_SS.get())
                .add(ModBlocks.CATALYST_BROKEN.get())

                .add(ModBlocks.INGILDERD_BLACKSTONE.get())

                .addAll(ModBlocks.PYRITE_BLOCKS.getAllAsResourceKey())
                .addAll(ModBlocks.GILDREAN.getAllAsResourceKey())
                .addAll(ModBlocks.INFERNAL_TIN.getAllAsResourceKey())
                .addAll(ModBlocks.ARCTIC_IRON.getAllAsResourceKey())
                .addAll(ModBlocks.LAVINTINE.getAllAsResourceKey())

                .addAll(ModBlocks.ROSELITH_CRYSTAL_GROUP.getAllAsResourceKey())
                .addAll(ModBlocks.GARNETORE_CRYSTAL_GROUP.getAllAsResourceKey())
                .addAll(ModBlocks.VERDANTITE_CRYSTAL_GROUP.getAllAsResourceKey())
                .addAll(ModBlocks.LUMANITE_CRYSTAL_GROUP.getAllAsResourceKey())

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
                .add(ModBlocks.DUNGEON_PORTAL.get())
                .add(ModBlocks.INFUSED_END_STONE.get())
        ;


        //tag(BlockTags.FALL_DAMAGE_RESETTING).add(ModBlocks.INFUSED_COBWEB.get());
    }

    private void addIDungeonPickaxeMinableToTag() {
        List<ResourceKey<Block>> blocks = new ArrayList<>();
        getAllKnownModBlockKeys().forEach(block -> {
            if (block instanceof IDungeonPickaxeMinable)
                tag(ModTags.Blocks.DUNGEON_TOOL_MINABLE).add(block);
        });
    }

    private Stream<? extends Block> getAllKnownModBlockKeys() {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(DeferredHolder::value)
                .filter(block -> block != Blocks.AIR);
    }
}
