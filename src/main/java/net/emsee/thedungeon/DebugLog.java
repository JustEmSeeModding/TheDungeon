package net.emsee.thedungeon;

import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DebugLog {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Boolean ENABLED = true;

    private static final Map<DebugType, Boolean> DebugTypes = Util.make(new HashMap<>(), (map) -> {
        map.put(DebugType.INSTANCE_SETUP, true);
        map.put(DebugType.SAVE_DATA, true);
        map.put(DebugType.SAVE_DATA_DETAILED, true);
        map.put(DebugType.GENERATING_STEPS, true);
        map.put(DebugType.GENERATING_TICKS, false);
        map.put(DebugType.GENERATING_TICKS_DETAILS, false);
        map.put(DebugType.GENERATING_REQUIRED_PLACEMENTS, false);
        map.put(DebugType.FORCED_CHUNK_UPDATES, true);
        map.put(DebugType.FORCED_CHUNK_UPDATES_DETAILS, false);
        map.put(DebugType.GENERIC, true);
        map.put(DebugType.WARNINGS, true);
        map.put(DebugType.IMPORTANT, true);
    });

    public enum DebugType {
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

    public static void logInfo(DebugType debugType, String msg) {
        if (isDebugMode(debugType)) LOGGER.info(msg);
    }

    public static void logInfo(DebugType debugType, String msg, Object... objects) {
        if (isDebugMode(debugType)) LOGGER.info(msg, objects);
    }

    public static void logInfo(DebugType debugType, Supplier<String> msg) {
        if (isDebugMode(debugType)) LOGGER.info(msg.get());
    }


    public static void logWarn(DebugType debugType, String msg) {
        if (isDebugMode(debugType)) LOGGER.warn(msg);
    }

    public static void logWarn(DebugType debugType, Supplier<String> msg) {
        if (isDebugMode(debugType)) LOGGER.warn(msg.get());
    }

    public static void logWarn(DebugType debugType, String msg, Object... objects) {
        if (isDebugMode(debugType)) LOGGER.warn(msg, objects);
    }

    public static void logError(DebugType debugType, String msg) {
        if (isDebugMode(debugType)) LOGGER.error(msg);
    }

    public static void logError(DebugType debugType, Supplier<String> msg) {
        if (isDebugMode(debugType)) LOGGER.error(msg.get());
    }

    public static void logError(DebugType debugType, String msg, Object... objects) {
        if (isDebugMode(debugType)) LOGGER.error(msg, objects);
    }

    private static boolean isDebugMode(DebugType debugType) {
        return ENABLED && DebugTypes.containsKey(debugType) && DebugTypes.get(debugType);
    }
}