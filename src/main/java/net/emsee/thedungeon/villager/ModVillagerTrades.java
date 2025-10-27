package net.emsee.thedungeon.villager;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.item.ModItems;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

import java.util.List;

@EventBusSubscriber(modid = TheDungeon.MOD_ID)
public final class ModVillagerTrades {
    static final float VANILLA_LOW_MULTIPLIER = 0.05f;
    static final float VANILLA_HIGH_MULTIPLIER = 0.20f;

    /**
     * add all trades here,
     * maybe if more villagers join, make a separate class and call that through here for each villager to keep it more organized
     */
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event){
        if (event.getType() == ModVillagers.DUNGEON_SCHOLAR.value()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 4),
                    new ItemStack(ModItems.DUNGEON_ESSENCE_SHARD.get(), 1),
                    16,
                    2,
                    VANILLA_LOW_MULTIPLIER
            ));
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 3),
                    new ItemStack(ModBlocks.INFUSED_THREAD.get(), 1),
                    16,
                    2,
                    VANILLA_LOW_MULTIPLIER
            ));
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.INFUSED_ALLOY_INGOT, 2),
                    new ItemStack(Items.EMERALD, 1),
                    12,
                    10,
                    VANILLA_LOW_MULTIPLIER
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
                    VANILLA_HIGH_MULTIPLIER
            ));
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 6),
                    new ItemStack(ModItems.SCHOLAR_LEGGINGS.get()),
                    12,
                    15,
                    VANILLA_HIGH_MULTIPLIER
            ));
            trades.get(/*villager level*/3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 4),
                    new ItemStack(ModItems.SCHOLAR_BOOTS.get()),
                    12,
                    15,
                    VANILLA_HIGH_MULTIPLIER
            ));
            trades.get(4).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5),
                    new ItemStack(ModItems.INFUSED_DAGGER.get(), 1),
                    1,
                    20,
                    VANILLA_LOW_MULTIPLIER
            ));
            trades.get(4).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 8),
                    new ItemStack(ModItems.SCHOLAR_CHESTPLATE.get()),
                    12,
                    20,
                    VANILLA_HIGH_MULTIPLIER
            ));
            trades.get(5).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 17),
                    new ItemStack(ModItems.SHATTERED_PORTAL_CORE.get(), 1),
                    1,
                    30,
                    VANILLA_LOW_MULTIPLIER
            ));
            trades.get(5).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(ModItems.PORTAL_CORE.get(), 1),
                    new ItemStack(Items.EMERALD, 19),
                    2,
                    30,
                    VANILLA_LOW_MULTIPLIER
            ));
        }
    }

    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event){
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        //genericTrades.add()

        rareTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 25),
                new ItemStack(ModItems.PORTAL_CORE.get(), 1),
                1,
                30,
                VANILLA_LOW_MULTIPLIER
        ));
    }
}
