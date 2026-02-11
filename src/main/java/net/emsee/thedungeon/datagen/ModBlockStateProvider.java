package net.emsee.thedungeon.datagen;


import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public final class ModBlockStateProvider extends BlockStateProvider {

    ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TheDungeon.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        block(ModBlocks.DUNGEON_PORTAL);

        blockWithItem(ModBlocks.PYRITE_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_PYRITE_ORE);

        blockWithItem(ModBlocks.INFUSED_DIRT);
        blockWithItem(ModBlocks.INFUSED_CLAY);
        blockWithItem(ModBlocks.INFUSED_SAND);
        blockWithItem(ModBlocks.INFUSED_GRAVEL);

        blockWithItem(ModBlocks.INFUSED_STONE);
        blockWithItem(ModBlocks.INFUSED_DEEPSLATE);
        blockWithItem(ModBlocks.INFUSED_STONE_BRICKS);

        blockWithItem(ModBlocks.INFUSED_NETHERRACK);
        blockWithItem(ModBlocks.INFUSED_SOUL_SAND);
        blockWithItem(ModBlocks.INFUSED_SOUL_SOIL);

        blockWithItem(ModBlocks.INFUSED_END_STONE);
        blockWithItem(ModBlocks.INFUSED_END_STONE_BRICKS);

        semiTransparentBlockWithItem(ModBlocks.INFUSED_GLASS);

        blockWithItem(ModBlocks.ROSELITH_BLOCK);
        blockWithItem(ModBlocks.BUDDING_ROSELITH);
        blockWithItem(ModBlocks.GARNETORE_BLOCK);
        blockWithItem(ModBlocks.BUDDING_GARNETORE);
        blockWithItem(ModBlocks.VERDATITE_BLOCK);
        blockWithItem(ModBlocks.BUDDING_VERDATITE);
        blockWithItem(ModBlocks.LUMANITE_BLOCK);
        blockWithItem(ModBlocks.BUDDING_LUMANITE);

        clusterBlock(ModBlocks.ROSELITH_CLUSTER, "roselith_cluster");
        clusterBlock(ModBlocks.LARGE_ROSELITH_BUD, "large_roselith_bud");
        clusterBlock(ModBlocks.MEDIUM_ROSELITH_BUD, "medium_roselith_bud");
        clusterBlock(ModBlocks.SMALL_ROSELITH_BUD, "small_roselith_bud");

        clusterBlock(ModBlocks.GARNETORE_CLUSTER, "garnetore_cluster");
        clusterBlock(ModBlocks.LARGE_GARNETORE_BUD, "large_garnetore_bud");
        clusterBlock(ModBlocks.MEDIUM_GARNETORE_BUD, "medium_garnetore_bud");
        clusterBlock(ModBlocks.SMALL_GARNETORE_BUD, "small_garnetore_bud");

        clusterBlock(ModBlocks.VERDATITE_CLUSTER, "verdantite_cluster");
        clusterBlock(ModBlocks.LARGE_VERDATITE_BUD, "large_verdantite_bud");
        clusterBlock(ModBlocks.MEDIUM_VERDATITE_BUD, "medium_verdantite_bud");
        clusterBlock(ModBlocks.SMALL_VERDATITE_BUD, "small_verdantite_bud");

        clusterBlock(ModBlocks.LUMANITE_CLUSTER, "lumanite_cluster");
        clusterBlock(ModBlocks.LARGE_LUMANITE_BUD, "large_lumanite_bud");
        clusterBlock(ModBlocks.MEDIUM_LUMANITE_BUD, "medium_lumanite_bud");
        clusterBlock(ModBlocks.SMALL_LUMANITE_BUD, "small_lumanite_bud");
    }

    private void crossBlock(DeferredBlock<?> deferredBlock, String modelName, String textureName) {
        simpleBlock(deferredBlock.get(), models().cross(modelName, ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "block/" + textureName)).renderType("cutout"));
    }

    private void semiTransparentBlockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), models().cubeAll(deferredBlock.getId().getPath(), ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "block/" + deferredBlock.getId().getPath())).renderType("translucent"));
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void block(DeferredBlock<?> deferredBlock) {
        simpleBlock(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock, ResourceLocation texture) {
        simpleBlockWithItem(deferredBlock.get(), this.models().cubeAll(BuiltInRegistries.BLOCK.getKey(deferredBlock.get()).getPath(), texture));
    }

    private void blockWithItemCopyFromOtherBlock(DeferredBlock<?> deferredBlock, DeferredBlock<?> copyDeferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(copyDeferredBlock.get()));
    }

    /**
     * Generates blockstate and model for amethyst cluster blocks.
     * These blocks have a facing property that determines their orientation.
     *
     * @param deferredBlock the cluster block to generate states for
     * @param textureName the name of the texture to use for the cluster model
     */
    private void clusterBlock(DeferredBlock<?> deferredBlock, String textureName) {
        String blockName = deferredBlock.getId().getPath();
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "block/" + textureName);

        // Create the blockstate with facing variants using the same rotation logic as amethyst clusters
        getVariantBuilder(deferredBlock.get())
                .forAllStatesExcept(state -> {
                    Direction facing = state.getValue(AmethystClusterBlock.FACING);
                    int rotX = switch (facing) {
                        case UP -> 0;
                        case DOWN -> 180;
                        case NORTH, EAST, SOUTH, WEST -> 90;
                    };
                    int rotY = switch (facing) {
                        case UP , DOWN, NORTH -> 0;
                        case SOUTH -> 180;
                        case WEST -> 270;
                        case EAST -> 90;
                    };

                    return ConfiguredModel
                            .builder()
                            .modelFile(this.models().cross(blockName, texture).renderType("cutout"))
                            .rotationX(rotX)
                            .rotationY(rotY)
                            .build();
                }, AmethystClusterBlock.WATERLOGGED);

        // Create item model
        itemModels().getBuilder(blockName)
                .parent(this.models().getExistingFile(this.mcLoc("item/generated")))
                .texture("layer0", texture);
    }
}
