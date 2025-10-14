package net.emsee.thedungeon.worldSaveData.NBT;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.src.types.Dungeon;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.dungeon.registry.ModDungeons;
import net.emsee.thedungeon.dungeon.src.types.DungeonInstance;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.emsee.thedungeon.network.ModNetworking;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.worldSaveData.DungeonSaveData;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.*;

public final class DungeonNBTData {
    private final Queue<DungeonInstance<?>> dungeonProgressQueue = new LinkedList<>();
    private final Map<DungeonRank, Queue<DungeonInstance<?>>> dungeonPassiveQueue = Util.make(Maps.newHashMap(), (map) -> {
        for (DungeonRank rank : DungeonRank.values())
            map.put(rank, new LinkedList<>());
    });
    private long timeLeft = -1;
    private long lastExecutionTime = -1;
    private long lastMinuteAnnouncement = -1;
    private long lastSecondAnnouncement = -1;
    private DungeonRank nextToCollapse = DungeonRank.F;
    private boolean finishedForcedChunks = false;

    private final Map<DungeonRank, Boolean> isOpen = Util.make(Maps.newHashMap(), (map) -> {
        for (DungeonRank rank : DungeonRank.values())
            map.put(rank, false);
    });
    private final Map<DungeonRank, List<BlockPos>> portalPositions = Util.make(Maps.newHashMap(), (map) -> {
        for (DungeonRank rank : DungeonRank.values())
            map.put(rank, new ArrayList<>());
    });

