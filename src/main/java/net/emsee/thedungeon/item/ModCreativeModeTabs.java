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
                        output.accept(ModItems.DUNGEON_CLOCK);
                        output.accept(ModItems.TEST_DUMMY);
                        output.accept(ModItems.INFUSED_DAGGER);
                        output.accept(ModItems.INFUSED_CHISEL);
                        output.accept(ModItems.INFUSED_ALLOY_HELMET);
                        output.accept(ModItems.INFUSED_ALLOY_CHESTPLATE);
                        output.accept(ModItems.INFUSED_ALLOY_LEGGINGS);
                        output.accept(ModItems.INFUSED_ALLOY_BOOTS);
                        output.accept(ModItems.SCHOLAR_HELMET);
                        output.accept(ModItems.SCHOLAR_CHESTPLATE);
                        output.accept(ModItems.SCHOLAR_LEGGINGS);
                        output.accept(ModItems.SCHOLAR_BOOTS);
                    }).build());

    public static final Supplier<CreativeModeTab> DUNGEON_INGREDIENTS_TAB = CREATIVE_MODE_TAB.register("dungeon_ingredients_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.PORTAL_CORE.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon_tools_tab"))
                    .title(Component.translatable("creativetab.thedungeon.dungeon_ingredients"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.PORTAL_CORE);
                        output.accept(ModItems.SHATTERED_PORTAL_CORE);
                        output.accept(ModItems.DUNGEON_ESSENCE_SHARD);
                        output.accept(ModBlocks.INFUSED_THREAD);
                        output.accept(ModItems.INFUSED_ALLOY_INGOT);
                    }).build());

    public static final Supplier<CreativeModeTab> DUNGEON_BLOCKS_TAB = CREATIVE_MODE_TAB.register("dungeon_blocks_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModBlocks.DUNGEON_PORTAL_F.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon_ingredients_tab"))
                    .title(Component.translatable("creativetab.thedungeon.dungeon_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.DUNGEON_PORTAL_F);
                        output.accept(ModBlocks.DUNGEON_PORTAL_E);
                        output.accept(ModBlocks.DUNGEON_PORTAL_D);
                        //output.accept(ModBlocks.DUNGEON_PORTAL_C);
                        //output.accept(ModBlocks.DUNGEON_PORTAL_B);
                        //output.accept(ModBlocks.DUNGEON_PORTAL_A);
                        //output.accept(ModBlocks.DUNGEON_PORTAL_S);
                        //output.accept(ModBlocks.DUNGEON_PORTAL_SS);
                        output.accept(ModBlocks.DUNGEON_PORTAL_EXIT);
                        output.accept(ModBlocks.DUNGEON_PORTAL_UNSTABLE);
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
                    }).build());

    public static final Supplier<CreativeModeTab> DUNGEON_SPAWN_EGGS_TAB = CREATIVE_MODE_TAB.register("dungeon_spawn_eggs_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModSpawnEggs.DEATH_KNIGHT_SPAWN_EGG.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon_blocks_tab"))
                    .title(Component.translatable("creativetab.thedungeon.dungeon_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModSpawnEggs.DEATH_KNIGHT_SPAWN_EGG);
                        output.accept(ModSpawnEggs.SKELETON_KNIGHT_SPAWN_EGG);
                        output.accept(ModSpawnEggs.CAVE_GOBLIN_SPAWN_EGG);
                        output.accept(ModSpawnEggs.SHADOW_GOBLIN_SPAWN_EGG);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
