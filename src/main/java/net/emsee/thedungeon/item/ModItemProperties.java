package net.emsee.thedungeon.item;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.item.custom.DungeonPortalCompas;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;


public class ModItemProperties {
    public static void addCustomItemProperties() {
        ItemProperties.register(ModItems.PYRITE_COMPASS.get(), TheDungeon.defaultResourceLocation("angle"), new CompassItemPropertyFunction((level, stack, entity) -> DungeonPortalCompas.getTargetPosition(stack)));

    }
}
