package net.emsee.thedungeon.dungeon.src.Biome;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.registry.DungeonBiome;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public abstract class DungeonBiomeRegistry {

    public abstract CompoundTag toCompound();

    public static DungeonBiomeRegistry fromCompound(CompoundTag tag) {
        String className = tag.getString("className");
        DungeonBiomeRegistry toReturn = null;

        if (className.equals(GridDungeonBiomeRegistry.class.getName()))
            toReturn = new GridDungeonBiomeRegistry(0,0, new BlockPos(0,0,0));
        else {
            DebugLog.logError(DebugLog.DebugType.WARNINGS, "BiomeRegistry class not handled: {}", className);
        }

        if (toReturn!=null) toReturn.fromCompoundLocal(tag);
        return toReturn;
    }
    protected abstract void fromCompoundLocal(CompoundTag tag);

    /**
     * returns null if no biome
     */
    public abstract DungeonBiome getBiomeAt(BlockPos pos);
}
