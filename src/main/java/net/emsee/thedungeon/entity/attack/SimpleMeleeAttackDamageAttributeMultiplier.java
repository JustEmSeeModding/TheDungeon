package net.emsee.thedungeon.entity.attack;


import net.emsee.thedungeon.entity.custom.abstracts.DungeonAnimatedMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * uses the mob's attack damage attribute as part of the damage calculation, allowing for more dynamic damage based on the mob's stats
 */
public class SimpleMeleeAttackDamageAttributeMultiplier<E extends DungeonAnimatedMob> extends SimpleMeleeAttack<E> {
    public SimpleMeleeAttackDamageAttributeMultiplier(E entity, float damageMultiplier, int animationID, int duration, int cooldown, int hurtDelay, AttackHand hands) {
        super(entity,damageMultiplier, animationID, duration, cooldown, hurtDelay, hands);
    }

    public SimpleMeleeAttackDamageAttributeMultiplier(E entity, float damageMultiplier, int animationID, int duration, int cooldown, int hurtDelay, AttackHand hands, HandPredicate mainHandPredicate, HandPredicate offHandPredicate) {
        super(entity,damageMultiplier, animationID, duration, cooldown, hurtDelay, hands, mainHandPredicate, offHandPredicate);
    }

    @Override
    protected void applyDamage(LivingEntity target) {
        target.hurt(entity.damageSources().mobAttack(entity), damage * (float)entity.getAttributeValue(Attributes.ATTACK_DAMAGE));
    }
}
