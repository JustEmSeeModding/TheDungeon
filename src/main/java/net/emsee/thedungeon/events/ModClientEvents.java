package net.emsee.thedungeon.events;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.item.interfaces.IDungeonToolTips;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.client.event.GatherSkippedAttributeTooltipsEvent;


@EventBusSubscriber(modid = TheDungeon.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void IDungeonToolTipsAddTooltips(AddAttributeTooltipsEvent event) {
        if (event.getStack().getItem() instanceof IDungeonToolTips item)
            item.addAttributeTooltips(event);
    }

    @SubscribeEvent
    public static void IDungeonToolTipsRemoveDefaultTooltips(GatherSkippedAttributeTooltipsEvent event) {
        if (event.getStack().getItem() instanceof IDungeonToolTips)
            event.setSkipAll(true);
    }
}
