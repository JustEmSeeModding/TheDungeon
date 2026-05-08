package net.emsee.thedungeon.block.custom;

import net.emsee.thedungeon.block.custom.interfaces.IDungeonPickaxeMinable;
import net.emsee.thedungeon.item.custom.DungeonItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
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

    /*@Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("block.thedungeon.required_mining_level"));
    }*/
}
