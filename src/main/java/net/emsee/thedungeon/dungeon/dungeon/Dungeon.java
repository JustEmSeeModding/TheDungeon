package net.emsee.thedungeon.dungeon.dungeon;

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
        F(new BlockPos(0,150,0), "F"),
        E(new BlockPos(1500,150,1500), "E"),
        D(new BlockPos(-1500,150,1500), "D"),
        C(new BlockPos(1500,150,-1500), "C"),
        B(new BlockPos(-1500,150,-1500), "B"),
        A(new BlockPos(3000,150,3000), "A"),
        S(new BlockPos(-3000,150,3000), "S"),
        SS(new BlockPos(3000,150,-3000), "SS");

        private final BlockPos centerPos;
        private final String name;

        DungeonRank(BlockPos centerPos, String name) {
            this.centerPos = centerPos ;
            this.name = name;
        }

        public BlockPos getCenterPos() {return centerPos;}

        public String getName() {
            return name;
        }

        public static DungeonRank getByName(String name) {
            for (DungeonRank rank : DungeonRank.values())
                if (name.equals(rank.getName())) return rank;
            return null;
        }

        public static DungeonRank getNext(DungeonRank rank) {
            int i = 0;
            for (DungeonRank r : DungeonRank.values()) {
                if (r == rank) break;
                i++;
            }
            return DungeonRank.values()[(i+1)%DungeonRank.values().length];
        }

        public static DungeonRank getClosestRank(BlockPos pos) {
            double lowestDist = -1;
            DungeonRank toReturn = null;
            for (DungeonRank rank : DungeonRank.values()) {
                double dist = rank.getCenterPos().distSqr(pos);
                if (lowestDist == -1 || dist < lowestDist) {
                    lowestDist = dist;
                    toReturn = rank;
                }
            }
            return toReturn;
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

    public static void Cleanup(MinecraftServer server, DungeonRank rank) {
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        Dungeon cleanup = null;
        switch (rank) {
            case F -> cleanup=ModDungeons.CLEANUP_F;
            case E -> cleanup=ModDungeons.CLEANUP_E;
            case D -> cleanup=ModDungeons.CLEANUP_D;
            case C -> cleanup=ModDungeons.CLEANUP_C;
            case B -> cleanup=ModDungeons.CLEANUP_B;
            case A -> cleanup=ModDungeons.CLEANUP_A;
            case S -> cleanup=ModDungeons.CLEANUP_S;
            case SS -> cleanup=ModDungeons.CLEANUP_SS;
        }
        saveData.addToProgressQueue(cleanup.GetCopy());
        GlobalDungeonManager.KillAllInDungeon(server, rank);

    }

    public static void PriorityCleanup(MinecraftServer server, DungeonRank rank) {
        Dungeon cleanup = null;
        switch (rank) {
            case F -> cleanup=ModDungeons.CLEANUP_F;
            case E -> cleanup=ModDungeons.CLEANUP_E;
            case D -> cleanup=ModDungeons.CLEANUP_D;
            case C -> cleanup=ModDungeons.CLEANUP_C;
            case B -> cleanup=ModDungeons.CLEANUP_B;
            case A -> cleanup=ModDungeons.CLEANUP_A;
            case S -> cleanup=ModDungeons.CLEANUP_S;
            case SS -> cleanup=ModDungeons.CLEANUP_SS;
        }

        if (cleanup==null)
            throw new IllegalStateException("cleanup was NULL");

        GlobalDungeonManager.AddToQueueFront(cleanup.GetCopy(), server);
        GlobalDungeonManager.KillAllInDungeon(server, rank);
    }

    public final String GetResourceName() {
        return resourceName;
    }

    public final Component GetTranslatedName() {
        return Component.translatable(resourceName);
    }

    public DungeonRank getRank() {
        return rank;
    }

    public Integer getWeight() {
        return weight;
    }

    public int getID() {
        return ID;
    }

    /**
     * gets a copy of base dungeon
     */
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
}
