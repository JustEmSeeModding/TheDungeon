package net.emsee.thedungeon.item.interfaces;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.client.event.AddAttributeTooltipsEvent;

import java.util.*;

public interface IDungeonToolTips {
    ChatFormatting TITLE_FORMATTING = ChatFormatting.AQUA;
    ChatFormatting MAIN_STAT_FORMATTING = ChatFormatting.GOLD;
    ChatFormatting POSITIVE_FORMATTING = ChatFormatting.GREEN;
    ChatFormatting NEGATIVE_FORMATTING = ChatFormatting.RED;

    /**
     * Component[0] = title, leave NULL for no title
     */
    default LinkedHashMap<Component, Component[]> getPrefixComponents () {return null;}

    default LinkedHashMap<Component, Component[]> getSuffixComponents () {return null;}

    default LinkedHashMap<SlotType, Component[]> getExtraComponents () {return new LinkedHashMap<>();}

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

        // add prefix component
        addFixedComponents(getPrefixComponents(), event);

        LinkedHashMap<SlotType, Component[]> extraComponents = getExtraComponents();

        // handle every SlotType
        addAttributeList(SlotType.MAIN_HAND,
                Component.translatable("attribute.thedungeon.title.mainhand").withStyle(TITLE_FORMATTING),
                entries.get(SlotType.MAIN_HAND), extraComponents.get(SlotType.MAIN_HAND), event);
        addAttributeList(SlotType.OFFHAND,
                Component.translatable("attribute.thedungeon.title.offhand").withStyle(TITLE_FORMATTING),
                entries.get(SlotType.OFFHAND), extraComponents.get(SlotType.OFFHAND), event);
        addAttributeList(SlotType.ANY_HAND,
                Component.translatable("attribute.thedungeon.title.anyhand").withStyle(TITLE_FORMATTING),
                entries.get(SlotType.ANY_HAND), extraComponents.get(SlotType.ANY_HAND), event);
        addAttributeList(SlotType.EQUIPPED,
                Component.translatable("attribute.thedungeon.title.armor").withStyle(TITLE_FORMATTING),
                entries.get(SlotType.EQUIPPED), extraComponents.get(SlotType.EQUIPPED), event);
        addAttributeList(SlotType.FULL_BODY,
                Component.translatable("attribute.thedungeon.title.full_armor_set").withStyle(TITLE_FORMATTING),
                entries.get(SlotType.FULL_BODY), extraComponents.get(SlotType.FULL_BODY), event);
        addAttributeList(SlotType.UNASSIGNED,
                Component.literal("YOU FOUND AN ERROR, these are not bound to a slot:"),
                entries.get(SlotType.UNASSIGNED), extraComponents.get(SlotType.UNASSIGNED), event);

