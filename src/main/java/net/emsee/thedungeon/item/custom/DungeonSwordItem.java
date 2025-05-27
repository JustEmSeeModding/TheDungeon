package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.emsee.thedungeon.item.interfaces.IDungeonToolTips;
import net.emsee.thedungeon.item.interfaces.IDungeonWeapon;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DungeonSwordItem extends SwordItem implements IDungeonCarryItem, IDungeonToolTips, IDungeonWeapon {
    public DungeonSwordItem(Tier tier, Properties properties) {
        super(tier, properties.rarity(Rarity.RARE));
    }

    public DungeonSwordItem(Tier p_tier, Item.Properties p_properties, Tool toolComponentData) {
        super(p_tier, p_properties.rarity(Rarity.RARE), toolComponentData);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(DungeonItem.DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }


    public static @NotNull ItemAttributeModifiers createAttributes(Tier tier, int attackDamage, float attackSpeed) {
        return createAttributes(tier, (float)attackDamage, attackSpeed);
    }

    public static @NotNull ItemAttributeModifiers createAttributes(Tier tier, float attackDamage , float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (attackDamage + tier.getAttackDamageBonus()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    /*@Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof DungeonMob dungeonMob) {
             if (attacker.getAttributeValue(ModAttributes.DUNGEON_ATTACK_DAMAGE) > 0) {
                 float multiplier = 1;
                 if (attacker instanceof Player player) {
                     multiplier *= .2f + player.getAttackStrengthScale(.5f) * player.getAttackStrengthScale(.5f) *.8f;
                 }
                 float damageAmount = ((float) attacker.getAttribute(ModAttributes.DUNGEON_ATTACK_DAMAGE).getValue()) * multiplier;
                dungeonMob.hurt(attacker.damageSources().source(ModDamageTypes.DUNGEON_WEAPON, attacker), damageAmount);
            }
            return false;
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        return super.onLeftClickEntity(stack, player, entity);
    }*/
}


