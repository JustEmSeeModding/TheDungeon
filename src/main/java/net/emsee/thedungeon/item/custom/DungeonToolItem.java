package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.block.custom.interfaces.IDungeonPickaxeMinable;
import net.emsee.thedungeon.dungeonClass.DungeonClass;
import net.emsee.thedungeon.dungeonClass.DungeonSubClass;
import net.emsee.thedungeon.item.DungeonItemRank;
import net.emsee.thedungeon.item.ModTiers;
import net.emsee.thedungeon.item.interfaces.ICanTakeItemToDungeon;
import net.emsee.thedungeon.utils.ModTags;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.*;


public class DungeonToolItem extends DungeonWeaponItem {

    final ModTiers miningTier;

    public DungeonToolItem(WeaponType weaponType, boolean isSweeping, ModTiers tier, DungeonItemRank rank, DeferredHolder<DungeonClass,?>[] classes, DeferredHolder<DungeonSubClass<?>,?>[] subClasses, Properties properties) {
        super(weaponType, isSweeping, tier, rank, classes, subClasses, properties.stacksTo(1).rarity(Rarity.RARE).component(DataComponents.CAN_BREAK, createAdventureCheck(tier)), tier.createToolProperties(BlockTags.MINEABLE_WITH_PICKAXE));
        this.miningTier = tier;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY)
            return state.is(ModTags.Blocks.DUNGEON_TOOL_MINABLE) && !state.is(miningTier.getIncorrectBlocksForDrops());
        else return super.canAttackBlock(state, level, pos, player);
    }

    private static AdventureModePredicate createAdventureCheck(ModTiers tier) {
        List<BlockPredicate> predicates = new ArrayList<>();

        BlockPredicate predicate = BlockPredicate.Builder.block()
                .of(ModTags.Blocks.DUNGEON_TOOL_MINABLE)
                //and not of tag :tier.getIncorrectBlocksForDrops()
                .build();

        predicates.add(predicate);

        return new AdventureModePredicate(predicates, false); // true = show in tooltip
    }

    public void breakEvent(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level level = player.level();

        if (player.isCreative()) return;
        if (level instanceof ServerLevel serverLevel) {
            BlockPos pos = event.getPos();
            ItemStack stack = player.getMainHandItem();
            BlockState oldState = event.getState();
            BlockState replaceBlock = getNewBlockStateFromMined(oldState);
            BlockEntity blockentity = oldState.hasBlockEntity() ? level.getBlockEntity(pos) : null;
            List<ItemStack> drops = new ArrayList<>();
            if (!oldState.requiresCorrectToolForDrops() || isCorrectToolForDrops(stack, oldState))
                drops = Block.getDrops(oldState, serverLevel, pos, blockentity);
            for (ItemStack drop : drops)
                if (drop.getItem() instanceof ICanTakeItemToDungeon)
                    player.getInventory().placeItemBackInInventory(drop);
            level.setBlockAndUpdate(pos, replaceBlock);
            stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }

    }

    private static BlockState getNewBlockStateFromMined(BlockState oldState) {
        BlockState replaceBlock = Blocks.AIR.defaultBlockState();
        if (oldState.getBlock() instanceof IDungeonPickaxeMinable minable) {
            replaceBlock = minable.getMinedReplacement().get();
        }
        return replaceBlock;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility) || super.canPerformAction(stack, itemAbility);
    }

    @Override
    public LinkedHashMap<Component, Component[]> getPrefixComponents(ItemStack stack) {
        LinkedHashMap<Component, Component[]> toReturn = super.getPrefixComponents(stack);
        toReturn.put(Component.translatable("item.thedungeon.tooltip.tool_type"),
                new Component[]{
                        Component.translatable("item.thedungeon.tooltip.tool_type.pickaxe").withStyle(ChatFormatting.GREEN),
                        Component.translatable("item.thedungeon.tooltip.tool.mining_level", getMiningLevel()).withStyle(ChatFormatting.GREEN)
                });
        return toReturn;
    }

    private int getMiningLevel() {
        return miningTier.getIntLevel();
    }
}
