package net.emsee.thedungeon.network;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.worldSaveData.DungeonSaveData;
import net.emsee.thedungeon.worldSaveData.NBT.DungeonNBTData;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = TheDungeon.MOD_ID)
public class ModNetworking {
    private static final String networkVersion = "1";

    public static final ResourceLocation DUNGEON_DATA_SYNC = TheDungeon.defaultResourceLocation("dungeon_data_sync");

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(networkVersion).executesOn(HandlerThread.MAIN);
        registrar.playToClient(
                DungeonNBTData.DataPacket.TYPE,
                DungeonNBTData.DataPacket.STREAM_CODEC,
                new DungeonSaveData.PayloadHandler()
        );
    }
}
