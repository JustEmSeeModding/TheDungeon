package net.emsee.thedungeon.gameRule;

import net.minecraft.world.level.GameRules.Category;

public final class ModGamerules {
    public final static String MANUAL_STEPPING = "dungeonManualGenerationStepping";
    public final static String AUTO_DUNGEON_CYCLING = "dungeonAutoCycling";
    public final static String CALCULATOR_STEPS_PER_TICK = "dungeonCalculatorStepsPerTick";
    public final static String PLACER_STEPS_PER_TICK = "dungeonPlacerStepsPerTick";
    public final static String POST_PROCESSOR_STEPS_PER_TICK = "dungeonPostProcessorStepsPerTick";
    public final static String SPAWNER_STEPS_PER_TICK = "dungeonSpawnerStepsPerTick";
    public final static String DUNGEON_SEED_OVERRIDE = "dungeonSeedOverride";
    public final static String DUNGEON_KILL_ON_REGEN = "dungeonKillOnRegenerate";
    public final static String DUNGEON_CLEAN_ON_REGEN = "dungeonCleanupOnRegenerate";
    public static final String TICKS_BETWEEN_COLLAPSES = "dungeonTicksBetweenCollapse";


    public static void registerRules() {
        GameruleRegistry.register(MANUAL_STEPPING, Category.UPDATES, false);
        GameruleRegistry.register(AUTO_DUNGEON_CYCLING, Category.UPDATES, true);

        GameruleRegistry.register(CALCULATOR_STEPS_PER_TICK, Category.UPDATES, 400);
        GameruleRegistry.register(PLACER_STEPS_PER_TICK, Category.UPDATES, 1);
        GameruleRegistry.register(POST_PROCESSOR_STEPS_PER_TICK, Category.UPDATES, 1);
        GameruleRegistry.register(SPAWNER_STEPS_PER_TICK, Category.UPDATES, 10);
        GameruleRegistry.register(TICKS_BETWEEN_COLLAPSES, Category.UPDATES, 10/*minutes*/*60*20);

        GameruleRegistry.register(DUNGEON_SEED_OVERRIDE, Category.UPDATES, -1);
        GameruleRegistry.register(DUNGEON_KILL_ON_REGEN, Category.UPDATES, true);
        GameruleRegistry.register(DUNGEON_CLEAN_ON_REGEN, Category.UPDATES, true);
    }
}
