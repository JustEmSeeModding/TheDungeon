package net.emsee.thedungeon;

import net.emsee.thedungeon.attachmentType.ModAttachmentTypes;
import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.block.entity.ModBlockEntities;
import net.emsee.thedungeon.component.ModDataComponentTypes;
import net.emsee.thedungeon.criterion.ModCriteriaTriggerTypes;
import net.emsee.thedungeon.dungeon.ModCleanupDungeons;
import net.emsee.thedungeon.dungeon.ModDungeons;
import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.events.ModEntityRegisterEvents;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.emsee.thedungeon.item.ModArmorMaterials;
import net.emsee.thedungeon.item.ModCreativeModeTabs;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.recipe.ModRecipes;
import net.emsee.thedungeon.villager.ModVillagers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(TheDungeon.MOD_ID)
public final class TheDungeon
{
    public static final String MOD_ID = "thedungeon";

    public TheDungeon(IEventBus modEventBus, ModContainer modContainer)
    {
        DebugLog.logInfo(DebugLog.DebugLevel.INSTANCE_SETUP,"Constructing mod instance...");
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        ModCriteriaTriggerTypes.register(modEventBus);

        loadClass(ModDungeons.class);
        loadClass(ModCleanupDungeons.class);

        ModCreativeModeTabs.register(modEventBus);

        ModAttributes.register(modEventBus);

        ModItems.register(modEventBus);
        ModArmorMaterials.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModDataComponentTypes.register(modEventBus);

        ModEntities.register(modEventBus);

        ModAttachmentTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModVillagers.register(modEventBus);

        ModGamerules.registerRules();

        DebugLog.logInfo(DebugLog.DebugLevel.INSTANCE_SETUP,"Mod instance constructed successfully.");
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        DebugLog.logInfo(DebugLog.DebugLevel.INSTANCE_SETUP,"Common setup...");

        DebugLog.logInfo(DebugLog.DebugLevel.INSTANCE_SETUP,"Common setup phase finished successfully.");
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }


    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            DebugLog.logInfo(DebugLog.DebugLevel.INSTANCE_SETUP,"Client Setup...");
            ModEntityRegisterEvents.ClientEntityRendererSetup(event);
        }
    }

    /**
     * Adapted from: <a href="https://github.com/Aizistral-Studios/Enigmatic-Legacy/blob/1.20.X/src/main/java/com/aizistral/enigmaticlegacy/EnigmaticLegacy.java">Aizistral</a>
     * @author Aizistral
     */
    private void loadClass(Class<?> theClass) {
        try {
            Class.forName(theClass.getName());
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("This can't be happening.");
        }
    }

    public static ResourceLocation defaultResourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
