package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.block.custom.interfaces.IDungeonPickaxeMinable;
import net.emsee.thedungeon.utils.ModTags;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class ModBlockTagProvider extends BlockTagsProvider {
    public static final List<ModTags.Blocks.MinableTag> miningLevels = new ArrayList<>();

    ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, TheDungeon.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        setupSimpleMiningBlockLevelTags(Util.make(new HashMap<>(),map -> {
            map.put(ModBlocks.PYRITE_BLOCKS.getAllAsResourceKey(), 1);

            map.put(ModBlocks.INGILDERD_BLACKSTONE.get(), 1);
            map.put(ModBlocks.GILDREAN.getAllAsResourceKey(), 1);

            map.put(ModBlocks.DUNGEON_PORTAL.get(), 2);
            map.put(ModBlocks.CATALYST_F.get(), 2);
            map.put(ModBlocks.CATALYST_E.get(), 2);
            map.put(ModBlocks.CATALYST_D.get(), 2);
            map.put(ModBlocks.CATALYST_C.get(), 2);
            map.put(ModBlocks.CATALYST_B.get(), 2);
            map.put(ModBlocks.CATALYST_A.get(), 2);
            map.put(ModBlocks.CATALYST_S.get(), 2);
            map.put(ModBlocks.CATALYST_SS.get(), 2);
            map.put(ModBlocks.CATALYST_BROKEN.get(), 2);
            map.put(ModBlocks.ARCTIC_IRON.getAllAsResourceKey(), 2);

            map.put(ModBlocks.LAVINTINE.getAllAsResourceKey(),3);

            map.put(ModBlocks.INFERNAL_TIN.getAllAsResourceKey(), 4);

            map.put(ModBlocks.KOBALT_BLOCK.get(), 5);
        }));

        setupCustomMiningLevelTags();


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

    private void setupCustomMiningLevelTags() {
        //tag(BlockTags.INCORRECT_FOR_WOODEN_TOOL).addTag(ModTags.Blocks.NEEDS_NETHERITE_TOOL);
        //tag(BlockTags.INCORRECT_FOR_GOLD_TOOL).addTag(ModTags.Blocks.NEEDS_NETHERITE_TOOL);
        //tag(BlockTags.INCORRECT_FOR_STONE_TOOL).addTag(ModTags.Blocks.NEEDS_NETHERITE_TOOL);
        //tag(BlockTags.INCORRECT_FOR_IRON_TOOL).addTag(ModTags.Blocks.NEEDS_NETHERITE_TOOL);
        //tag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL).addTag(ModTags.Blocks.NEEDS_NETHERITE_TOOL);

        miningLevels.forEach((tag) -> {
            if (tag.needs != null) {
                tag(tag.needs);
            }
            tag(tag.incorrect);

            if (tag.LEVEL < 1)
                tag(tag.incorrect).addTag(BlockTags.NEEDS_STONE_TOOL);
            if (tag.LEVEL < 2)
                tag(tag.incorrect).addTag(BlockTags.NEEDS_IRON_TOOL);
            if (tag.LEVEL < 3)
                tag(tag.incorrect).addTag(BlockTags.NEEDS_DIAMOND_TOOL);

            if (tag.needs != null) {
                if (tag.LEVEL > 0) {
                    tag(BlockTags.INCORRECT_FOR_WOODEN_TOOL).addTag(tag.needs);
                    tag(BlockTags.INCORRECT_FOR_GOLD_TOOL).addTag(tag.needs);
                }
                if (tag.LEVEL > 1)
                    tag(BlockTags.INCORRECT_FOR_STONE_TOOL).addTag(tag.needs);
                if (tag.LEVEL > 2)
                    tag(BlockTags.INCORRECT_FOR_IRON_TOOL).addTag(tag.needs);
                if (tag.LEVEL > 3) {
                    tag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL).addTag(tag.needs);
                }
                if (tag.LEVEL > 4) {
                    tag(BlockTags.INCORRECT_FOR_NETHERITE_TOOL).addTag(tag.needs);
                }
            }

            miningLevels.forEach((otherTag) -> {
                if (otherTag.LEVEL < tag.LEVEL && tag.needs != null) {
                    tag(otherTag.incorrect).addTag(tag.needs);
                }
            });
        });
    }

    private void setupSimpleMiningBlockLevelTags(Map<Object, Integer> map) {
        map.forEach((minable, level) -> {
            if (level == 0) {
                return;
            }
            if (level == 1)
                addMinableToTag(BlockTags.NEEDS_STONE_TOOL, minable);
            if (level == 2)
                addMinableToTag(BlockTags.NEEDS_IRON_TOOL,minable);
            if (level == 3)
                addMinableToTag(BlockTags.NEEDS_DIAMOND_TOOL,minable);
            //if (level == 4)
            //    addMinableToTag(ModTags.Blocks.NEEDS_NETHERITE_TOOL,minable);

            miningLevels.forEach((tag) -> {
                if (tag.LEVEL == level) {
                    addMinableToTag(tag.needs,minable);
                }
            });
        });
    }

    @SuppressWarnings("unchecked")
    private void addMinableToTag(TagKey<Block> tag, Object minable) {
        switch (minable) {
            case null -> throw new NullPointerException("minable cannot be null");
            case Block block -> tag(tag).add(block);
            case ResourceKey<?> block -> tag(tag).add((ResourceKey<Block>) block);
            case TagKey<?> otherTag -> tag(tag).addTag((TagKey<Block>) otherTag);
            case List<?> keyList -> tag(tag).addAll((List<ResourceKey<Block>>) keyList);
            default -> throw new IllegalArgumentException("minable of unhandled type: " + minable.getClass().getName());
        }
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
