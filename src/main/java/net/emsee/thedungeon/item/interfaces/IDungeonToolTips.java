package net.emsee.thedungeon.item.interfaces;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.client.event.AddAttributeTooltipsEvent;

import java.util.ArrayList;
import java.util.List;

public interface IDungeonToolTips {
    ChatFormatting titleFormatting = ChatFormatting.AQUA;
    ChatFormatting mainStatFormatting = ChatFormatting.GOLD;
    ChatFormatting extraPositiveFormatting = ChatFormatting.GREEN;
    ChatFormatting extraNegativeFormatting = ChatFormatting.RED;

    /**
     * Component[0] = title
     */
    default Component[] getPrefixComponents () {return null;}

    /**
     * Component[0] = title
     */
    default Component[] getSuffixComponents () {return null;}

    default void addAttributeTooltips(AddAttributeTooltipsEvent event) {
        ItemStack stack = event.getStack();

        List<ItemAttributeModifiers.Entry> handAttributes = new ArrayList<>();
        List<ItemAttributeModifiers.Entry> armorAttributes = new ArrayList<>();
        List<ItemAttributeModifiers.Entry> fullArmorAttributes = new ArrayList<>();
        List<ItemAttributeModifiers.Entry> offhandAttributes = new ArrayList<>();
        List<ItemAttributeModifiers.Entry> miscAttributes = new ArrayList<>();

        List<ItemAttributeModifiers.Entry> modifiers = stack.getAttributeModifiers().modifiers();
        modifiers.forEach(entry -> {
            switch (entry.slot()) {
                case MAINHAND -> handAttributes.add(entry);
                case OFFHAND -> offhandAttributes.add(entry);
                case HEAD, CHEST, LEGS, FEET -> armorAttributes.add(entry);
                case BODY -> fullArmorAttributes.add(entry);
                default -> miscAttributes.add(entry);
            }
        });
        addComponents(getPrefixComponents(), event);
        addAtributeList(
                Component.translatable("attribute.thedungeon.title.mainhand").withStyle(titleFormatting),
                handAttributes, event);
        addAtributeList(
                Component.translatable("attribute.thedungeon.title.offhand").withStyle(titleFormatting),
                offhandAttributes, event);
        addAtributeList(
                Component.translatable("attribute.thedungeon.title.armor").withStyle(titleFormatting),
                armorAttributes, event);
        addAtributeList(
                Component.translatable("attribute.thedungeon.title.full_armor_set").withStyle(titleFormatting),
                fullArmorAttributes, event);
        addAtributeList(
                Component.literal("YOU FOUND AN ERROR, these are not bound to a slot:"),
                miscAttributes, event);
        addComponents(getSuffixComponents(), event);

    }

    static void addAtributeList(Component title, List<ItemAttributeModifiers.Entry> list, AddAttributeTooltipsEvent event) {
        if (list.isEmpty()) return;
        event.addTooltipLines(Component.empty());
        if (title!=null) event.addTooltipLines(title);
        list.forEach(entry -> {
            MutableComponent component = Component.literal("| ").withStyle(titleFormatting);
            if (entry.modifier().amount()==0) return;
            if (entry.matches(Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID))
                component.append(entry.attribute().value().toBaseComponent(entry.modifier().amount()+1, 1, false, event.getContext().flag()).withStyle(mainStatFormatting));
            else if (entry.matches(Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_ID))
                component.append(entry.attribute().value().toBaseComponent(entry.modifier().amount()+4, 4, false, event.getContext().flag()).withStyle(mainStatFormatting));
            else if (entry.matches(Attributes.ARMOR, ResourceLocation.withDefaultNamespace("armor.helmet")) ||
                    entry.matches(Attributes.ARMOR, ResourceLocation.withDefaultNamespace("armor.chestplate")) ||
                    entry.matches(Attributes.ARMOR, ResourceLocation.withDefaultNamespace("armor.leggings")) ||
                    entry.matches(Attributes.ARMOR, ResourceLocation.withDefaultNamespace("armor.boots")) ||
                    entry.matches(Attributes.ARMOR, ResourceLocation.withDefaultNamespace("armor.body")))
               component.append(entry.attribute().value().toBaseComponent(entry.modifier().amount(), 0, true, event.getContext().flag()).withStyle(mainStatFormatting));
            else if (entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.helmet")) ||
                    entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.chestplate")) ||
                    entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.leggings")) ||
                    entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.boots")) ||
                    entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.body")))
                component.append(entry.attribute().value().toBaseComponent(entry.modifier().amount(), 0, true, event.getContext().flag()).withStyle(mainStatFormatting));
            else
                component.append(entry.attribute().value().toComponent(entry.modifier(), event.getContext().flag()).withStyle(entry.modifier().amount()>0 ? extraPositiveFormatting: extraNegativeFormatting));
            event.addTooltipLines(component);
        });
    }

    static void addComponents(Component[] components, AddAttributeTooltipsEvent event) {
        if (components == null) return;
        event.addTooltipLines(Component.empty());
        Component title = components[0];
        if (title!=null) event.addTooltipLines(title);
        for (int i = 1; i < components.length; i++) {
            MutableComponent component = Component.literal("| ").withStyle(titleFormatting);
            event.addTooltipLines(component);
            event.addTooltipLines(component);
        }
    }
}
