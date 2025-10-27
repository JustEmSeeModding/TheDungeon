package net.emsee.thedungeon.entity.custom.goblin.hobGoblin;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.entity.ai.DungeonTargetSelectorGoal;
import net.emsee.thedungeon.entity.ai.MultiAnimatedAttackGoal;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.emsee.thedungeon.entity.custom.goblin.AbstractGoblinEntity;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.mobEffect.ModMobEffects;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
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
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.portal.DimensionTransition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class HobGoblinEntity extends AbstractGoblinEntity implements Merchant {
    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(HobGoblinEntity.class, EntityDataSerializers.INT);

    private static final WeightedMap.Int<Variant> variants = Util.make(new WeightedMap.Int<>(), map -> {
        for (Variant variant : Variant.values()) {
            map.put(variant, variant.weight);
        }
    });

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
                // default attacks
                .withAttack(0, 12,8,1f,.75f, 1, null,Pair.of(List.of(ModItems.GOBLINS_DAGGER.get(), Items.AIR),List.of()),3)
                .withAttack(1, 12,8,1f,.75f, 1, null,Pair.of(List.of(),List.of(ModItems.GOBLINS_DAGGER.get())),2)
                .withAttack(2,12,18, 2f,1, 1, null, Pair.of(List.of(ModItems.GOBLINS_DAGGER.get()),List.of(ModItems.GOBLINS_DAGGER.get())), 1 )

                // hammer attacks
                .withAttack(0, 12,23,1f,1f, 1, null,Pair.of(List.of(ModItems.GOBLINS_FORGEHAMMER.get()),List.of()),3)
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
                .add(ModAttributes.DUNGEON_MOB_MIN_PERCEPTION, 500)
                .add(ModAttributes.DUNGEON_MOB_MAX_PERCEPTION, 800);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        double rDouble = random.nextDouble();
        switch (getVariant()) {
            case FIGHTER -> {
                if (rDouble>.67) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOBLINS_DAGGER.get()));
                    this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.GOBLINS_DAGGER.get()));
                }
                else if (rDouble>.33){
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOBLINS_DAGGER.get()));
                    this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.GOBLINS_DAGGER.get()));
                } else {
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOBLINS_DAGGER.get()));
                    this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.KOBALT_SHIELD.get()));
                }
            }
            case FORGER -> {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOBLINS_FORGEHAMMER.get()));
            }
            case SCAVENGER -> {}
            default -> {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOBLINS_DAGGER.get()));
                this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.GOBLINS_DAGGER.get()));
            }
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
    protected Component getTypeName() {
        String variantName = getVariant().getResource();
        return Component.translatable("entity.thedungeon.hob_goblin."+variantName);
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
        return level().isClientSide;
    }

    protected void updateTrades() {
        VillagerTrades.ItemListing[] comonItemlisting =  HobGoblinTrades.getHobGoblinTrades(getVariant()).get(1);
        VillagerTrades.ItemListing[] raraItemlisting = HobGoblinTrades.getHobGoblinTrades(getVariant()).get(2);
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

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (!this.level().isClientSide) {
            compound.putInt("Variant", this.getTypeVariant());
            MerchantOffers merchantoffers = this.getOffers();
            if (!merchantoffers.isEmpty()) {
                compound.put("Offers", MerchantOffers.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), merchantoffers).getOrThrow());
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Variant")) {
            this.entityData.set(VARIANT, compound.getInt("Variant"));
        }
        if (compound.contains("Offers")) {
            DataResult<MerchantOffers> dataResult = MerchantOffers.CODEC.parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compound.get("Offers"));
            dataResult.resultOrPartial(Util.prefix("Failed to load offers: ", string -> DebugLog.logWarn(DebugLog.DebugType.WARNINGS, string))).ifPresent((p_323775_) -> {
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
        return player.isCreative() || player.hasEffect(ModMobEffects.HOB_GOBLIN_TRADEABLE);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT,0);
    }

    private int getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public Variant getVariant() {
        return Variant.getById(this.getTypeVariant() & 255);
    }

    private void setVariant(Variant variant) {
        this.entityData.set(VARIANT, variant.getId() & 255);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        //if (getVariant() == null) {
            Variant variant = variants.getRandom(this.random);
            this.setVariant(variant);
        //}
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public enum Variant {
        FIGHTER(0, 100, "fighter"),
        FORGER(1,50, "forge_worker"),
        SCAVENGER(2,10, "scavenger")
        ;

        private static final Variant[] BY_ID = Arrays.stream(values()).sorted(
                Comparator.comparingInt(Variant::getId)).toArray(Variant[]::new);
        private final int id;
        private final int weight;
        private final String resource;

        Variant(int id, int weight, String resource) {
            this.id=id;
            this.weight=weight;
            this.resource = resource;
        }

        public int getId() {return id;}

        public static Variant getById(int id) {
            return BY_ID[id % BY_ID.length];
        }

        public String getResource() {
            return resource;
        }
    }

}
