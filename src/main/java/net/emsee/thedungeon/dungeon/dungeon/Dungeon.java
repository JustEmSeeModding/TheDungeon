package net.emsee.thedungeon.dungeon.dungeon;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.GlobalDungeonManager;
import net.emsee.thedungeon.dungeon.ModDungeons;
import net.emsee.thedungeon.worldSaveData.DungeonSaveData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public abstract class Dungeon {
    private static int lastID = 0;
    public enum DungeonRank {
        UTIL(5, "util"),
        F(15, "F"),
        E(20, "E"),
        D(25, "D"),
        C(25, "C"),
        B(25, "B"),
        A(25, "A"),
        S(25, "S"),
        SS(30, "SS");

        private final int tickInterval;
        private final String name;

        DungeonRank(int minutes, String name) {
            this.tickInterval = minutes * 60 * 20;
            this.name = name;
        }

        public int getTickInterval() {
            return tickInterval;
        }

        public String getName() {
            return name;
        }
    }

    protected final String resourceName;
    private final int ID;
    protected boolean utilDungeon;
    protected final DungeonRank rank;
    protected final int weight;

    // constructor
    public Dungeon(String resourceName, DungeonRank rank, int weight) {
        //TheDungeon.LOGGER.info("Creating dungeon class :{}", GetResourceName());
        ID = lastID++;
        this.resourceName = resourceName;
        this.rank = rank;
        this.weight = weight;
    }

    protected Dungeon(String resourceName, DungeonRank rank, int weight, int ID) {
        //TheDungeon.LOGGER.info("Creating dungeon class :{}", GetResourceName());
        this.ID = ID;
        this.resourceName = resourceName;
        this.rank = rank;
        this.weight = weight;
    }

    // constructionMethod

    public Dungeon IsUtilDungeon(boolean is) {
        utilDungeon = is;
        return this;
    }

    // methods
    public abstract void Generate(ServerLevel serverLevel, BlockPos worldPos);

    public abstract void GenerationTick(ServerLevel serverLevel);

    public static void Cleanup(MinecraftServer server) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        saveData.addToProgressQueue(ModDungeons.CLEANUP.GetCopy());
        GlobalDungeonManager.KillAllInDungeon(server);

    }

    public static void PriorityCleanup(MinecraftServer server) {
        GlobalDungeonManager.AddToQueueFront(ModDungeons.CLEANUP.GetCopy(), server);
        GlobalDungeonManager.KillAllInDungeon(server);
    }

    public final String GetResourceName() {
        return resourceName;
    }

    public final Component GetTranslatedName() {
        return Component.translatable(resourceName);
    }

    public final Dungeon GetCopy() {
        return GetCopy(ID);
    };

    protected abstract Dungeon GetCopy(int ID);

    public abstract boolean IsDoneGenerating();

    public abstract boolean IsBusyGenerating();

    @Override
    public String toString() {
        return (GetResourceName() + "-" + GetTranslatedName());
    }

    public DungeonRank getRank() {
        return rank;
    }

    public int getTickInterval() {
        return rank.getTickInterval();
    }

    public Integer getWeight() {
        return weight;
    }

    public int getID() {
        return ID;
    }
}
