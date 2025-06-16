package net.emsee.thedungeon.worldgen.dimention;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.worldgen.biome.ModBiomes;
import net.emsee.thedungeon.worldgen.chunkGen.VoidChunkGenerator;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.OptionalLong;

/**
 * honestly, don't touch this, this is pain
 */
public final class ModDimensions {
    public static final ResourceKey<LevelStem> DUNGEON_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon"));
    public static final ResourceKey<Level> DUNGEON_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon"));
    public static final ResourceKey<DimensionType> DUNGEON_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon_type"));

    public static void bootstrapType(BootstrapContext<DimensionType> context) {
        context.register(DUNGEON_TYPE, new DimensionType(
                OptionalLong.of(6000), // fixedTime
                true, // hasSkylight //<<TODO what is better?
                false, // hasCeiling
                false, // ultraWarm
                false, // natural
                1.0, // coordinateScale
                false, // bedWorks
                false, // respawnAnchorWorks
                -96, // minY
                320+96, // height
                320+96, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
    }

    public static void bootstrapStem(BootstrapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator wrappedChunkGenerator =
                new VoidChunkGenerator(
                        //new NoiseBasedChunkGenerator(
                        new FixedBiomeSource(biomeRegistry.getOrThrow(ModBiomes.DUNGEON_BIOME)),
                        noiseGenSettings.getOrThrow(NoiseGeneratorSettings.END));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.DUNGEON_TYPE), wrappedChunkGenerator);

        context.register(DUNGEON_KEY, stem);
    }
}
