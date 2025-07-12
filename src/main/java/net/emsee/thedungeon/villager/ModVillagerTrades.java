package net.emsee.thedungeon.villager;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.item.ModItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = TheDungeon.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class ModVillagerTrades {
    static final float vanillaLowMultiplier = .05f;
    static final float vanillaHighMultiplier = .2f;

    /**
     * add all trades here,
     * maybe if more villagers join, make a separate class and call that through here for each villager to keep it more organized
     */
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event){
        if (event.getType() == ModVillagers.DUNGEON_SCHOLAR.value()) {
            Int2ObjectMap<List<net.minecraft.world.entity.npc.VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 4),
                    new ItemStack(ModItems.DUNGEON_ESSENCE_SHARD.get(), 1),
                    16,
                    2,
                    vanillaLowMultiplier
            ));
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 3),
                    new ItemStack(ModBlocks.INFUSED_THREAD.get(), 1),
                    16,
                    2,
                    vanillaLowMultiplier
            ));
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.INFUSED_ALLOY_INGOT, 2),
                    new ItemStack(Items.EMERALD, 1),
                    12,
                    10,
                    vanillaLowMultiplier
            ));
            /*trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 2),
                    new ItemStack(ModBlocks.INFUSED_GLASS.get(), 3),
                    12,
                    5,
                    vanillaLowMultiplier
            ));*/
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 4),
                    new ItemStack(ModItems.SCHOLAR_HELMET.get()),
                    12,
                    10,
                    vanillaHighMultiplier
            ));
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 6),
                    new ItemStack(ModItems.SCHOLAR_LEGGINGS.get()),
                    12,
                    15,
                    vanillaHighMultiplier
            ));
            trades.get(/*villager level*/3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 4),
                    new ItemStack(ModItems.SCHOLAR_BOOTS.get()),
                    12,
                    15,
                    vanillaHighMultiplier
            ));
            trades.get(4).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5),
                    new ItemStack(ModItems.INFUSED_DAGGER.get(), 1),
                    1,
                    20,
                    vanillaLowMultiplier
            ));
            trades.get(4).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 8),
                    new ItemStack(ModItems.SCHOLAR_CHESTPLATE.get()),
                    12,
                    20,
                    vanillaHighMultiplier
            ));
            trades.get(5).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 17),
                    new ItemStack(ModItems.SHATTERED_PORTAL_CORE.get(), 1),
                    1,
                    30,
                    vanillaLowMultiplier
            ));
            trades.get(5).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.PORTAL_CORE.get(), 1),
                    new ItemStack(Items.EMERALD, 19),
                    2,
                    30,
                    vanillaLowMultiplier
            ));
        }
    }

    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event){
        List<net.minecraft.world.entity.npc.VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<net.minecraft.world.entity.npc.VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        rareTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 25),
                new ItemStack(ModItems.PORTAL_CORE.get(), 1),
                1,
                30,
                vanillaLowMultiplier
        ));
    }

    public static Int2ObjectMap<net.minecraft.world.entity.npc.VillagerTrades.ItemListing[]> getHobGoblinTrades() {
        return toIntMap(ImmutableMap.of(
                // common trades
                1, new net.minecraft.world.entity.npc.VillagerTrades.ItemListing[]{
                        new ItemsForPyrite(ModItems.INFUSED_ALLOY_INGOT.get(), 2, 1, 1),
                        new ItemsForPyrite(ModItems.INFUSED_DAGGER.get(), 4, 1, 1),
                        new ItemsForPyrite(ModItems.INFUSED_CHISEL.get(), 4, 1, 1),

                },

                // rare trades
                2,new net.minecraft.world.entity.npc.VillagerTrades.ItemListing[]{
                        new ItemsForPyrite(ModItems.PORTAL_CORE.get(), 14, 1, 1),
                }));
    }




    private static Int2ObjectMap<net.minecraft.world.entity.npc.VillagerTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, net.minecraft.world.entity.npc.VillagerTrades.ItemListing[]> map) {
        return new Int2ObjectOpenHashMap<>(map);
    }

    static class ItemsForPyrite implements net.minecraft.world.entity.npc.VillagerTrades.ItemListing {
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
}
