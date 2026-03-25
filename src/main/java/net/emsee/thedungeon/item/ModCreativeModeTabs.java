package net.emsee.thedungeon.item;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.simpleRegisterGroup.SimpleBlockGroup;
import net.emsee.thedungeon.simpleRegisterGroup.SimpleItemGroup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TheDungeon.MOD_ID);

    public static final Supplier<CreativeModeTab> DUNGEON_TOOLS_TAB = CREATIVE_MODE_TAB.register("dungeon_tools_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.INFUSED_DAGGER.get()))
                    .title(Component.translatable("creativetab.thedungeon.dungeon_tools"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.DUNGEON_DEBUG_TOOL);//TODO comment out at release
                        output.accept(ModItems.DUNGEON_CLOCK);
                        output.accept(ModItems.PYRITE_COMPASS);
                        output.accept(ModItems.SOUL_BOUND_TOTEM);
                        output.accept(ModItems.LUMINOUS_GOGGLES);
                        output.accept(ModItems.TEST_DUMMY);
                        output.accept(ModItems.INFUSED_DAGGER);
                        output.accept(ModItems.INFUSED_CHISEL);
                        output.accept(ModItems.KOBALT_DAGGER);
                        output.accept(ModItems.GOBLINS_FORGEHAMMER);
                        output.accept(ModItems.KOBALT_SHIELD);
                        acceptItemGroup(output, ModItems.INFUSED_ARMOR_SET);
                        acceptItemGroup(output, ModItems.SCHOLAR_ARMOR_SET);
                        acceptItemGroup(output, ModItems.ARCTIC_IRONCLAD_ARMOR_SET);
                        acceptItemGroup(output, ModItems.KOBALT_ARMOR_SET);
                    }).build());


    public static final Supplier<CreativeModeTab> DUNGEON_INGREDIENTS_TAB = CREATIVE_MODE_TAB.register("dungeon_ingredients_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.CATALYST_CORE.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon_tools_tab"))
                    .title(Component.translatable("creativetab.thedungeon.dungeon_ingredients"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.CATALYST_CORE);
                        output.accept(ModItems.SHATTERED_CATALYST_CORE);
                        output.accept(ModItems.DUNGEON_ESSENCE_SHARD);
                        output.accept(ModBlocks.INFUSED_THREAD);
                        output.accept(ModItems.INFUSED_ALLOY_INGOT);
                        output.accept(ModItems.INFUSED_WHEAT);
                        output.accept(ModItems.PYRITE);
                        output.accept(ModItems.PYRITE_COIN);
                        acceptItemGroup(output, ModItems.GILDREAN);
                        acceptItemGroup(output, ModItems.INFERNAL_TIN);
                        acceptItemGroup(output, ModItems.ARCTIC_IRON);
                        acceptItemGroup(output, ModItems.LAVINTINE);
                        acceptItemGroup(output, ModItems.KOBALT);
                        output.accept(ModItems.ROSELITH_CRYSTAL);
                        output.accept(ModItems.GARNETORE_PIECE);
                        output.accept(ModItems.VERDANTITE_CHUNK);
                        output.accept(ModItems.LUMANITE_FRAGMENT);
                    }).build());

    public static final Supplier<CreativeModeTab> DUNGEON_FOOD_TAB = CREATIVE_MODE_TAB.register("dungeon_food_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.GILDREAN_APPLE.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon_ingredients_tab"))
                    .title(Component.translatable("creativetab.thedungeon.dungeon_food"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.INFUSED_BREAD);
                        output.accept(ModItems.GOBLIN_MEAT);
                        output.accept(ModItems.GILDREAN_APPLE);
                    }).build());

    public static final Supplier<CreativeModeTab> DUNGEON_BLOCKS_TAB = CREATIVE_MODE_TAB.register("dungeon_blocks_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModBlocks.PYRITE_BLOCKS.BLOCK.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon_food_tab"))
                    .title(Component.translatable("creativetab.thedungeon.dungeon_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.DUNGEON_PORTAL);
                        output.accept(ModBlocks.CATALYST_BROKEN);
                        output.accept(ModBlocks.CATALYST_F);
                        output.accept(ModBlocks.CATALYST_E);
                        //output.accept(ModBlocks.CATALYST_D);
                        //output.accept(ModBlocks.CATALYST_C);
                        //output.accept(ModBlocks.CATALYST_B);
                        //output.accept(ModBlocks.CATALYST_A);
                        //output.accept(ModBlocks.CATALYST_S);
                        //output.accept(ModBlocks.CATALYST_SS);
                        output.accept(ModBlocks.GOBLIN_FORGE);
                        acceptBlockGroup(output,ModBlocks.PYRITE_BLOCKS);
                        acceptBlockGroup(output,ModBlocks.GILDREAN);
                        output.accept(ModBlocks.INGILDERD_BLACKSTONE);
                        acceptBlockGroup(output,ModBlocks.INFERNAL_TIN);
                        acceptBlockGroup(output,ModBlocks.ARCTIC_IRON);
                        acceptBlockGroup(output,ModBlocks.LAVINTINE);
                        output.accept(ModBlocks.KOBALT_BLOCK);
                        output.accept(ModBlocks.INFUSED_GRASS_BLOCK);
                        output.accept(ModBlocks.INFUSED_DIRT);
                        output.accept(ModBlocks.INFUSED_CLAY);
                        output.accept(ModBlocks.INFUSED_SAND);
                        output.accept(ModBlocks.INFUSED_GRAVEL);
                        output.accept(ModBlocks.INFUSED_STONE);
                        output.accept(ModBlocks.INFUSED_DEEPSLATE);
                        output.accept(ModBlocks.INFUSED_STONE_BRICKS);
                        output.accept(ModBlocks.INFUSED_NETHERRACK);
                        output.accept(ModBlocks.INFUSED_SOUL_SAND);
                        output.accept(ModBlocks.INFUSED_SOUL_SOIL);
                        output.accept(ModBlocks.INFUSED_END_STONE);
                        output.accept(ModBlocks.INFUSED_END_STONE_BRICKS);
                        output.accept(ModBlocks.INFUSED_GLASS);
                        acceptBlockGroup(output,ModBlocks.ROSELITH_CRYSTAL_GROUP);
                        acceptBlockGroup(output,ModBlocks.GARNETORE_CRYSTAL_GROUP);
                        acceptBlockGroup(output,ModBlocks.VERDANTITE_CRYSTAL_GROUP);
                        acceptBlockGroup(output,ModBlocks.LUMANITE_CRYSTAL_GROUP);
                    }).build());

    public static final Supplier<CreativeModeTab> DUNGEON_SPAWN_EGGS_TAB = CREATIVE_MODE_TAB.register("dungeon_spawn_eggs_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModSpawnEggs.DEATH_KNIGHT_SPAWN_EGG.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon_blocks_tab"))
                    .title(Component.translatable("creativetab.thedungeon.dungeon_spawn_eggs"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModSpawnEggs.DEATH_KNIGHT_SPAWN_EGG);
                        output.accept(ModSpawnEggs.SKELETON_KNIGHT_SPAWN_EGG);
                        output.accept(ModSpawnEggs.CAVE_GOBLIN_SPAWN_EGG);
                        output.accept(ModSpawnEggs.SHADOW_GOBLIN_SPAWN_EGG);
                        output.accept(ModSpawnEggs.HOB_GOBLIN_SPAWN_EGG);
                        output.accept(ModSpawnEggs.CRYSTAL_GOLEM_SPAWN_EGG);
                        output.accept(ModSpawnEggs.LUMINOUS_CRAWLER_SPAWN_EGG);
                    }).build());

    private static void acceptItemGroup(CreativeModeTab.Output output, SimpleItemGroup group) {
        group.getAllAsItem().forEach(output::accept);
    }

    private static void acceptBlockGroup(CreativeModeTab.Output output, SimpleBlockGroup group) {
        group.getAllAsBlock().forEach(output::accept);
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
