package net.emsee.thedungeon.utils;

import net.emsee.thedungeon.dungeon.mobSpawnRules.MobSpawnRules;
import net.minecraft.util.RandomSource;

import java.util.*;

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

    /**
     * gets a random value from the map using a weighted function
     */
    public static <T> T getRandomFromWeightedMapI(Map<T,Integer> map, Random random) {
        int totalWeight = 0;
        for (T t: map.keySet()) {
            totalWeight += map.get(t);
        }
        if (totalWeight <= 0) return null;
        int chosenWeight = (int)Math.ceil(random.nextDouble()*(totalWeight));

        for (T t: map.keySet()) {
            chosenWeight -= map.get(t);
            if (chosenWeight<=0) return t;
        }
        return null;
    }

    /**
     * gets a random value from the map using a weighted function
     */
    public static <T> T getRandomFromWeightedMapI(Map<T,Integer> map, RandomSource random) {
        int totalWeight = 0;
        for (T t: map.keySet()) {
            totalWeight += map.get(t);
        }
        if (totalWeight <= 0) return null;
        int chosenWeight = (int)Math.ceil(random.nextDouble()*(totalWeight));

        for (T t: map.keySet()) {
            chosenWeight -= map.get(t);
            if (chosenWeight<=0) return t;
        }
        return null;
    }

    /**
     * gets a random value from the map using a weighted function
     */
    public static <T> T getRandomFromWeightedMapD(Map<T,Double> map, Random random) {
        double totalWeight = 0;
        for (T t: map.keySet()) {
            totalWeight += map.get(t);
        }
        if (totalWeight <= 0) return null;
        double chosenWeight = random.nextDouble()*(totalWeight);

        for (T t: map.keySet()) {
            chosenWeight -= map.get(t);
            if (chosenWeight<=0) return t;
        }
        return null;
    }

    /**
     * gets a random value from the map using a weighted function
     */
    public static <T> T getRandomFromWeightedMapD(Map<T,Double> map, RandomSource random) {
        double totalWeight = 0;
        for (T t: map.keySet()) {
            totalWeight += map.get(t);
        }
        if (totalWeight <= 0) return null;
        double chosenWeight = random.nextDouble()*(totalWeight);

        for (T t: map.keySet()) {
            chosenWeight -= map.get(t);
            if (chosenWeight<=0) return t;
        }
        return null;
    }

    /**
     * gets a random value skipping all with lower priorities
     */
    public static <T> T getRandomFromPriorityMap(Map<T,Integer> map, Random random, int minimumPriority) {
        int highest = minimumPriority;
        List<T> tempList = new ArrayList<>();
        for (T t: map.keySet()) {
            if (map.get(t)==highest)
                tempList.add(t);
            else if (map.get(t)>highest) {
                highest=map.get(t);
                tempList = new ArrayList<>();
                tempList.add(t);
            }
        }
        return getRandomFromList(tempList, random);
    }

    /**
     * gets a random value skipping all with lower priorities
     */
    public static <T> T getRandomFromPriorityMap(Map<T,Integer> map, RandomSource random, int minimumPriority) {
        int highest = minimumPriority;
        List<T> tempList = new ArrayList<>();
        for (T t: map.keySet()) {
            if (map.get(t)==highest)
                tempList.add(t);
            else if (map.get(t)>highest) {
                highest=map.get(t);
                tempList = new ArrayList<>();
                tempList.add(t);
            }
        }
        return getRandomFromList(tempList, random);
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
}
