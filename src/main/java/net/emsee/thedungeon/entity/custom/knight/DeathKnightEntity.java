package net.emsee.thedungeon.entity.custom.knight;

import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class DeathKnightEntity extends AbstractKnightEntity{
    public DeathKnightEntity(EntityType<? extends DungeonPathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.ARMOR, 18)
                .add(Attributes.ATTACK_DAMAGE,4)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0)
                .add(Attributes.MOVEMENT_SPEED, .22)
                .add(Attributes.KNOCKBACK_RESISTANCE, .5)
                .add(Attributes.STEP_HEIGHT, 1)
                .add(ModAttributes.DUNGEON_MOB_REACH, 3.5)
                .add(ModAttributes.DUNGEON_MOB_MIN_PERCEPTION, 150)
                .add(ModAttributes.DUNGEON_MOB_MAX_PERCEPTION, 550);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_SWORD));
    }
}
