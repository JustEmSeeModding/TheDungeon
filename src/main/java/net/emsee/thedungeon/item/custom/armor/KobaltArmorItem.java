package net.emsee.thedungeon.item.custom.armor;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.item.custom.DungeonArmorItem;
import net.emsee.thedungeon.mobEffect.ModMobEffects;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.LinkedHashMap;

public class KobaltArmorItem extends DungeonArmorItem {
    private final double aggroBoost = -5;


    public KobaltArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return super.getDefaultAttributeModifiers()
                .withModifierAdded(ModAttributes.PLAYER_DUNGEON_AGGRO_TO_ENEMY, new AttributeModifier(TheDungeon.defaultResourceLocation("infused_alloy.aggro." + type.getName()), aggroBoost, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(type.getSlot()));
    }

    @Override
    public LinkedHashMap<SlotType, Component[]> getExtraComponents() {
        return Util.make(new LinkedHashMap<>(), map -> {
            map.put(SlotType.FULL_BODY, new Component[]{ Component.translatable("attribute.thedungeon.extra.hob_goblin_friendly").withStyle(POSITIVE_FORMATTING)});
        });
    }

    @Override
    protected void onFullSetTick(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(ModMobEffects.HOB_GOBLIN_TRADEABLE, 5, 1));
    }
}
