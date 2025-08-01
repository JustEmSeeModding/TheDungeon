package net.emsee.thedungeon.utils;

import net.minecraft.util.RandomSource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class WeightedMap {
    public static class Int<T> extends LinkedHashMap<T, Integer> {
        public Int() {
            super();
        }

        public Int(Map<T,Integer> map) {
            this();
            putAll(map);
        }
        public T getRandom(Random random) {
            if (size()==0) return null;
            int totalWeight = 0;
            for (T t : keySet()) {
                totalWeight += get(t);
            }
            if (totalWeight <= 0) return null;
            int chosenWeight = (int) Math.ceil(random.nextDouble() * (totalWeight));

            for (T t : keySet()) {
                chosenWeight -= get(t);
                if (chosenWeight <= 0) return t;
            }
            return null;
        }

        public T getRandom(RandomSource random) {
            if (size()==0) return null;
            int totalWeight = 0;
            for (T t : keySet()) {
                totalWeight += get(t);
            }
            if (totalWeight <= 0) return null;
            int chosenWeight = (int) Math.ceil(random.nextDouble() * (totalWeight));

            for (T t : keySet()) {
                chosenWeight -= get(t);
                if (chosenWeight <= 0) return t;
            }
            return null;
        }

        public int totalWeight() {
            int toReturn = 0;
            for (int i : values()) {
                toReturn+=i;
            }
            return toReturn;
        }
    }

    public static class Dbl<T> extends LinkedHashMap<T, Double> {
        public Dbl() {
            super();
        }

        public Dbl(Map<T,Double> map) {
            this();
            putAll(map);
        }

        public T getRandom(Random random) {
            if (size()==0) return null;
            double totalWeight = 0;
            for (T t : keySet()) {
                totalWeight += get(t);
            }
            if (totalWeight <= 0) return null;
            double chosenWeight = Math.ceil(random.nextDouble() * (totalWeight));

            for (T t : keySet()) {
                chosenWeight -= get(t);
                if (chosenWeight <= 0) return t;
            }
            return null;
        }

        public T getRandom(RandomSource random) {
            if (size()==0) return null;
            double totalWeight = 0;
            for (T t : keySet()) {
                totalWeight += get(t);
            }
            if (totalWeight <= 0) return null;
            double chosenWeight = Math.ceil(random.nextDouble() * (totalWeight));

            for (T t : keySet()) {
                chosenWeight -= get(t);
                if (chosenWeight <= 0) return t;
            }
            return null;
        }

        public double totalWeight() {
            double toReturn = 0;
            for (double i : values()) {
                toReturn+=i;
            }
            return toReturn;
        }
    }
}
