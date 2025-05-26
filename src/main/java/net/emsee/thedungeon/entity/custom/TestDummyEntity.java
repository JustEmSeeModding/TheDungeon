package net.emsee.thedungeon.entity.custom;

import net.emsee.thedungeon.damageType.ModDamageTypes;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.emsee.thedungeon.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;


public class TestDummyEntity extends DungeonPathfinderMob {
    public TestDummyEntity(EntityType<? extends DungeonPathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1)
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.STEP_HEIGHT, 0);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        super.hurt(source, amount);
        if (source.getEntity() != null
                && !source.getEntity().level().isClientSide
                && !super.isInvulnerableTo(source)
                && source.getEntity() instanceof Player player
                && !player.isCrouching()
        ) {
            player.sendSystemMessage(Component.literal("Damage: " + amount));
        }
        if (source.isCreativePlayer()) {
            return true;
        }
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (
                source.is(ModDamageTypes.DUNGEON_RESET) ||
                        source.is(DamageTypes.FELL_OUT_OF_WORLD) ||
                        source.is(DamageTypes.GENERIC_KILL)
        )
            return false;
        if (source.getEntity() instanceof Player player) {
            if(player.isCrouching()) return false;
        }
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.isCrouching()) {
            kill();
            player.getInventory().placeItemBackInInventory(new ItemStack(ModItems.TEST_DUMMY.get()));
        }
        return super.mobInteract(player, hand);
    }
}
