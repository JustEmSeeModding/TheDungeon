package net.emsee.thedungeon.worldSaveData.NBT;

import net.emsee.thedungeon.dungeon.ModDungeons;
import net.emsee.thedungeon.dungeon.dungeon.Dungeon;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class DungeonNBTData {
    private static final boolean addCleanupToStart = false;

    private final Queue<Dungeon> dungeonProgressQueue = new LinkedList<>();
    private final Queue<Dungeon> dungeonPassiveQueue = new LinkedList<>();
    private int tickInterval = 30/*-minutes -> to Ticks*/ *60*20;
    private long lastExecutionTime = -1;
    private long lastMinuteAnnouncement = -1;
    private long lastSecondAnnouncement = -1;
    private boolean isOpen;
    private boolean isTiming = true;
    private final List<BlockPos> portalPositions = new ArrayList<>();

    public CompoundTag SerializeNBT() {
        CompoundTag toReturn = new CompoundTag();
        toReturn.putLong("lastExecutionTime", lastExecutionTime);
        toReturn.putLong("lastMinuteAnnouncement", lastMinuteAnnouncement);
        toReturn.putLong("lastSecondAnnouncement", lastSecondAnnouncement);
        toReturn.putBoolean("isOpen", isOpen);
        toReturn.putBoolean("isTiming", isTiming);
        Queue<Dungeon> tempProgressQueue = new LinkedList<>(dungeonProgressQueue);
        int i = 0;
        while (!tempProgressQueue.isEmpty()) {
            //TheDungeon.LOGGER.info("");
            toReturn.putString("dungeonProgressQueue" + i, tempProgressQueue.remove().GetResourceName());
            i++;
        }

        Queue<Dungeon> tempPassiveQueue = new LinkedList<>(dungeonProgressQueue);
        i = 0;
        while (!tempPassiveQueue.isEmpty()) {
            //TheDungeon.LOGGER.info("");
            toReturn.putString("dungeonPassiveQueue" + i, tempPassiveQueue.remove().GetResourceName());
            i++;
        }

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
        return toReturn;
    }

    public void DeserializeNBT(CompoundTag tag) {
        lastExecutionTime = tag.getLong("lastExecutionTime");
        lastMinuteAnnouncement = tag.getLong("lastMinuteAnnouncement");
        lastSecondAnnouncement = tag.getLong("lastSecondAnnouncement");
        isOpen = tag.getBoolean("isOpen");
        isTiming = tag.getBoolean("isTiming");

        int i = 0;
        dungeonProgressQueue.clear();
        while (tag.contains("dungeonQueue" + i)) {
            Dungeon toAdd = ModDungeons.GetByResourceName(tag.getString("dungeonProgressQueue" + i));
            if (addCleanupToStart && i == 0 && toAdd != ModDungeons.CLEANUP) {
                dungeonProgressQueue.add(ModDungeons.CLEANUP.GetCopy());
            }
            dungeonProgressQueue.add(toAdd.GetCopy());
            i++;
        }

        i = 0;
        dungeonPassiveQueue.clear();
        while (tag.contains("dungeonQueue" + i)) {
            Dungeon toAdd = ModDungeons.GetByResourceName(tag.getString("dungeonPassiveQueue" + i));
            dungeonPassiveQueue.add(toAdd.GetCopy());
            i++;
        }

        i = 0;
        portalPositions.clear();
        while (tag.contains("PortalPosX" + i)) {
            portalPositions.add(new BlockPos(tag.getInt("PortalPosX" + i), tag.getInt("PortalPosY" + i), tag.getInt("PortalPosZ" + i)));
            i++;
        }
    }

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