    public CompoundTag SerializeNBT() {
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"Serializing NBT:");
        CompoundTag toReturn = new CompoundTag();
        toReturn.putLong("lastExecutionTime", lastExecutionTime);
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"lastExecutionTime: {}", lastExecutionTime);
        toReturn.putLong("lastMinuteAnnouncement", lastMinuteAnnouncement);
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"lastMinuteAnnouncement: {}", lastMinuteAnnouncement);
        toReturn.putLong("lastSecondAnnouncement", lastSecondAnnouncement);
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"lastSecondAnnouncement: {}", lastSecondAnnouncement);
        toReturn.putBoolean("finishedForcedChunks", finishedForcedChunks);
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"finishedForcedChunks: {}", finishedForcedChunks);
        for (DungeonRank rank : isOpen.keySet())
            toReturn.putBoolean("isOpen_" + rank.getName(), isOpen.get(rank));
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"isOpen: {}", ListAndArrayUtils.mapToString(isOpen));
        if (nextToCollapse!= null) toReturn.putString("nextToCollapse", nextToCollapse.getName());
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"nextToCollapse: {}", nextToCollapse);

        //TODO re add dungeon queues to save files
        /*
        for (DungeonInstance<?> dungeon : dungeonProgressQueue) {
            toReturn.putString("dungeonProgressQueue_" + i, dungeon.getResourceName());
            i++;
        }
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"dungeonProgressQueue: {}", dungeonProgressQueue);


        for (DungeonRank rank : dungeonPassiveQueue.keySet()) {
            for (DungeonInstance<?> dungeon : dungeonPassiveQueue.get(rank)) {
                toReturn.putString("dungeonPassiveQueue_" + rank.getName() + "_" + i, dungeon.getResourceName());
                i++;
            }
        }
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"dungeonPassiveQueue: {}", ListAndArrayUtils.mapToString(dungeonPassiveQueue));
        */
        int i = 0;
        for (DungeonRank rank : portalPositions.keySet()) {
            for (BlockPos pos : portalPositions.get(rank)) {
                toReturn.putInt("PortalPosX_" + rank.getName() + "_" + i, pos.getX());
                toReturn.putInt("PortalPosY_" + rank.getName() + "_" + i, pos.getY());
                toReturn.putInt("PortalPosZ_" + rank.getName() + "_" + i, pos.getZ());
                i++;
            }
        }
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"portalPositions: {}", ListAndArrayUtils.mapToString(portalPositions));
        return toReturn;
    }

    public void DeserializeNBT(CompoundTag tag) {
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"Deserializing NBT:");
        lastExecutionTime = tag.getLong("lastExecutionTime");
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"lastExecutionTime: {}", lastExecutionTime);
        lastMinuteAnnouncement = tag.getLong("lastMinuteAnnouncement");
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"lastMinuteAnnouncement: {}", lastMinuteAnnouncement);
        lastSecondAnnouncement = tag.getLong("lastSecondAnnouncement");
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"lastSecondAnnouncement: {}", lastSecondAnnouncement);
        finishedForcedChunks = tag.getBoolean("finishedForcedChunks");
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"finishedForcedChunks: {}", finishedForcedChunks);
        for (DungeonRank rank : DungeonRank.values())
            isOpen.put(rank,tag.getBoolean("isOpen_" + rank.getName()));
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"isOpen: {}", ListAndArrayUtils.mapToString(isOpen));
        nextToCollapse = DungeonRank.getByName(tag.getString("nextToCollapse"));
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"nextToCollapse: {}", nextToCollapse);
        //TODO re add dungeon queues to save files
        /*
        dungeonProgressQueue.clear();
        while (tag.contains("dungeonProgressQueue_" + i)) {
            DungeonInstance<?> toAdd = ModDungeons.GetByResourceName(tag.getString("dungeonProgressQueue_" + i));
            dungeonProgressQueue.add(toAdd.getCopy());
            i++;
        }
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"dungeonProgressQueue: {}", dungeonProgressQueue);

        for (DungeonRank rank : dungeonPassiveQueue.keySet()) {
            i = 0;
            Queue<DungeonInstance<?>> passiveQueue = dungeonPassiveQueue.get(rank);
            while (tag.contains("dungeonPassiveQueue_" + rank.getName() + "_" + i)) {
                DungeonInstance<?> toAdd = ModDungeons.GetByResourceName(tag.getString("dungeonPassiveQueue_" + rank.getName() + "_" + i));
                passiveQueue.add(toAdd.getCopy());
                i++;
            }
        }
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"dungeonPassiveQueue: {}", ListAndArrayUtils.mapToString(dungeonPassiveQueue));
        */
        for (DungeonRank rank : portalPositions.keySet()) {
            int i = 0;
            portalPositions.get(rank).clear();
            while (tag.contains("PortalPosX_" + rank.getName() + "_" + i)) {
                portalPositions.get(rank).add(new BlockPos(
                        tag.getInt("PortalPosX_" + rank.getName() + "_" + i),
                        tag.getInt("PortalPosY_" + rank.getName() + "_" + i),
                        tag.getInt("PortalPosZ_" + rank.getName() + "_" + i)));
                i++;
            }
        }
        DebugLog.logInfo(DebugLog.DebugType.SAVE_DATA_DETAILED,"portalPositions: {}", ListAndArrayUtils.mapToString(portalPositions));
    }


    public long getLastExecutionTime() {
        return lastExecutionTime;
    }

    public long getLastMinuteAnnouncement() {
        return lastMinuteAnnouncement;
    }

    public long getLasSecondAnnouncement() {
        return lastSecondAnnouncement;
    }

    public void setLastExecutionTime(long lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
    }

    public void setLastMinuteAnnouncement(long lastMinuteAnnouncement) {
        this.lastMinuteAnnouncement = lastMinuteAnnouncement;
    }

    public void setLastSecondAnnouncement(long lastSecondAnnouncement) {
        this.lastSecondAnnouncement = lastSecondAnnouncement;
    }

    public Queue<DungeonInstance<?>> getDungeonProgresQueue() {
        return dungeonProgressQueue;
    }

    public Queue<DungeonInstance<?>> getDungeonPassiveQueue(DungeonRank rank) {
        if (rank==null) return null;
        return dungeonPassiveQueue.get(rank);
    }

    public boolean getDungeonOpen(DungeonRank rank) {
        return isOpen.get(rank);
    }

    public void setDungeonOpen(DungeonRank rank, boolean is) {
        if (rank == null) return;
        isOpen.put(rank, is);
    }

    public Map<DungeonRank, List<BlockPos>> getPortalPositions() {
        return portalPositions;
    }

    public DungeonRank getNextToCollapse() {
        return nextToCollapse;
    }

    public void setNextToCollapse(DungeonRank rank) {
        if (rank==null) return;
        nextToCollapse = rank;
    }

    public void setFinishedForcedChunks(Boolean finishedForcedChunks) {
        this.finishedForcedChunks = finishedForcedChunks;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void serverUpdateTimeLeft(MinecraftServer server) {
        long worldTime = server.overworld().getGameTime();
        timeLeft = GameruleRegistry.getIntegerGamerule(server, ModGamerules.TICKS_BETWEEN_COLLAPSES) - (worldTime - lastExecutionTime);
    }

    public DataPacket createPacket() {
        return new DataPacket(timeLeft, nextToCollapse, finishedForcedChunks);
    }

    public Boolean getFinishedForcedChunks() {
        return finishedForcedChunks;
    }

    public record DataPacket(
    long timeLeft,
    DungeonRank nextToCollapse,
    boolean finishedForcedChunks
    ) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<DataPacket> TYPE = new CustomPacketPayload.Type<>(ModNetworking.DUNGEON_DATA_SYNC);

        public static final StreamCodec<ByteBuf, DataPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_LONG,
                DataPacket::timeLeft,
                ByteBufCodecs.fromCodec(DungeonRank.CODEC),
                DataPacket::nextToCollapse,
                ByteBufCodecs.BOOL,
                DataPacket::finishedForcedChunks,
                DataPacket::new
                );

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
