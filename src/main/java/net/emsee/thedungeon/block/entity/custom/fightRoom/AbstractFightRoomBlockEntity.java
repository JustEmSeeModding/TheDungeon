package net.emsee.thedungeon.block.entity.custom.fightRoom;

import net.emsee.thedungeon.block.custom.fightRoom.AbstractFightRoomBlock;
import net.emsee.thedungeon.dungeon.src.mobSpawnRules.MobSpawnRule;
import net.emsee.thedungeon.utils.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractFightRoomBlockEntity extends BlockEntity {
    protected final Fight fight = setupFight();
    protected FightState fightState = FightState.UNSTARTED;
    protected final List<UUID> spawnedEntityUUIDs = new ArrayList<>();

    public AbstractFightRoomBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected abstract Fight setupFight();
    protected abstract BlockPos getNegativeCornerOffset();
    protected abstract BlockPos getPositiveCornerOffset();

    protected abstract void onFightStart();
    protected abstract void onProgressWave(int progress);
    protected abstract void onFightEnd();
    protected abstract void onFightEndEarly(int progress);

    public final void tryStartFight() {
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (fightState != FightState.UNSTARTED) return;

        Direction blockDirection =getBlockState().getValue(AbstractFightRoomBlock.FACING);
        List<UUID> spawned = fight.spawnNextWave(serverLevel, getBlockPos(), blockDirection, getMinCorner(blockDirection), getMaxCorner(blockDirection));
        spawnedEntityUUIDs.addAll(spawned);
        fightState = FightState.STARTED;
        onFightStart();
        setChanged();
    }

    public final void restartFight() {
        if (fightState != FightState.FINISHED) return;
        fightState = FightState.UNSTARTED;
        spawnedEntityUUIDs.clear();
        fight.reset();
        tryStartFight();
        setChanged();
    }

    public final void progressWave() {
        if (!(level instanceof ServerLevel serverLevel)) return;
        Direction blockDirection =getBlockState().getValue(AbstractFightRoomBlock.FACING);
        List<UUID> spawned = fight.spawnNextWave(serverLevel, getBlockPos(), blockDirection, getMinCorner(blockDirection), getMaxCorner(blockDirection));
        spawnedEntityUUIDs.addAll(spawned);
        onProgressWave(fight.wave);
        setChanged();
    }

    public final void finishFight() {
        fightState = FightState.FINISHED;
        spawnedEntityUUIDs.clear();
        onFightEnd();
        setChanged();
    }

    public void cutFightShort() {
        killAllSpawnedNoDrops();
        fightState = FightState.UNSTARTED;
        int progress = fight.wave;
        fight.reset();
        onFightEndEarly(progress);
        setChanged();
    }

    protected void killAllSpawnedNoDrops() {
        if (level == null || level.isClientSide) return;
        for (UUID uuid : spawnedEntityUUIDs) {
            Entity entity = ((ServerLevel) level).getEntity(uuid);
            if (entity != null) {
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
        }
        spawnedEntityUUIDs.clear();
        setChanged();
    }

    public BlockPos getMinCorner(Direction direction) {
        Rotation rotation = BlockUtils.directionToRotation(direction);
        BlockPos negativeCorner = getNegativeCornerOffset().rotate(rotation);
        BlockPos positiveCorner = getPositiveCornerOffset().rotate(rotation);

        int X = Math.min(positiveCorner.getX(), negativeCorner.getX());
        int Y = Math.min(positiveCorner.getY(), negativeCorner.getY());
        int Z = Math.min(positiveCorner.getZ(), negativeCorner.getZ());
        return getBlockPos().offset(X,Y,Z);
    }

    public BlockPos getMaxCorner(Direction direction) {
        Rotation rotation = BlockUtils.directionToRotation(direction);
        BlockPos negativeCorner = getNegativeCornerOffset().rotate(rotation);
        BlockPos positiveCorner = getPositiveCornerOffset().rotate(rotation);

        int X = Math.max(positiveCorner.getX(), negativeCorner.getX());
        int Y = Math.max(positiveCorner.getY(), negativeCorner.getY());
        int Z = Math.max(positiveCorner.getZ(), negativeCorner.getZ());
        return getBlockPos().offset(X,Y,Z);
    }

    public AABB getRoomAABB(Direction direction) {
        return new AABB(getMinCorner(direction).getCenter().add(-.5,-.5,-.5), getMaxCorner(direction).getCenter().add(.5,.5,.5));
    }

    protected boolean checkIfWaveOver() {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        spawnedEntityUUIDs.removeIf(uuid -> {
            Entity e = serverLevel.getEntity(uuid);
            return e == null || !e.isAlive();
        });
        setChanged();
        return spawnedEntityUUIDs.isEmpty();
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if (level == null || level.isClientSide) return;
        if (fightState != FightState.STARTED) return;
        boolean waveOver = checkIfWaveOver();
        boolean hasNextWave = fight.hasNextWave();
        if (waveOver) {
            if (hasNextWave) {
                progressWave();
                setChanged();
            } else {
                finishFight();
            }
        }

    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        ListTag list = new ListTag();
        for (UUID uuid : spawnedEntityUUIDs) {
            list.add(NbtUtils.createUUID(uuid));
        }
        tag.put("SpawnedEntities", list);
        tag.putString("FightState", fightState.name());
        tag.put("FightData", fight.toCompound());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        spawnedEntityUUIDs.clear();
        ListTag list = tag.getList("SpawnedEntities", Tag.TAG_INT_ARRAY);
        for (Tag t : list) {
            spawnedEntityUUIDs.add(NbtUtils.loadUUID(t));
        }
        fightState = FightState.valueOf(tag.getString("FightState"));
        fight.fromCompound(tag.getCompound("FightData"));
    }

    protected enum FightState {
        UNSTARTED,
        STARTED,
        FINISHED
    }

    public static class Fight {
        final List<FightWave> waves = new ArrayList<>();
        int wave = -1;

        public Fight addWave(FightWave wave) {
            waves.add(wave);
            return this;
        }

        public CompoundTag toCompound() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("Wave", wave);
            return tag;
        }

        public void fromCompound(CompoundTag tag) {
            wave = tag.getInt("Wave");
        }

        public List<UUID> spawnNextWave(ServerLevel level, BlockPos pos, Direction blockDirection, BlockPos minCorner, BlockPos maxCorner) {
            wave++;
            if (wave >= waves.size()) return List.of();

            FightWave fightWave = waves.get(wave);

            return fightWave.spawn(level, pos, blockDirection, minCorner, maxCorner);
        }

        public boolean hasNextWave() {
           return wave < waves.size()-1;
        }

        public void reset() {
            wave=-1;
        }
    }

    public static class FightWave {
        final List<MobSpawnRule> spawns = new ArrayList<>();
        protected List<UUID> spawn(ServerLevel level, BlockPos pos, Direction blockDirection, BlockPos minCorner, BlockPos maxCorner) {
            List<UUID> uuids = new ArrayList<>();

            for (MobSpawnRule spawn : spawns) {
                uuids.addAll(spawn.spawn(level, pos, minCorner, maxCorner, BlockUtils.directionToRotation(blockDirection)));
            }

            return uuids;
        }

        public FightWave addSpawn(MobSpawnRule spawnRule) {
            spawns.add(spawnRule);
            return this;
        }
    }
}
