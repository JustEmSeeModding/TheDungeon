package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.dungeonClass.DungeonClass;
import net.emsee.thedungeon.dungeonClass.DungeonSubClass;
import net.emsee.thedungeon.item.DungeonItemRank;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import top.theillusivec4.curios.api.SlotContext;

import java.util.LinkedHashMap;

public class LuminousGoggles extends DungeonCurio{
    public LuminousGoggles(Properties properties, DungeonItemRank rank, DeferredHolder<DungeonClass, ?>[] classes, DeferredHolder<DungeonSubClass<?>, ?>[] subClasses) {
        super(properties, rank, classes, subClasses);
    }

    @Override
    public LinkedHashMap<SlotType, Component[]> getExtraComponents(ItemStack stack) {
        LinkedHashMap<SlotType, Component[]> toReturn = super.getExtraComponents(stack);
        toReturn.put(SlotType.CURIO, new Component[]{Component.translatable("item.thedungeon.tooltip.luminous_goggles_equipped").withStyle(POSITIVE_FORMATTING)});
        return toReturn;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        slotContext.entity().addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20*15, 0));
    }
}
