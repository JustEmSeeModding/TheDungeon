package net.emsee.thedungeon.dungeon.types;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.util.DungeonRank;
import net.emsee.thedungeon.dungeon.GlobalDungeonManager;
import net.emsee.thedungeon.dungeon.generators.GridDungeonGenerator;
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
            throw new IllegalStateException("Dungeon has an even width ({})! This should be odd, width:" + roomWidth);
        }
        if (collection.getWidth() != roomWidth || collection.getHeight() != roomHeight) {
            throw new IllegalStateException("RoomCollection " + collection + " is not the same size as the Dungeon " + this);
        }
        roomCollection = collection;
    }

    public GridDungeon(String resourceName, DungeonRank rank, int weight, int roomWidth, int roomHeight, GridRoomCollection collection, int ID) {
        super(resourceName, rank, weight, ID);
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        if (roomWidth % 2 != 1) {
            throw new IllegalStateException("Dungeon has an even width ({})! This should be odd, width:" + roomWidth);
        }
        if (collection.getWidth() != roomWidth || collection.getHeight() != roomHeight) {
            throw new IllegalStateException("RoomCollection " + collection + " is not the same size as the Dungeon " + this);
        }
        roomCollection = collection;
    }

    //// construction methods


    public GridDungeon setDepth(int depth) {
        this.dungeonDepth = depth;
        return this;
    }

    /**
     * the chance each room take to stop generating next rooms
     */
    public GridDungeon setRoomEndChance(float roomEndChance) {
        this.roomEndChance = roomEndChance;
        return this;
    }

    /**
     * sets the max number of floors the dungeon is allowed to have (including the main floor)
     * - in case allowDownGeneration is false use "setMaxFloorHeightOneWay()" instead
     */
    public GridDungeon setMaxFloorHeight(int maxFloors) {
        if (maxFloors % 2 != 1)
            throw new IllegalStateException("Max floors must be odd");
        maxFloorHeight = maxFloors;
        return this;
    }

    /**
     * sets the max number of floors the dungeon is allowed to have in a single direction (including the main floor)
     */
    public GridDungeon setMaxFloorHeightOneWay(int maxFloors) {
        return setMaxFloorHeight((maxFloors - 1) * 2 + 1);
    }

    /**
     * when set to false the dungeon will not generate below the main floor,
     * when a scaled room goes below the main floor it can generate but any connections below the main floor will not be continued
     */
    private GridDungeon allowDownGeneration(boolean value) {
        canGenerateDown = value;
        return this;
    }

    /**
     * the method this dungeon uses to select the room that can generate a connection
     */
    public GridDungeon setRoomPickMethod(RoomGenerationPickMethod method) {
        pickMethod = method;
        return this;
    }

    /**
     * when true: once the entire dungeon has finished generating all empty spaces will be filled with fallback
     */
    public GridDungeon setFillWithFallback(boolean state) {
        fillWithFallbackWhenDone = state;
        return this;
    }

    //// methods
    @Override
    public void generate(ServerLevel serverLevel, BlockPos worldPos) {
        long selectedSeed = GameruleRegistry.getIntegerGamerule(serverLevel.getServer(), ModGamerules.DUNGEON_SEED_OVERRIDE);
        if (selectedSeed == -1) {
            generateSeeded(new Random().nextLong());
        } else {
            generateSeeded(selectedSeed);
        }
    }

    /**
     * generates the dungeon at the same position with a seed
     */
    public void generateSeeded(long seed) {
        DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS, "Starting Dungeon Generation...");
        DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS, "Dungeon: {}", this.getResourceName());
        generator = new GridDungeonGenerator(this, seed);
        DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS, "Seed: {}", seed);
    }


    @Override
    public void generationTick(ServerLevel serverLevel) {
        if (generator != null) {
            generator.step(serverLevel);
            if (generator.isDone()) {
                DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_STEPS, "Finished Dungeon Generation");
                generator = null;
                generated = true;
                GlobalDungeonManager.OpenDungeon(serverLevel.getServer(), this, utilDungeon);
            }
        }
    }

    @Override
    public Dungeon getCopy(int ID) {
        return new GridDungeon(resourceName, rank, weight, roomWidth, roomHeight, roomCollection.getCopy(), ID).
                setDepth(dungeonDepth).
                setRoomEndChance(roomEndChance).
                setMaxFloorHeight(maxFloorHeight).
                allowDownGeneration(canGenerateDown).
                setRoomPickMethod(pickMethod).
                setFillWithFallback(fillWithFallbackWhenDone).
                isUtilDungeon(utilDungeon).
                setOverrideCenter(overrideCenter);
    }

    public RoomGenerationPickMethod getRoomPickMethod() {
        return pickMethod;
    }

    public int getDungeonDepth() {
        return dungeonDepth;
    }

    public int getMaxFloorHeight() {
        return maxFloorHeight;
    }

    public int getMaxFloorHeightFromCenterOffset() {
        return (getMaxFloorHeight() - 1) / 2;
    }

    public GridRoomCollection getRoomCollection() {
        return roomCollection.getCopy();
    }

    public GridRoom getStaringRoom() {
        return roomCollection.getStartingRoom();
    }

    public float getRoomEndChance() {
        return roomEndChance;
    }

    @Override
    public boolean isDoneGenerating() {
        return (!isBusyGenerating()) && generated;
    }

    @Override
    public boolean isBusyGenerating() {
        return generator != null;
    }

    public int getRoomWidth() {
        return roomWidth;
    }

    public int getRoomHeight() {
        return roomHeight;
    }

    public boolean isDownGenerationDisabled() {
        return !canGenerateDown;
    }

    public boolean shouldFillWithFallback() {
        return fillWithFallbackWhenDone;
    }
}
