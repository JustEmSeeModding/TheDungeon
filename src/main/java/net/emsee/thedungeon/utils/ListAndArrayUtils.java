package net.emsee.thedungeon.utils;

import net.minecraft.util.RandomSource;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class ListAndArrayUtils {

    /**
     * gets a random value from the list
     */
    public static <T> T getRandomFromList(List<T> list, Random random) {
        if (list.isEmpty()) return null;
        return list.get((int)Math.round(random.nextDouble()*(list.size()-1)));
    }

    /**
     * gets a random value from the list
     */
    public static <T> T getRandomFromList(List<T> list, RandomSource random) {
        if (list.isEmpty()) return null;
        return list.get((int)Math.round(random.nextDouble()*(list.size()-1)));
    }

    public static <T, M> boolean mapEquals(Map<T, M> mapOne, Map<T, M> mapTwo) {
        // Quick check for same reference or size mismatch
        if (mapOne == mapTwo) return true;
        if (mapOne.size() != mapTwo.size()) return false;

        // Check all keys in mapOne and compare values safely
        for (T key : mapOne.keySet()) {
            if (!mapTwo.containsKey(key)) return false;
            if (!Objects.equals(mapOne.get(key), mapTwo.get(key))) return false;
        }
        return true;
    }

    public static <T,M> String mapToString(Map<T,M> map) {
        StringBuilder toReturn = new StringBuilder("{");
        for (T t : map.keySet()) {
            toReturn.append("[").append(t).append("-=-").append(map.get(t)).append("],");
        }
        toReturn.append("}");
        return toReturn.toString();
    }

    public static <T> boolean listEquals(List<T> listOne, List<T> listTwo) {
        boolean equals = true;
        List<T> tempListTwo = new ArrayList<>(listTwo);
        for (T t : listOne) {
            if (!tempListTwo.contains(t)) return false;
            tempListTwo.remove(t);
        }
        return tempListTwo.isEmpty();
    }

    /**
     * removes the old key and assigned its value to the new one
     */
    public static <K,V> Map<K,V> replaceKey(Map<K,V> map, K key, K newKey) {
        if (!map.containsKey(key)) throw new IllegalStateException("map does not contain key");
        V value = map.get(key);
        map.remove(key);
        map.put(newKey, value);
        return map;
    }

    /**
     * a foreach method for maps that makes sure the value stays assigned even if the hash changes
     */
    public static <K,V> Map<K,V> mapForEachSafe(Map<K,V> map, BiConsumer<K,V> consumer) {
        List<Map.Entry<K,V>> entries = new ArrayList<>(map.entrySet());
        map.clear();

        for (Map.Entry<K,V> entry : entries) {
            consumer.accept(entry.getKey(), entry.getValue());
            map.put(entry.getKey(), entry.getValue());
        }

        return map;
    }

    /**
     * a for method for maps that makes sure the value stays assigned even if the hash changes
     */
    public static <K, V> Map<K, V> mapForSafe(
            Map<K, V> map,
            K oldKey,
            Function<K, K> keyTransformer
    ) {
        if (!map.containsKey(oldKey)) {
            throw new IllegalStateException("map does not contain key");
        }

        K newKey = keyTransformer.apply(oldKey);
        replaceKey(map, oldKey, newKey);
        return map;
    }
}
