package net.emsee.thedungeon.dungeon.room;

import net.emsee.thedungeon.dungeon.mobSpawnRules.MobSpawnRule;
import net.emsee.thedungeon.dungeon.util.Connection;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.*;
import java.util.function.Consumer;

public class GridRoomGroup extends AbstractGridRoom {

    protected final WeightedMap.Int<AbstractGridRoom> gridRooms = new WeightedMap.Int<>();

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
                new GridRoomBasic(recourseLocation,gridWidth, gridHeight)
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
    public GridRoomGroup addRoom(AbstractGridRoom gridRoom) {

        if (!ListAndArrayUtils.mapEquals(connections, gridRoom.connections))
            throw new IllegalStateException(this+":added room does not have the same connections as the group");

        if (!ListAndArrayUtils.mapEquals(connectionTags, gridRoom.connectionTags))
            throw new IllegalStateException(this+":added room does not have the same connection tags as the group");

        if (this.heightScale != gridRoom.heightScale || this.northSizeScale != gridRoom.northSizeScale || this.eastSizeScale != gridRoom.eastSizeScale)
            throw new IllegalStateException(this+":added room does not have the same scale as the group");

        gridRooms.put(gridRoom, gridRoom.getWeight());
        return this;
    }

    protected GridRoomGroup setRoomMap(WeightedMap.Int<AbstractGridRoom> rooms) {
        gridRooms.clear();
        for (AbstractGridRoom room : rooms.keySet())
            gridRooms.put(room.getCopy(), rooms.get(room));
        return this;
    }

    public GridRoomGroup applyToAll(Consumer<AbstractGridRoom> method) {
        ListAndArrayUtils.mapForEachSafe(gridRooms, (k, v)-> method.accept(k));
        return this;
    }

    @Override
    public AbstractGridRoom withStructureProcessor(StructureProcessor processor) {
        return applyToAll(room -> room.withStructureProcessor(processor));
    }

    @Override
    public AbstractGridRoom clearStructureProcessors() {
        return applyToAll(AbstractGridRoom::clearStructureProcessors);
    }

    @Override
    public AbstractGridRoom addMobSpawnRule(MobSpawnRule rule) {
        return applyToAll(room -> room.addMobSpawnRule(rule));
    }

    @Override
    public AbstractGridRoom setConnectionTag(Connection connection, String tag) {
        super.setConnectionTag(connection, tag);
        return applyToAll(room->room.setConnectionTag(connection, tag));
    }

    @Override
    public AbstractGridRoom setAllConnectionTags(String tag) {
        super.setAllConnectionTags(tag);
        return applyToAll(room->room.setAllConnectionTags(tag));
    }

    @Override
    protected AbstractGridRoom setConnectionTags(Map<Connection, String> tags) {
        super.setConnectionTags(tags);
        return applyToAll(room->room.setConnectionTags(tags));
    }

    @Override
    public AbstractGridRoom setGenerationPriority(int generationPriority) {
        return applyToAll(room -> room.setGenerationPriority(generationPriority));
    }

    @Override
    public AbstractGridRoom setOverrideEndChance(float value) {
        return applyToAll(room -> room.setOverrideEndChance(value));
    }

    @Override
    public AbstractGridRoom skipCollectionProcessors() {
        return applyToAll(AbstractGridRoom::skipCollectionProcessors);
    }

    @Override
    @Deprecated
    protected AbstractGridRoom setOverrideEndChance(float value, boolean doOverride) {
        throw new IllegalStateException(this+ ":setOverrideEndChance(v,d) should not be used for groups");
    }

    @Override
    @Deprecated
    public float getOverrideEndChance() {
        throw new IllegalStateException(this+ ":getOverrideEndChance() should not be used for groups");
    }

    @Override
    @Deprecated
    protected AbstractGridRoom setStructureProcessors(StructureProcessorList processors) {
        throw new IllegalStateException(this+ ":setStructureProcessors(p) should not be used for groups");
    }

    @Override
    @Deprecated
    public StructureProcessorList getStructureProcessors() {
        throw new IllegalStateException(this+ ":getStructureProcessors() should not be used for groups");
    }

    @Override
    public void placeFeature(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList processors, Random random) {
        throw new IllegalStateException("placeFeature cant be called on a group");
    }

    @Override
    @Deprecated
    protected AbstractGridRoom setSpawnRules(List<MobSpawnRule> list) {
        throw new IllegalStateException(this+ ":setSpawnRules(l) should not be used for groups");
    }

    //methods

    /**
     * Creates a deep copy of this GridRoomGroup, including all properties and contained rooms.
     *
     * @return a new GridRoomGroup instance with identical configuration and room mappings
     */
    @Override
    public AbstractGridRoom getCopy() {
        return new GridRoomGroup(gridWidth, gridHeight, differentiationID)
                .setRoomMap(gridRooms)
                .setSizeHeight(northSizeScale, eastSizeScale, heightScale)
                .setOffsets(connectionOffsets)
                .setConnectionTags(connectionTags)
                .setConnections(connections)
                .doAllowRotation(allowRotation, allowUpDownConnectedRotation)
                .withWeight(weight);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GridRoomGroup otherGroup &&
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
                differentiationID == otherGroup.differentiationID &&
                skipCollectionProcessors == otherGroup.skipCollectionProcessors;
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
        result = 31 * result + (skipCollectionProcessors ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "roomGroup, rooms:" + ListAndArrayUtils.mapToString(gridRooms);
    }

    public AbstractGridRoom getRandom(Random random) {
        AbstractGridRoom toReturn = gridRooms.getRandom(random);
        if (toReturn == null)
            throw new IllegalStateException("error choosing room");
        return toReturn.getCopy();
    }
}
