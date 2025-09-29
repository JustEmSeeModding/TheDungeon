package net.emsee.thedungeon.events;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.src.GlobalDungeonManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.Objects;

@EventBusSubscriber(modid = TheDungeon.MOD_ID)
public class LevelEvents {

    //TODO there is a lot of issues with this that need to be fixed or replace with something else

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if(!event.getLevel().isClientSide())
            GlobalDungeonManager.updateForcedChunks(Objects.requireNonNull(event.getLevel().getServer()));
    }


}
