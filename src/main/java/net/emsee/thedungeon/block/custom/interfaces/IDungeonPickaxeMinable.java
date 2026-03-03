package net.emsee.thedungeon.block.custom.interfaces;

import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public interface IDungeonPickaxeMinable {
    Supplier<BlockState> getMinedReplacement();
}
