package net.emsee.thedungeon.worldgen.biome;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

/**
 * honestly, don't touch this, this is pain
 */
public final class ModBiomes {
    public static final ResourceKey<Biome> DUNGEON_BIOME = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon_biome"));


    public static void bootstrap(BootstrapContext<Biome> context) {
        context.register(DUNGEON_BIOME, dungeon_biome(context));
    }


    private static Biome dungeon_biome(BootstrapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));
        //we need to follow the same order as vanilla biomes for the BiomeDefaultFeatures
        //BiomeDefaultFeatures.addExtraGold(biomeBuilder);
        //biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_PLAINS);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0f)
                .temperature(0.7f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .fogColor(0)
                        .skyColor(0)
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533)
                        .backgroundMusic(Musics.GAME).build())
                .build();
    }
}
