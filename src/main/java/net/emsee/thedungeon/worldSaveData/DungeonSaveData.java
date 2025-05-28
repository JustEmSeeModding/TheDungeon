package net.emsee.thedungeon.worldSaveData;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.dungeon.Dungeon;
import net.emsee.thedungeon.worldSaveData.NBT.DungeonNBTData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class DungeonSaveData extends SavedData {
    private static final String DATA_NAME = TheDungeon.MOD_ID + "_dungeon_data";
    final DungeonNBTData dungeonData = new DungeonNBTData();

    /**
     * Returns a factory for creating and loading DungeonSaveData instances with the LEVEL data fix type.
     *
     * @return a Factory configured for DungeonSaveData persistence and loading
     */
    public static Factory<DungeonSaveData> factory() {
        return new Factory<>(DungeonSaveData::new, DungeonSaveData::load, DataFixTypes.LEVEL);
    }

    /**
     * Serializes the current dungeon data into the provided NBT tag.
     *
     * @param tag the NBT tag to write data into
     * @param pRegistries the registry provider for data fixing
     * @return the updated NBT tag containing the serialized dungeon data
     */
    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider pRegistries) {
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA,"DungeonData saving");
        tag.put("DungeonData", dungeonData.SerializeNBT());
        return tag;
    }

    /**
     * Loads a DungeonSaveData instance from the provided NBT data.
     *
     * @param nbt the compound tag containing serialized dungeon data
     * @param provider the registry provider for data lookup
     * @return a new DungeonSaveData instance with its state restored from NBT
     */
    private static DungeonSaveData load(CompoundTag nbt, HolderLookup.Provider provider) {
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA,"DungeonData Loading");
        DungeonSaveData data = new DungeonSaveData();
        data.dungeonData.DeserializeNBT(nbt.getCompound("DungeonData"));
        return data;
    }

    public long GetLastExecutionTime() {
        return dungeonData.GetLastExecutionTime();
    }

    public long GetLastMinuteAnnouncement() {
        return dungeonData.GetLastMinuteAnnouncement();
    }

    public long GetLasSecondAnnouncement() {
        return dungeonData.GetLasSecondAnnouncement();
    }

    public void SetLastExecutionTime(long lastExecutionTime) {
        dungeonData.SetLastExecutionTime(lastExecutionTime);
        setDirty();
    }

    public void SetLastMinuteAnnouncement(long lastMinuteAnnouncement) {
        dungeonData.SetLastMinuteAnnouncement(lastMinuteAnnouncement);
        setDirty();
    }

    /****
     * Updates the timestamp for the last second announcement and marks the data as dirty for saving.
     *
     * @param lastSecondAnnouncement the new timestamp for the last second announcement
     */
    public void SetLastSecondAnnouncement(long lastSecondAnnouncement) {
        dungeonData.SetLastSecondAnnouncement(lastSecondAnnouncement);
        setDirty();
    }

    /**
     * Retrieves the persistent DungeonSaveData instance for the overworld from the given Minecraft server,
     * creating it if it does not already exist.
     *
     * @param server the Minecraft server from which to obtain the dungeon save data
     * @return the DungeonSaveData instance associated with the overworld
     */
    public static DungeonSaveData Get(MinecraftServer server) {
        ResourceKey<Level> overworldResourceKey = Level.OVERWORLD;
        ServerLevel overworld = server.getLevel(overworldResourceKey);
        assert overworld != null;
        DimensionDataStorage storage = overworld.getDataStorage();
        return storage.computeIfAbsent(factory(), DATA_NAME);
    }

    public boolean isProgressQueueEmpty() {
        return dungeonData.getDungeonProgresQueue().isEmpty();
    }

    public Dungeon peekProgressQueue() {
        return dungeonData.getDungeonProgresQueue().peek();
    }

    public void addToProgressQueue(Dungeon dungeon) {
        dungeonData.getDungeonProgresQueue().add(dungeon);
        setDirty();
    }

    public Dungeon removeFromProgressQueue() {
        Dungeon toReturn = dungeonData.getDungeonProgresQueue().remove();
        setDirty();
        return toReturn;
    }

    public boolean isPassiveQueueEmpty(Dungeon.DungeonRank rank) {
        if (rank==null) return true;
        return dungeonData.getDungeonPassiveQueue(rank).isEmpty();
    }

    public Dungeon peekPassiveQueue(Dungeon.DungeonRank rank) {
        if (rank == null) return null;
        return dungeonData.getDungeonPassiveQueue(rank).peek();
    }

    public void addToPassiveQueue(Dungeon dungeon) {
        dungeonData.getDungeonPassiveQueue(dungeon.getRank()).add(dungeon);
        setDirty();
    }

    /****
     * Removes and returns the first dungeon from the passive queue.
     * @return the dungeon removed from the passive queue
     */
    public Dungeon removeFromPassiveQueue(Dungeon.DungeonRank rank) {
        Dungeon toReturn = dungeonData.getDungeonPassiveQueue(rank).remove();
        setDirty();
        return toReturn;
    }

    /**
     * Removes and returns the next dungeon from the progress queue, or returns null if the queue is empty.
     * @return the next Dungeon in the progress queue, or null if the queue is empty
     */
    public Dungeon pollFromProgressQueue() {
        Dungeon toReturn = dungeonData.getDungeonProgresQueue().poll();
        setDirty();
        return toReturn;
    }

    /**
     * Adds all dungeons from the provided list to the progress queue and marks the data as dirty for saving.
     *
     * @param list the list of dungeons to add to the progress queue
     */
    public void addAllToProgressQueue(List<Dungeon> list) {
        dungeonData.getDungeonProgresQueue().addAll(list);
        setDirty();
    }

    /**
     * Returns whether the dungeon is currently open.
     *
     * @return true if the dungeon is open; false otherwise
     */
    public boolean isDungeonOpen(Dungeon.DungeonRank rank) {
        return dungeonData.getDungeonOpen(rank);
    }

    public void setDungeonOpen(Dungeon.DungeonRank rank, boolean isOpen) {
        dungeonData.setDungeonOpen(rank, isOpen);
        setDirty();
    }

    public void clearPortalPositions(Dungeon.DungeonRank rank) {
        if (rank==null) return;
        dungeonData.getPortalPositions().get(rank).clear();
        setDirty();
    }

    public int portalPositionAmount(Dungeon.DungeonRank rank) {
        if (rank==null) return -1;
        return dungeonData.getPortalPositions().get(rank).size();
    }

    public BlockPos getPortalPosition(int portalID, Dungeon.DungeonRank rank) {
        if (rank==null) return null;
        return dungeonData.getPortalPositions().get(rank).get(portalID);
    }

    public boolean portalPositionsEmpty(Dungeon.DungeonRank rank) {
        if (rank==null) return true;
        return dungeonData.getPortalPositions().get(rank).isEmpty();
    }

    public void addPortalPosition(BlockPos pos, Dungeon.DungeonRank rank) {
        if (rank==null) return;
        if (!dungeonData.getPortalPositions().get(rank).contains(pos))
            dungeonData.getPortalPositions().get(rank).add(pos);
        setDirty();
    }

    public void removePortalPosition(BlockPos pos, Dungeon.DungeonRank rank) {
        if (rank==null) return;
        dungeonData.getPortalPositions().get(rank).remove(pos);
        setDirty();
    }

    public List<BlockPos> getAllPortalPositions(Dungeon.DungeonRank rank) {
        if (rank==null) return null;
        return new ArrayList<>(dungeonData.getPortalPositions().get(rank));
    }

    public int getTickInterval() {return dungeonData.getTickInterval();}

    public void setTickInterval(int interval) {
        dungeonData.setTickInterval(interval);
        setDirty();
    }

    public Dungeon.DungeonRank getNextToCollapse() {
        return dungeonData.getNextToCollapse();
    }

    public void SelectNextForCollapse() {
        dungeonData.setNextToCollapse(Dungeon.DungeonRank.getNext(getNextToCollapse()));
        setDirty();
    }
}
