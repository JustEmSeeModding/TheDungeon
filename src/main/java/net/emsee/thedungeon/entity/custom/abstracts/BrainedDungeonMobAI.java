package net.emsee.thedungeon.entity.custom.abstracts;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;

public abstract class BrainedDungeonMobAI {
    protected abstract Brain<BrainedDungeonMob<?>> makeBrain(Brain<BrainedDungeonMob<?>> brain);

    public void onHitTarget(BrainedDungeonMob<?> entity, LivingEntity target) {

    }

    protected void wasHurtBy(BrainedDungeonMob<?> entity, LivingEntity livingEntity) {

    }
}
