package net.emsee.thedungeon.block.custom;

import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.Config;
import net.emsee.thedungeon.block.entity.custom.DungeonPortalBlockEntity;
import net.emsee.thedungeon.damageType.ModDamageTypes;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.dungeon.src.GlobalDungeonManager;
import net.emsee.thedungeon.recipe.DungeonInfusionRecipe;
import net.emsee.thedungeon.recipe.DungeonInfusionRecipeInput;
import net.emsee.thedungeon.recipe.ModRecipes;
import net.emsee.thedungeon.utils.ModDungeonTeleportHandling;
import net.emsee.thedungeon.item.custom.DungeonItem;
import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.emsee.thedungeon.utils.ParticleUtils;
import net.emsee.thedungeon.worldSaveData.DungeonSaveData;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DungeonPortal extends BaseEntityBlock implements IDungeonCarryItem {
    public static final MapCodec<DungeonPortal> CODEC = simpleCodec(DungeonPortal::new);

    public static final BooleanProperty STABLE = BooleanProperty.create("stable");
    public static final BooleanProperty EXIT = BooleanProperty.create("is_exit");
    public static final IntegerProperty PORTAL_ID = IntegerProperty.create("portal_id", 0, 500);
    public static final EnumProperty<DungeonRank> RANK = EnumProperty.create("rank_id", DungeonRank.class);

    private static final float unstableDamageAmount = 4;

    private static final int blockChangeRadius = 5;
    private static final int blockChangeTries = 30;

    private static final long LOW_TIME_LOCK = 2400;

    public DungeonPortal(Properties properties) {
        super(properties.randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(STABLE, false).setValue(EXIT,false).setValue(PORTAL_ID, 0).setValue(RANK, DungeonRank.F));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STABLE,EXIT,PORTAL_ID,RANK);
    }

    public int getExitID(MinecraftServer server, BlockState state, BlockPos pos, Level level) {
        int id = state.getValue(PORTAL_ID) - 1;

        if (id < 0) {
            id = GlobalDungeonManager.giveRandomPortalID(level.random);

            level.setBlockAndUpdate(pos,state.setValue(PORTAL_ID, id + 1));
        }
        return id;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (state.getValue(STABLE)) {
            if (!level.isClientSide) {
                MinecraftServer server = level.getServer();
                if (player.isCreative() || GlobalDungeonManager.isOpen(server, state.getValue(RANK))) {
                    if (timeCheck(player, server, state) || player.isCreative())
                        ModDungeonTeleportHandling.playerTeleportDungeon(player, getExitID(server, state,pos,level), state.getValue(RANK));
                } else {
                    player.displayClientMessage(Component.translatable("message.thedungeon.dungeon_portal.dungeon_closed"), true);
                }
            } else {
                level.playLocalSound(pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1, 1, false);
            }
        } else {
            if (!level.isClientSide) {
                player.hurt(level.damageSources().source(ModDamageTypes.UNSTABLE_PORTAL), unstableDamageAmount);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(DungeonItem.DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(STABLE)) {
            transformBlocks(level, pos);
        }
        /*if (!level.isClientSide && level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            GlobalDungeonManager.addPortalLocation(level.getServer(), pos, DungeonRank.getClosestTo(pos));
        }*/ // portals are now handled different
        if (!GlobalDungeonManager.isOpen(level.getServer(), state.getValue(RANK)))
            level.setBlockAndUpdate(pos, state.setValue(PORTAL_ID, 0));
        super.randomTick(state, level, pos, random);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (level instanceof Level l)
            return getNewBlockState(l, pos);
        return state;
    }

    /*
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide && level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            if (state.getValue(EXIT))
                GlobalDungeonManager.addPortalLocation(level.getServer(), pos, DungeonRank.getClosestTo(pos));
            else
                level.setBlockAndUpdate(pos, ModBlocks.DUNGEON_PORTAL.get().defaultBlockState().setValue(EXIT, true));
        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }


    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock() && level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            GlobalDungeonManager.removePortalLocation(level.getServer(), pos, DungeonRank.getClosestTo(pos));
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }*/

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }

    private boolean timeCheck(Player player, MinecraftServer server, BlockState state) {
        if (player.isCrouching()) return true;
        if (player.level().dimension() == ModDimensions.DUNGEON_LEVEL_KEY) return true;
        DungeonSaveData saveData = DungeonSaveData.Get(server);
        long worldTime = server.overworld().getGameTime();
        long timeLeft = Config.TICKS_BETWEEN_COLLAPSES.getAsLong() - (worldTime - saveData.GetLastExecutionTime());
        if (state.getValue(RANK) == saveData.getNextToCollapse() && timeLeft <= LOW_TIME_LOCK) {
            long secondsLeft = (long) Math.ceil(timeLeft / (20f));
            player.displayClientMessage(Component.translatable("announcement.thedungeon.low_time_teleport", secondsLeft).withStyle(ChatFormatting.RED).withStyle(ChatFormatting.UNDERLINE), true);
            return false;
        }
        return true;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return getNewBlockState(context.getLevel(), context.getClickedPos());
    }

    private BlockState getNewBlockState(Level level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        if (level.dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            return defaultBlockState().setValue(STABLE, true).setValue(EXIT,true).setValue(PORTAL_ID, 0).setValue(RANK, DungeonRank.getClosestTo(pos));
        }
        else if (below.getBlock() instanceof DungeonCatalystBlock catalistBlock) {
            return defaultBlockState().setValue(STABLE, true).setValue(EXIT,false).setValue(PORTAL_ID, 0).setValue(RANK, catalistBlock.getCatalistRank());
        }
        return defaultBlockState().setValue(STABLE, false).setValue(EXIT,false).setValue(PORTAL_ID, 0).setValue(RANK, DungeonRank.F);
    }

    @Override
    public void animateTick( BlockState state,  Level level,  BlockPos pos,  RandomSource random) {
        if (!state.getValue(STABLE))
            if (level instanceof ClientLevel clientLevel) {
                ParticleUtils.sphereExpand(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, 1, 2, clientLevel, ParticleTypes.WARPED_SPORE, 0, 0, 0, .5, 10);
                ParticleUtils.sphereExpand(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, 1, 2, clientLevel, ParticleTypes.CRIMSON_SPORE, 0, 0, 0, .5, 1);
            }
        super.animateTick(state, level, pos, random);
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
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DungeonPortalBlockEntity(blockPos, blockState);
    }
}
