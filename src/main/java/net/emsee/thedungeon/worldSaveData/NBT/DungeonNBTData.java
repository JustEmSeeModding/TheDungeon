package net.emsee.thedungeon.worldSaveData.NBT;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.ModDungeons;
import net.emsee.thedungeon.dungeon.dungeon.Dungeon;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

public final class DungeonNBTData {
    private static final boolean addCleanupToStart = false;

    private final Queue<Dungeon> dungeonProgressQueue = new LinkedList<>();
    private final Queue<Dungeon> dungeonPassiveQueue = new LinkedList<>();
    //TODO convert to this VV
    /*private final Map<Dungeon.DungeonRank,Queue<Dungeon>> dungeonProgressQueue =
            Map.of(
                    Dungeon.DungeonRank.F, new LinkedList<>(),
                    Dungeon.DungeonRank.E, new LinkedList<>(),
                    Dungeon.DungeonRank.D, new LinkedList<>(),
                    Dungeon.DungeonRank.C, new LinkedList<>(),
                    Dungeon.DungeonRank.B, new LinkedList<>(),
                    Dungeon.DungeonRank.A, new LinkedList<>(),
                    Dungeon.DungeonRank.S, new LinkedList<>(),
                    Dungeon.DungeonRank.SS, new LinkedList<>()
            );
    private final Map<Dungeon.DungeonRank,Queue<Dungeon>>  dungeonPassiveQueue = Map.of(
            Dungeon.DungeonRank.F, new LinkedList<>(),
            Dungeon.DungeonRank.E, new LinkedList<>(),
            Dungeon.DungeonRank.D, new LinkedList<>(),
            Dungeon.DungeonRank.C, new LinkedList<>(),
            Dungeon.DungeonRank.B, new LinkedList<>(),
            Dungeon.DungeonRank.A, new LinkedList<>(),
            Dungeon.DungeonRank.S, new LinkedList<>(),
            Dungeon.DungeonRank.SS, new LinkedList<>()
    );*/
    private int tickInterval = 30/*-minutes -> to Ticks*/ *60*20;
    private long lastExecutionTime = -1;
    private long lastMinuteAnnouncement = -1;
    private long lastSecondAnnouncement = -1;
    private boolean isOpen;
    private boolean isTiming = true;
    private final List<BlockPos> portalPositions = new ArrayList<>();

    /**
     * Serializes the current dungeon state and related metadata into a CompoundTag.
     *
     * The serialized data includes timing fields, open and timing status, the contents of the dungeon progress and passive queues, and all portal positions.
     *
     * @return a CompoundTag containing all relevant dungeon state information
     */
    public CompoundTag SerializeNBT() {
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("Serializing:");
        CompoundTag toReturn = new CompoundTag();
        toReturn.putLong("lastExecutionTime", lastExecutionTime);
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("lastExecutionTime: {}", lastExecutionTime);
        toReturn.putLong("lastMinuteAnnouncement", lastMinuteAnnouncement);
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("lastMinuteAnnouncement: {}", lastMinuteAnnouncement);
        toReturn.putLong("lastSecondAnnouncement", lastSecondAnnouncement);
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("lastSecondAnnouncement: {}", lastSecondAnnouncement);
        toReturn.putBoolean("isOpen", isOpen);
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("isOpen: {}", isOpen);
        toReturn.putBoolean("isTiming", isTiming);
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("isTiming: {}", isTiming);
        Queue<Dungeon> tempProgressQueue = new LinkedList<>(dungeonProgressQueue);
        int i = 0;
        while (!tempProgressQueue.isEmpty()) {
            //TheDungeon.LOGGER.info("");
            toReturn.putString("dungeonProgressQueue" + i, tempProgressQueue.remove().GetResourceName());
            i++;
        }
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("dungeonProgressQueue: {}", dungeonProgressQueue);
        Queue<Dungeon> tempPassiveQueue = new LinkedList<>(dungeonPassiveQueue);
        i = 0;
        while (!tempPassiveQueue.isEmpty()) {
            //TheDungeon.LOGGER.info("");
            toReturn.putString("dungeonPassiveQueue" + i, tempPassiveQueue.remove().GetResourceName());
            i++;
        }
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("dungeonPassiveQueue: {}", dungeonPassiveQueue);
        List<BlockPos> tempPosList = new ArrayList<>(portalPositions);
        i = 0;
        while (!tempPosList.isEmpty()) {
            //TheDungeon.LOGGER.info("");
            BlockPos pos = tempPosList.getFirst();
            toReturn.putInt("PortalPosX" + i, pos.getX());
            toReturn.putInt("PortalPosY" + i, pos.getY());
            toReturn.putInt("PortalPosZ" + i, pos.getZ());
            i++;
            tempPosList.removeFirst();
        }
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("portalPositions: {}", portalPositions);
        return toReturn;
    }

