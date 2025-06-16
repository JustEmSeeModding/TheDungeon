package net.emsee.thedungeon.utils;

import net.minecraft.util.RandomSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WeightedMap {
    public static class Int<T> extends HashMap<T, Integer> {
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
    }

    public static class Dbl<T> extends HashMap<T, Double> {
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
    }
}
