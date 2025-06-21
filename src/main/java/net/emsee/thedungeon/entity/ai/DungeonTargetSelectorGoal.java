package net.emsee.thedungeon.entity.ai;

import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class DungeonTargetSelectorGoal extends NearestAttackableTargetGoal<Player> {
    // must all return true for the player to be considered "valid"
    Predicate<Player> selectionPredicate = p -> true;

    public DungeonTargetSelectorGoal(Mob mob, boolean mustSee) {
        super(mob, Player.class, mustSee);

    }

    public DungeonTargetSelectorGoal(Mob mob, boolean mustSee, Predicate<LivingEntity> targetPredicate) {
        super(mob, Player.class, mustSee, targetPredicate);
    }

    public DungeonTargetSelectorGoal(Mob mob, boolean mustSee, boolean mustReach) {
        super(mob, Player.class, mustSee, mustReach);
    }

    public DungeonTargetSelectorGoal(Mob mob, int randomInterval, boolean mustSee, boolean mustReach, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(mob, Player.class, randomInterval, mustSee, mustReach, targetPredicate);
    }

    public DungeonTargetSelectorGoal addPredicate(Predicate<Player> predicate) {
        selectionPredicate = selectionPredicate.and(predicate);
        return this;
    }

    @Override
    protected void findTarget() {
        double maxRange = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        double minPerception = mob.getAttributeValue(ModAttributes.MIN_PERCEPTION);
        double maxPerception = mob.getAttributeValue(ModAttributes.MAX_PERCEPTION);

        List<Entity> nearEntities = mob.level().getEntities(mob, mob.getBoundingBox().inflate(maxRange));

        // creates a weighted map of all players using aggro
        WeightedMap.Dbl<Player> nearPlayers = new WeightedMap.Dbl<>();
        double totalAggro = 0;
        for (Entity entity : nearEntities) {
            if (entity instanceof Player player && !player.isCreative()) {
                double playerAggro = getPlayerAggro(player, minPerception, maxPerception);
                if (checkPredicates(player))
                    nearPlayers.put(player, playerAggro);
            }
        }

        this.target = nearPlayers.getRandom(mob.level().getRandom());
    }

    protected double getPlayerAggro(Player player, double minPerception, double maxPerception) {
        double playerAggro = player.getAttributeValue(ModAttributes.DUNGEON_AGGRO_TO_ENEMY);
        double maxRange = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        double multiplier = ((maxRange - mob.distanceTo(player))/maxRange);
        if (playerAggro >= maxPerception) multiplier *=2;
        else if (playerAggro < minPerception) multiplier /=2;
        return playerAggro * multiplier;
    }

    private boolean checkPredicates(Player player) {
        return selectionPredicate.test(player);
    }
}
