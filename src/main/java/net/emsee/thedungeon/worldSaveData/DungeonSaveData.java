package net.emsee.thedungeon.worldSaveData;

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

    public static Factory<DungeonSaveData> factory() {
        return new Factory<>(DungeonSaveData::new, DungeonSaveData::load, DataFixTypes.LEVEL);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider pRegistries) {
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.GENERIC)) TheDungeon.LOGGER.info("DungeonData saving");
        tag.put("DungeonData", dungeonData.SerializeNBT());
        return tag;
    }

    private static DungeonSaveData load(CompoundTag nbt, HolderLookup.Provider provider) {
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.GENERIC)) TheDungeon.LOGGER.info("DungeonData Loading");
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

    public void SetLastSecondAnnouncement(long lastSecondAnnouncement) {
        dungeonData.SetLastSecondAnnouncement(lastSecondAnnouncement);
        setDirty();
    }

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

    public boolean isPassiveQueueEmpty() {
        return dungeonData.getDungeonPassiveQueue().isEmpty();
    }

    public Dungeon peekPassiveQueue() {
        return dungeonData.getDungeonPassiveQueue().peek();
    }

    public void addToPassiveQueue(Dungeon dungeon) {
        dungeonData.getDungeonPassiveQueue().add(dungeon);
        setDirty();
    }

    public Dungeon removeFromPassiveQueue() {
        Dungeon toReturn = dungeonData.getDungeonPassiveQueue().remove();
        setDirty();
        return toReturn;
    }

    public Dungeon pollFromProgressQueue() {
        Dungeon toReturn = dungeonData.getDungeonProgresQueue().poll();
        setDirty();
        return toReturn;
    }

    public void addAllToProgressQueue(List<Dungeon> list) {
        dungeonData.getDungeonProgresQueue().addAll(list);
        setDirty();
    }

    public boolean isDungeonOpen() {
        return dungeonData.getDungeonOpen();
    }

    public void setDungeonOpen(boolean isOpen) {
        dungeonData.setDungeonOpen(isOpen);
        setDirty();
    }

    public void clearPortalPositions() {
        dungeonData.getPortalPositions().clear();
        setDirty();
    }

    public int portalPositionAmount() {
        return dungeonData.getPortalPositions().size();
    }

    public BlockPos getPortalPosition(int portalID) {
        return dungeonData.getPortalPositions().get(portalID);
    }

    public boolean portalPositionsEmpty() {
        return dungeonData.getPortalPositions().isEmpty();
    }

    public void addPortalPosition(BlockPos pos) {
        if (!dungeonData.getPortalPositions().contains(pos))
            dungeonData.getPortalPositions().add(pos);
        setDirty();
    }

    public void removePortalPosition(BlockPos pos) {
        dungeonData.getPortalPositions().remove(pos);
        setDirty();
    }

    public List<BlockPos> getAllPortalPositions() {
        return new ArrayList<>(dungeonData.getPortalPositions());
    }

    public int getTickInterval() {return dungeonData.getTickInterval();}

    public void setTickInterval(int interval) {
        dungeonData.setTickInterval(interval);
        setDirty();
    }

    public boolean isTiming() {
        return dungeonData.isTiming();
    }
    public void setTiming(boolean timing) {
        dungeonData.setTiming(timing);
        setDirty();
    }
}
