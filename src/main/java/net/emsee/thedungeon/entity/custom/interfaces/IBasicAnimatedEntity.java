package net.emsee.thedungeon.entity.custom.interfaces;

public interface IBasicAnimatedEntity {
    boolean isPlayingAttackAnimation();

    default void startRunning() {}
    default void stopRunning() {}
    boolean isRunning();
}
