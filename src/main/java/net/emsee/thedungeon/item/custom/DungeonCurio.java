package net.emsee.thedungeon.item.custom;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.item.interfaces.IDungeonToolTips;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DungeonCurio extends DungeonItem implements IDungeonToolTips, ICurioItem
{
     public DungeonCurio(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public List<Component> getSlotsTooltip(List<Component> tooltips, Item.TooltipContext context, ItemStack stack) {
        return new ArrayList<>();
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, TooltipContext context, ItemStack stack) {
        return new ArrayList<>();
    }

    @Override
    public final Multimap<Holder<Attribute>, AttributeModifier>  getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
         return getAttributeModifiers(stack);
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        return LinkedHashMultimap.create();
    }

    @Override
    public LinkedHashMap<Component, Component[]> getPrefixComponents(ItemStack stack) {
        return Util.make(new LinkedHashMap<>(), map -> {
            // Get all Curios tags associated with this item stack
            List<String> slotTypes = new ArrayList<>();
            for (TagKey<Item> tag : stack.getTags().toList()) {
                ResourceLocation tagId = tag.location();
                if (tagId.getNamespace().equals("curios")) {
                    String path = tagId.getPath();
                    if (path.equals("curio")) {
                        map.put(Component.translatable("item.thedungeon.tooltip.curio_type"),
                                new Component[]{Component.translatable("item.thedungeon.tooltip.curio_type.any").withStyle(POSITIVE_FORMATTING)});
                        return;
                    } else {
                        slotTypes.add(path);
                    }
                }
            }

            Component[] slotComponents = slotTypes.stream()
                    .map(slot -> Component.translatable("item.thedungeon.tooltip.curio_type." + slot).withStyle(POSITIVE_FORMATTING))
                    .toArray(Component[]::new);


            map.put(Component.translatable("item.thedungeon.tooltip.curio_type"), slotComponents);
        });
    }
}
