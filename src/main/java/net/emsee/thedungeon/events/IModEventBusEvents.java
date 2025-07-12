package net.emsee.thedungeon.events;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.block.ModBlocks;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GrassColor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@EventBusSubscriber(modid = TheDungeon.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class IModEventBusEvents {
    @SubscribeEvent
    public static void entityAttributeModificationEvent(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ModAttributes.PLAYER_INCOMING_DAMAGE_REDUCTION, 0);
        event.add(EntityType.PLAYER, ModAttributes.PLAYER_DUNGEON_AGGRO_TO_ENEMY, 500);
    }

    @SubscribeEvent
    public static void blockColorLoad(RegisterColorHandlersEvent.Block event) {
        event.getBlockColors().register((bs, world, pos, index) -> world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.get(0.5D, 1.0D), ModBlocks.INFUSED_GRASS_BLOCK.get());
    }

    @SubscribeEvent
    public static void itemColorLoad(RegisterColorHandlersEvent.Item event) {
        event.getItemColors().register((stack, index) -> {
            return GrassColor.get(0.5D, 1.0D);
        }, ModBlocks.INFUSED_GRASS_BLOCK.get());
    }
}
