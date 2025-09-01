package net.emsee.thedungeon.entity.client.Goblin.hobGoblin;

import java.util.Arrays;
import java.util.Comparator;

public enum HobGoblinVariant {
    DEFAULT(0, "fighter"),
    FORGER(1, "forge_worker");

    private static final HobGoblinVariant[] BY_ID = Arrays.stream(values()).sorted(
            Comparator.comparingInt(HobGoblinVariant::getId)).toArray(HobGoblinVariant[]::new);
    private final int id;
    private final String resource;

    HobGoblinVariant(int id, String recource) {
        this.id=id;
        this.resource = recource;
    }

    public int getId() {return id;}

    public static HobGoblinVariant getById(int id) {
        return BY_ID[id % BY_ID.length];
    }

    public String getResource() {
        return resource;
    }
}
