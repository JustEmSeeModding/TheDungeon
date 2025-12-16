package net.emsee.thedungeon;

import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameRules;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue AUTO_DUNGEON_CYCLING = BUILDER
            .comment("Automatically cycle new dungeons")
            .define("dungeon_auto_cycling", true);

    public static final ModConfigSpec.LongValue TICKS_BETWEEN_COLLAPSES = BUILDER
            .comment("The amount of ticks between every dungeon collapse")
            .defineInRange("dungeon_ticks_between_collapse", 10L/*minutes*/*60*20, 1, Long.MAX_VALUE);

    public static final ModConfigSpec.IntValue CALCULATOR_STEPS_PER_TICK = BUILDER
            .comment("the amount of steps the calculator takes every tick")
            .defineInRange("calculator_steps_per_tick", 400, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue PLACER_STEPS_PER_TICK = BUILDER
            .comment("the amount of steps the placer takes every tick")
            .defineInRange("placer_steps_per_tick", 1, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue POST_PROCESSOR_STEPS_PER_TICK = BUILDER
            .comment("the amount of steps the post processor takes every tick")
            .defineInRange("post_process_steps_per_tick", 1, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue SPAWNER_STEPS_PER_TICK = BUILDER
            .comment("the amount of steps the spawner takes every tick")
            .defineInRange("spawner_steps_per_tick", 15, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.BooleanValue OVERRIDE_DUNGEON_SEED = BUILDER
            .comment("Override the used seed")
            .define("dungeon_do_seed_override", false);

    public static final ModConfigSpec.LongValue DUNGEON_SEED_OVERRIDE = BUILDER
            .comment("The seed to override to")
            .defineInRange("dungeon_seed_override", 0L, Long.MIN_VALUE, Long.MAX_VALUE);


    public static final ModConfigSpec.BooleanValue DUNGEON_KILL_ON_REGEN = BUILDER
            .comment("Kill entities on regeneration of dungeon")
            .define("dungeon_kill_on_regenerate", true);

    public static final ModConfigSpec.BooleanValue DUNGEON_CLEAN_ON_REGEN = BUILDER
            .comment("Generate cleanup before every generation")
            .define("dungeon_cleanup_on_regenerate", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
