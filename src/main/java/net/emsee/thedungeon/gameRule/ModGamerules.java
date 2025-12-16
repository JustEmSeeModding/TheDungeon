package net.emsee.thedungeon.gameRule;

import net.minecraft.world.level.GameRules.Category;

public final class ModGamerules {
    public final static String MANUAL_STEPPING = "dungeonManualGenerationStepping";


    public static void registerRules() {
        GameruleRegistry.register(MANUAL_STEPPING, Category.UPDATES, false);
    }
}
