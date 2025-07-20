package net.emsee.thedungeon.dungeon.src.types;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.src.room.AbstractGridRoom;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.dungeon.src.GlobalDungeonManager;
import net.emsee.thedungeon.dungeon.src.generators.GridDungeonGenerator;
import net.emsee.thedungeon.dungeon.src.GridRoomCollection;
import net.minecraft.server.level.ServerLevel;

/**
 * custom generation in a grid based system
 */
public class GridDungeon extends Dungeon {
    public enum RoomGenerationPickMethod {
        FIRST, LAST, RANDOM
    }

    private final GridRoomCollection roomCollection;
    private final int gridCellWidth; // these could be a Vec2 maybe, IDK
    private final int gridCellHeight; //^
    private int dungeonDepth = 20;
    private float roomEndChance = .1f;
    private int maxFloorHeight = 999999;
    private boolean canGenerateDown = true;

    private GridDungeonGenerator generator = null;
    private boolean generated;
    private boolean fillWithFallbackWhenDone = false;

    private RoomGenerationPickMethod pickMethod = RoomGenerationPickMethod.FIRST;

    //// constructor
    public GridDungeon(String resourceName, DungeonRank rank, int weight, int gridCellWidth, int gridCellHeight, GridRoomCollection collection) {
        super(resourceName, rank, weight);
        this.gridCellWidth = gridCellWidth;
        this.gridCellHeight = gridCellHeight;
        if (gridCellWidth % 2 != 1)
            throw new IllegalStateException(this+":Dungeon has an even width! This should be odd, width:" + gridCellWidth);
        if (collection == null)
            throw new IllegalStateException(this+":Dungeon collection is set to NULL");
        if (collection.getGridCellWidth() != gridCellWidth || collection.getGridCellHeight() != gridCellHeight)
            throw new IllegalStateException(this+":RoomCollection (" + collection + ") is not the same size as the Dungeon");

        roomCollection = collection;
    }

    protected GridDungeon(String resourceName, DungeonRank rank, int weight, int gridCellWidth, int gridCellHeight, GridRoomCollection collection, int ID) {
        super(resourceName, rank, weight, ID);
        this.gridCellWidth = gridCellWidth;
        this.gridCellHeight = gridCellHeight;
        if (gridCellWidth % 2 != 1) {
            throw new IllegalStateException("Dungeon has an even width ({})! This should be odd, width:" + gridCellWidth);
        }
        if (collection.getGridCellWidth() != gridCellWidth || collection.getGridCellHeight() != gridCellHeight) {
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
     * <p>
     * = when a scaled room goes below the lowest or above the highest floor it can generate but any connections below the main floor will not be continued
     * <p>
     * = in case allowDownGeneration is false use "setMaxFloorHeightOneWay()" instead
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
     * when set to false the dungeon will not generate below the main floor
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
    public GridDungeon setFillWithFallback(boolean doFill) {
        fillWithFallbackWhenDone = doFill;
        return this;
    }

    //// methods
    @Override
    public void generateSeeded(long seed) {
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "Starting Dungeon Generation...");
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "Dungeon: {}", this.getResourceName());
        generator = new GridDungeonGenerator(this, seed);
        DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "Seed: {}", seed);
    }


    @Override
    public void generationTick(ServerLevel serverLevel) {
        if (generator != null) {
            generator.step(serverLevel);
            if (generator.isDone()) {
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_STEPS, "Finished Dungeon Generation");
                generator = null;
                generated = true;
                GlobalDungeonManager.openDungeon(serverLevel.getServer(), this, utilDungeon);
            }
        }
    }

    @Override
    public Dungeon getCopy(int ID) {
        return new GridDungeon(resourceName, rank, weight, gridCellWidth, gridCellHeight, roomCollection.getCopy(), ID).
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

    public AbstractGridRoom getStaringRoom() {
        AbstractGridRoom toReturn = roomCollection.getStartingRoom();
        if (toReturn == null) return null;
        return toReturn.getCopy();
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

    @Override
    public boolean canManualStepNow() {
        return generator.currentStep()==GridDungeonGenerator.GenerationTask.PLACING_ROOMS;
    }

    public int getGridCellWidth() {
        return gridCellWidth;
    }

    public int getGridCellHeight() {
        return gridCellHeight;
    }

    public boolean isDownGenerationDisabled() {
        return !canGenerateDown;
    }

    public boolean shouldFillWithFallback() {
        return fillWithFallbackWhenDone;
    }
}
