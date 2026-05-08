package net.emsee.thedungeon.dungeon.src.types;

import net.emsee.thedungeon.Config;
import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.registry.ModDungeons;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.emsee.thedungeon.worldSaveData.DungeonSaveData;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class DungeonInstance<T extends Dungeon<?,?>>{
    protected final T dungeon;
    protected Long savedSeed = null;

    protected DungeonInstance(T dungeon) {
        this.dungeon = dungeon;
    }

    /**
     * starts dungeon generation.
     */
    public final void generate(ServerLevel level) {
        MinecraftServer server = level.getServer();
        // if a seed is still in memory, use that.
        if (!Config.OVERRIDE_DUNGEON_SEED.getAsBoolean() && savedSeed!=null) {
            generateSeeded(server,savedSeed);
            return;
        }

        long seed = Config.OVERRIDE_DUNGEON_SEED.getAsBoolean() ? Config.DUNGEON_SEED_OVERRIDE.getAsLong() : ThreadLocalRandom.current().nextLong();
        generateSeeded(server, seed);
    }

    /**
     * generates the dungeon with a seed
     */
    public final void generateSeeded(MinecraftServer server, long seed) {
        savedSeed = seed;
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        saveData.setDirty();
        localGenerateSeeded(seed, server.getLevel(ModDimensions.DUNGEON_LEVEL_KEY));
    }

    protected abstract void localGenerateSeeded(long seed, ServerLevel serverLevel);

    /**
     * runs every tick while generating.
     */
    public abstract void generationTick(ServerLevel serverLevel);


    /**
     * returns true if the dungeon has successfully generated
     */
    public abstract boolean isDoneGenerating();

    /**
     * returns true if the dungeon has started but not yet finished generating
     */
    public abstract boolean isBusyGenerating();

    public abstract boolean canManualStepNow();

    public DungeonRank getRank() {
        return dungeon.getRank();
    }

    public T getRaw() {
        return dungeon;
    }

    public static DungeonInstance<?> fromSaveTag(CompoundTag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Empty save tag");
        }
        String dungeonName = tag.getString("DungeonName");
        Dungeon<?, ?> dungeon = ModDungeons.getByName(dungeonName);
        if (dungeon == null) {
            throw new IllegalStateException("Unknown dungeon: " + dungeonName);
        }
        DungeonInstance<?> instance = dungeon.createInstance();
        instance.loadSaveTag(tag);
        return instance;
    }

    protected abstract void loadSaveTag(CompoundTag tag);

    public final CompoundTag getSaveTag() {
        CompoundTag tag = toSaveTag();
        Optional<ResourceKey<Dungeon<?,?>>> resourceKey = ModDungeons.DUNGEON_REGISTRY.getResourceKey(dungeon);
        if (resourceKey.isEmpty()) {
            DebugLog.logError(DebugLog.DebugType.WARNINGS, "Can't find resource key for dungeon: {}", dungeon);
            throw new IllegalStateException("Dungeon not registered: " + dungeon);
        }
        tag.putString("DungeonName", resourceKey.get().location().getPath());
        return tag;
    }

    protected abstract CompoundTag toSaveTag();

    public long getSavedSeed() {
        return savedSeed;
    }
}
