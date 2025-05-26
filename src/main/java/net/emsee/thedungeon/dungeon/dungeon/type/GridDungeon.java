package net.emsee.thedungeon.dungeon.dungeon.type;

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

    // constructor

    public GridDungeon(String resourceName, DungeonRank rank, int weight, int roomWidth, int roomHeight, GridRoomCollection collection) {
        super(resourceName, rank, weight);
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        if (roomWidth % 2 != 1) {
            TheDungeon.LOGGER.error("Dungeon ({}) has an even width ({})! This should be odd.", this, this.roomWidth);
        }
        if (collection.getWidth() != roomWidth || collection.getHeight() != roomHeight) {
            TheDungeon.LOGGER.error("RoomCollection ({}:{},{}) is not the same size as the Dungeon ({}:{},{})", collection, collection.getWidth(), collection.getHeight(), this, roomWidth, roomHeight);
        }
        roomCollection = collection;
    }

    public GridDungeon(String resourceName, DungeonRank rank, int weight, int roomWidth, int roomHeight, GridRoomCollection collection, int ID) {
        super(resourceName, rank, weight, ID);
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        if (roomWidth % 2 != 1) {
            TheDungeon.LOGGER.error("Dungeon ({}) has an even width ({})! This should be odd.", this, this.roomWidth);
        }
        if (collection.getWidth() != roomWidth || collection.getHeight() != roomHeight) {
            TheDungeon.LOGGER.error("RoomCollection ({}:{},{}) is not the same size as the Dungeon ({}:{},{})", collection, collection.getWidth(), collection.getHeight(), this, roomWidth, roomHeight);
        }
        roomCollection = collection;
    }

    // construction methods


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

    // methods

    @Override
    public void Generate(ServerLevel serverLevel, BlockPos worldPos) {
        long selectedSeed = GameruleRegistry.getIntegerGamerule(serverLevel.getServer(), ModGamerules.DUNGEON_SEED_OVERRIDE);
        if (selectedSeed == -1) {
            Generate(worldPos, new Random().nextLong());
        } else {
            Generate(worldPos, selectedSeed);
        }
    }

    public void Generate(BlockPos worldPos, long seed) {
        TheDungeon.LOGGER.info("Starting Dungeon Generation...");
        TheDungeon.LOGGER.info("Dungeon: {}", this.GetResourceName());
        generator = new GridDungeonGenerator(this, worldPos, seed);
        TheDungeon.LOGGER.info("Seed: {}", seed);
    }

    @Override
    public void GenerationTick(ServerLevel serverLevel) {
        if (generator != null) {
            generator.step(serverLevel);
            if (generator.isDone()) {
                generator = null;
                TheDungeon.LOGGER.info("Finished Dungeon Generation");
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
