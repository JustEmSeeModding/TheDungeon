package net.emsee.thedungeon.dungeon.src.Biome;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.emsee.thedungeon.dungeon.registry.DungeonBiome;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public abstract class DungeonBiomeRegistry {

    public abstract CompoundTag toCompound();

    public static DungeonBiomeRegistry fromCompound(CompoundTag tag) {
        String className = tag.getString("className");

        //TODO make it use the class name
        DungeonBiomeRegistry toReturn = new GridDungeonBiomeRegistry(0,0, new BlockPos(0,0,0));

        toReturn.fromCompoundLocal(tag);
        return toReturn;
    }
    protected abstract void fromCompoundLocal(CompoundTag tag);

    /**
     * returns null if no biome
     */
    public abstract DungeonBiome getBiomeAt(BlockPos pos);

    /*public static final PrimitiveCodec<DungeonBiomeRegistry> CODEC = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<DungeonBiomeRegistry> read(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input).flatMap(save -> save != null ? DataResult.success(fromCompound(save)) : DataResult.error(() -> "Error"));
        }

        @Override
        public <T> T write(DynamicOps<T> ops, DungeonBiomeRegistry value) {
            return ops.createString(value.toCompound());
        }
    };*/
}
