package net.emsee.thedungeon.entity.custom.goblin;

import com.google.common.collect.Lists;
import com.ibm.icu.impl.Pair;
import com.mojang.serialization.DataResult;
import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.entity.ai.DungeonTargetSelectorGoal;
import net.emsee.thedungeon.entity.ai.MultiAnimatedAttackGoal;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.villager.ModVillagerTrades;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HobGoblinEntity extends AbstractGoblinEntity implements Merchant {
    private Player tradingPlayer;
    protected MerchantOffers offers;


    public HobGoblinEntity(EntityType<? extends DungeonPathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void addTargetGoals() {
        this.targetSelector.addGoal(1,
                new DungeonTargetSelectorGoal(this, true)
                        .addPredicate(player -> !isFriendlyToPlayer(player))
        );
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void setupAttackGoal() {
        this.goalSelector.addGoal(1, new MultiAnimatedAttackGoal<>(this, 1.2, true)
                .withAttack(12,8,.5f,.75f, 1, 3)
                .withAttack(12,8,.5f,.75f, 1, null,Pair.of(List.of(),List.of(ModItems.INFUSED_DAGGER.get(), Items.STONE_AXE)),2)
                .withAttack(12,18, 1f,1, 1, null, Pair.of(List.of(ModItems.INFUSED_DAGGER.get()),List.of(ModItems.INFUSED_DAGGER.get())), 1 )
        );
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 50.0)
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ARMOR, 4)
                .add(Attributes.ATTACK_DAMAGE,3)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0)
                .add(Attributes.MOVEMENT_SPEED, .27)
                .add(Attributes.KNOCKBACK_RESISTANCE, .1)
                .add(Attributes.STEP_HEIGHT, 1)
                .add(ModAttributes.DUNGEON_MOB_REACH, 2.5)
                .add(ModAttributes.MIN_PERCEPTION, 500)
                .add(ModAttributes.MAX_PERCEPTION, 800);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        double rDouble = random.nextDouble();
        if (rDouble>.67) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.INFUSED_DAGGER.get()));
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.INFUSED_DAGGER.get()));
        }
        else if (rDouble>.33){
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_AXE));
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.STONE_AXE));
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        }
    }

    @Override
    protected @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.isAlive() && !this.isTrading() && isFriendlyToPlayer(player)) {
            //if (hand == InteractionHand.MAIN_HAND) {
            //  player.awardStat(Stats.TALKED_TO_VILLAGER);
            //}

            if (!this.level().isClientSide) {
                if (this.getOffers().isEmpty()) {
                    return InteractionResult.CONSUME;
                }
                this.setTradingPlayer(player);
                this.openTradingScreen(player, Objects.requireNonNull(this.getDisplayName()), 1);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public void setTradingPlayer(@javax.annotation.Nullable Player player) {
        this.tradingPlayer = player;
    }

    @Override
    public @Nullable Player getTradingPlayer() {
        return tradingPlayer;
    }

    @Override
    public MerchantOffers getOffers() {
        if (this.level().isClientSide) {
            throw new IllegalStateException("Cannot load Villager offers on the client");
        } else {
            if (this.offers == null) {
                this.offers = new MerchantOffers();
                this.updateTrades();
            }

            return this.offers;
        }
    }

    @Override
    public void overrideOffers(MerchantOffers merchantOffers) {

    }

    @Override
    public void notifyTrade(MerchantOffer offer) {
        offer.increaseUses();
        //this.ambientSoundTime = -this.getAmbientSoundInterval();
        //this.rewardTradeXp(offer);
        //if (this.tradingPlayer instanceof ServerPlayer) {
            //CriteriaTriggers.TRADE.trigger((ServerPlayer)this.tradingPlayer, this, offer.getResult());
        //}

        //NeoForge.EVENT_BUS.post(new TradeWithVillagerEvent(this.tradingPlayer, offer, this));
    }

    @Override
    public void notifyTradeUpdated(ItemStack itemStack) {}

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    public void overrideXp(int i) {    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.WANDERING_TRADER_YES;
    }

    @Override
    public boolean isClientSide() {
        return false;
    }

    protected void updateTrades() {
        VillagerTrades.ItemListing[] comonItemlisting =  ModVillagerTrades.getHobGoblinTrades().get(1);
        VillagerTrades.ItemListing[] raraItemlisting = ModVillagerTrades.getHobGoblinTrades().get(2);
        if (comonItemlisting != null && raraItemlisting != null) {
            MerchantOffers merchantoffers = this.getOffers();
            this.addOffersFromItemListings(merchantoffers, comonItemlisting, 5);
            int i = this.random.nextInt(raraItemlisting.length);
            VillagerTrades.ItemListing listing = raraItemlisting[i];
            MerchantOffer merchantoffer = listing.getOffer(this, this.random);
            if (merchantoffer != null) {
                merchantoffers.add(merchantoffer);
            }
        }
    }

    protected void addOffersFromItemListings(MerchantOffers givenMerchantOffers, VillagerTrades.ItemListing[] newTrades, int maxNumbers) {
        ArrayList<VillagerTrades.ItemListing> arraylist = Lists.newArrayList(newTrades);
        int i = 0;

        while(i < maxNumbers && !arraylist.isEmpty()) {
            MerchantOffer merchantoffer = (arraylist.remove(this.random.nextInt(arraylist.size()))).getOffer(this, this.random);
            if (merchantoffer != null) {
                givenMerchantOffers.add(merchantoffer);
                ++i;
            }
        }
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (!this.level().isClientSide) {
            MerchantOffers merchantoffers = this.getOffers();
            if (!merchantoffers.isEmpty()) {
                compound.put("Offers", MerchantOffers.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), merchantoffers).getOrThrow());
            }
        }
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Offers")) {
            DataResult<MerchantOffers> dataResult = MerchantOffers.CODEC.parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compound.get("Offers"));
            dataResult.resultOrPartial(Util.prefix("Failed to load offers: ", string -> DebugLog.logWarn(DebugLog.DebugLevel.WARNINGS, string))).ifPresent((p_323775_) -> {
                this.offers = p_323775_;
            });
        }
    }

    @Override
    public Entity changeDimension(DimensionTransition transition) {
        this.stopTrading();
        return super.changeDimension(transition);
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        this.stopTrading();
    }

    protected void stopTrading() {
        this.setTradingPlayer(null);
    }

    public boolean isTrading() {
        return this.tradingPlayer != null;
    }

    protected boolean isFriendlyToPlayer(Player player) {
        return player.isCreative();
    }
}
