package net.emsee.thedungeon.item;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
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
                        output.accept(ModItems.TEST_DUMMY);
                        output.accept(ModItems.INFUSED_DAGGER);
                        output.accept(ModItems.INFUSED_CHISEL);
                        output.accept(ModItems.KOBALT_DAGGER);
                        output.accept(ModItems.GOBLINS_FORGEHAMMER);
                        output.accept(ModItems.KOBALT_SHIELD);
                        output.accept(ModItems.INFUSED_ALLOY_HELMET);
                        output.accept(ModItems.INFUSED_ALLOY_CHESTPLATE);
                        output.accept(ModItems.INFUSED_ALLOY_LEGGINGS);
                        output.accept(ModItems.INFUSED_ALLOY_BOOTS);
                        output.accept(ModItems.SCHOLAR_HELMET);
                        output.accept(ModItems.SCHOLAR_CHESTPLATE);
                        output.accept(ModItems.SCHOLAR_LEGGINGS);
                        output.accept(ModItems.SCHOLAR_BOOTS);
                        output.accept(ModItems.KOBALT_HELMET);
                        output.accept(ModItems.KOBALT_CHESTPLATE);
                        output.accept(ModItems.KOBALT_LEGGINGS);
                        output.accept(ModItems.KOBALT_BOOTS);
                    }).build());

    public static final Supplier<CreativeModeTab> DUNGEON_INGREDIENTS_TAB = CREATIVE_MODE_TAB.register("dungeon_ingredients_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.CATALIST_CORE.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon_tools_tab"))
                    .title(Component.translatable("creativetab.thedungeon.dungeon_ingredients"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.CATALIST_CORE);
                        output.accept(ModItems.SHATTERED_CATALIST_CORE);
                        output.accept(ModItems.DUNGEON_ESSENCE_SHARD);
                        output.accept(ModBlocks.INFUSED_THREAD);
                        output.accept(ModItems.INFUSED_ALLOY_INGOT);
                        output.accept(ModItems.KOBALT_INGOT);
                        output.accept(ModItems.PYRITE);
                        output.accept(ModItems.PYRITE_COIN);
                    }).build());

    public static final Supplier<CreativeModeTab> DUNGEON_FOOD_TAB = CREATIVE_MODE_TAB.register("dungeon_food_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.GOBLIN_MEAT.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon_ingredients_tab"))
                    .title(Component.translatable("creativetab.thedungeon.dungeon_food"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.GOBLIN_MEAT);
                    }).build());

    public static final Supplier<CreativeModeTab> DUNGEON_BLOCKS_TAB = CREATIVE_MODE_TAB.register("dungeon_blocks_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModBlocks.DUNGEON_PORTAL.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon_food_tab"))
                    .title(Component.translatable("creativetab.thedungeon.dungeon_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.DUNGEON_PORTAL);
                        output.accept(ModBlocks.CATALIST_BROKEN);
                        output.accept(ModBlocks.CATALIST_F);
                        output.accept(ModBlocks.CATALIST_E);
                        //output.accept(ModBlocks.CATALIST_D);
                        //output.accept(ModBlocks.CATALIST_C);
                        //output.accept(ModBlocks.CATALIST_B);
                        //output.accept(ModBlocks.CATALIST_A);
                        //output.accept(ModBlocks.CATALIST_S);
                        //output.accept(ModBlocks.CATALIST_SS);
                        output.accept(ModBlocks.GOBLIN_FORGE);
                        output.accept(ModBlocks.PYRITE_ORE);
                        output.accept(ModBlocks.DEEPSLATE_PYRITE_ORE);
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
                        output.accept(ModBlocks.ROSE_QUARTZ_BLOCK);
                        output.accept(ModBlocks.BUDDING_ROSE_QUARTZ);
                        output.accept(ModBlocks.SMALL_ROSE_QUARTZ_BUD);
                        output.accept(ModBlocks.MEDIUM_ROSE_QUARTZ_BUD);
                        output.accept(ModBlocks.LARGE_ROSE_QUARTZ_BUD);
                        output.accept(ModBlocks.ROSE_QUARTZ_CLUSTER);
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
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
