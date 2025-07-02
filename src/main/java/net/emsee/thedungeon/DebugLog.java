package net.emsee.thedungeon;

import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Supplier;

public class DebugLog {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final List<DebugLevel> debugLevels = Util.make(Lists.newArrayList(), (list) -> {
        list.add(DebugLevel.INSTANCE_SETUP);
        list.add(DebugLevel.SAVE_DATA);
        //list.add(DebugLevel.SAVE_DATA_DETAILED);
        list.add(DebugLevel.GENERATING_STEPS);
        //list.add(DebugLevel.GENERATING_TICKS);
        //list.add(DebugLevel.GENERATING_TICKS_DETAILS);
        //list.add(DebugLevel.GENERATING_REQUIRED_PLACEMENTS);
        list.add(DebugLevel.FORCED_CHUNK_UPDATES);
        list.add(DebugLevel.FORCED_CHUNK_UPDATES_DETAILS);
        list.add(DebugLevel.GENERIC);
        list.add(DebugLevel.WARNINGS);
        list.add(DebugLevel.IMPORTANT);
    });

    public enum DebugLevel {
        INSTANCE_SETUP,
        SAVE_DATA,
        SAVE_DATA_DETAILED,
        GENERATING_STEPS,
        GENERATING_TICKS,
        GENERATING_REQUIRED_PLACEMENTS,
        GENERATING_TICKS_DETAILS,
        FORCED_CHUNK_UPDATES,
        FORCED_CHUNK_UPDATES_DETAILS,
        GENERIC,
        WARNINGS,
        IMPORTANT
    }

    public static void logInfo(DebugLevel debugLevel, String msg) {
        if (isDebugMode(debugLevel)) LOGGER.info(msg);
    }

    public static void logInfo(DebugLevel debugLevel, String msg, Object... objects) {
        if (isDebugMode(debugLevel)) LOGGER.info(msg, objects);
    }

    public static void logInfo(DebugLevel debugLevel, Supplier<String> msg) {
        if (isDebugMode(debugLevel)) LOGGER.info(msg.get());
    }


    public static void logWarn(DebugLevel debugLevel, String msg) {
        if (isDebugMode(debugLevel)) LOGGER.warn(msg);
    }

    public static void logWarn(DebugLevel debugLevel, Supplier<String> msg) {
        if (isDebugMode(debugLevel)) LOGGER.warn(msg.get());
    }

    public static void logWarn(DebugLevel debugLevel, String msg, Object... objects) {
        if (isDebugMode(debugLevel)) LOGGER.warn(msg, objects);
    }

    public static void logError(DebugLevel debugLevel, String msg) {
        if (isDebugMode(debugLevel)) LOGGER.error(msg);
    }

    public static void logError(DebugLevel debugLevel, Supplier<String> msg) {
        if (isDebugMode(debugLevel)) LOGGER.error(msg.get());
    }

    public static void logError(DebugLevel debugLevel, String msg, Object... objects) {
        if (isDebugMode(debugLevel)) LOGGER.error(msg, objects);
    }

    private static boolean isDebugMode(DebugLevel level) {
        return debugLevels.contains(level);
    }
}