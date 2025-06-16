package net.emsee.thedungeon.utils;

import net.minecraft.util.RandomSource;

import java.util.*;

public class PriorityMap<T> extends HashMap<T,Integer> {
    public PriorityMap(){
        super();
    }

    public PriorityMap(Map<T,Integer> map) {
        this();
        putAll(map);
    }

    /**
     * gets a random value from only the highest priority in the list
     */
    public T getRandom(Random random) {
        return getRandom(random, 0);
    }

    /**
     * gets a random value from only the highest priority in the list
     */
    public T getRandom(Random random, int minimumPriority) {
        int highest = minimumPriority;
        List<T> tempList = new ArrayList<>();
        for (T t: keySet()) {
            if (get(t)==highest)
                tempList.add(t);
            else if (get(t)>highest) {
                highest=get(t);
                tempList = new ArrayList<>();
                tempList.add(t);
            }
        }
        return ListAndArrayUtils.getRandomFromList(tempList, random);
    }

    /**
     * gets a random value from only the highest priority in the list
     */
    public T getRandom(RandomSource random) {
        return getRandom(random, 0);
    }

    /**
     * gets a random value from only the highest priority in the list
     */
    public  T getRandom(RandomSource random, int minimumPriority) {
        int highest = minimumPriority;
        List<T> tempList = new ArrayList<>();
        for (T t: keySet()) {
            if (get(t)==highest)
                tempList.add(t);
            else if (get(t)>highest) {
                highest=get(t);
                tempList = new ArrayList<>();
                tempList.add(t);
            }
        }
        return ListAndArrayUtils.getRandomFromList(tempList, random);
    }
}
