package net.emsee.thedungeon.entity.custom.goblin.hobGoblin;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.item.ModItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/// uses the same logic as wandering trader trades
public class HobGoblinTrades {
    // the item used for trading
    private static final ItemLike cashItem = ModItems.PYRITE_COIN;

    public static Int2ObjectMap<VillagerTrades.ItemListing[]> getHobGoblinTrades(HobGoblinEntity.Variant variant) {
        return switch (variant) {
            case FIGHTER ->
                    toIntMap(ImmutableMap.of(
                            // common trades
                            1, new VillagerTrades.ItemListing[]{
                                    new ItemsForCash(ModItems.PYRITE_COMPASS, 5, 1,2, 1)
                            },

                            // rare trades
                            2,new VillagerTrades.ItemListing[]{
                                    new ItemsForCash(ModItems.CATALYST_CORE, 14, 1, 1),
                            }));
            case FORGER ->
                    toIntMap(ImmutableMap.of(
                            // common trades
                            1, new VillagerTrades.ItemListing[]{
                                    new ItemsForCash(ModItems.INFUSED_ALLOY_INGOT, 3, 1,7, 1),
                                    new ItemsForCash(ModItems.KOBALT_INGOT, 2, 1,5, 1),
                                    new CashForItems(Items.COPPER_INGOT, 1,8,1),
                                    new CashForItems(Items.IRON_INGOT, 1,8,2),
                                    new CashForItems(ModItems.DUNGEON_ESSENCE_SHARD, 1,10,1),
                                    new ItemsForCash(ModItems.GOBLINS_FORGEHAMMER, 16, 1,1),
                            },

                            // rare trades
                            2,new VillagerTrades.ItemListing[]{
                                    new ItemsForCash(ModItems.CATALYST_CORE, 14, 1,1),
                            }));
            case SCAVENGER -> toIntMap(ImmutableMap.of(
                    // common trades
                    1, new VillagerTrades.ItemListing[]{
                            new ItemsForCash(ModItems.PYRITE_COMPASS, 5, 1,1),
                            new ItemsForCash(ModBlocks.INFUSED_THREAD, 3, 1,3),
                    },

                    // rare trades
                    2,new VillagerTrades.ItemListing[]{
                            new ItemsForCash(ModItems.SHATTERED_CATALYST_CORE, 14, 1,1),
                    }));
            case MINER -> toIntMap(ImmutableMap.of(
                    // common trades
                    1, new VillagerTrades.ItemListing[]{
                            new ItemsForCash(Items.COAL, 1, 2,9),
                            new ItemsForCash(Items.RAW_IRON, 2, 1,7),
                            new ItemsForCash(Items.RAW_COPPER, 1, 1,7),
                            new ItemsForCash(Items.DIAMOND, 4, 1,3),
                    },

                    // rare trades
                    2,new VillagerTrades.ItemListing[]{
                            new ItemsForCash(Items.EMERALD, 3, 1,3),
                            new CashForItems(ModItems.INFUSED_CHISEL, 1, 2,9),
                    }));
        };

    }




    private static Int2ObjectMap<VillagerTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> map) {
        return new Int2ObjectOpenHashMap<>(map);
    }

    static class ItemsForCash implements VillagerTrades.ItemListing {
        private final ItemStack itemStack;
        private final int cost;
        private final int maxUses;
        private final float priceMultiplier;
        private final ResourceKey<EnchantmentProvider> enchantmentProvider;

        public ItemsForCash(Block block, int cost, int numberOfItems, int maxUses) {
            this(new ItemStack(block), cost, numberOfItems, maxUses);
        }

        public ItemsForCash(ItemLike item, int cost, int numberOfItems) {
            this(new ItemStack(item), cost, numberOfItems, 12);
        }

        public ItemsForCash(ItemLike item, int cost, int numberOfItems, int maxUses) {
            this(new ItemStack(item), cost, numberOfItems, maxUses);
        }

        public ItemsForCash(ItemStack itemStack, int cost, int numberOfItems, int maxUses) {
            this(itemStack, cost, numberOfItems, maxUses, 0.05F);
        }

        public ItemsForCash(ItemLike item, int cost, int numberOfItems, int maxUses, float priceMultiplier) {
            this(new ItemStack(item), cost, numberOfItems, maxUses, priceMultiplier);
        }

        public ItemsForCash(ItemLike item, int cost, int numberOfItems, int maxUses, float priceMultiplier, ResourceKey<EnchantmentProvider> enchantmentProvider) {
            this(new ItemStack(item), cost, numberOfItems, maxUses, priceMultiplier, enchantmentProvider);
        }

        public ItemsForCash(ItemStack itemStack, int cost, int numberOfItems, int maxUses, float priceMultiplier) {
            this(itemStack, cost, numberOfItems, maxUses, priceMultiplier, null);
        }

        public ItemsForCash(ItemStack itemStack, int cost, int numberOfItems, int maxUses, float priceMultiplier, ResourceKey<EnchantmentProvider> enchantmentProvider) {
            this.itemStack = itemStack;
            this.cost = cost;
            this.itemStack.setCount(numberOfItems);
            this.maxUses = maxUses;
            this.priceMultiplier = priceMultiplier;
            this.enchantmentProvider = enchantmentProvider;
        }

        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            ItemStack itemstack = this.itemStack.copy();
            Level level = trader.level();
            if (enchantmentProvider!=null)
                EnchantmentHelper.enchantItemFromProvider(itemstack, level.registryAccess(), enchantmentProvider, level.getCurrentDifficultyAt(trader.blockPosition()), random);
            return new MerchantOffer(new ItemCost(cashItem, this.cost), itemstack, this.maxUses, 1, this.priceMultiplier);
        }
    }

    public static class CashForItems implements VillagerTrades.ItemListing {
        private final ItemCost itemStack;
        private final int maxUses;
        private final int cashAmount;
        private final float priceMultiplier;

        public CashForItems(ItemLike item, int cost, int maxUses) {
            this(item, cost, maxUses, 1);
        }

        public CashForItems(ItemLike item, int cost, int maxUses, int cashAmount) {
            this(new ItemCost(item.asItem(), cost), maxUses, cashAmount);
        }

        public CashForItems(ItemCost itemStack, int maxUses, int cashAmount) {
            this.itemStack = itemStack;
            this.maxUses = maxUses;
            this.cashAmount = cashAmount;
            this.priceMultiplier = 0.05F;
        }

        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            return new MerchantOffer(this.itemStack, new ItemStack(cashItem, this.cashAmount), this.maxUses, 1, this.priceMultiplier);
        }
    }
}
