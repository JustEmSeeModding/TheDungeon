package net.emsee.thedungeon.block.custom;

import net.emsee.thedungeon.block.custom.interfaces.IDungeonPickaxeMinable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class SimpleMinableBlock extends DungeonBlock implements IDungeonPickaxeMinable {
    private final Supplier<BlockState> replaceBlock;

    public SimpleMinableBlock(Properties properties, Supplier<BlockState> replaceBlock) {
        super(properties);
        this.replaceBlock = replaceBlock;
    }

    @Override
    public Supplier<BlockState> getMinedReplacement() {
        return replaceBlock;
    }
}
