package net.emsee.thedungeon;

import com.mojang.logging.LogUtils;
import net.emsee.thedungeon.attachmentType.ModAttachmentTypes;
import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.block.entity.ModBlockEntities;
import net.emsee.thedungeon.component.ModDataComponentTypes;
import net.emsee.thedungeon.dungeon.ModDungeons;
import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.events.entityCreation.ModEventBusClientEvents;
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
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(TheDungeon.MOD_ID)
public final class TheDungeon
{
    public enum DebugMode {
        ALL(100),
        GENERIC(2),
        IMPORTANT_ONLY(1),
        NONE(0);

        private final int debugLevel;

        DebugMode(int debugLevel) {this.debugLevel =debugLevel;}
        public int getDebugLevel() { return debugLevel;}
        public boolean is(DebugMode mode) {return debugLevel>=mode.debugLevel;}
    }
    public static final String MOD_ID = "thedungeon";
    public static final Logger LOGGER = LogUtils.getLogger();


    public static final DebugMode debugMode = DebugMode.GENERIC;

    public TheDungeon(IEventBus modEventBus, ModContainer modContainer)
    {
        LOGGER.info("Constructing mod instance...");
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        loadClass(ModDungeons.class);

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
        //modEventBus.addListener(this::addCreative);

        ModGamerules.registerRules();

        LOGGER.info("Mod instance constructed successfully.");
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Common setup...");

        LOGGER.info("Common setup phase finished successfully.");
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }


    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("Client Setup...");
            ModEventBusClientEvents.ClientEntityRendererSetup(event);

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

    public static ResourceLocation resourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
