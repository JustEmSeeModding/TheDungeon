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
    boolean ignoreLOS = false;
    boolean ignoreVisibilityCheck = false;

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

    public DungeonTargetSelectorGoal ignoreLOS() {
        ignoreLOS = true;
        return this;
    }

    public DungeonTargetSelectorGoal ignoreVisibilityCheck() {
        ignoreVisibilityCheck = true;
        return this;
    }

    @Override
    protected void findTarget() {
        double maxRange = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        double minPerception = mob.getAttributeValue(ModAttributes.DUNGEON_MOB_MIN_PERCEPTION);
        double maxPerception = mob.getAttributeValue(ModAttributes.DUNGEON_MOB_MAX_PERCEPTION);

        List<Entity> nearEntities = mob.level().getEntities(mob, mob.getBoundingBox().inflate(maxRange+2));

        // creates a weighted map of all players using aggro
        WeightedMap.Dbl<Player> nearPlayers = new WeightedMap.Dbl<>();
        double totalAggro = 0;
        for (Entity entity : nearEntities) {
            if (entity instanceof Player player && !player.isCreative()) {
                double playerAggro = getPlayerAggro(player, minPerception, maxPerception);
                if (playerAggro>0&&
                        checkPredicates(player) &&
                        hasLineOfSight(player))
                    nearPlayers.put(player, playerAggro);
            }
        }

        this.target = nearPlayers.getRandom(mob.level().getRandom());
    }

    protected double getPlayerAggro(Player player, double minPerception, double maxPerception) {
        double playerAggro = player.getAttributeValue(ModAttributes.PLAYER_DUNGEON_AGGRO_TO_ENEMY);
        double maxRange = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        playerAggro *= ((maxRange - mob.distanceTo(player))/maxRange);
        playerAggro *= getPlayerVisibilityPercent(player);

        double perceptionMultiplier = 1;
        if (playerAggro >= maxPerception) perceptionMultiplier *=2;
        else if (playerAggro < minPerception/10) perceptionMultiplier = 0;
        else if (playerAggro < minPerception) perceptionMultiplier /=2;

        playerAggro*=perceptionMultiplier;
        return playerAggro;
    }

    private double getPlayerVisibilityPercent(Player player) {
        if (ignoreVisibilityCheck) return 1;
        return player.getVisibilityPercent(mob);
    }

    private boolean checkPredicates(Player player) {
        return selectionPredicate.test(player);
    }

    private boolean hasLineOfSight(Player player) {
        return ignoreLOS || mob.getSensing().hasLineOfSight(player);
    }
}
