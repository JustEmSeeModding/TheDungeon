package net.emsee.thedungeon.screen.custom;

import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.block.entity.custom.GoblinForgeBlockEntity;
import net.emsee.thedungeon.recipe.GoblinForgeRecipe;
import net.emsee.thedungeon.recipe.ModRecipes;
import net.emsee.thedungeon.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class GoblinForgeMenu extends AbstractContainerMenu {
    public final GoblinForgeBlockEntity entity;
    private final Level level;
    private final ContainerData data;

    public GoblinForgeMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }


    public GoblinForgeMenu(int containerId, Inventory inv, BlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.GOBLIN_FORGE_MENU.get(), containerId);
        this.entity = (GoblinForgeBlockEntity) blockEntity;
        this.level = inv.player.level();
        this.data = data;

        addSlot(new SlotItemHandler(this.entity.itemHandler, GoblinForgeBlockEntity.INPUT_SLOT_ONE, 47, 17));
        addSlot(new SlotItemHandler(this.entity.itemHandler, GoblinForgeBlockEntity.INPUT_SLOT_TWO, 65, 17));
        addSlot(new ResultSlot(inv.player, this.entity.itemHandler, GoblinForgeBlockEntity.OUTPUT_SLOT, 116, 35));
        addSlot(new FuelSlot(this, this.entity.itemHandler, GoblinForgeBlockEntity.FUEL_SLOT, 56, 53));

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        addDataSlots(data);
    }


    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();
            if (index == 2) {
                if (!this.moveItemStackTo(stackInSlot, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stackInSlot, itemstack);
            } else if (index != 3 && index != 1 && index != 0) {
                if (this.canSmeltSlotOne(stackInSlot) && canInsertInSlotOne(stackInSlot)) {
                    if (!this.moveItemStackTo(stackInSlot, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.canSmeltSlotTwo(stackInSlot) && canInsertInSlotTwo(stackInSlot)) {
                    if (!this.moveItemStackTo(stackInSlot, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(stackInSlot)) {
                    if (!this.moveItemStackTo(stackInSlot, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 4 && index < 31) {
                    if (!this.moveItemStackTo(stackInSlot, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 31 && index < 40 && !this.moveItemStackTo(stackInSlot, 4, 31, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackInSlot, 4, 40, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return itemstack;
    }

    private boolean canInsertInSlotOne(ItemStack stack) {
        ItemStack inSlot = getSlot(0).getItem();
        return inSlot.isEmpty() || inSlot.is(stack.getItem());
    }

    private boolean canInsertInSlotTwo(ItemStack stack) {
        ItemStack inSlot = getSlot(1).getItem();
        return inSlot.isEmpty() || inSlot.is(stack.getItem());
    }

    protected boolean canSmeltSlotOne(ItemStack stack) {
        for (RecipeHolder<GoblinForgeRecipe> recipeHolder : this.level.getRecipeManager().getAllRecipesFor(ModRecipes.GOBLIN_FORGE_TYPE.get())) {
            if (recipeHolder.value().canInputInOne(stack))
                return true;
        }
        return false;
    }

    protected boolean canSmeltSlotTwo(ItemStack stack) {
        for (RecipeHolder<GoblinForgeRecipe> recipeHolder : this.level.getRecipeManager().getAllRecipesFor(ModRecipes.GOBLIN_FORGE_TYPE.get())) {
            if (recipeHolder.value().canInputInTwo(stack))
                return true;
        }
        return false;
    }

    protected boolean isFuel(ItemStack stack) {
        return stack.getBurnTime(null) / GoblinForgeBlockEntity.FUEL_BURN_SPEED_MULTIPLIER > 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()),
                player, ModBlocks.GOBLIN_FORGE.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledArrowProgress() {
        int progress = data.get(0);
        int maxProgress = data.get(1);
        int arrowPixelSize = 24;

        return maxProgress != 0 && progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }

    public boolean hasFuel() {
        return data.get(2) > 0;
    }

    public int getScaledFlameHeight() {
        int fuel = data.get(2);
        int maxFuel = data.get(3);
        int fuelPixelHeight = 16;
        int minShownPixels = 1;

        return fuel != 0 && maxFuel != 0 ? (int)Math.ceil((fuel * (fuelPixelHeight - minShownPixels * 1f) / maxFuel) + minShownPixels) : minShownPixels;
    }

    public static class FuelSlot extends SlotItemHandler {
        private final GoblinForgeMenu menu;

        public FuelSlot(GoblinForgeMenu menu, ItemStackHandler handler, int slot, int xPosition, int yPosition) {
            super(handler, slot, xPosition, yPosition);
            this.menu = menu;
        }

        public boolean mayPlace(ItemStack stack) {
            return this.menu.isFuel(stack) || isBucket(stack);
        }

        public int getMaxStackSize(ItemStack stack) {
            return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
        }

        public static boolean isBucket(ItemStack stack) {
            return stack.is(Items.BUCKET);
        }
    }

    public static class ResultSlot extends SlotItemHandler {
        private final Player player;
        private int removeCount;

        public ResultSlot(Player player, ItemStackHandler handler, int slot, int xPosition, int yPosition) {
            super(handler, slot, xPosition, yPosition);
            this.player = player;
        }

        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        public ItemStack remove(int amount) {
            if (this.hasItem()) {
                this.removeCount += Math.min(amount, this.getItem().getCount());
            }

            return super.remove(amount);
        }

        public void onTake(Player player, ItemStack stack) {
            this.checkTakeAchievements(stack);
            super.onTake(player, stack);
        }

        protected void onQuickCraft(ItemStack stack, int amount) {
            this.removeCount += amount;
            this.checkTakeAchievements(stack);
        }

        protected void checkTakeAchievements(ItemStack stack) {
            stack.onCraftedBy(this.player.level(), this.player, this.removeCount);
            if (this.player instanceof ServerPlayer serverplayer) {
                if (this.container instanceof AbstractFurnaceBlockEntity abstractfurnaceblockentity) {
                    abstractfurnaceblockentity.awardUsedRecipesAndPopExperience(serverplayer);
                }
            }

            this.removeCount = 0;
            EventHooks.firePlayerSmeltedEvent(this.player, stack);
        }
    }
}


