package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.emsee.thedungeon.item.interfaces.IDungeonToolTips;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class DungeonDiggerItem extends DiggerItem implements IDungeonCarryItem, IDungeonToolTips {
    // List of blocks this tool can break
    protected static Map<Block,Block> getBreakableBlocks() {
        return Map.of(
                Blocks.GILDED_BLACKSTONE, Blocks.BLACKSTONE,
                Blocks.IRON_ORE, Blocks.STONE,
                Blocks.GOLD_ORE, Blocks.STONE,
                Blocks.DIAMOND_ORE, Blocks.STONE
        );
    }

    public DungeonDiggerItem(Tier tier, TagKey<Block> blocks, Properties properties) {
        super(tier, blocks, properties.stacksTo(1).component(DataComponents.CAN_BREAK, createAdventureCheck()));
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY)
            return getBreakableBlocks().containsKey(state.getBlock());
        else return super.canAttackBlock(state, level, pos, player);
    }

    private static AdventureModePredicate createAdventureCheck() {
        List<BlockPredicate> predicates = new ArrayList<>();
        Collection<Block> blocks = new ArrayList<>(getBreakableBlocks().keySet());
        BlockPredicate predicate = BlockPredicate.Builder.block()
                .of(blocks)
                .build();
        predicates.add(predicate);


        return new AdventureModePredicate(predicates, false); // true = show in tooltip
    }

    public void breakEvent(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player.isCreative()) return;
        Level level = player.level();
        if (level instanceof ServerLevel serverLevel) {
            BlockPos pos = event.getPos();
            BlockState oldState = event.getState();
            Block replaceBlock = getBreakableBlocks().get(oldState.getBlock());
            BlockEntity blockentity = oldState.hasBlockEntity() ? level.getBlockEntity(pos) : null;
            List<ItemStack> drops = Block.getDrops(oldState, serverLevel, pos, blockentity);
            for (ItemStack drop : drops)
                player.getInventory().placeItemBackInInventory(drop);
            level.setBlockAndUpdate(pos, replaceBlock.defaultBlockState());
        }
    }
}
