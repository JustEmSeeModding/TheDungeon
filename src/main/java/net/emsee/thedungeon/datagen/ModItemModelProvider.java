package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.item.ModSpawnEggs;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public final class ModItemModelProvider extends ItemModelProvider {
    ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TheDungeon.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.PORTAL_CORE.asItem());
        basicItem(ModItems.SHATTERED_PORTAL_CORE.asItem());
        basicItem(ModItems.DUNGEON_ESSENCE_SHARD.asItem());
        basicItem(ModItems.INFUSED_ALLOY_INGOT.asItem());
        basicItem(ModItems.DUNGEON_CLOCK.asItem());
        basicBlockItemOtherTexturePath(ModBlocks.INFUSED_THREAD, "thedungeon", "item/infused_thread");
        basicItemOtherTexturePath(ModItems.DUNGEON_DEBUG_TOOL, "minecraft", "item/stick");

        basicItem(ModItems.INFUSED_ALLOY_HELMET.asItem());
        basicItem(ModItems.INFUSED_ALLOY_CHESTPLATE.asItem());
        basicItem(ModItems.INFUSED_ALLOY_LEGGINGS.asItem());
        basicItem(ModItems.INFUSED_ALLOY_BOOTS.asItem());

        basicItem(ModItems.SCHOLAR_HELMET.asItem());
        basicItem(ModItems.SCHOLAR_CHESTPLATE.asItem());
        basicItem(ModItems.SCHOLAR_LEGGINGS.asItem());
        basicItem(ModItems.SCHOLAR_BOOTS.asItem());

        withExistingParent(ModSpawnEggs.DEATH_KNIGHT_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModSpawnEggs.SKELETON_KNIGHT_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModSpawnEggs.CAVE_GOBLIN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModSpawnEggs.SHADOW_GOBLIN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }

    private void basicSingleTextureBlockItem(DeferredBlock<?> block) {
        withExistingParent(block.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(ResourceLocation.DEFAULT_NAMESPACE, "item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "block/" + block.getId().getPath()));
    }

    private void basicBlockItemOtherTexturePath(DeferredBlock<?> block, String TextureNamespace, String TexturePath) {
        withExistingParent(block.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(ResourceLocation.DEFAULT_NAMESPACE, "item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(TextureNamespace, TexturePath));
    }

    private void basicItemOtherTexturePath(DeferredItem<?> item, String TextureNamespace, String TexturePath) {
        withExistingParent(item.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(ResourceLocation.DEFAULT_NAMESPACE, "item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(TextureNamespace, TexturePath));
    }
}
