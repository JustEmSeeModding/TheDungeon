package net.emsee.thedungeon.worldSaveData.NBT;

import com.google.common.collect.Maps;
import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.ModDungeons;
import net.emsee.thedungeon.dungeon.dungeon.Dungeon;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

public final class DungeonNBTData {
    private final Queue<Dungeon> dungeonProgressQueue = new LinkedList<>();
    private final Map<Dungeon.DungeonRank, Queue<Dungeon>> dungeonPassiveQueue = Util.make(Maps.newHashMap(), (map) -> {
        map.put(Dungeon.DungeonRank.F, new LinkedList<>());
        map.put(Dungeon.DungeonRank.E, new LinkedList<>());
        map.put(Dungeon.DungeonRank.D, new LinkedList<>());
        map.put(Dungeon.DungeonRank.C, new LinkedList<>());
        map.put(Dungeon.DungeonRank.B, new LinkedList<>());
        map.put(Dungeon.DungeonRank.A, new LinkedList<>());
        map.put(Dungeon.DungeonRank.S, new LinkedList<>());
        map.put(Dungeon.DungeonRank.SS, new LinkedList<>());
    });
    private int tickInterval = 10/*-minutes -> to Ticks*/ * 60 * 20;
    private long lastExecutionTime = -1;
    private long lastMinuteAnnouncement = -1;
    private long lastSecondAnnouncement = -1;
    private Dungeon.DungeonRank nextToCollapse = Dungeon.DungeonRank.F;

    private final Map<Dungeon.DungeonRank, Boolean> isOpen = Util.make(Maps.newHashMap(), (map) -> {
        map.put(Dungeon.DungeonRank.F, false);
        map.put(Dungeon.DungeonRank.E, false);
        map.put(Dungeon.DungeonRank.D, false);
        map.put(Dungeon.DungeonRank.C, false);
        map.put(Dungeon.DungeonRank.B, false);
        map.put(Dungeon.DungeonRank.A, false);
        map.put(Dungeon.DungeonRank.S, false);
        map.put(Dungeon.DungeonRank.SS, false);
    });
    private final Map<Dungeon.DungeonRank, List<BlockPos>> portalPositions = Map.of(
            Dungeon.DungeonRank.F, new ArrayList<>(),
            Dungeon.DungeonRank.E, new ArrayList<>(),
            Dungeon.DungeonRank.D, new ArrayList<>(),
            Dungeon.DungeonRank.C, new ArrayList<>(),
            Dungeon.DungeonRank.B, new ArrayList<>(),
            Dungeon.DungeonRank.A, new ArrayList<>(),
            Dungeon.DungeonRank.S, new ArrayList<>(),
            Dungeon.DungeonRank.SS, new ArrayList<>()
    );

