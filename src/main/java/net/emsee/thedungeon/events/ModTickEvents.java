package net.emsee.thedungeon.events;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.src.GlobalDungeonManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = TheDungeon.MOD_ID)
public class ModTickEvents {
    @SubscribeEvent
    public static void DungeonServerTick(ServerTickEvent.Pre event) {
        GlobalDungeonManager.tick(event);
    }
}
