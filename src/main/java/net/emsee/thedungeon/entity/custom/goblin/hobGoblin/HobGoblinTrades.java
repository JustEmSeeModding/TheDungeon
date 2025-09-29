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
import net.neoforged.neoforge.common.BasicItemListing;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public class HobGoblinTrades {
    public static Int2ObjectMap<VillagerTrades.ItemListing[]> getHobGoblinTrades(HobGoblinEntity.Variant variant) {
        return switch (variant) {
            case FIGHTER ->
                    toIntMap(ImmutableMap.of(
                            // common trades
                            1, new VillagerTrades.ItemListing[]{
                                    new ItemsForPyrite(ModItems.INFUSED_DAGGER.get(), 4, 1,2, 1),
                                    new ItemsForPyrite(ModItems.INFUSED_CHISEL.get(), 4, 1,2, 1),
                                    new ItemsForPyrite(ModItems.PYRITE_COMPASS.get(), 5, 1,2, 1)
                            },

                            // rare trades
                            2,new VillagerTrades.ItemListing[]{
                                    new ItemsForPyrite(ModItems.PORTAL_CORE.get(), 14, 1, 1),
                            }));
            case FORGER ->
                    toIntMap(ImmutableMap.of(
                            // common trades
                            1, new VillagerTrades.ItemListing[]{
                                    new ItemsForPyrite(ModItems.INFUSED_ALLOY_INGOT.get(), 3, 1,7, 1),
                                    new ItemsForPyrite(ModItems.KOBALT_INGOT.get(), 2, 1,5, 1),
                                    new PyriteForItems(Items.COPPER_INGOT, 1,8,1,1),
                                    new PyriteForItems(Items.IRON_INGOT, 1,8,1,2),
                                    new PyriteForItems(ModItems.DUNGEON_ESSENCE_SHARD, 1,10,1,1),
                                    new ItemsForPyrite(ModItems.GOBLINS_FORGEHAMMER.get(), 16, 1,1, 1),
                            },

                            // rare trades
                            2,new VillagerTrades.ItemListing[]{
                                    new ItemsForPyrite(ModItems.PORTAL_CORE.get(), 14, 1,1, 1),
                            }));
            case SCAVENGER -> toIntMap(ImmutableMap.of(
                    // common trades
                    1, new VillagerTrades.ItemListing[]{
                            new ItemsForPyrite(ModItems.DUNGEON_ESSENCE_SHARD.get(), 2, 1,7, 1),
                    },

                    // rare trades
                    2,new VillagerTrades.ItemListing[]{
                            new ItemsForPyrite(ModItems.TEST_BELT.get(), 14, 1,1, 1),
                    }));
        };

    }




    private static Int2ObjectMap<VillagerTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> map) {
        return new Int2ObjectOpenHashMap<>(map);
    }

    static class ItemsForPyrite implements VillagerTrades.ItemListing {
        private final ItemStack itemStack;
        private final int pyriteCost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        private final Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider;

        public ItemsForPyrite(Block block, int pyriteCost, int numberOfItems, int maxUses, int villagerXp) {
            this(new ItemStack(block), pyriteCost, numberOfItems, maxUses, villagerXp);
        }

        public ItemsForPyrite(Item item, int pyriteCost, int numberOfItems, int villagerXp) {
            this(new ItemStack(item), pyriteCost, numberOfItems, 12, villagerXp);
        }

        public ItemsForPyrite(Item item, int pyriteCost, int numberOfItems, int maxUses, int villagerXp) {
            this(new ItemStack(item), pyriteCost, numberOfItems, maxUses, villagerXp);
        }

        public ItemsForPyrite(ItemStack itemStack, int pyriteCost, int numberOfItems, int maxUses, int villagerXp) {
            this(itemStack, pyriteCost, numberOfItems, maxUses, villagerXp, 0.05F);
        }

        public ItemsForPyrite(Item item, int pyriteCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier) {
            this(new ItemStack(item), pyriteCost, numberOfItems, maxUses, villagerXp, priceMultiplier);
        }

        public ItemsForPyrite(Item item, int pyriteCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier, ResourceKey<EnchantmentProvider> enchantmentProvider) {
            this(new ItemStack(item), pyriteCost, numberOfItems, maxUses, villagerXp, priceMultiplier, Optional.of(enchantmentProvider));
        }

        public ItemsForPyrite(ItemStack itemStack, int pyriteCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier) {
            this(itemStack, pyriteCost, numberOfItems, maxUses, villagerXp, priceMultiplier, Optional.empty());
        }

        public ItemsForPyrite(ItemStack itemStack, int pyriteCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier, Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider) {
            this.itemStack = itemStack;
            this.pyriteCost = pyriteCost;
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
            return new MerchantOffer(new ItemCost(ModItems.PYRITE, this.pyriteCost), itemstack, this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class PyriteForItems implements VillagerTrades.ItemListing {
        private final ItemCost itemStack;
        private final int maxUses;
        private final int villagerXp;
        private final int pyriteAmount;
        private final float priceMultiplier;

        public PyriteForItems(ItemLike item, int cost, int maxUses, int villagerXp) {
            this(item, cost, maxUses, villagerXp, 1);
        }

        public PyriteForItems(ItemLike item, int cost, int maxUses, int villagerXp, int pyriteAmount) {
            this(new ItemCost(item.asItem(), cost), maxUses, villagerXp, pyriteAmount);
        }

        public PyriteForItems(ItemCost itemStack, int maxUses, int villagerXp, int pyriteAmount) {
            this.itemStack = itemStack;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.pyriteAmount = pyriteAmount;
            this.priceMultiplier = 0.05F;
        }

        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            return new MerchantOffer(this.itemStack, new ItemStack(ModItems.PYRITE.get(), this.pyriteAmount), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class EmeraldsForVillagerTypeItem implements VillagerTrades.ItemListing {
        private final Map<VillagerType, Item> trades;
        private final int cost;
        private final int maxUses;
        private final int villagerXp;

        public EmeraldsForVillagerTypeItem(int cost, int maxUses, int villagerXp, Map<VillagerType, Item> trades) {
            this.trades = trades;
            this.cost = cost;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
        }

        @Nullable
        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            if (trader instanceof VillagerDataHolder villagerdataholder) {
                Item item = this.trades.get(villagerdataholder.getVillagerData().getType());
                if (item == null) {
                    return null;
                } else {
                    ItemCost itemcost = new ItemCost(item, this.cost);
                    return new MerchantOffer(itemcost, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, 0.05F);
                }
            } else {
                return null;
            }
        }
    }
}
