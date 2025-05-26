package net.emsee.thedungeon.dungeon.room;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class GridRoomGroup extends GridRoom {
    /** Weighted map */
    protected Map<GridRoom, Integer> gridRooms = new HashMap<>();

    //constructor
    public GridRoomGroup(int gridWidth, int gridHeight) {
        super(gridWidth, gridHeight);
    }

    //construction methods
    public GridRoomGroup addSimpleRoom(String recourseLocation, int Weight) {
        addRoom(
                new GridRoom(gridWidth, gridHeight).withResourceLocation(recourseLocation).withWeight(weight).
                        setSizeHeight(northSizeScale, eastSizeScale, heightScale).
                        setOffsets(connectionOffsets).
                        setConnectionTags(connectionTags).
                        setConnections(connections).
                        doAllowRotation(allowRotation, allowUpDownConnectedRotation).
                        setGenerationPriority(generationPriority).
                        setOverrideEndChance(overrideEndChance, doOverrideEndChance).
                        setSpawnRules(spawnRules)
        );

        return this;
    }

    /**
     * A roomGroup must always have the same connections
     */
    public GridRoomGroup addRoom(GridRoom gridRoom) {

        //for (GridRoomUtils.Connection connection : connections.keySet()) {
            //gridRoom.HasConnection(connection, connectionTags.get(connection));

        if (!ListAndArrayUtils.mapEquals(connections, gridRoom.connections)) {
            TheDungeon.LOGGER.error("LargeRoom ({}) does not have the same connections/Tags as RoomGroup ({})", gridRoom, this);
            return this;
        }




        if (this.heightScale != gridRoom.heightScale || this.northSizeScale != gridRoom.northSizeScale || this.eastSizeScale != gridRoom.eastSizeScale) {
            TheDungeon.LOGGER.error("LargeRoom ({}) does not have the same Scale as RoomGroup ({})", gridRoom, this);
            return this;
        }

        gridRooms.put(gridRoom, gridRoom.getWeight());


        return this;
    }

    protected GridRoomGroup setRoomMap(Map<GridRoom, Integer> rooms) {
        gridRooms = rooms;
        return this;
    }

    /**
     * Gridroom group should not have resourceLocation
     */
    @Deprecated
    @Override
    public GridRoom withResourceLocation(ResourceLocation resourceLocation) {
        TheDungeon.LOGGER.error("Gridroom group should not have resourceLocation");
        return this;
    }

    //methods

    @Override
    public ResourceLocation getResourceLocation(Random random) {
        return null;
    }

    @Override
    public GridRoom getCopy() {
        return new GridRoomGroup(gridWidth, gridHeight).
                setRoomMap(gridRooms).
                setSizeHeight(northSizeScale, eastSizeScale, heightScale).
                setOffsets(connectionOffsets).
                setConnectionTags(connectionTags).
                setConnections(connections).
                doAllowRotation(allowRotation, allowUpDownConnectedRotation).
                withWeight(weight).
                setGenerationPriority(generationPriority).
                setOverrideEndChance(overrideEndChance, doOverrideEndChance).
                setSpawnRules(spawnRules)
                .setStructureProcessors(structureProcessors);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof GridRoomGroup otherGroup) {
            return
                    gridWidth == otherGroup.gridWidth &&
                            gridHeight == otherGroup.gridHeight &&
                            resourceLocation == otherGroup.resourceLocation &&
                            ListAndArrayUtils.mapEquals(connections, otherGroup.connections) &&
                            weight == otherGroup.weight &&
                            allowRotation == otherGroup.allowRotation &&
                            allowUpDownConnectedRotation == otherGroup.allowUpDownConnectedRotation &&
                            northSizeScale == otherGroup.northSizeScale &&
                            eastSizeScale == otherGroup.eastSizeScale &&
                            heightScale == otherGroup.heightScale &&
                            ListAndArrayUtils.mapEquals(gridRooms, otherGroup.gridRooms) &&
                            connectionOffsets.equals(otherGroup.connectionOffsets) &&
                            connectionTags.equals(otherGroup.connectionTags) &&
                            generationPriority == otherGroup.generationPriority &&
                            overrideEndChance == otherGroup.overrideEndChance &&
                            doOverrideEndChance == otherGroup.doOverrideEndChance &&
                            ListAndArrayUtils.listEquals(spawnRules, otherGroup.spawnRules) &&
                            ListAndArrayUtils.listEquals(structureProcessors.list(), otherGroup.structureProcessors.list());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + gridWidth;
        result = 31 * result + gridHeight;
        result = 31 * result + (resourceLocation != null ? resourceLocation.hashCode() : 0);
        result = 31 * result + (connections != null ? connections.hashCode() : 0);
        result = 31 * result + weight;
        result = 31 * result + (allowRotation ? 1 : 0);
        result = 31 * result + (allowUpDownConnectedRotation ? 1 : 0);
        result = 31 * result + Double.hashCode(northSizeScale);
        result = 31 * result + Double.hashCode(eastSizeScale);
        result = 31 * result + Double.hashCode(heightScale);
        result = 31 * result + (gridRooms != null ? gridRooms.hashCode() : 0);
        result = 31 * result + (connectionOffsets != null ? connectionOffsets.hashCode() : 0);
        result = 31 * result + (connectionTags != null ? connectionTags.hashCode() : 0);
        result = 31 * result + generationPriority;
        result = 31 * result + Double.hashCode(overrideEndChance);
        result = 31 * result + (doOverrideEndChance ? 1 : 0);
        result = 31 * result + (spawnRules != null ? spawnRules.hashCode() : 0);
        result = 31 * result + (structureProcessors != null ? structureProcessors.list().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "roomGroup";
    }

    public GridRoom getRandom(Random random) {
        return ListAndArrayUtils.getRandomFromWeightedMapI(gridRooms, random).getCopy();
    }
}