        // add suffix components
        addFixedComponents(getSuffixComponents(), event);

    }

    static void addAttributeList(SlotType slot, Component title, List<ItemAttributeModifiers.Entry> list, Component[] otherComponents, AddAttributeTooltipsEvent event) {
        if (list.isEmpty() && otherComponents==null) return;

        // add empty line spacing (vanilla has it and it looks cleaner)
        // then add the title
        event.addTooltipLines(Component.empty());
        if (title != null) event.addTooltipLines(title);

        // for hand weapons count all damage
        if (!list.isEmpty() && slot == SlotType.MAIN_HAND)
            handleHandAttributes(list, event);

        if (!list.isEmpty() && event.getStack().getItem() instanceof ArmorItem armorItem)
            handleArmorAttributes(armorItem, list, event);

        if (otherComponents != null)
            for (Component extra : otherComponents) {
                MutableComponent component = Component.literal("| ").withStyle(TITLE_FORMATTING);
                component.append(extra);
                event.addTooltipLines(component);
            }


        // handle every entry
        list.forEach(entry -> {
            MutableComponent component = Component.literal("| ").withStyle(TITLE_FORMATTING);
            if (entry.modifier().amount()==0) return;

            // we want to skip this as this is handled above
            if (slot == SlotType.MAIN_HAND && entry.attribute() == Attributes.ATTACK_DAMAGE)
                return;
            if (slot == SlotType.MAIN_HAND && entry.attribute() == Attributes.ATTACK_SPEED)
                return;

            // add added damage for all things not hand weapons
            if (entry.matches(Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID))
                component.append(entry.attribute().value().toComponent(entry.modifier(), event.getContext().flag()).withStyle(MAIN_STAT_FORMATTING));
            // custom attack speed
            else if (entry.matches(Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_ID))
                component.append(entry.attribute().value().toBaseComponent(entry.modifier().amount()+4, 4, false, event.getContext().flag()).withStyle(MAIN_STAT_FORMATTING));

            // custom armor values
            else if (entry.attribute() == Attributes.ARMOR && event.getStack().getItem() instanceof ArmorItem)
               return;

            // custom armor toughness values
            else if (entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.helmet")) ||
                    entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.chestplate")) ||
                    entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.leggings")) ||
                    entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.boots")) ||
                    entry.matches(Attributes.ARMOR_TOUGHNESS, ResourceLocation.withDefaultNamespace("armor.body")))
                component.append(entry.attribute().value().toBaseComponent(entry.modifier().amount(), 0, true, event.getContext().flag()).withStyle(MAIN_STAT_FORMATTING));

            // add all leftover values
            else
                component.append(entry.attribute().value().toComponent(entry.modifier(), event.getContext().flag()).withStyle(getFormat(entry, false)));

            // add converted component to the tooltip
            event.addTooltipLines(component);
        });
    }

    private static void handleHandAttributes(List<ItemAttributeModifiers.Entry> list, AddAttributeTooltipsEvent event) {
        double mainDamage = 0;
        double mainSpeed = 0;
        List<ItemAttributeModifiers.Entry> otherDamage = new ArrayList<>();
        List<ItemAttributeModifiers.Entry> otherSpeed = new ArrayList<>();
        for (ItemAttributeModifiers.Entry entry : list) {
            //if (entry.matches(Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID))
            if (entry.attribute() == Attributes.ATTACK_DAMAGE) {
                if (entry.matches(Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID))
                    mainDamage = entry.modifier().amount();
                else
                    otherDamage.add(entry);
            } else if (entry.attribute() == Attributes.ATTACK_SPEED) {
                if (entry.matches(Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_ID))
                    mainSpeed = entry.modifier().amount();
                else
                    otherSpeed.add(entry);
            }
            // add all total damage as a component
        }
        if (mainDamage>0) {
            MutableComponent mainDamageComponent = Component.literal("| ").withStyle(TITLE_FORMATTING);
            mainDamageComponent.append(Attributes.ATTACK_DAMAGE.value().toBaseComponent(mainDamage + 1, 1, true, event.getContext().flag()).withStyle(MAIN_STAT_FORMATTING));
            event.addTooltipLines(mainDamageComponent);
        }
        for (ItemAttributeModifiers.Entry entry : otherDamage) {
            MutableComponent damageComponent = Component.literal("|  ").withStyle(TITLE_FORMATTING);
            damageComponent.append(entry.attribute().value().toComponent(entry.modifier(), event.getContext().flag()).withStyle(getFormat(entry, false)));
            event.addTooltipLines(damageComponent);
        }
        MutableComponent mainSpeedComponent = Component.literal("| ").withStyle(TITLE_FORMATTING);
        mainSpeedComponent.append(Attributes.ATTACK_SPEED.value().toBaseComponent(mainSpeed + 4, 4, false, event.getContext().flag()).withStyle(MAIN_STAT_FORMATTING));
        event.addTooltipLines(mainSpeedComponent);
        for (ItemAttributeModifiers.Entry entry : otherSpeed) {
            MutableComponent speedComponent = Component.literal("|  ").withStyle(TITLE_FORMATTING);
            speedComponent.append(entry.attribute().value().toComponent(entry.modifier(), event.getContext().flag()).withStyle(getFormat(entry, false)));
            event.addTooltipLines(speedComponent);
        }
    }

    private static void handleArmorAttributes(ArmorItem armorItem, List<ItemAttributeModifiers.Entry> list, AddAttributeTooltipsEvent event) {
        double mainArmor = 0;
        ArmorItem.Type armorType = armorItem.getType();
        List<ItemAttributeModifiers.Entry> otherArmor = new ArrayList<>();
        for (ItemAttributeModifiers.Entry entry : list) {
            //if (entry.matches(Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID))
            if (entry.attribute() == Attributes.ARMOR) {
                if (entry.matches(Attributes.ARMOR, ResourceLocation.withDefaultNamespace("armor." + armorType.getName())))
                    mainArmor = entry.modifier().amount();
                else
                    otherArmor.add(entry);
            }
        }
        if (mainArmor>0) {
            MutableComponent mainArmorComponent = Component.literal("| ").withStyle(TITLE_FORMATTING);
            mainArmorComponent.append(Attributes.ARMOR.value().toBaseComponent(mainArmor, 0, true, event.getContext().flag()).withStyle(MAIN_STAT_FORMATTING));
            event.addTooltipLines(mainArmorComponent);
        }
        for (ItemAttributeModifiers.Entry entry : otherArmor) {
            MutableComponent damageComponent = Component.literal("|  ").withStyle(TITLE_FORMATTING);
            damageComponent.append(entry.attribute().value().toComponent(entry.modifier(), event.getContext().flag()).withStyle(getFormat(entry, false)));
            event.addTooltipLines(damageComponent);
        }
    }

    static void addFixedComponents(Map<Component, Component[]> components, AddAttributeTooltipsEvent event) {
        // no components? return
        if (components == null) return;

        components.forEach((title, lines) -> {
            // add empty line spacing (vanilla has it and it looks cleaner)
            event.addTooltipLines(Component.empty());
            // then add the title
            event.addTooltipLines(title.copy().withStyle(TITLE_FORMATTING));

            // now add every other line
            for (Component line : lines) {
                MutableComponent component = Component.literal("| ").withStyle(TITLE_FORMATTING);
                component.append(line);
                event.addTooltipLines(component);
            }
        });
    }

    enum SlotType {
        UNASSIGNED,
        MAIN_HAND,
        OFFHAND,
        ANY_HAND,
        EQUIPPED,
        FULL_BODY;

        static SlotType fromDefaultSlotGroup(EquipmentSlotGroup slotGroup) {
            SlotType toReturn = UNASSIGNED;
            toReturn = switch (slotGroup) {
                case ANY,ARMOR -> UNASSIGNED;
                case MAINHAND ->  MAIN_HAND;
                case OFFHAND -> OFFHAND;
                case HAND -> ANY_HAND;
                case HEAD, CHEST, LEGS, FEET ->  EQUIPPED;
                case BODY ->  FULL_BODY;
            };
            return toReturn;
        }
    }

    private static ChatFormatting getFormat(ItemAttributeModifiers.Entry entry, boolean invert) {
        return Objects.equals(entry.attribute().value().getStyle(entry.modifier().amount() > 0 ^ invert).getColor(), ChatFormatting.BLUE.getColor()) ? POSITIVE_FORMATTING : NEGATIVE_FORMATTING;
    }
}