    /**
     * Restores the dungeon state from the provided CompoundTag.
     *
     * Reads and sets all internal fields, including timing values, open and timing flags, dungeon progress and passive queues, and portal positions, based on the serialized data in the tag. Existing queues and position lists are cleared before repopulation. If configured, a cleanup dungeon may be added at the start of the progress queue.
     *
     * @param tag the CompoundTag containing serialized dungeon data
     */
    public void DeserializeNBT(CompoundTag tag) {
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("Deserializing:");
        lastExecutionTime = tag.getLong("lastExecutionTime");
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("lastExecutionTime: {}", lastExecutionTime);
        lastMinuteAnnouncement = tag.getLong("lastMinuteAnnouncement");
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("lastMinuteAnnouncement: {}", lastMinuteAnnouncement);
        lastSecondAnnouncement = tag.getLong("lastSecondAnnouncement");
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("lastSecondAnnouncement: {}", lastSecondAnnouncement);
        isOpen = tag.getBoolean("isOpen");
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("isOpen: {}", isOpen);
        isTiming = tag.getBoolean("isTiming");
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("isTiming: {}", isTiming);

        int i = 0;
        dungeonProgressQueue.clear();
        while (tag.contains("dungeonProgressQueue" + i)) {
            Dungeon toAdd = ModDungeons.GetByResourceName(tag.getString("dungeonProgressQueue" + i));
            if (addCleanupToStart && i == 0 && toAdd != ModDungeons.CLEANUP_OLD) {
                dungeonProgressQueue.add(ModDungeons.CLEANUP_OLD.GetCopy());
            }
            dungeonProgressQueue.add(toAdd.GetCopy());
            i++;
        }
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("dungeonProgressQueue: {}", dungeonProgressQueue);

        i = 0;
        dungeonPassiveQueue.clear();
        while (tag.contains("dungeonPassiveQueue" + i)) {
            Dungeon toAdd = ModDungeons.GetByResourceName(tag.getString("dungeonPassiveQueue" + i));
            dungeonPassiveQueue.add(toAdd.GetCopy());
            i++;
        }
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("dungeonPassiveQueue: {}", dungeonPassiveQueue);

        i = 0;
        portalPositions.clear();
        while (tag.contains("PortalPosX" + i)) {
            portalPositions.add(new BlockPos(tag.getInt("PortalPosX" + i), tag.getInt("PortalPosY" + i), tag.getInt("PortalPosZ" + i)));
            i++;
        }
        if (TheDungeon.debugMode.is(TheDungeon.DebugMode.ALL)) TheDungeon.LOGGER.info("portalPositions: {}", portalPositions);
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

    public Queue<Dungeon> getDungeonPassiveQueue() {return dungeonPassiveQueue;}

    public boolean getDungeonOpen() {
        return isOpen;
    }

    public void setDungeonOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isTiming() {return isTiming;}
    public void setTiming(boolean timing) {isTiming = timing;}

    public List<BlockPos> getPortalPositions() {
        return portalPositions;
    }

    public void setTickInterval(int tickInterval) {this.tickInterval = tickInterval;}
    public int getTickInterval() {return this.tickInterval;}
}
