package net.emsee.thedungeon.simpleRegisterGroup;

import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.block.custom.DungeonAmethystClusterBlock;
import net.emsee.thedungeon.block.custom.DungeonBlock;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class SimpleBlockGroup {
    public final String name;
    public SimpleBlockGroup(String name) {
        this.name = name;
    }

    public abstract List<DeferredBlock<?>> getAll();
    public final List<Block> getAllAsBlock() {
        List<Block> toReturn = new ArrayList<>();
        getAll().forEach(block -> toReturn.add(block.get()));
        return toReturn;
    }

    public final List<ResourceKey<Block>> getAllAsResourceKey() {
        List<ResourceKey<Block>> toReturn = new ArrayList<>();
        getAll().forEach(block -> toReturn.add(block.getKey()));
        return toReturn;
    }

    public List<Item> getAllAsItem() {
        List<Item> toReturn = new ArrayList<>();
        getAll().forEach(block -> toReturn.add(block.get().asItem()));
        return toReturn;
    }

    public List<ItemLike> getAllAsItemLike() {
        List<ItemLike> toReturn = new ArrayList<>();
        getAll().forEach(block -> toReturn.add(block.get().asItem()));
        return toReturn;
    }

    public static class ItemBlockAndOre extends  SimpleBlockGroup implements WithPackedItemBlock, WithOreBlock, WithOres {
        private final DeferredBlock<DungeonBlock> BLOCK;
        private final DeferredBlock<DungeonBlock> ORE;

        public ItemBlockAndOre(String name, Supplier<DungeonBlock> block, Supplier<DungeonBlock> ore) {
            super(name);
            this.BLOCK = ModBlocks.registerBlock(name+"_block", block);
            this.ORE = ModBlocks.registerBlock(name+"_ore", ore);
        }

        @Override
        public List<DeferredBlock<?>> getAll() {
            return List.of(BLOCK, ORE);
        }

        @Override
        public DeferredBlock<?> getPackedItemBlock() {
            return BLOCK;
        }
        @Override
        public DeferredBlock<?> getOreBlock() {
            return ORE;
        }

        @Override
        public List<DeferredBlock<?>> getAllOres() {
            return List.of(ORE);
        }
    }

    public  static class ItemBlockAndOreAndDeepslateOre extends SimpleBlockGroup implements WithPackedItemBlock, WithOreBlock, WithDeepslateOreBlock, WithOres {
        public final DeferredBlock<DungeonBlock> BLOCK;
        public final DeferredBlock<DungeonBlock> ORE;
        public final DeferredBlock<DungeonBlock> DEEPSLATE_ORE;
        public ItemBlockAndOreAndDeepslateOre(String name, Supplier<DungeonBlock> block, Supplier<DungeonBlock> ore, Supplier<DungeonBlock> deepslateOre) {
            super(name);
            this.DEEPSLATE_ORE = ModBlocks.registerBlock("deepslate_"+name+"_ore", deepslateOre);
            this.BLOCK = ModBlocks.registerBlock(name+"_block", block);
            this.ORE = ModBlocks.registerBlock(name+"_ore", ore);
        }

        @Override
        public List<DeferredBlock<?>> getAll() {
            return List.of(BLOCK, ORE, DEEPSLATE_ORE);
        }

        @Override
        public DeferredBlock<?> getPackedItemBlock() {
            return BLOCK;
        }

        @Override
        public DeferredBlock<?> getDeepslateOreBlock() {
            return DEEPSLATE_ORE;
        }

        @Override
        public DeferredBlock<?> getOreBlock() {
            return ORE;
        }

        @Override
        public List<DeferredBlock<?>> getAllOres() {
            return List.of(ORE, DEEPSLATE_ORE);
        }
    }

    public static class ItemBlockAndRawAndOre extends SimpleBlockGroup implements WithPackedItemBlock, WithRawBlock, WithOreBlock, WithOres {
        public final DeferredBlock<DungeonBlock> BLOCK;
        public final DeferredBlock<DungeonBlock> RAW_BLOCK;
        public final DeferredBlock<DungeonBlock> ORE;
        public ItemBlockAndRawAndOre(String name, Supplier<DungeonBlock> block, Supplier<DungeonBlock> rawBlock, Supplier<DungeonBlock> ore) {
            super(name);
            this.BLOCK = ModBlocks.registerBlock(name+"_block", block);
            this.RAW_BLOCK = ModBlocks.registerBlock("raw_"+name+"_block", rawBlock);
            this.ORE = ModBlocks.registerBlock(name+"_ore", ore);
        }

        @Override
        public List<DeferredBlock<?>> getAll() {
            return List.of(BLOCK, RAW_BLOCK, ORE);
        }

        @Override
        public DeferredBlock<?> getPackedItemBlock() {
            return BLOCK;
        }

        @Override
        public DeferredBlock<?> getOreBlock() {
            return ORE;
        }

        @Override
        public DeferredBlock<?> getRawBlock() {
            return RAW_BLOCK;
        }

        @Override
        public List<DeferredBlock<?>> getAllOres() {
            return List.of(ORE);
        }
    }

    public static class ItemBlockAndRawAndOreAndDeepslateOre extends SimpleBlockGroup implements WithPackedItemBlock, WithRawBlock, WithOreBlock, WithDeepslateOreBlock, WithOres {
        public final DeferredBlock<DungeonBlock> BLOCK;
        public final DeferredBlock<DungeonBlock> RAW_BLOCK;
        public final DeferredBlock<DungeonBlock> ORE;
        public final DeferredBlock<DungeonBlock> DEEPSLATE_ORE;
        public ItemBlockAndRawAndOreAndDeepslateOre(String name, Supplier<DungeonBlock> block, Supplier<DungeonBlock> rawBlock, Supplier<DungeonBlock> ore, Supplier<DungeonBlock> deepslateOre) {
            super(name);
            this.BLOCK = ModBlocks.registerBlock(name+"_block", block);
            this.RAW_BLOCK = ModBlocks.registerBlock("raw_"+name+"_block", rawBlock);
            this.ORE = ModBlocks.registerBlock(name+"_ore", ore);
            this.DEEPSLATE_ORE = ModBlocks.registerBlock("deepslate_"+name+"_ore", deepslateOre);
        }

        @Override
        public List<DeferredBlock<?>> getAll() {
            return List.of(BLOCK, RAW_BLOCK, ORE, DEEPSLATE_ORE);
        }

        @Override
        public DeferredBlock<?> getPackedItemBlock() {
            return BLOCK;
        }

        @Override
        public DeferredBlock<?> getDeepslateOreBlock() {
            return DEEPSLATE_ORE;
        }

        @Override
        public DeferredBlock<?> getOreBlock() {
            return ORE;
        }

        @Override
        public DeferredBlock<?> getRawBlock() {
            return RAW_BLOCK;
        }

        @Override
        public List<DeferredBlock<?>> getAllOres() {
            return List.of(ORE,DEEPSLATE_ORE);
        }
    }


    public static class CrystalBlockAndClusterGroup<A extends AmethystBlock,B extends AmethystBlock> extends SimpleBlockGroup {
        public final DeferredBlock<A> BLOCK;
        public final DeferredBlock<B> BUDDING_BLOCK;
        public final CrystalClusterGroup CLUSTERS;

        public CrystalBlockAndClusterGroup(String name, Supplier<A> block, Supplier<B> buddingBlock) {
            super(name);
            BLOCK = ModBlocks.registerBlock(name+"_block", block);
            BUDDING_BLOCK = ModBlocks.registerBlock("budding_"+name, buddingBlock);
            CLUSTERS = new CrystalClusterGroup(name);
        }

        @Override
        public List<DeferredBlock<?>> getAll() {
            List<DeferredBlock<?>> toReturn = new java.util.ArrayList<>(List.of(BLOCK, BUDDING_BLOCK));
            toReturn.addAll(CLUSTERS.getAll());
            return toReturn;
        }
    }

    public static class CrystalClusterGroup extends SimpleBlockGroup {
        public final DeferredBlock<DungeonAmethystClusterBlock> CLUSTER;
        public final DeferredBlock<DungeonAmethystClusterBlock> LARGE_BUD;
        public final DeferredBlock<DungeonAmethystClusterBlock> MEDIUM_BUD;
        public final DeferredBlock<DungeonAmethystClusterBlock> SMALL_BUD;

        @Override
        public List<DeferredBlock<?>> getAll() {
            return List.of(CLUSTER, LARGE_BUD, MEDIUM_BUD, SMALL_BUD);
        }

        public CrystalClusterGroup(String name) {
            super(name);
            CLUSTER = ModBlocks.registerBlock(name+"_cluster",
                    () -> new DungeonAmethystClusterBlock(7.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER)));

            LARGE_BUD = ModBlocks.registerBlock("large_"+name+"_bud",
                    () -> new DungeonAmethystClusterBlock(5.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_AMETHYST_BUD)));

            MEDIUM_BUD = ModBlocks.registerBlock("medium_"+name+"_bud",
                    () -> new DungeonAmethystClusterBlock(4.0F, 3.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD)));

           SMALL_BUD = ModBlocks.registerBlock("small_"+name+"_bud",
                    () -> new DungeonAmethystClusterBlock(3.0F, 4.0F,BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD)));

        }
    }

    public interface WithPackedItemBlock {
            DeferredBlock<?> getPackedItemBlock();
    }
    public interface WithRawBlock {
        DeferredBlock<?> getRawBlock();
    }
    public interface WithOreBlock {
        DeferredBlock<?> getOreBlock();
    }
    public interface WithDeepslateOreBlock {
        DeferredBlock<?> getDeepslateOreBlock();
    }
    public interface WithSpecialOreBlock {
        DeferredBlock<?> getSpecialOreBlock();
    }

    public interface WithOres {
        List<DeferredBlock<?>> getAllOres();
        default List<ItemLike> getAllOresAsItems() {
            List<ItemLike> toReturn = new ArrayList<>();
            getAllOres().forEach(ore -> toReturn.add(ore.get().asItem()));
            return toReturn;
        }
    }
}
