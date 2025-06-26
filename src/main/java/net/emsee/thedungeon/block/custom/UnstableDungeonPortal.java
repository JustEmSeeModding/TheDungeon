package net.emsee.thedungeon.block.custom;

import net.emsee.thedungeon.damageType.ModDamageTypes;
import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.emsee.thedungeon.recipe.DungeonInfusionRecipe;
import net.emsee.thedungeon.recipe.DungeonInfusionRecipeInput;
import net.emsee.thedungeon.recipe.ModRecipes;
import net.emsee.thedungeon.utils.ParticleUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class UnstableDungeonPortal extends DungeonBlock implements IDungeonCarryItem {
    private static final int blockChangeRadius = 5;
    private static final int blockChangeTries = 30;

    public UnstableDungeonPortal(Properties properties) {
        super(properties.randomTicks());
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        transformBlocks(level, pos);
        super.randomTick(state, level, pos, random);
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (level instanceof ClientLevel clientLevel) {
            ParticleUtils.sphereExpand(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, 1, 2, clientLevel, ParticleTypes.WARPED_SPORE, 0, 0, 0, .5, 10);
            ParticleUtils.sphereExpand(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, 1, 2, clientLevel, ParticleTypes.CRIMSON_SPORE, 0, 0, 0, .5, 1);
        }
        super.animateTick(state, level, pos, random);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        explode(state, level, pos);
        level.destroyBlock(pos, true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean onDestroyedByPlayer(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player, boolean willHarvest, @NotNull FluidState fluid) {
        if (!player.isCreative()) {
            //level.explode(player, pos.getX(), pos.getY() - 1, pos.getZ(), 10, Level.ExplosionInteraction.MOB);
            explode(state, level, pos);
        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    private void explode(BlockState state, Level level, final BlockPos pos) {
        level.removeBlock(pos, false);
        Objects.requireNonNull(pos);
        Vec3 vec3 = pos.getCenter();

        level.explode(null, level.damageSources().source(ModDamageTypes.UNSTABLE_PORTAL), new ExplosionDamageCalculator(), vec3, 2.5F, false, Level.ExplosionInteraction.BLOCK);
        level.setBlock(pos, state, 0);
    }

    private void transformBlocks(Level level, BlockPos pos) {
        BlockPos minCorner = pos.offset(-blockChangeRadius, -blockChangeRadius, -blockChangeRadius);
        BlockPos maxCorner = pos.offset(blockChangeRadius, blockChangeRadius, blockChangeRadius);
        Random random = new Random();
        int tries = blockChangeTries;
        while (tries > 0) {
            BlockPos currentPos = new BlockPos(random.nextInt(minCorner.getX(), maxCorner.getX() + 1), random.nextInt(minCorner.getY(), maxCorner.getY() + 1), random.nextInt(minCorner.getZ(), maxCorner.getZ() + 1));
            transformBlock(level, currentPos);
            tries--;
        }
    }

    private void transformBlock(Level level, BlockPos pos) {
        Block block = level.getBlockState(pos).getBlock();
        Optional<RecipeHolder<DungeonInfusionRecipe>> recipe = getCurrentRecipe(block.asItem(), level);
        if (recipe.filter(dungeonInfusionRecipeRecipeHolder -> dungeonInfusionRecipeRecipeHolder.value().output() != null).isEmpty())
            return;
        ItemStack output = recipe.get().value().output().copy();
        if (output.getItem() instanceof BlockItem blockItem) {
            level.setBlockAndUpdate(pos, blockItem.getBlock().defaultBlockState());
            output.shrink(1);
            if (output.getCount() >= 1)
                level.addFreshEntity(new ItemEntity(level, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, output));
        } else {
            level.removeBlock(pos, false);
            level.addFreshEntity(new ItemEntity(level, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, output));
        }

    }

    private Optional<RecipeHolder<DungeonInfusionRecipe>> getCurrentRecipe(Item item, Level level) {
        return level.getRecipeManager().getRecipeFor(ModRecipes.DUNGEON_INFUSION_TYPE.get(), new DungeonInfusionRecipeInput(new ItemStack(item)), level);
    }

    @Override
    public int getLightEmission(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return 15;
    }
}