    /**
     * Serializes the current dungeon state and related metadata into a CompoundTag.
     * <p>
     * The serialized data includes timing fields, open and timing status, the contents of the dungeon progress and passive queues, and all portal positions.
     *
     * @return a CompoundTag containing all relevant dungeon state information
     */
    public CompoundTag SerializeNBT() {
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"Serializing:");
        CompoundTag toReturn = new CompoundTag();
        toReturn.putLong("lastExecutionTime", lastExecutionTime);
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"lastExecutionTime: {}", lastExecutionTime);
        toReturn.putLong("lastMinuteAnnouncement", lastMinuteAnnouncement);
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"lastMinuteAnnouncement: {}", lastMinuteAnnouncement);
        toReturn.putLong("lastSecondAnnouncement", lastSecondAnnouncement);
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"lastSecondAnnouncement: {}", lastSecondAnnouncement);
        for (Dungeon.DungeonRank rank : isOpen.keySet())
            toReturn.putBoolean("isOpen_" + rank.getName(), isOpen.get(rank));
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"isOpen: {}", ListAndArrayUtils.mapToString(isOpen));
        if (nextToCollapse!= null) toReturn.putString("nextToCollapse", nextToCollapse.getName());
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"nextToCollapse: {}", nextToCollapse);

        int i = 0;
        for (Dungeon dungeon : dungeonProgressQueue) {
            toReturn.putString("dungeonProgressQueue" + i, dungeon.GetResourceName());
            i++;
        }
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"dungeonProgressQueue: {}", dungeonProgressQueue);

        i = 0;
        for (Dungeon.DungeonRank rank : dungeonPassiveQueue.keySet()) {
            for (Dungeon dungeon : dungeonPassiveQueue.get(rank)) {
                toReturn.putString("dungeonPassiveQueue_" + rank.getName() + "_" + i, dungeon.GetResourceName());
                i++;
            }
        }
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"dungeonPassiveQueue: {}", ListAndArrayUtils.mapToString(dungeonPassiveQueue));

        i = 0;
        for (Dungeon.DungeonRank rank : portalPositions.keySet()) {
            for (BlockPos pos : portalPositions.get(rank)) {
                toReturn.putInt("PortalPosX_" + rank.getName() + "_" + i, pos.getX());
                toReturn.putInt("PortalPosY_" + rank.getName() + "_" + i, pos.getY());
                toReturn.putInt("PortalPosZ_" + rank.getName() + "_" + i, pos.getZ());
                i++;
            }
        }
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"portalPositions: {}", ListAndArrayUtils.mapToString(portalPositions));
        return toReturn;
    }

    /**
     * Restores the dungeon state from the provided CompoundTag.
     * <p>
     * Reads and sets all internal fields, including timing values, open and timing flags, dungeon progress and passive queues, and portal positions, based on the serialized data in the tag. Existing queues and position lists are cleared before repopulation. If configured, a cleanup dungeon may be added at the start of the progress queue.
     *
     * @param tag the CompoundTag containing serialized dungeon data
     */
    public void DeserializeNBT(CompoundTag tag) {
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"Deserializing:");
        lastExecutionTime = tag.getLong("lastExecutionTime");
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"lastExecutionTime: {}", lastExecutionTime);
        lastMinuteAnnouncement = tag.getLong("lastMinuteAnnouncement");
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"lastMinuteAnnouncement: {}", lastMinuteAnnouncement);
        lastSecondAnnouncement = tag.getLong("lastSecondAnnouncement");
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"lastSecondAnnouncement: {}", lastSecondAnnouncement);
        for (Dungeon.DungeonRank rank : Dungeon.DungeonRank.values())
            isOpen.put(rank,tag.getBoolean("isOpen_" + rank.getName()));
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"isOpen: {}", ListAndArrayUtils.mapToString(isOpen));
        nextToCollapse = Dungeon.DungeonRank.getByName(tag.getString("nextToCollapse"));
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"nextToCollapse: {}", nextToCollapse);

        int i = 0;
        dungeonProgressQueue.clear();
        while (tag.contains("dungeonProgressQueue" + i)) {
            Dungeon toAdd = ModDungeons.GetByResourceName(tag.getString("dungeonProgressQueue" + i));
            dungeonProgressQueue.add(toAdd.GetCopy());
            i++;
        }
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"dungeonProgressQueue: {}", dungeonProgressQueue);

        for (Dungeon.DungeonRank rank : dungeonPassiveQueue.keySet()) {
            i = 0;
            Queue<Dungeon> passiveQueue = dungeonPassiveQueue.get(rank);
            while (tag.contains("dungeonPassiveQueue_" + rank.getName() + "_" + i)) {
                Dungeon toAdd = ModDungeons.GetByResourceName(tag.getString("dungeonPassiveQueue_" + rank.getName() + "_" + i));
                passiveQueue.add(toAdd.GetCopy());
                i++;
            }
        }
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"dungeonPassiveQueue: {}", ListAndArrayUtils.mapToString(dungeonPassiveQueue));

        for (Dungeon.DungeonRank rank : portalPositions.keySet()) {
            i = 0;
            portalPositions.get(rank).clear();
            while (tag.contains("PortalPosX" + i)) {
                portalPositions.get(rank).add(new BlockPos(
                        tag.getInt("PortalPosX_" + rank.getName() + "_" + i),
                        tag.getInt("PortalPosY_" + rank.getName() + "_" + i),
                        tag.getInt("PortalPosZ_" + rank.getName() + "_" + i)));
                i++;
            }
        }
        DebugLog.logInfo(DebugLog.DebugLevel.SAVE_DATA_DETAILED,"portalPositions: {}", ListAndArrayUtils.mapToString(portalPositions));
    }

    /**
     * Returns the timestamp of the last dungeon execution.
     *
     * @return the last execution time in milliseconds
     */
    public long GetLastExecutionTime() {
        return lastExecutionTime;
    }

    public long GetLastMinuteAnnouncement() {
        return lastMinuteAnnouncement;
    }

    public long GetLasSecondAnnouncement() {
        return lastSecondAnnouncement;
    }

    public void SetLastExecutionTime(long lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
    }

    public void SetLastMinuteAnnouncement(long lastMinuteAnnouncement) {
        this.lastMinuteAnnouncement = lastMinuteAnnouncement;
    }

    public void SetLastSecondAnnouncement(long lastSecondAnnouncement) {
        this.lastSecondAnnouncement = lastSecondAnnouncement;
    }

    public Queue<Dungeon> getDungeonProgresQueue() {
        return dungeonProgressQueue;
    }

    public Queue<Dungeon> getDungeonPassiveQueue(Dungeon.DungeonRank rank) {
        if (rank==null) return null;
        return dungeonPassiveQueue.get(rank);
    }

    public boolean getDungeonOpen(Dungeon.DungeonRank rank) {
        return isOpen.get(rank);
    }

    public void setDungeonOpen(Dungeon.DungeonRank rank, boolean is) {
        if (rank == null) return;
        isOpen.put(rank, is);
    }

    public Map<Dungeon.DungeonRank, List<BlockPos>> getPortalPositions() {
        return portalPositions;
    }

    public void setTickInterval(int tickInterval) {
        this.tickInterval = tickInterval;
    }

    public int getTickInterval() {
        return this.tickInterval;
    }

    public Dungeon.DungeonRank getNextToCollapse() {
        return nextToCollapse;
    }

    public void setNextToCollapse(Dungeon.DungeonRank rank) {
        if (rank==null) return;
        nextToCollapse = rank;
    }
}
