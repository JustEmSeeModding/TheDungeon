package net.emsee.thedungeon.dungeon.room;

import net.emsee.thedungeon.dungeon.mobSpawnRules.MobSpawnRule;
import net.emsee.thedungeon.dungeon.util.Connection;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.*;
import java.util.function.Consumer;

public class GridRoomGroup extends GridRoom {

    protected final WeightedMap.Int<GridRoom> gridRooms = new WeightedMap.Int<>();

    public GridRoomGroup(int gridWidth, int gridHeight, int ID) {
        super(gridWidth, gridHeight, ID);
    }

    public GridRoomGroup(int gridWidth, int gridHeight) {
        super(gridWidth, gridHeight);
    }


    /**
     * Adds a new simple room to the group with the specified resource location and weight.
     */
    public GridRoomGroup addSimpleRoom(String recourseLocation, int weight) {
        addRoom(
                new GridRoom(gridWidth, gridHeight).withResourceLocation(recourseLocation)
                        .withWeight(weight).
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

        if (!ListAndArrayUtils.mapEquals(connections, gridRoom.connections))
            throw new IllegalStateException(this+":added room does not have the same connections as the group");

        if (!ListAndArrayUtils.mapEquals(connectionTags, gridRoom.connectionTags))
            throw new IllegalStateException(this+":added room does not have the same connection tags as the group");

        if (this.heightScale != gridRoom.heightScale || this.northSizeScale != gridRoom.northSizeScale || this.eastSizeScale != gridRoom.eastSizeScale)
            throw new IllegalStateException(this+":added room does not have the same scale as the group");

        gridRooms.put(gridRoom, gridRoom.getWeight());
        return this;
    }

    protected GridRoomGroup setRoomMap(WeightedMap.Int<GridRoom> rooms) {
        gridRooms.clear();
        for (GridRoom room : rooms.keySet())
            gridRooms.put(room.getCopy(), rooms.get(room));
        return this;
    }

    public GridRoomGroup applyToAll(Consumer<GridRoom> method) {
        ListAndArrayUtils.mapForEachSafe(gridRooms, (k, v)-> method.accept(k));
        //gridRooms.keySet().forEach(method);
        return this;
    }

    @Override
    public GridRoom withStructureProcessor(StructureProcessor processor) {
        return applyToAll(room -> room.withStructureProcessor(processor));
    }

    @Override
    public GridRoom clearStructureProcessors() {
        return applyToAll(GridRoom::clearStructureProcessors);
    }

    @Override
    public GridRoom addMobSpawnRule(MobSpawnRule rule) {
        return applyToAll(room -> room.addMobSpawnRule(rule));
    }

    @Override
    public GridRoom setConnectionTag(Connection connection, String tag) {
        super.setConnectionTag(connection, tag);
        return applyToAll(room->room.setConnectionTag(connection, tag));
    }

    @Override
    public GridRoom setAllConnectionTags(String tag) {
        super.setAllConnectionTags(tag);
        return applyToAll(room->room.setAllConnectionTags(tag));
    }

    @Override
    protected GridRoom setConnectionTags(Map<Connection, String> tags) {
        super.setConnectionTags(tags);
        return applyToAll(room->room.setConnectionTags(tags));
    }

    @Override
    public GridRoom setGenerationPriority(int generationPriority) {
        return applyToAll(room -> room.setGenerationPriority(generationPriority));
    }

    @Override
    public GridRoom setOverrideEndChance(float value) {
        return applyToAll(room -> room.setOverrideEndChance(value));
    }

    @Override
    @Deprecated
    public GridRoom withResourceLocation(String path) {
        throw new IllegalStateException(this+ ":withResourceLocation(p) should not be used for groups");
    }

    @Override
    @Deprecated
    public GridRoom withResourceLocation(String nameSpace, String path) {
        throw new IllegalStateException(this+ ":withResourceLocation(n,p) should not be used for groups");
    }

    @Override
    @Deprecated
    public GridRoom withResourceLocation(ResourceLocation resourceLocation) {
        throw new IllegalStateException(this+ ":withResourceLocation(r) should not be used for groups");
    }

    @Override
    @Deprecated
    protected GridRoom setOverrideEndChance(float value, boolean doOverride) {
        throw new IllegalStateException(this+ ":setOverrideEndChance(v,d) should not be used for groups");
    }

    @Override
    @Deprecated
    public float getOverrideEndChance() {
        throw new IllegalStateException(this+ ":getOverrideEndChance() should not be used for groups");
    }

    @Override
    @Deprecated
    protected GridRoom setStructureProcessors(StructureProcessorList processors) {
        throw new IllegalStateException(this+ ":setStructureProcessors(p) should not be used for groups");
    }

    @Override
    @Deprecated
    public StructureProcessorList getStructureProcessors() {
        throw new IllegalStateException(this+ ":getStructureProcessors() should not be used for groups");
    }

    @Override
    @Deprecated
    protected GridRoom setSpawnRules(List<MobSpawnRule> list) {
        throw new IllegalStateException(this+ ":setSpawnRules(l) should not be used for groups");
    }

    //methods
    @Override
    public ResourceLocation getResourceLocation(Random random) {
        throw new IllegalStateException(this+ ":getResourceLocation(r) should not be used for groups");
    }

    /**
     * Creates a deep copy of this GridRoomGroup, including all properties and contained rooms.
     *
     * @return a new GridRoomGroup instance with identical configuration and room mappings
     */
    @Override
    public GridRoom getCopy() {
        return new GridRoomGroup(gridWidth, gridHeight, differentiationID)
                .setRoomMap(gridRooms)
                .setSizeHeight(northSizeScale, eastSizeScale, heightScale)
                .setOffsets(connectionOffsets)
                .setConnectionTags(connectionTags)
                .setConnections(connections)
                .doAllowRotation(allowRotation, allowUpDownConnectedRotation)
                .withWeight(weight);
                //.setGenerationPriority(generationPriority).
                //.setOverrideEndChance(overrideEndChance, doOverrideEndChance).
                //.setSpawnRules(spawnRules).
                //.setStructureProcessors(structureProcessors);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof GridRoomGroup otherGroup) {
            return
                    gridWidth == otherGroup.gridWidth &&
                            gridHeight == otherGroup.gridHeight &&
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
                            ListAndArrayUtils.listEquals(structureProcessors.list(), otherGroup.structureProcessors.list()) &&
                            differentiationID == otherGroup.differentiationID;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + gridWidth;
        result = 31 * result + gridHeight;
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
        result = 31 * result + differentiationID;
        return result;
    }

    @Override
    public String toString() {
        return "roomGroup, rooms:" + ListAndArrayUtils.mapToString(gridRooms);
    }

    public GridRoom getRandom(Random random) {
        GridRoom toReturn = gridRooms.getRandom(random);
        if (toReturn == null)
            throw new IllegalStateException("error choosing room");
        return toReturn.getCopy();
    }
}
