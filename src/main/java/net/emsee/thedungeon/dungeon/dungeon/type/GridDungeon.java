package net.emsee.thedungeon.dungeon.dungeon.type;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.GlobalDungeonManager;
import net.emsee.thedungeon.dungeon.dungeon.Dungeon;
import net.emsee.thedungeon.dungeon.dungeon.generator.GridDungeonGenerator;
import net.emsee.thedungeon.dungeon.room.GridRoom;
import net.emsee.thedungeon.dungeon.roomCollections.GridRoomCollection;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;

/**
 * custom generation in a grid based system
 */
public class GridDungeon extends Dungeon {

    public enum RoomGenerationPickMethod {
        FIRST,
        LAST,
        RANDOM
    }

    private final GridRoomCollection roomCollection;
    private final int roomWidth;
    private final int roomHeight;
    private int dungeonDepth = 20;
    private float roomEndChance = .1f;
    private int maxFloorHeight = 999999;
    private boolean canGenerateDown = true;

    private GridDungeonGenerator generator = null;
    private boolean generated;
    private boolean fillWithFallbackWhenDone = false;

    private RoomGenerationPickMethod pickMethod = RoomGenerationPickMethod.FIRST;

    //// constructor
    public GridDungeon(String resourceName, DungeonRank rank, int weight, int roomWidth, int roomHeight, GridRoomCollection collection) {
        super(resourceName, rank, weight);
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        if (roomWidth % 2 != 1) {
            throw new IllegalStateException("Dungeon has an even width ({})! This should be odd, width:"+roomWidth);
        }
        if (collection.getWidth() != roomWidth || collection.getHeight() != roomHeight) {
            throw new IllegalStateException("RoomCollection "+collection+" is not the same size as the Dungeon "+this);
        }
        roomCollection = collection;
    }

    public GridDungeon(String resourceName, DungeonRank rank, int weight, int roomWidth, int roomHeight, GridRoomCollection collection, int ID) {
        super(resourceName, rank, weight, ID);
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        if (roomWidth % 2 != 1) {
            throw new IllegalStateException("Dungeon has an even width ({})! This should be odd, width:"+roomWidth);
        }
        if (collection.getWidth() != roomWidth || collection.getHeight() != roomHeight) {
            throw new IllegalStateException("RoomCollection "+collection+" is not the same size as the Dungeon "+this);
        }
        roomCollection = collection;
    }

    //// construction methods


    public GridDungeon setDepth(int depth) {
        this.dungeonDepth = depth;
        return this;
    }

    public GridDungeon setRoomEndChance(float roomEndChance) {
        this.roomEndChance = roomEndChance;
        return this;
    }

    public GridDungeon setMaxFloorHeight(int maxFloors) {
        maxFloorHeight = maxFloors;
        return this;
    }

    public GridDungeon disallowDownGeneration() {
        canGenerateDown = false;
        return this;
    }

    private GridDungeon allowDownGeneration(boolean value) {
        canGenerateDown = value;
        return this;
    }

    public GridDungeon setRoomPickMethod(RoomGenerationPickMethod method) {
        pickMethod = method;
        return this;
    }

    public GridDungeon setFillWithFallback(boolean state) {
        fillWithFallbackWhenDone = state;
        return this;
    }

    //// methods

    @Override
    public void Generate(ServerLevel serverLevel, BlockPos worldPos) {
        long selectedSeed = GameruleRegistry.getIntegerGamerule(serverLevel.getServer(), ModGamerules.DUNGEON_SEED_OVERRIDE);
        if (selectedSeed == -1) {
            GenerateSeeded(new Random().nextLong());
        } else {
            GenerateSeeded(selectedSeed);
        }
    }

    /**
     * generates the dungeon at the same position wit a diggerent seed
     */
    public void GenerateSeeded(long seed) {
        DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"Starting Dungeon Generation...");
        DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"Dungeon: {}", this.GetResourceName());
        generator = new GridDungeonGenerator(this, seed);
        DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"Seed: {}", seed);
    }

    /**
     * runs every tick while generating
     */
    @Override
    public void GenerationTick(ServerLevel serverLevel) {
        if (generator != null) {
            generator.step(serverLevel);
            if (generator.isDone()) {
                DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS,"Finished Dungeon Generation");
                generator = null;
                generated = true;
                GlobalDungeonManager.OpenDungeon(serverLevel.getServer(), this, utilDungeon);
            }
        }
    }

    public GridRoomCollection GetRoomCollection() {
        return roomCollection;
    }

    public RoomGenerationPickMethod getRoomPickMethod() {
        return pickMethod;
    }

    @Override
    public Dungeon GetCopy(int ID) {
        return new GridDungeon(resourceName, rank, weight, roomWidth, roomHeight, roomCollection.getCopy(), ID).
                setDepth(dungeonDepth).
                setRoomEndChance(roomEndChance).
                setMaxFloorHeight(maxFloorHeight).
                allowDownGeneration(canGenerateDown).
                setRoomPickMethod(pickMethod).
                setFillWithFallback(fillWithFallbackWhenDone).
                IsUtilDungeon(utilDungeon);
    }

    public int GetDungeonDepth() {
        return dungeonDepth;
    }

    public int GetMaxFloorHeight() {
        return maxFloorHeight;
    }

    public int GetMaxFloorHeightFromCenterOffset() {
        return GetMaxFloorHeight() / 2;
    }

    public GridRoomCollection getRoomCollection() {
        return roomCollection.getCopy();
    }

    public GridRoom GetStaringRoom() {
        return roomCollection.getStartingRoom();
    }

    public float GetRoomEndChance() {
        return roomEndChance;
    }

    public boolean IsGenerating() {
        return generator != null;
    }

    @Override
    public boolean IsDoneGenerating() {
        return (!IsGenerating()) && generated;
    }

    @Override
    public boolean IsBusyGenerating() {
        return IsGenerating();
    }

    public int GetRoomWidth() {
        return roomWidth;
    }

    public int GetRoomHeight() {
        return roomHeight;
    }

    public boolean IsDownGenerationDisabled() {
        return !canGenerateDown;
    }

    public boolean shouldFillWithFallback() {
        return fillWithFallbackWhenDone;
    }
}
