package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.damageType.ModDamageTypes;
import net.emsee.thedungeon.worldgen.biome.ModBiomes;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

// world gen and damage types

public final class ModDatapackBuiltinEntriesProvider extends DatapackBuiltinEntriesProvider {
    static RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType)
            .add(Registries.BIOME, ModBiomes::bootstrap)
            .add(Registries.LEVEL_STEM, ModDimensions::bootstrapStem)
            .add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap);

    ModDatapackBuiltinEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(TheDungeon.MOD_ID));
    }
}
