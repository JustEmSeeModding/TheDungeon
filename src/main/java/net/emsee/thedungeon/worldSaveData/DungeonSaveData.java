package net.emsee.thedungeon.worldSaveData;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.src.types.Dungeon;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.worldSaveData.NBT.DungeonNBTData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class DungeonSaveData extends SavedData {
    private static final String DATA_NAME = TheDungeon.MOD_ID + "_dungeon_data";
    private final DungeonNBTData dungeonData = new DungeonNBTData();

    @OnlyIn(Dist.CLIENT)
    private static DungeonNBTData.DataPacket CLIENT_DATA = null;

    /**
     * Returns a factory for creating and loading DungeonSaveData instances with the LEVEL data fix type.
     */
    private static Factory<DungeonSaveData> factory() {
        return new Factory<>(DungeonSaveData::new, DungeonSaveData::load, DataFixTypes.LEVEL);
    }

    /**
     * Serializes the current dungeon data into the provided NBT tag.
     */
    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider pRegistries) {
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA,"DungeonData saving");
        tag.put("DungeonData", dungeonData.SerializeNBT());
        PacketDistributor.sendToAllPlayers(dungeonData.createPacket());
        return tag;
    }

    @Override
    public void setDirty() {
        this.setDirty(true);
        PacketDistributor.sendToAllPlayers(dungeonData.createPacket());
    }

    /**
     * Loads a DungeonSaveData instance from the provided NBT data.
     */
    private static DungeonSaveData load(CompoundTag nbt, HolderLookup.Provider provider) {
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA,"DungeonData Loading");
        DungeonSaveData data = new DungeonSaveData();
        data.dungeonData.DeserializeNBT(nbt.getCompound("DungeonData"));
        PacketDistributor.sendToAllPlayers(data.dungeonData.createPacket());
        return data;
    }

    public long GetLastExecutionTime() {
        return dungeonData.getLastExecutionTime();
    }

    public long GetLastMinuteAnnouncement() {
        return dungeonData.getLastMinuteAnnouncement();
    }

    public long GetLasSecondAnnouncement() {
        return dungeonData.getLasSecondAnnouncement();
    }

    public void SetLastExecutionTime(long lastExecutionTime) {
        dungeonData.setLastExecutionTime(lastExecutionTime);
        setDirty();
    }

    public void SetLastMinuteAnnouncement(long lastMinuteAnnouncement) {
        dungeonData.setLastMinuteAnnouncement(lastMinuteAnnouncement);
        setDirty();
    }


    public void SetLastSecondAnnouncement(long lastSecondAnnouncement) {
        dungeonData.setLastSecondAnnouncement(lastSecondAnnouncement);
        setDirty();
    }

    public static DungeonSaveData Get(MinecraftServer server) {
        ResourceKey<Level> overworldResourceKey = Level.OVERWORLD;
        ServerLevel overworld = server.getLevel(overworldResourceKey);
        assert overworld != null;
        DimensionDataStorage storage = overworld.getDataStorage();
        return storage.computeIfAbsent(factory(), DATA_NAME);
    }

    public long getTimeLeft() {
        return dungeonData.getTimeLeft();
    }

    @OnlyIn(Dist.CLIENT)
    public static DungeonNBTData.DataPacket GetClient() {
        return CLIENT_DATA;
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

    public boolean isPassiveQueueEmpty(DungeonRank rank) {
        if (rank==null) return true;
        return dungeonData.getDungeonPassiveQueue(rank).isEmpty();
    }

    public Dungeon peekPassiveQueue(DungeonRank rank) {
        if (rank == null) return null;
        return dungeonData.getDungeonPassiveQueue(rank).peek();
    }

    public void addToPassiveQueue(Dungeon dungeon) {
        dungeonData.getDungeonPassiveQueue(dungeon.getRank()).add(dungeon);
        setDirty();
    }

    public Dungeon removeFromPassiveQueue(DungeonRank rank) {
        Dungeon toReturn = dungeonData.getDungeonPassiveQueue(rank).remove();
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

    public boolean isDungeonOpen(DungeonRank rank) {
        return dungeonData.getDungeonOpen(rank);
    }

    public void setDungeonOpen(DungeonRank rank, boolean isOpen) {
        dungeonData.setDungeonOpen(rank, isOpen);
        setDirty();
    }

    public void clearPortalPositions(DungeonRank rank) {
        if (rank==null) return;
        dungeonData.getPortalPositions().get(rank).clear();
        setDirty();
    }

    public int portalPositionAmount(DungeonRank rank) {
        if (rank==null) return -1;
        return dungeonData.getPortalPositions().get(rank).size();
    }

    public BlockPos getPortalPosition(int portalID, DungeonRank rank) {
        if (rank==null) return null;
        return dungeonData.getPortalPositions().get(rank).get(portalID);
    }

    public boolean portalPositionsEmpty(DungeonRank rank) {
        if (rank==null) return true;
        return dungeonData.getPortalPositions().get(rank).isEmpty();
    }

    public void addPortalPosition(BlockPos pos, DungeonRank rank) {
        if (rank==null) return;
        if (!dungeonData.getPortalPositions().get(rank).contains(pos))
            dungeonData.getPortalPositions().get(rank).add(pos);
        setDirty();
    }

    public void removePortalPosition(BlockPos pos, DungeonRank rank) {
        if (rank==null) return;
        dungeonData.getPortalPositions().get(rank).remove(pos);
        setDirty();
    }

    public List<BlockPos> getAllPortalPositions(DungeonRank rank) {
        if (rank==null) return null;
        return new ArrayList<>(dungeonData.getPortalPositions().get(rank));
    }

    public DungeonRank getNextToCollapse() {
        return dungeonData.getNextToCollapse();
    }

    public void SelectNextForCollapse() {
        dungeonData.setNextToCollapse(DungeonRank.getNext(getNextToCollapse()));
        setDirty();
    }

    public boolean isFinishedForcedChunks() {
        return dungeonData.getFinishedForcedChunks();
    }

    public void setFinishedForcedChunks() {
        dungeonData.setFinishedForcedChunks(true);
        setDirty();
    }

    public void serverUpdateTimeLeft(MinecraftServer server) {
        dungeonData.serverUpdateTimeLeft(server);
        PacketDistributor.sendToAllPlayers(dungeonData.createPacket());
    }

    public static class PayloadHandler implements IPayloadHandler<DungeonNBTData.DataPacket> {
        @Override
        public void handle(DungeonNBTData.DataPacket dataPacket, IPayloadContext iPayloadContext) {
            iPayloadContext.enqueueWork(() -> {
                        CLIENT_DATA = dataPacket;
                    })
                    .exceptionally(e -> {
                        // Handle exception
                        iPayloadContext.disconnect(Component.translatable("thedungeon.networking.failed", e.getMessage()));
                        return null;
                    });

        }
    }
}
