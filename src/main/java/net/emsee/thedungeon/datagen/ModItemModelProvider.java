package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.item.ModSpawnEggs;
import net.emsee.thedungeon.simpleRegisterGroup.SimpleItemGroup;
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
        simpleBlockItem(ModBlocks.GOBLIN_FORGE.get());

        basicItem(ModItems.CATALYST_CORE.get());
        basicItem(ModItems.SHATTERED_CATALYST_CORE.get());
        basicItem(ModItems.DUNGEON_ESSENCE_SHARD.get());
        basicItem(ModItems.GOBLIN_MEAT.get());
        basicBlockItemOtherTexturePath(ModBlocks.INFUSED_THREAD, "thedungeon", "item/infused_thread");
        basicItemOtherTexturePath(ModItems.DUNGEON_DEBUG_TOOL, "minecraft", "item/stick");

        basicItem(ModItems.INFUSED_ALLOY_INGOT.get());
        basicItem(ModItems.PYRITE.get());
        basicItem(ModItems.PYRITE_COIN.get());
        simpleItemGroup(ModItems.GILDREAN);
        simpleItemGroup(ModItems.INFERNAL_TIN);
        simpleItemGroup(ModItems.ARCTIC_IRON);
        simpleItemGroup(ModItems.LAVINTINE);
        basicItem(ModItems.GILDREAN_APPLE.get());
        basicItem(ModItems.ROSELITH_CRYSTAL.get());
        basicItem(ModItems.GARNETORE_PIECE.get());
        basicItem(ModItems.VERDANTITE_CHUNK.get());
        basicItem(ModItems.LUMANITE_FRAGMENT.get());
        simpleItemGroup(ModItems.KOBALT);

        basicItem(ModItems.SOUL_BOUND_TOTEM.get());

        basicArmorItems(ModItems.INFUSED_ARMOR_SET);
        basicArmorItems(ModItems.SCHOLAR_ARMOR_SET);
        basicArmorItems(ModItems.KOBALT_ARMOR_SET);

        withExistingParent(ModItems.INFUSED_DAGGER.getId().getPath(), TheDungeon.defaultResourceLocation("item/dagger")).texture("0", TheDungeon.defaultResourceLocation("item/infused_dagger"));
        withExistingParent(ModItems.INFUSED_CHISEL.getId().getPath(), TheDungeon.defaultResourceLocation("item/chisel")).texture("0", TheDungeon.defaultResourceLocation("item/infused_chisel"));
        withExistingParent(ModItems.KOBALT_DAGGER.getId().getPath(), TheDungeon.defaultResourceLocation("item/dagger")).texture("0", TheDungeon.defaultResourceLocation("item/kobalt_dagger"));
        withExistingParent(ModItems.GOBLINS_FORGEHAMMER.getId().getPath(), TheDungeon.defaultResourceLocation("item/small_hammer")).texture("0", TheDungeon.defaultResourceLocation("item/goblins_forgehammer"));
        withExistingParent(ModItems.KOBALT_SHIELD.getId().getPath(), TheDungeon.defaultResourceLocation("item/small_shield")).texture("0", TheDungeon.defaultResourceLocation("item/kobalt_shield"));

        withExistingParent(ModBlocks.INFUSED_GRASS_BLOCK.getId().getPath(), TheDungeon.defaultResourceLocation("block/infused_grass_block"));

        withExistingParent(ModSpawnEggs.DEATH_KNIGHT_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModSpawnEggs.SKELETON_KNIGHT_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModSpawnEggs.CAVE_GOBLIN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModSpawnEggs.SHADOW_GOBLIN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModSpawnEggs.HOB_GOBLIN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModSpawnEggs.CRYSTAL_GOLEM_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModSpawnEggs.LUMINOUS_CRAWLER_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));

        basicBlockItemOtherTexturePath(ModBlocks.DUNGEON_PORTAL, TheDungeon.MOD_ID, "item/dungeon_portal");
    }

    private void simpleItemGroup(SimpleItemGroup itemGroup) {
        itemGroup.getAll().forEach(entry -> basicItem(entry.get()));
    }

    private void basicArmorItems(SimpleItemGroup.ArmorSet<?> armorSet) {
        basicItem(armorSet.HELMET.get());
        basicItem(armorSet.CHESTPLATE.get());
        basicItem(armorSet.LEGGINGS.get());
        basicItem(armorSet.BOOTS.get());
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
