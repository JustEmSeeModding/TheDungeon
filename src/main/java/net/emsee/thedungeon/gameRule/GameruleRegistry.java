package net.emsee.thedungeon.gameRule;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A collection of utilities for interacting with Minecraft's gamerules.
 */
public final class GameruleRegistry {
    /**
     * A map containing the gamerule keys and their IDs.
     */
    private static final Map<String, Key<?>> ruleIDMap = new HashMap<>();

    /**
     * Register a new gamerule.
     *
     * @param name         The name of the gamerule to register. Must be unique.
     * @param category     The category to put the gamerule under.
     * @param defaultValue The value to set the gamerule to by default.
     * @return A key that can be used to access the gamerule.
     */
    private static <T extends Value<T>> Key<T> register(String name, Category category, Type<T> defaultValue) {
        var key = GameRules.register(name, category, defaultValue);
        ruleIDMap.put(name, key);
        return key;
    }

    /**
     * Register a new gamerule.
     *
     * @param name         The name of the gamerule to register. Must be unique.
     * @param category     The category to put the gamerule under.
     * @param defaultValue The value to set the gamerule to by default.
     * @return A key that can be used to access the gamerule.
     */
    public static Key<BooleanValue> register(String name, Category category, boolean defaultValue) {
        return register(name, category, BooleanValue.create(defaultValue));
    }

    /**
     * Register a new gamerule.
     *
     * @param name         The name of the gamerule to register. Must be unique.
     * @param category     The category to put the gamerule under.
     * @param defaultValue The value to set the gamerule to by default.
     * @return A key that can be used to access the gamerule.
     */
    public static Key<IntegerValue> register(String name, Category category, int defaultValue) {
        return register(name, category, IntegerValue.create(defaultValue));
    }

    /**
     * Get the value associated with a gamerule.
     *
     * @param <T>   The type of the value held in the gamerule.
     * @param server The server to retrieve the gamerule value from.
     * @param key   The key to retrieve the gamerule for.
     * @return The value wrapper object for the gamerule.
     */
    public static <T extends Value<T>> T getGamerule(MinecraftServer server, Key<T> key) {
        return server.getGameRules().getRule(key);
    }

    /**
     * Get the value associated with a gamerule.
     *
     * @param <T>   The type of the value held in the gamerule.
     * @param server The server to retrieve the gamerule value from.
     * @param id    The id to retrieve the gamerule for.
     * @return The value wrapper object for the gamerule.
     */
    public static <T extends Value<T>> T getGamerule(MinecraftServer server, String id) {
        @SuppressWarnings("unchecked")
        var key = (Key<T>) ruleIDMap.get(id);
        return server.getGameRules().getRule(key);
    }

    /**
     * Get the boolean value associated with a gamerule.
     *
     * @param server The server to retrieve the gamerule value from.
     * @param key   The key to retrieve the gamerule for.
     * @return The boolean value for the gamerule.
     */
    public static boolean getBooleanGamerule(MinecraftServer server, Key<BooleanValue> key) {
        return getGamerule(server, key).get();
    }

    /**
     * Get the boolean value associated with a gamerule.
     *
     * @param server The server to retrieve the gamerule value from.
     * @param id    The id to retrieve the gamerule for.
     * @return The boolean value for the gamerule.
     */
    public static boolean getBooleanGamerule(MinecraftServer server, String id) {
        return GameruleRegistry.<BooleanValue>getGamerule(server, id).get();
    }

    /**
     * Get the integer value associated with a gamerule.
     *
     * @param server The server to retrieve the gamerule value from.
     * @param key   The key to retrieve the gamerule for.
     * @return The integer value for the gamerule.
     */
    public static int getIntegerGamerule(MinecraftServer server, Key<IntegerValue> key) {
        return getGamerule(server, key).get();
    }

    /**
     * Get the integer value associated with a gamerule.
     *
     * @param server The server to retrieve the gamerule value from.
     * @param id    The id to retrieve the gamerule for.
     * @return The integer value for the gamerule.
     */
    public static int getIntegerGamerule(MinecraftServer server, String id) {
        return GameruleRegistry.<IntegerValue>getGamerule(server, id).get();
    }
}