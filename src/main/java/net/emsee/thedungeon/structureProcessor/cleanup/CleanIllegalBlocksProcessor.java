package net.emsee.thedungeon.structureProcessor.cleanup;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.BlockPalletReplacementProcessor;
import net.emsee.thedungeon.structureProcessor.PostProcessor;
import net.emsee.thedungeon.utils.BlockUtils;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CleanIllegalBlocksProcessor extends BlockPalletReplacementProcessor implements PostProcessor {
    public static final CleanIllegalBlocksProcessor INSTANCE = new CleanIllegalBlocksProcessor();

    public static final MapCodec<CleanIllegalBlocksProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public BlockUtils.ForEachMethod getMethod() {
        return BlockUtils.ForEachMethod.BOTTOM_TO_TOP;
    }

    @Override
    public boolean skipBlockForProcessing(LevelReader level, BlockPos pos, BlockState state) {
        if (state.getBlock() == Blocks.AIR) return true;
        return PostProcessor.super.skipBlockForProcessing(level, pos, state) ||
                getReplacements().get(state.getBlock()) != null;
    }

    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                for (Block illegalBlock : Util.make(new ArrayList<Block>(), list->{
                    // storage blocks / functional blocks
                    list.add(Blocks.CHEST);
                    list.add(Blocks.ENDER_CHEST);
                    list.add(Blocks.BARREL);
                    list.add(Blocks.FURNACE);
                    list.add(Blocks.BLAST_FURNACE);
                    list.add(Blocks.SMOKER);
                    list.add(Blocks.BREWING_STAND);
                    list.add(Blocks.COMPOSTER);
                    list.add(Blocks.BEEHIVE);
                    list.add(Blocks.BEE_NEST);
                    list.add(Blocks.CAULDRON);
                    list.add(Blocks.LAVA_CAULDRON);
                    list.add(Blocks.WATER_CAULDRON);
                    list.add(Blocks.POWDER_SNOW_CAULDRON);
                    list.add(Blocks.BEACON);
                    list.add(Blocks.CONDUIT);
                    list.add(Blocks.ENCHANTING_TABLE);
                    list.add(Blocks.RESPAWN_ANCHOR);
                    list.add(Blocks.VAULT);
                    list.add(Blocks.TRIAL_SPAWNER);
                    list.add(Blocks.SPAWNER);
                    list.add(Blocks.END_PORTAL_FRAME);
                    list.add(Blocks.DRAGON_EGG);
                    list.add(Blocks.TURTLE_EGG);
                    list.add(Blocks.FROGSPAWN);
                    list.add(Blocks.SNIFFER_EGG);
                    list.add(Blocks.POWDER_SNOW);

                    // shulker boxes
                    list.add(Blocks.SHULKER_BOX);
                    list.add(Blocks.WHITE_SHULKER_BOX);
                    list.add(Blocks.LIGHT_GRAY_SHULKER_BOX);
                    list.add(Blocks.GRAY_SHULKER_BOX);
                    list.add(Blocks.BLACK_SHULKER_BOX);
                    list.add(Blocks.BROWN_SHULKER_BOX);
                    list.add(Blocks.RED_SHULKER_BOX);
                    list.add(Blocks.ORANGE_SHULKER_BOX);
                    list.add(Blocks.YELLOW_SHULKER_BOX);
                    list.add(Blocks.LIME_SHULKER_BOX);
                    list.add(Blocks.GREEN_SHULKER_BOX);
                    list.add(Blocks.CYAN_SHULKER_BOX);
                    list.add(Blocks.LIGHT_BLUE_SHULKER_BOX);
                    list.add(Blocks.BLUE_SHULKER_BOX);
                    list.add(Blocks.PURPLE_SHULKER_BOX);
                    list.add(Blocks.MAGENTA_SHULKER_BOX);
                    list.add(Blocks.PINK_SHULKER_BOX);

                    // particles
                    list.add(Blocks.CAMPFIRE);
                    list.add(Blocks.SOUL_CAMPFIRE);
                    list.add(Blocks.SPORE_BLOSSOM);
                    list.add(Blocks.DRIPSTONE_BLOCK);

                    // noisy
                    list.add(Blocks.NETHER_PORTAL);
                    list.add(Blocks.END_PORTAL);
                    list.add(Blocks.NOTE_BLOCK);
                    list.add(Blocks.JUKEBOX);
                    list.add(Blocks.BIG_DRIPLEAF);
                    list.add(Blocks.BIG_DRIPLEAF_STEM);

                    // fluids
                    list.add(Blocks.WATER);
                    list.add(Blocks.ICE);
                    list.add(Blocks.LAVA);

                    // redstone
                    list.add(Blocks.REDSTONE_WIRE);
                    list.add(Blocks.REDSTONE_ORE);
                    list.add(Blocks.REPEATER);
                    list.add(Blocks.COMPARATOR);
                    list.add(Blocks.COMMAND_BLOCK);
                    list.add(Blocks.SLIME_BLOCK);
                    list.add(Blocks.HONEY_BLOCK);
                    list.add(Blocks.TNT);
                    list.add(Blocks.PISTON);
                    list.add(Blocks.STICKY_PISTON);
                    list.add(Blocks.PISTON_HEAD);
                    list.add(Blocks.MOVING_PISTON);
                    list.add(Blocks.PISTON_HEAD);
                    list.add(Blocks.DROPPER);
                    list.add(Blocks.DISPENSER);
                    list.add(Blocks.OBSERVER);
                    list.add(Blocks.CRAFTER);
                    list.add(Blocks.HOPPER);
                    list.add(Blocks.TRIPWIRE_HOOK);
                    list.add(Blocks.RAIL);
                    list.add(Blocks.ACTIVATOR_RAIL);
                    list.add(Blocks.DETECTOR_RAIL);
                    list.add(Blocks.POWERED_RAIL);
                    list.add(Blocks.LIGHTNING_ROD);

                    // sculk stuff
                    list.add(Blocks.SCULK_SENSOR);
                    list.add(Blocks.SCULK_SHRIEKER);
                    list.add(Blocks.SCULK_CATALYST);
                    list.add(Blocks.CALIBRATED_SCULK_SENSOR);

                    // infested blocks
                    list.add(Blocks.INFESTED_COBBLESTONE);
                    list.add(Blocks.INFESTED_DEEPSLATE);
                    list.add(Blocks.INFESTED_STONE);
                    list.add(Blocks.INFESTED_CHISELED_STONE_BRICKS);
                    list.add(Blocks.INFESTED_STONE_BRICKS);
                    list.add(Blocks.INFESTED_CRACKED_STONE_BRICKS);
                    list.add(Blocks.INFESTED_MOSSY_STONE_BRICKS);
                }))
                    map.put(illegalBlock, new WeightedMap.Int<>(Map.of(new ReplaceInstance(Blocks.AIR::defaultBlockState), 1)));
            });

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
