package net.emsee.thedungeon.block.entity.custom;

import net.emsee.thedungeon.block.custom.GoblinForgeBlock;
import net.emsee.thedungeon.block.entity.ModBlockEntities;
import net.emsee.thedungeon.recipe.GoblinForgeRecipe;
import net.emsee.thedungeon.recipe.GoblinForgeRecipeInput;
import net.emsee.thedungeon.recipe.ModRecipes;
import net.emsee.thedungeon.screen.custom.GoblinForgeMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GoblinForgeBlockEntity extends BlockEntity implements MenuProvider{
    public final ItemStackHandler itemHandler =  new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            if (level == null) return;
            setChanged();
            if (!level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return switch (slot) {
                case 0, 1 -> true;
                case 3 -> getFuelForItem(stack) > 0;
                default -> false;
            };
        }
    };

    public static final int INPUT_SLOT_ONE = 0;
    public static final int INPUT_SLOT_TWO = 1;
    public static final int OUTPUT_SLOT = 2;
    public static final int FUEL_SLOT = 3;

    private final IItemHandler sidedInputOne = new RangedWrapper(itemHandler, INPUT_SLOT_ONE, INPUT_SLOT_ONE+1);
    private final IItemHandler sidedInputTwo = new RangedWrapper(itemHandler, INPUT_SLOT_TWO, INPUT_SLOT_TWO+1);
    private final IItemHandler sidedInputFuel = new RangedWrapper(itemHandler, FUEL_SLOT, FUEL_SLOT+1);
    private final IItemHandler sidesOutput = new RangedWrapper(itemHandler, OUTPUT_SLOT, OUTPUT_SLOT+1);

    public static final int FUEL_BURN_SPEED_MULTIPLIER = 4;

    protected final ContainerData data;
    private final int DEFAULT_MAX_PROGRESS = 150;
    private int progress = 0;
    private int maxProgress = DEFAULT_MAX_PROGRESS;
    private int burnTimeLeft = 0;
    private int fuelMaxBurnTime = 100;


    public GoblinForgeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.GOBLIN_FORGE_BLOCK_ENTITY.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> GoblinForgeBlockEntity.this.progress;
                    case 1 -> GoblinForgeBlockEntity.this.maxProgress;
                    case 2 -> GoblinForgeBlockEntity.this.burnTimeLeft;
                    case 3 -> GoblinForgeBlockEntity.this.fuelMaxBurnTime;
                    default -> 0;
                };

            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: GoblinForgeBlockEntity.this.progress = value; break;
                    case 1: GoblinForgeBlockEntity.this.maxProgress = value; break;
                    case 2: GoblinForgeBlockEntity.this.burnTimeLeft = value; break;
                    case 3: GoblinForgeBlockEntity.this.fuelMaxBurnTime = value; break;
                    default: break;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    public void clearContents() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void dropContents() {
        if (level == null) return;
        SimpleContainer inv = new  SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(level, worldPosition, inv);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.thedungeon.goblin_forge_block");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new GoblinForgeMenu(i, inventory, this, data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("goblin_forge.progress", progress);
        tag.putInt("goblin_forge.max_progress", maxProgress);
        tag.putInt("goblin_forge.burntime_left", burnTimeLeft);
        tag.putInt("goblin_forge.fuel_max_burntime", fuelMaxBurnTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries,tag.getCompound("inventory"));
        progress = tag.getInt("goblin_forge.progress");
        maxProgress = tag.getInt("goblin_forge.max_progress");
        burnTimeLeft = tag.getInt("goblin_forge.burntime_left");
        fuelMaxBurnTime = tag.getInt("goblin_forge.fuel_max_burntime");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if(hasRecipe() && isOutputSlotEmptyOrReceivable()) {
            if (!hasFuel())
                checkForNewFuel();
            if (hasFuel()) {
                increaseCraftingProgress();
                decreaseFuel();
                level.setBlockAndUpdate(blockPos, blockState.setValue(GoblinForgeBlock.LIT, true));
                setChanged(level, blockPos, blockState);

                if (hasCraftingFinished()) {
                    craftItem();
                    resetProgress();
                }
            } else {
                level.setBlockAndUpdate(blockPos, blockState.setValue(GoblinForgeBlock.LIT, false));
                resetProgress();
            }
        } else {
            if (hasFuel()) {
                decreaseFuel();
            }
            level.setBlockAndUpdate(blockPos, blockState.setValue(GoblinForgeBlock.LIT, false));
            resetProgress();
        }
    }

    private void decreaseFuel() {
        burnTimeLeft--;
    }

    private void checkForNewFuel() {
        if (burnTimeLeft<=0 && hasFuelInFuelSlot()) {
            ItemStack fuel = itemHandler.getStackInSlot(FUEL_SLOT);
            burnTimeLeft = getFuelForItem(fuel);
            fuelMaxBurnTime = getFuelForItem(fuel);
            if (fuel.hasCraftingRemainingItem()) {
                itemHandler.setStackInSlot(FUEL_SLOT, fuel.getCraftingRemainingItem());
            } else
                itemHandler.extractItem(FUEL_SLOT,1,false);

        }
    }

    private boolean hasFuelInFuelSlot() {
        return getFuelForItem(itemHandler.getStackInSlot(FUEL_SLOT))>0;
    }

    private int getFuelForItem(ItemStack stack) {
        return stack.getBurnTime(null) / FUEL_BURN_SPEED_MULTIPLIER;
    }

    private boolean hasFuel() {
        return burnTimeLeft > 0;
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress=DEFAULT_MAX_PROGRESS;
    }

    private void craftItem() {
        Optional<RecipeHolder<GoblinForgeRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) return;

        ItemStack output = recipe.get().value().output();

        itemHandler.extractItem(INPUT_SLOT_ONE, 1, false);
        itemHandler.extractItem(INPUT_SLOT_TWO, 1, false);
        itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(output.getItem(),
                output.getCount() + itemHandler.getStackInSlot(OUTPUT_SLOT).getCount()));
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() < itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<GoblinForgeRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return false;

        ItemStack output = recipe.get().value().getResultItem(null);

        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);
    }

    public Optional<RecipeHolder<GoblinForgeRecipe>> getCurrentRecipe() {
        if (level == null) return Optional.empty();
        return this.level.getRecipeManager().getRecipeFor(ModRecipes.GOBLIN_FORGE_TYPE.get(), new GoblinForgeRecipeInput(
                itemHandler.getStackInSlot(INPUT_SLOT_ONE),
                itemHandler.getStackInSlot(INPUT_SLOT_TWO)
        ), level);
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                itemHandler.getStackInSlot(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ? 64 : itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = itemHandler.getStackInSlot(OUTPUT_SLOT).getCount();
        return maxCount >= currentCount + count;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public IItemHandler getItemHandler(Direction direction) {
        if (direction == Direction.DOWN) {
            return sidesOutput;
        } else if (direction == Direction.UP) {
            return sidedInputOne;
        } else if (direction == getBlockState().getValue(GoblinForgeBlock.FACING)) {
            return sidedInputTwo;
        }
        return sidedInputFuel;
    }
}