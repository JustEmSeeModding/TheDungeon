package net.emsee.thedungeon.entity.ai;

import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

/** when mob is far enough away it wil run at its target*/
public class DungeonRunToTargetGoal extends DungeonWalkToTargetGoal {
    public DungeonRunToTargetGoal(DungeonPathfinderMob mob, double speedModifier, float startDistance, float stopDistance, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, startDistance, stopDistance, followingTargetEvenIfNotSeen);
    }

    @Override
    public void start() {
        super.start();
        this.mob.setRunning(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setRunning(false);
    }
}

