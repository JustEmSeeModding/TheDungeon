package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.item.ModSpawnEggs;
import net.emsee.thedungeon.item.custom.armor.DungeonArmorItem;
import net.emsee.thedungeon.simpleRegisterGroup.SimpleItemGroup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.LinkedHashMap;

public final class ModItemModelProvider extends ItemModelProvider {
    private final static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

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

        basicItem(ModItems.INFUSED_BREAD.get());
        basicItem(ModItems.INFUSED_WHEAT.get());

        basicArmorItems(ModItems.INFUSED_ARMOR_SET);
        basicTrimmedArmorItems(ModItems.SCHOLAR_ARMOR_SET);
        basicArmorItems(ModItems.KOBALT_ARMOR_SET);
        basicArmorItems(ModItems.ARCTIC_IRONCLAD_ARMOR_SET);

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

    private <A extends DungeonArmorItem> void basicTrimmedArmorItems(SimpleItemGroup.ArmorSet<A> armorSet) {
        trimmedArmorItem(armorSet.HELMET);
        trimmedArmorItem(armorSet.CHESTPLATE);
        trimmedArmorItem(armorSet.LEGGINGS);
        trimmedArmorItem(armorSet.BOOTS);
    }

    // from : https://github.com/Tutorials-By-Kaupenjoe/NeoForge-Course-121-Module-2/commit/016638b1681f7da1bc1a52e6a778d5c5b71c89f2#diff-4b38e7b1fbd9382979e81d1e312a1bc4ebbc0586f41741edc9685b36229c03e0
    // Shoutout to El_Redstoniano for making this and Kaupenjoe for converting it to 1.21.1
    private <A extends DungeonArmorItem> void trimmedArmorItem(DeferredItem<A> itemDeferredItem) {
        final String MOD_ID = TheDungeon.MOD_ID; // Change this to your mod id

        ArmorItem armorItem = itemDeferredItem.get();
        trimMaterials.forEach((trimMaterial, value) -> {
            float trimValue = value;

            String armorType = switch (armorItem.getEquipmentSlot()) {
                case HEAD -> "helmet";
                case CHEST -> "chestplate";
                case LEGS -> "leggings";
                case FEET -> "boots";
                default -> "";
            };

            String armorItemPath = armorItem.toString();
            String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
            String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
            ResourceLocation armorItemResLoc = ResourceLocation.parse(armorItemPath);
            ResourceLocation trimResLoc = ResourceLocation.parse(trimPath); // minecraft namespace
            ResourceLocation trimNameResLoc = ResourceLocation.parse(currentTrimName);

            // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
            // avoid an IllegalArgumentException
            existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

            // Trimmed armorItem files
            getBuilder(currentTrimName)
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", armorItemResLoc.getNamespace() + ":item/" + armorItemResLoc.getPath())
                    .texture("layer1", trimResLoc);

            // Non-trimmed armorItem file (normal variant)
            this.withExistingParent(itemDeferredItem.getId().getPath(),
                            mcLoc("item/generated"))
                    .override()
                    .model(new ModelFile.UncheckedModelFile(trimNameResLoc.getNamespace() + ":item/" + trimNameResLoc.getPath()))
                    .predicate(mcLoc("trim_type"), trimValue).end()
                    .texture("layer0",
                            ResourceLocation.fromNamespaceAndPath(MOD_ID,
                                    "item/" + itemDeferredItem.getId().getPath()));
        });
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
