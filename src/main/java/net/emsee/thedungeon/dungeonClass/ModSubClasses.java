package net.emsee.thedungeon.dungeonClass;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeonClass.mainClass.Classless;
import net.emsee.thedungeon.dungeonClass.mainClass.TankClass;
import net.emsee.thedungeon.dungeonClass.subClass.SubClassless;
import net.emsee.thedungeon.item.ModSpawnEggs;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSubClasses {
    public static final DeferredRegister<DungeonSubClass<?>> SUBCLASSES =
            DeferredRegister.create(ResourceKey.createRegistryKey(TheDungeon.defaultResourceLocation("dungeon_subclasses")),TheDungeon.MOD_ID);


    public static final DeferredHolder<DungeonSubClass<?>, SubClassless> CLASSLESS = SUBCLASSES.register("classless",
            SubClassless::new);


    public static void register(IEventBus eventBus) {
        SUBCLASSES.register(eventBus);
    }
}
