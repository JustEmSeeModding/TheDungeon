package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.Stream;

public class ModEntityLootProvider extends EntityLootSubProvider {
    protected ModEntityLootProvider(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        this.add(ModEntities.CAVE_GOBLIN.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem
                                        .lootTableItem(ModItems.GOBLIN_MEAT)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0f, 3.0f)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f)))))
        );
        this.add(ModEntities.SHADOW_GOBLIN.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem
                                        .lootTableItem(ModItems.GOBLIN_MEAT)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0f, 3.0f)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f)))))
        );
        this.add(ModEntities.HOB_GOBLIN.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem
                                        .lootTableItem(ModItems.GOBLIN_MEAT)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0f, 3.0f)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem
                                        .lootTableItem(ModItems.RAW_KOBALT)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0f, 1.0f)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f))))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.05f, 0.02f)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem
                                        .lootTableItem(Items.RAW_IRON)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0f, 1.0f)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f))))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.25f, 0.05f)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem
                                        .lootTableItem(ModItems.PYRITE)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0f, 1.0f)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f))))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.25f, 0.05f)))
        );
        this.add(ModEntities.SKELETON_KNIGHT.get(), LootTable.lootTable());
        this.add(ModEntities.DEATH_KNIGHT.get(), LootTable.lootTable());
        //throw new RuntimeException("test");
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return ModEntities.ENTITY_TYPES.getEntries().stream().map(Holder::value);
    }
}
