package net.emsee.thedungeon.item.interfaces;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.client.event.AddAttributeTooltipsEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IDungeonToolTips {
    ChatFormatting TITLE_FORMATTING = ChatFormatting.AQUA;
    ChatFormatting MAIN_STAT_FORMATTING = ChatFormatting.GOLD;
    ChatFormatting POSITIVE_FORMATTING_EXTRA = ChatFormatting.GREEN;
    ChatFormatting NEGATIVE_FORMATTING_EXTRA = ChatFormatting.RED;

    /**
     * Component[0] = title, leave NULL for no title
     */
    default Component[] getPrefixComponents () {return null;}

    /**
     * Component[0] = title, leave NULL for no title
     */
    default Component[] getSuffixComponents () {return null;}

    default void addAttributeTooltips(AddAttributeTooltipsEvent event) {
        ItemStack stack = event.getStack();

        // create a map with an empty list linked to each SlotType
        Map<SlotType, List<ItemAttributeModifiers.Entry>> entries = Util.make(new HashMap<>(), map -> {for (SlotType slot : SlotType.values()){map.put(slot, new ArrayList<>());}});

        // get all modifiers
        List<ItemAttributeModifiers.Entry> modifiers = stack.getAttributeModifiers().modifiers();

        // add every modifier to the list of the correct slot
        modifiers.forEach(entry -> {
            entries.get(SlotType.fromDefaultSlotGroup(entry.slot())).add(entry);
        });

        // add suffix component
        addComponentArray(getPrefixComponents(), event);

        // handle every SlotType
        addAttributeList(SlotType.HAND,
                Component.translatable("attribute.thedungeon.title.mainhand").withStyle(TITLE_FORMATTING),
                entries.get(SlotType.HAND), event);
        addAttributeList(SlotType.OFFHAND,
                Component.translatable("attribute.thedungeon.title.offhand").withStyle(TITLE_FORMATTING),
                entries.get(SlotType.OFFHAND), event);
        addAttributeList(SlotType.EQUIPPED,
                Component.translatable("attribute.thedungeon.title.armor").withStyle(TITLE_FORMATTING),
                entries.get(SlotType.EQUIPPED), event);
        addAttributeList(SlotType.FULL_BODY,
                Component.translatable("attribute.thedungeon.title.full_armor_set").withStyle(TITLE_FORMATTING),
                entries.get(SlotType.FULL_BODY), event);
        addAttributeList(SlotType.DEFAULT,
                Component.literal("YOU FOUND AN ERROR, these are not bound to a slot:"),
                entries.get(SlotType.DEFAULT), event);

        // add prefix components
        addComponentArray(getSuffixComponents(), event);

    }

    static void addAttributeList(SlotType slot, Component title, List<ItemAttributeModifiers.Entry> list, AddAttributeTooltipsEvent event) {
        if (list.isEmpty()) return;

        // add empty line spacing (vanilla has it and it looks cleaner)
        // then add the title
        event.addTooltipLines(Component.empty());
        if (title!=null) event.addTooltipLines(title);

        // for hand weapons count all damage
        if (slot==SlotType.HAND) {
            double totalDamage =0;
            for (ItemAttributeModifiers.Entry entry : list) {
                if (entry.matches(Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID))
                    totalDamage += entry.modifier().amount();
                // add all total damage as a component
                MutableComponent component = Component.literal("| ").withStyle(TITLE_FORMATTING);
                component.append(entry.attribute().value().toBaseComponent(totalDamage + 1, 1, false, event.getContext().flag()).withStyle(MAIN_STAT_FORMATTING));
            }
        }

        // handle every entry
        list.forEach(entry -> {
            MutableComponent component = Component.literal("| ").withStyle(TITLE_FORMATTING);
            if (entry.modifier().amount()==0) return;

            // add added damage for all things not hand weapons
            if (slot != SlotType.HAND && entry.matches(Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID))
                component.append(entry.attribute().value().toComponent(entry.modifier(), event.getContext().flag()).withStyle(MAIN_STAT_FORMATTING));

            // custom attack speed
            else if (entry.matches(Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_ID))
                component.append(entry.attribute().value().toBaseComponent(entry.modifier().amount()+4, 4, false, event.getContext().flag()).withStyle(MAIN_STAT_FORMATTING));

            // custom armor values
            else if (entry.matches(Attributes.ARMOR, ResourceLocation.withDefaultNamespace("armor.helmet")) ||
                    entry.matches(Attributes.ARMOR, ResourceLocation.withDefaultNamespace("armor.chestplate")) ||
                    entry.matches(Attributes.ARMOR, ResourceLocation.withDefaultNamespace("armor.leggings")) ||
                    entry.matches(Attributes.ARMOR, ResourceLocation.withDefaultNamespace("armor.boots")) ||
                    entry.matches(Attributes.ARMOR, ResourceLocation.withDefaultNamespace("armor.body")))
               component.append(entry.attribute().value().toBaseComponent(entry.modifier().amount(), 0, true, event.getContext().flag()).withStyle(MAIN_STAT_FORMATTING));

            // custom armor toughness values
            else if (entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.helmet")) ||
                    entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.chestplate")) ||
                    entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.leggings")) ||
                    entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.boots")) ||
                    entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.body")))
                component.append(entry.attribute().value().toBaseComponent(entry.modifier().amount(), 0, true, event.getContext().flag()).withStyle(MAIN_STAT_FORMATTING));

            // add all leftover values
            else
                component.append(entry.attribute().value().toComponent(entry.modifier(), event.getContext().flag()).withStyle(entry.modifier().amount()>0 ? POSITIVE_FORMATTING_EXTRA : NEGATIVE_FORMATTING_EXTRA));

            // add converted component to the tooltip
            event.addTooltipLines(component);
        });
    }

    static void addComponentArray(Component[] components, AddAttributeTooltipsEvent event) {
        // no components? just return
        if (components == null) return;

        // add empty line spacing (vanilla has it and it looks cleaner)
        // then add the title (if it exists)
        event.addTooltipLines(Component.empty());
        Component title = components[0];
        if (title!=null) event.addTooltipLines(title);

        // now add every other component
        for (int i = 1; i < components.length; i++) {
            MutableComponent component = Component.literal("| ").withStyle(TITLE_FORMATTING);
            component.append(components[i]);
            event.addTooltipLines(component);
        }
    }

    enum SlotType {
        DEFAULT,
        HAND,
        OFFHAND,
        EQUIPPED,
        FULL_BODY;

        static SlotType fromDefaultSlotGroup(EquipmentSlotGroup slotGroup) {
            SlotType toReturn = DEFAULT;
            switch (slotGroup) {
                case MAINHAND -> toReturn = HAND;
                case OFFHAND -> toReturn = OFFHAND;
                case HEAD, CHEST, LEGS, FEET -> toReturn = EQUIPPED;
                case BODY -> toReturn = FULL_BODY;
            }
            return toReturn;
        }
    }
}
