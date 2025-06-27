package net.emsee.thedungeon.dungeon.room;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.resources.ResourceLocation;

import java.util.Random;

/** Similar to GridRoomGroup but only uses multiple resource locations */
public class MultiResourceGridRoom extends GridRoom {
    WeightedMap.Int<ResourceLocation> resourceLocations = new WeightedMap.Int<>();

    public MultiResourceGridRoom(int gridWidth, int gridHeight) {
        super(gridWidth, gridHeight);
    }

    public MultiResourceGridRoom(int gridWidth, int gridHeight, int ID) {
        super(gridWidth, gridHeight, ID);
    }

    @Deprecated
    @Override
    public GridRoom withResourceLocation(ResourceLocation resourceLocation) {
        throw new IllegalStateException("Use withResourceLocation(R,I) instead");
    }

    @Override
    public GridRoom withResourceLocation(String path) {
        throw new IllegalStateException("Use withResourceLocation(S,I) instead");
    }

    @Override
    public GridRoom withResourceLocation(String nameSpace, String path) {
        throw new IllegalStateException("Use withResourceLocation(S,S,I) instead");
    }

    public MultiResourceGridRoom withResourceLocation(ResourceLocation resourceLocation, int weight) {
        if (resourceLocations.containsKey(resourceLocation))
            throw new IllegalStateException("MultiResourceGridRoom already contains this ResourceLocation (" + resourceLocation + ")");
        resourceLocations.put(resourceLocation, weight);
        return this;
    }

    public MultiResourceGridRoom withResourceLocation(String path, int weight) {
        return this.withResourceLocation(TheDungeon.MOD_ID, path, weight);
    }

    public MultiResourceGridRoom withResourceLocation(String nameSpace, String path, int weight) {
        return this.withResourceLocation(ResourceLocation.fromNamespaceAndPath(nameSpace, path), weight);
    }

    protected MultiResourceGridRoom setResourceLocations(WeightedMap.Int<ResourceLocation> resourceLocations) {
        this.resourceLocations.clear();
        this.resourceLocations.putAll(resourceLocations);
        return this;
    }

    @Override
    public ResourceLocation getResourceLocation(Random random) {
        return resourceLocations.getRandom(random);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof MultiResourceGridRoom otherRoom &&
                resourceLocation == otherRoom.resourceLocation &&
                gridWidth == otherRoom.gridWidth &&
                gridHeight == otherRoom.gridHeight &&
                ListAndArrayUtils.mapEquals(connections, otherRoom.connections) &&
                weight == otherRoom.weight &&
                allowRotation == otherRoom.allowRotation &&
                allowUpDownConnectedRotation == otherRoom.allowUpDownConnectedRotation &&
                northSizeScale == otherRoom.northSizeScale &&
                eastSizeScale == otherRoom.eastSizeScale &&
                heightScale == otherRoom.heightScale &&
                connectionOffsets.equals(otherRoom.connectionOffsets) &&
                connectionTags.equals(otherRoom.connectionTags) &&
                generationPriority == otherRoom.generationPriority &&
                overrideEndChance == otherRoom.overrideEndChance &&
                doOverrideEndChance == otherRoom.doOverrideEndChance &&
                ListAndArrayUtils.listEquals(spawnRules, otherRoom.spawnRules) &&
                ListAndArrayUtils.listEquals(structureProcessors.list(), otherRoom.structureProcessors.list()) &&
                differentiationID == otherRoom.differentiationID &&
                ListAndArrayUtils.mapEquals(resourceLocations, otherRoom.resourceLocations) &&
                skipCollectionProcessors == otherRoom.skipCollectionProcessors;

    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (resourceLocation != null ? resourceLocation.hashCode() : 0);
        result = 31 * result + gridWidth;
        result = 31 * result + gridHeight;
        result = 31 * result + connections.hashCode();
        result = 31 * result + weight;
        result = 31 * result + (allowRotation ? 1 : 0);
        result = 31 * result + (allowUpDownConnectedRotation ? 1 : 0);
        result = 31 * result + Double.hashCode(northSizeScale);
        result = 31 * result + Double.hashCode(eastSizeScale);
        result = 31 * result + Double.hashCode(heightScale);
        result = 31 * result + connectionOffsets.hashCode();
        result = 31 * result + connectionTags.hashCode();
        result = 31 * result + generationPriority;
        result = 31 * result + Double.hashCode(overrideEndChance);
        result = 31 * result + (doOverrideEndChance ? 1 : 0);
        result = 31 * result + spawnRules.hashCode();
        result = 31 * result + structureProcessors.list().hashCode();
        result = 31 * result + differentiationID;
        result = 31 * result + resourceLocations.hashCode();
        result = 31 * result + (skipCollectionProcessors ? 1:0);
        return result;
    }

    /**
     * Creates a copy of this.
     */
    public GridRoom getCopy() {
        return new MultiResourceGridRoom(gridWidth, gridHeight, differentiationID)
                .setResourceLocations(resourceLocations)
                .setSizeHeight(northSizeScale, eastSizeScale, heightScale)
                .setOffsets(connectionOffsets)
                .setConnectionTags(connectionTags)
                .setConnections(connections)
                .doAllowRotation(allowRotation, allowUpDownConnectedRotation)
                .withWeight(weight)
                .setGenerationPriority(generationPriority)
                .setOverrideEndChance(overrideEndChance, doOverrideEndChance)
                .setSpawnRules(spawnRules)
                .setStructureProcessors(structureProcessors)
                .setSkipCollectionProcessors(skipCollectionProcessors);

    }

    @Override
    public String toString() {
        return "MultiResourceGridRoom:"+ListAndArrayUtils.mapToString(resourceLocations);
    }
}
