package net.emsee.thedungeon.block.entity.custom;

import net.emsee.thedungeon.block.custom.DungeonPortal;
import net.emsee.thedungeon.block.entity.ModBlockEntities;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class DungeonPortalBlockEntity extends BlockEntity {
    private float animRotation = 0;
    private float frameCount = 0;

    private final float ROTATION_SPEED = .5f; // degree per sec
    private final float UNSTABLE_ROTATION_SPEED = 1.5f; // degree per sec
    private final float HEIGHT_SINE_AMP = .1f; // block up/down
    private final float HEIGHT_SINE_FREQ = .01f; // oscillations per frame
    private final float PULSE_SINE_AMP = .003f; // 1+-x scale
    private final float PULSE_SINE_FREQ = .05f; // oscillations per frame


    public DungeonPortalBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DUNGEON_PORTAL_BLOCK_ENTITY.get(), pos, blockState);
    }

    @OnlyIn(Dist.CLIENT)
    public float getAnimationHeight() {
        return -.5f + ((float) Math.sin(frameCount * HEIGHT_SINE_FREQ) * HEIGHT_SINE_AMP);
    }

    @OnlyIn(Dist.CLIENT)
    public float getAnimationScale() {
        return 1 + ((float) Math.sin(frameCount * PULSE_SINE_FREQ) * PULSE_SINE_AMP);
    }

    @OnlyIn(Dist.CLIENT)
    public float getAnimationRotation() {
        boolean stable = getBlockState().getValue(DungeonPortal.STABLE);
        float speed = stable ? ROTATION_SPEED : UNSTABLE_ROTATION_SPEED;
        animRotation = animRotation + speed;
        if (animRotation >= 360) {
            animRotation -= 360;
        }
        return animRotation;
    }

    @OnlyIn(Dist.CLIENT)
    public void updateClientFrame() {
        frameCount++;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public DungeonRank getRank() {
        return getBlockState().getValue(DungeonPortal.RANK);
    }

    public boolean isStable() {
        return getBlockState().getValue(DungeonPortal.STABLE);
    }

    public boolean isExit() {
        return getBlockState().getValue(DungeonPortal.EXIT);
    }
}
