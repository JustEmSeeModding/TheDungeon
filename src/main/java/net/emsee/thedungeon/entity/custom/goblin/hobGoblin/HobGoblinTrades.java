package net.emsee.thedungeon.entity.custom.goblin.hobGoblin;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.emsee.thedungeon.item.ModItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

/// uses the same logic as wandering trader trades
public class HobGoblinTrades {
    // the item used for trading
    private static final ItemLike cashItem = ModItems.PYRITE;

    public static Int2ObjectMap<VillagerTrades.ItemListing[]> getHobGoblinTrades(HobGoblinEntity.Variant variant) {
        return switch (variant) {
            case FIGHTER ->
                    toIntMap(ImmutableMap.of(
                            // common trades
                            1, new VillagerTrades.ItemListing[]{
                                    new ItemsForCash(ModItems.INFUSED_DAGGER, 4, 1,2, 1),
                                    new ItemsForCash(ModItems.INFUSED_CHISEL, 4, 1,2, 1),
                                    new ItemsForCash(ModItems.PYRITE_COMPASS, 5, 1,2, 1)
                            },

                            // rare trades
                            2,new VillagerTrades.ItemListing[]{
                                    new ItemsForCash(ModItems.PORTAL_CORE, 14, 1, 1),
                            }));
            case FORGER ->
                    toIntMap(ImmutableMap.of(
                            // common trades
                            1, new VillagerTrades.ItemListing[]{
                                    new ItemsForCash(ModItems.INFUSED_ALLOY_INGOT, 3, 1,7, 1),
                                    new ItemsForCash(ModItems.KOBALT_INGOT, 2, 1,5, 1),
                                    new CashForItems(Items.COPPER_INGOT, 1,8,1,1),
                                    new CashForItems(Items.IRON_INGOT, 1,8,1,2),
                                    new CashForItems(ModItems.DUNGEON_ESSENCE_SHARD, 1,10,1,1),
                                    new ItemsForCash(ModItems.GOBLINS_FORGEHAMMER, 16, 1,1, 1),
                            },

                            // rare trades
                            2,new VillagerTrades.ItemListing[]{
                                    new ItemsForCash(ModItems.PORTAL_CORE, 14, 1,1, 1),
                            }));
            case SCAVENGER -> toIntMap(ImmutableMap.of(
                    // common trades
                    1, new VillagerTrades.ItemListing[]{
                            new ItemsForCash(ModItems.DUNGEON_ESSENCE_SHARD, 2, 1,7, 1),
                    },

                    // rare trades
                    2,new VillagerTrades.ItemListing[]{
                            new ItemsForCash(ModItems.TEST_BELT.get(), 14, 1,1, 1),
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
        private final int villagerXp;
        private final float priceMultiplier;
        private final Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider;

        public ItemsForCash(Block block, int cost, int numberOfItems, int maxUses, int villagerXp) {
            this(new ItemStack(block), cost, numberOfItems, maxUses, villagerXp);
        }

        public ItemsForCash(ItemLike item, int cost, int numberOfItems, int villagerXp) {
            this(new ItemStack(item), cost, numberOfItems, 12, villagerXp);
        }

        public ItemsForCash(ItemLike item, int cost, int numberOfItems, int maxUses, int villagerXp) {
            this(new ItemStack(item), cost, numberOfItems, maxUses, villagerXp);
        }

        public ItemsForCash(ItemStack itemStack, int cost, int numberOfItems, int maxUses, int villagerXp) {
            this(itemStack, cost, numberOfItems, maxUses, villagerXp, 0.05F);
        }

        public ItemsForCash(ItemLike item, int cost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier) {
            this(new ItemStack(item), cost, numberOfItems, maxUses, villagerXp, priceMultiplier);
        }

        public ItemsForCash(ItemLike item, int cost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier, ResourceKey<EnchantmentProvider> enchantmentProvider) {
            this(new ItemStack(item), cost, numberOfItems, maxUses, villagerXp, priceMultiplier, Optional.of(enchantmentProvider));
        }

        public ItemsForCash(ItemStack itemStack, int cost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier) {
            this(itemStack, cost, numberOfItems, maxUses, villagerXp, priceMultiplier, Optional.empty());
        }

        public ItemsForCash(ItemStack itemStack, int cost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier, Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider) {
            this.itemStack = itemStack;
            this.cost = cost;
            this.itemStack.setCount(numberOfItems);
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
            this.enchantmentProvider = enchantmentProvider;
        }

        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            ItemStack itemstack = this.itemStack.copy();
            Level level = trader.level();
            this.enchantmentProvider.ifPresent((p_348340_) -> EnchantmentHelper.enchantItemFromProvider(itemstack, level.registryAccess(), p_348340_, level.getCurrentDifficultyAt(trader.blockPosition()), random));
            return new MerchantOffer(new ItemCost(cashItem, this.cost), itemstack, this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class CashForItems implements VillagerTrades.ItemListing {
        private final ItemCost itemStack;
        private final int maxUses;
        private final int villagerXp;
        private final int cashAmount;
        private final float priceMultiplier;

        public CashForItems(ItemLike item, int cost, int maxUses, int villagerXp) {
            this(item, cost, maxUses, villagerXp, 1);
        }

        public CashForItems(ItemLike item, int cost, int maxUses, int villagerXp, int cashAmount) {
            this(new ItemCost(item.asItem(), cost), maxUses, villagerXp, cashAmount);
        }

        public CashForItems(ItemCost itemStack, int maxUses, int villagerXp, int cashAmount) {
            this.itemStack = itemStack;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.cashAmount = cashAmount;
            this.priceMultiplier = 0.05F;
        }

        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            return new MerchantOffer(this.itemStack, new ItemStack(cashItem, this.cashAmount), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
}
