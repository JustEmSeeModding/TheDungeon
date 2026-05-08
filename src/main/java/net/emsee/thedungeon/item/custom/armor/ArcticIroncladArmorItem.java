package net.emsee.thedungeon.item.custom.armor;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.dungeonClass.ModClasses;
import net.emsee.thedungeon.item.DungeonItemRank;
import net.emsee.thedungeon.item.ModArmorMaterials;
import net.emsee.thedungeon.item.custom.armor.client.model.ArcticIroncladArmorModel;
import net.emsee.thedungeon.item.custom.armor.client.provider.IArmorModelProvider;
import net.emsee.thedungeon.item.custom.armor.client.provider.SimpleModelProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.LinkedHashMap;

public class ArcticIroncladArmorItem extends ModeledDungeonArmorItem{
    private static final double speedPenalty = -.002d;
    private static final double aggroBoost = 30;
    private static final double setHealthBonus = 2;


    public ArcticIroncladArmorItem(Type type, Properties properties) {
        super(ModArmorMaterials.ARCTIC_IRONCLAD, type, properties, DungeonItemRank.F, new DeferredHolder[]{ModClasses.TANK}, new DeferredHolder[]{});
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return super.getDefaultAttributeModifiers()
                .withModifierAdded(Attributes.MOVEMENT_SPEED, new AttributeModifier(TheDungeon.defaultResourceLocation("infused_alloy.speed." + type.getName()), speedPenalty, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(type.getSlot()))
                .withModifierAdded(ModAttributes.PLAYER_DUNGEON_AGGRO_TO_ENEMY, new AttributeModifier(TheDungeon.defaultResourceLocation("infused_alloy.aggro." + type.getName()), aggroBoost, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(type.getSlot()))
                .withModifierAdded(Attributes.MAX_HEALTH, new AttributeModifier(TheDungeon.defaultResourceLocation("infused_alloy.max_health_boost"), setHealthBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(EquipmentSlot.BODY));
    }

    @Override
    public LinkedHashMap<SlotType, Component[]> getExtraComponents(ItemStack stack) {
        LinkedHashMap<SlotType, Component[]> map = super.getExtraComponents(stack);
        map.put(SlotType.FULL_BODY, new Component[]{Component.translatable("attribute.thedungeon.extra.slow_bonus_on_hit").withStyle(POSITIVE_FORMATTING)});
        return map;
    }

    @Override
    public IArmorModelProvider createModelProvider() {
        return new SimpleModelProvider(ArcticIroncladArmorModel::createBodyLayer, ArcticIroncladArmorModel::new);
    }

    @Override
    public ResourceLocation createTextureLocation() {
        return makeCustomTextureLocation("arctic_ironclad");
    }

    @Override
    protected void onPostWearerDamaged(LivingDamageEvent.Post event) {
        if (event.getEntity() instanceof Player player)
            if (playerHasFullArmorOnOnlyTriggerChest(player) && event.getSource().getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));
            }
    }
}
