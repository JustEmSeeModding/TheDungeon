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
        map.put(DebugType.INSTANCE_SETUP,true);
        map.put(DebugType.SAVE_DATA,true);
        map.put(DebugType.SAVE_DATA_DETAILED,false);
        map.put(DebugType.GENERATING_STEPS,true);
        map.put(DebugType.GENERATING_TICKS,false);
        map.put(DebugType.GENERATING_TICKS_DETAILS,false);
        map.put(DebugType.GENERATING_REQUIRED_PLACEMENTS,false);
        map.put(DebugType.FORCED_CHUNK_UPDATES,true);
        map.put(DebugType.FORCED_CHUNK_UPDATES_DETAILS,true);
        map.put(DebugType.GENERIC,true);
        map.put(DebugType.WARNINGS,true);
        map.put(DebugType.IMPORTANT,true);
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

    public static void logInfo(DebugType DebugType, String msg) {
        if (isDebugMode(DebugType)) LOGGER.info(msg);
    }

    public static void logInfo(DebugType DebugType, String msg, Object... objects) {
        if (isDebugMode(DebugType)) LOGGER.info(msg, objects);
    }

    public static void logInfo(DebugType DebugType, Supplier<String> msg) {
        if (isDebugMode(DebugType)) LOGGER.info(msg.get());
    }


    public static void logWarn(DebugType DebugType, String msg) {
        if (isDebugMode(DebugType)) LOGGER.warn(msg);
    }

    public static void logWarn(DebugType DebugType, Supplier<String> msg) {
        if (isDebugMode(DebugType)) LOGGER.warn(msg.get());
    }

    public static void logWarn(DebugType DebugType, String msg, Object... objects) {
        if (isDebugMode(DebugType)) LOGGER.warn(msg, objects);
    }

    public static void logError(DebugType DebugType, String msg) {
        if (isDebugMode(DebugType)) LOGGER.error(msg);
    }

    public static void logError(DebugType DebugType, Supplier<String> msg) {
        if (isDebugMode(DebugType)) LOGGER.error(msg.get());
    }

    public static void logError(DebugType DebugType, String msg, Object... objects) {
        if (isDebugMode(DebugType)) LOGGER.error(msg, objects);
    }

    private static boolean isDebugMode(DebugType DebugType) {
        return ENABLED && DebugTypes.containsKey(DebugType) && DebugTypes.get(DebugType);
    }
}