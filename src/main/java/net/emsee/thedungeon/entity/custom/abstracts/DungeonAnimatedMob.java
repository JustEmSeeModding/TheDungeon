package net.emsee.thedungeon.entity.custom.abstracts;

import net.emsee.thedungeon.entity.custom.interfaces.IAnimatedEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public abstract class DungeonAnimatedMob extends DungeonPathfinderMob
        implements IAnimatedEntity {

    private static final EntityDataAccessor<Integer> ATTACK_ID =
            SynchedEntityData.defineId(DungeonAnimatedMob.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Byte> ATTACK_VERSION =
            SynchedEntityData.defineId(DungeonAnimatedMob.class, EntityDataSerializers.BYTE);

    protected DungeonAnimatedMob(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACK_ID, -1);
        builder.define(ATTACK_VERSION, (byte) 0);
    }

    @Override
    public void startAttackAnimation(int animationId, byte version) {
        this.entityData.set(ATTACK_ID, animationId);
        this.entityData.set(ATTACK_VERSION, version);
    }

    public int getAttackAnimationId() {
        return entityData.get(ATTACK_ID);
    }

    public byte getAttackAnimationVersion() {
        return entityData.get(ATTACK_VERSION);
    }
}
