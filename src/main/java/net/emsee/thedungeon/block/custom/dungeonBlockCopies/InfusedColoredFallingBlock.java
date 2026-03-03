package net.emsee.thedungeon.block.custom.dungeonBlockCopies;

import net.emsee.thedungeon.block.custom.interfaces.IDungeonPickaxeMinable;
import net.emsee.thedungeon.item.custom.DungeonItem;
import net.emsee.thedungeon.item.interfaces.ICanTakeItemToDungeon;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Supplier;

public class InfusedColoredFallingBlock extends ColoredFallingBlock implements ICanTakeItemToDungeon, IDungeonPickaxeMinable {
    final Block baseBlock;

    public InfusedColoredFallingBlock(ColorRGBA dustColor, Properties properties, Block baseBlock) {
        super(dustColor, properties);
        this.baseBlock = baseBlock;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item. TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(DungeonItem.DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public Supplier<BlockState> getMinedReplacement() {
        return baseBlock::defaultBlockState;
    }
}
