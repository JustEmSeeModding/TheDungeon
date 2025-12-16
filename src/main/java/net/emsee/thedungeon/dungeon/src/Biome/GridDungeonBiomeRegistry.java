package net.emsee.thedungeon.dungeon.src.Biome;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.registry.DungeonBiome;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

public class GridDungeonBiomeRegistry extends DungeonBiomeRegistry{
    final Map<Vec3i, DungeonBiome> registeredBiomes = new HashMap<>();
    int gridCellWidth;
    int gridCellHeight;
    BlockPos center;

    public GridDungeonBiomeRegistry(int gridCellWidth, int gridCellHeight, BlockPos center) {
        this.gridCellWidth = gridCellWidth;
        this.gridCellHeight = gridCellHeight;
        this.center = center;
    }

    @Override
    public CompoundTag toCompound() {
        final CompoundTag tag = new CompoundTag();
        tag.putString("className", this.getClass().getName());
        tag.putInt("gridWidth",gridCellWidth);
        tag.putInt("gridHeight",gridCellHeight);
        tag.putInt("centerX",center.getX());
        tag.putInt("centerY",center.getY());
        tag.putInt("centerZ",center.getZ());
        int i = 0;
        for (Vec3i vec : registeredBiomes.keySet()) {
            DungeonBiome biome = registeredBiomes.get(vec);
            tag.putInt("pos_" + i + "_x", vec.getX());
            tag.putInt("pos_" + i + "_y", vec.getY());
            tag.putInt("pos_" + i + "_z", vec.getZ());
            tag.putString("biome_" + i, biome.name());
            i++;
        }
        return tag;
    }

    @Override
    public void fromCompoundLocal(CompoundTag tag) {
        this.gridCellWidth = tag.getInt("gridWidth");
        this.gridCellHeight = tag.getInt("gridHeight");
        this.center = new BlockPos(
                tag.getInt("centerX"),
                tag.getInt("centerY"),
                tag.getInt("centerZ"));
        int i = 0;
        while (tag.contains("biome_" + i)) {
            Vec3i vec = new Vec3i(tag.getInt("pos_" + i + "_x"), tag.getInt("pos_" + i + "_y"), tag.getInt("pos_" + i + "_z"));
            String biomeName = tag.getString("biome_" + i);
            try {
                this.registeredBiomes.put(vec, DungeonBiome.valueOf(biomeName));
            } catch (IllegalArgumentException e) {
                DebugLog.logWarn(DebugLog.DebugType.SAVE_DATA, "Unknown biome name during load: " + biomeName);
            }
            i++;
        }
    }


    @Override
    public DungeonBiome getBiomeAt(BlockPos pos) {
        return registeredBiomes.get(getGridCell(pos));
    }

    public Vec3i getGridCell(BlockPos pos) {
        if (gridCellWidth == 0 || gridCellHeight == 0) return new Vec3i(0,0,0);

        BlockPos dif = pos.subtract(center);

        // offset the grid to the bottom corner
        dif = dif.offset(new Vec3i(
                gridCellWidth/2,
                0,
                gridCellWidth/2));
        DebugLog.logInfo(DebugLog.DebugType.IMPORTANT, dif + "");

        return new Vec3i(
                Math.floorDiv(dif.getX(), gridCellWidth),
                Math.floorDiv(dif.getY(), gridCellHeight),
                Math.floorDiv(dif.getZ(), gridCellWidth)
        );
    }

    public void setBiomeAt(Vec3i cellPos, DungeonBiome biome) {
        registeredBiomes.put(cellPos, biome);
    }
}
