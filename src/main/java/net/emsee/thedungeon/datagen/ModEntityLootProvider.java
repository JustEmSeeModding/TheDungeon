package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.entity.custom.goblin.hobGoblin.HobGoblinEntity;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.loot.ModLootTables;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.predicates.ValueCheckCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.Stream;

public class ModEntityLootProvider extends EntityLootSubProvider {
    protected ModEntityLootProvider(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        LootPool.Builder baseGoblinMeatPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(ModItems.GOBLIN_MEAT)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(
                                this.registries, UniformGenerator.between(0, 1))));

        this.add(ModEntities.CAVE_GOBLIN.get(),
                LootTable.lootTable()
                        .withPool(baseGoblinMeatPool));
        this.add(ModEntities.SHADOW_GOBLIN.get(),
                LootTable.lootTable()
                        .withPool(baseGoblinMeatPool));
        this.add(ModEntities.HOB_GOBLIN.get(), LootTable.lootTable());

        LootPool.Builder baseGoblinCoinPool =
                LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.PYRITE_COIN)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(
                                        this.registries, UniformGenerator.between(0, 1)))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(
                                        this.registries, 0.3f, 0.1f)));


        this.add(ModEntities.HOB_GOBLIN.get(),
                ModLootTables.HOB_GOBLIN_FIGHTER,
                LootTable.lootTable()
                        .withPool(baseGoblinMeatPool)
                        .withPool(baseGoblinCoinPool)
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem
                                        .lootTableItem(ModItems.INFUSED_DAGGER)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0, 1))))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.05f, 0.025f)))
        );
        this.add(ModEntities.HOB_GOBLIN.get(),
                ModLootTables.HOB_GOBLIN_FORGER,
                LootTable.lootTable()
                        .withPool(baseGoblinMeatPool)
                        .withPool(baseGoblinCoinPool)
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem
                                        .lootTableItem(ModItems.GOBLINS_FORGEHAMMER)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0, 1))))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.01f, 0.005f)))
        );
        this.add(ModEntities.HOB_GOBLIN.get(),
                ModLootTables.HOB_GOBLIN_SCAVENGER,
                LootTable.lootTable()
                        .withPool(baseGoblinMeatPool)
                        .withPool(baseGoblinCoinPool)
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem
                                        .lootTableItem(ModItems.SHATTERED_CATALYST_CORE)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f))))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, .005f, .001f)))
        );
        this.add(ModEntities.HOB_GOBLIN.get(),
                ModLootTables.HOB_GOBLIN_MINER,
                LootTable.lootTable()
                        .withPool(baseGoblinMeatPool)
                        .withPool(baseGoblinCoinPool)
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1, 4))
                                .add(LootItem
                                        .lootTableItem(ModItems.PYRITE)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f))))
                                .add(LootItem
                                        .lootTableItem(Items.RAW_IRON)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f))))
                                .add(LootItem
                                        .lootTableItem(Items.RAW_GOLD)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f))))
                                .add(LootItem
                                        .lootTableItem(Items.RAW_COPPER)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f)))))
        );
        this.add(ModEntities.SKELETON_KNIGHT.get(), LootTable.lootTable());
        this.add(ModEntities.DEATH_KNIGHT.get(), LootTable.lootTable());


        LootPool.Builder baseCrystalGolemPool =
                LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Blocks.CALCITE)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(
                                        this.registries, UniformGenerator.between(0,1)))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(
                                        this.registries, 0.2f, 0.75f)));
        this.add(ModEntities.CRYSTAL_GOLEM.get(), LootTable.lootTable());
        this.add(ModEntities.CRYSTAL_GOLEM.get(),
                ModLootTables.CRYSTAL_GOLEM_ROSE_QUARTZ,
                LootTable.lootTable()
                        .withPool(baseCrystalGolemPool)
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1, 4))
                                .add(LootItem
                                        .lootTableItem(ModItems.ROSE_QUARTZ)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, .1f, .05f)))

        );
        this.add(ModEntities.CRYSTAL_GOLEM.get(),
                ModLootTables.CRYSTAL_GOLEM_AMETHYST,
                LootTable.lootTable()
                        .withPool(baseCrystalGolemPool)
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1, 4))
                                .add(LootItem
                                        .lootTableItem(Items.AMETHYST_SHARD)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f))))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, .1f, .05f)))

        );
        this.add(ModEntities.CRYSTAL_GOLEM.get(),
                ModLootTables.CRYSTAL_GOLEM_DIAMOND,
                LootTable.lootTable()
                        .withPool(baseCrystalGolemPool)
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1, 4))
                                .add(LootItem
                                        .lootTableItem(Items.DIAMOND)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f))))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, .1f, .05f)))

        );
        this.add(ModEntities.CRYSTAL_GOLEM.get(),
                ModLootTables.CRYSTAL_GOLEM_EMERALD,
                LootTable.lootTable()
                        .withPool(baseCrystalGolemPool)
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1, 4))
                                .add(LootItem
                                        .lootTableItem(Items.EMERALD)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f))))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, .1f, .05f)))

        );
        //throw new RuntimeException("test");
    }



    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return ModEntities.ENTITY_TYPES.getEntries().stream().map(Holder::value);
    }
}
