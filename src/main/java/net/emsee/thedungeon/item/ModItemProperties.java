package net.emsee.thedungeon.item;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.src.GlobalDungeonManager;
import net.emsee.thedungeon.item.custom.DungeonClock;
import net.emsee.thedungeon.item.custom.DungeonPortalCompas;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;


public class ModItemProperties {
    public static void addCustomItemProperties() {
        ItemProperties.register(ModItems.PYRITE_COMPASS.get(), TheDungeon.defaultResourceLocation("angle"), new CompassItemPropertyFunction((level, stack, entity) -> DungeonPortalCompas.getTargetPosition(stack)));
        ItemProperties.register(ModItems.DUNGEON_CLOCK.get(), TheDungeon.defaultResourceLocation("time_left"), (itemStack, clientLevel, livingEntity, i) -> Math.clamp(DungeonClock.getTimeLeft(itemStack)/24000f,0f,1f));
        ItemProperties.register(ModItems.DUNGEON_CLOCK.get(), TheDungeon.defaultResourceLocation("in_next_collapse"), (itemStack, clientLevel, livingEntity, i) -> DungeonClock.isInNextToCollapse(itemStack)?1:0);
    }
}
