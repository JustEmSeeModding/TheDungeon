package net.emsee.thedungeon.item;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.item.custom.DungeonClock;
import net.emsee.thedungeon.item.custom.DungeonPortalCompas;
import net.emsee.thedungeon.worldSaveData.DungeonSaveData;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModItemProperties {
    public static void addCustomItemProperties() {
        ItemProperties.register(ModItems.PYRITE_COMPASS.get(), TheDungeon.defaultResourceLocation("angle"), new CompassItemPropertyFunction((level, stack, entity) -> DungeonPortalCompas.getTargetPosition(stack)));
        ItemProperties.register(ModItems.DUNGEON_CLOCK.get(), TheDungeon.defaultResourceLocation("time_left"), (itemStack, clientLevel, livingEntity, i) -> {
            if (DungeonSaveData.GetClient() == null) return 1f;
            return Math.clamp(DungeonSaveData.GetClient().timeLeft()/12000f,0f,1f);
        });
        ItemProperties.register(ModItems.DUNGEON_CLOCK.get(), TheDungeon.defaultResourceLocation("in_next_collapse_or_outside"), (itemStack, clientLevel, livingEntity, i) -> (DungeonClock.isInNextToCollapseOrOutside(livingEntity, clientLevel))?1:0);
    }
}
