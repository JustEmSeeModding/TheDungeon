package net.emsee.thedungeon.block.custom;

import net.emsee.thedungeon.block.custom.interfaces.IDungeonPickaxeMinable;
import net.emsee.thedungeon.item.custom.DungeonItem;
import net.emsee.thedungeon.item.interfaces.ICanTakeItemToDungeon;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Supplier;

public class DungeonAmethystClusterBlock extends AmethystClusterBlock implements ICanTakeItemToDungeon, IDungeonPickaxeMinable {
    public DungeonAmethystClusterBlock(float height, float aabbOffset, Properties properties) {
        super(height, aabbOffset, properties);
    }
    @Override
    public void appendHoverText(ItemStack stack, Item. TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(DungeonItem.DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public Supplier<BlockState> getMinedReplacement() {
        return Blocks.AIR::defaultBlockState;
    }
}
