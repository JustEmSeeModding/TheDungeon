package net.emsee.thedungeon;

import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DebugLog {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<DebugLevel, Boolean> debugLevels = Util.make(new HashMap<>(), (map) -> {
        map.put(DebugLevel.INSTANCE_SETUP,true);
        map.put(DebugLevel.SAVE_DATA,true);
        map.put(DebugLevel.SAVE_DATA_DETAILED,false);
        map.put(DebugLevel.GENERATING_STEPS,true);
        map.put(DebugLevel.GENERATING_TICKS,false);
        map.put(DebugLevel.GENERATING_TICKS_DETAILS,false);
        map.put(DebugLevel.GENERATING_REQUIRED_PLACEMENTS,false);
        map.put(DebugLevel.FORCED_CHUNK_UPDATES,true);
        map.put(DebugLevel.FORCED_CHUNK_UPDATES_DETAILS,true);
        map.put(DebugLevel.GENERIC,true);
        map.put(DebugLevel.WARNINGS,true);
        map.put(DebugLevel.IMPORTANT,true);
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
        return debugLevels.get(level);
    }
}