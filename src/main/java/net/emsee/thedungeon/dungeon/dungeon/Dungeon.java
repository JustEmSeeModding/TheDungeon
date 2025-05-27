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

        public int getTickInterval() {
            //TODO remove
            return 15 * 60 * 20;
        }
        public BlockPos getCenterPos() {return centerPos;}

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
        saveData.addToProgressQueue(ModDungeons.CLEANUP_OLD.GetCopy());
        GlobalDungeonManager.KillAllInDungeon(server);

    }

    public static void PriorityCleanup(MinecraftServer server) {
        GlobalDungeonManager.AddToQueueFront(ModDungeons.CLEANUP_OLD.GetCopy(), server);
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
