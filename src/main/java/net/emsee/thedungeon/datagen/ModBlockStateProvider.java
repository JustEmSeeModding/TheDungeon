package net.emsee.thedungeon.datagen;


import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public final class ModBlockStateProvider extends BlockStateProvider {

    ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TheDungeon.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.DUNGEON_PORTAL_F, TheDungeon.defaultResourceLocation("block/dungeon_portal"));
        blockWithItem(ModBlocks.DUNGEON_PORTAL_E, TheDungeon.defaultResourceLocation("block/dungeon_portal"));
        blockWithItem(ModBlocks.DUNGEON_PORTAL_D, TheDungeon.defaultResourceLocation("block/dungeon_portal"));
        blockWithItem(ModBlocks.DUNGEON_PORTAL_C, TheDungeon.defaultResourceLocation("block/dungeon_portal"));
        blockWithItem(ModBlocks.DUNGEON_PORTAL_B, TheDungeon.defaultResourceLocation("block/dungeon_portal"));
        blockWithItem(ModBlocks.DUNGEON_PORTAL_A, TheDungeon.defaultResourceLocation("block/dungeon_portal"));
        blockWithItem(ModBlocks.DUNGEON_PORTAL_S, TheDungeon.defaultResourceLocation("block/dungeon_portal"));
        blockWithItem(ModBlocks.DUNGEON_PORTAL_SS, TheDungeon.defaultResourceLocation("block/dungeon_portal"));
        blockWithItem(ModBlocks.DUNGEON_PORTAL_EXIT, TheDungeon.defaultResourceLocation("block/dungeon_portal"));
        blockWithItem(ModBlocks.DUNGEON_PORTAL_UNSTABLE);


        blockWithItem(ModBlocks.PYRITE_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_PYRITE_ORE);

        blockWithItem(ModBlocks.INFUSED_DIRT);
        //blockWithItem(ModBlocks.INFUSED_GRASS_BLOCK);
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

    }

    private void crossBlockTest(DeferredBlock<?> deferredBlock, String modelName, String textureName) {
        simpleBlockWithItem(deferredBlock.get(), models().cross(modelName, ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "block/" + textureName)).renderType("cutout"));
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

    private void blockWithItem(DeferredBlock<?> deferredBlock, ResourceLocation texture){
        simpleBlockWithItem(deferredBlock.get(), this.models().cubeAll( BuiltInRegistries.BLOCK.getKey(deferredBlock.get()).getPath(), texture));
    }

    private void blockWithItemCopyFromOtherBlock(DeferredBlock<?> deferredBlock, DeferredBlock<?> copyDeferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(copyDeferredBlock.get()));
    }
}
