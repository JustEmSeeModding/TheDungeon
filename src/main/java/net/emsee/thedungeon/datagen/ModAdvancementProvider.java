package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.item.ModItems;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class ModAdvancementProvider extends AdvancementProvider {

    ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper, List.of(
                ModAdvancementProvider::generateAdvancements
        ));
    }

    private static void generateAdvancements(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        AdvancementHolder dungeonRoot = createRootAdvancement("thedungeon", ModBlocks.DUNGEON_PORTAL, ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "textures/block/infused_dirt.png"), Map.of("pickup_infused_alloy",  InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.INFUSED_ALLOY_INGOT)), saver, existingFileHelper);
        AdvancementHolder essenceShard = createItemAdvancement("thedungeon","dungeon_essence_shard", ModItems.DUNGEON_ESSENCE_SHARD, dungeonRoot, AdvancementType.TASK, false, saver,existingFileHelper);
        AdvancementHolder infusedAlloy = createItemAdvancement("thedungeon","infused_alloy", ModItems.INFUSED_ALLOY_INGOT, essenceShard, AdvancementType.TASK, false, saver,existingFileHelper);
    }

    private static AdvancementHolder createItemAdvancement(String location, String name, ItemLike item, AdvancementHolder parent, AdvancementType advancementType, boolean hidden, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        String criterionName = "pickup_"+ getItemName(item);
        Advancement.Builder builder = Advancement.Builder.advancement()
                .display(
                new ItemStack(item),
                Component.translatable("advancements."+location+"."+name+".title"),
                Component.translatable("advancements."+location+"."+name+".description"),
                null,
                advancementType,
                true,
                true,
                hidden
        )
                .addCriterion(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(item))
                .requirements(AdvancementRequirements.allOf(List.of(criterionName)));
        if (parent!=null) {
            builder.parent(parent);
        }
        return builder.save(saver, ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, location+"/"+name), existingFileHelper);
    }

    private static AdvancementHolder createRootAdvancement(String location, ItemLike icon, ResourceLocation background, Map<String, Criterion> criterions, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        Advancement.Builder builder =  Advancement.Builder.advancement()
                .display(
                        new ItemStack(icon),
                        Component.translatable("advancements."+location+".root.title"),
                        Component.translatable("advancements."+location+".root.description"),
                        background,
                        AdvancementType.TASK,
                        false,
                        false,
                        false
                );

        for (String key : criterions.keySet()) {
            builder.addCriterion(key, criterions.get(key));
        }
        builder.requirements(AdvancementRequirements.allOf(criterions.keySet()));

        return builder.save(saver, ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, location+"/root"), existingFileHelper);
    }

    static String getItemName(ItemLike itemLike) {
        return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).getPath();
    }

}
