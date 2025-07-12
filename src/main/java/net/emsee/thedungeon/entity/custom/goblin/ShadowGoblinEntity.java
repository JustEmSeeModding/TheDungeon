package net.emsee.thedungeon.entity.custom.goblin;

import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class ShadowGoblinEntity extends AbstractGoblinEntity {
    public ShadowGoblinEntity(EntityType<? extends DungeonPathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.MAX_HEALTH, 15)
                .add(Attributes.ARMOR, 3)
                .add(Attributes.ATTACK_DAMAGE,2)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0)
                .add(Attributes.MOVEMENT_SPEED, .29)
                .add(Attributes.KNOCKBACK_RESISTANCE, .1)
                .add(Attributes.STEP_HEIGHT, 1)
                .add(ModAttributes.DUNGEON_MOB_REACH, 2.5)
                .add(ModAttributes.DUNGEON_MOB_MIN_PERCEPTION, 400)
                .add(ModAttributes.DUNGEON_MOB_MAX_PERCEPTION, 700);
    }
}
