package net.emsee.thedungeon.dungeonClass;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeonClass.mainClass.Classless;
import net.emsee.thedungeon.dungeonClass.mainClass.TankClass;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ModClasses {
    public static ResourceKey<Registry<DungeonClass>> CLASS_REGISTRY_KEY = ResourceKey.createRegistryKey(TheDungeon.defaultResourceLocation("dungeon_classes"));

    public static final Registry<DungeonClass> CLASS_REGISTRY =
            new RegistryBuilder<>(CLASS_REGISTRY_KEY)
            .sync(true)
                    .defaultKey(TheDungeon.defaultResourceLocation("empty"))
                    .create();

    public static final DeferredRegister<DungeonClass> CLASSES =
            DeferredRegister.create(CLASS_REGISTRY,TheDungeon.MOD_ID);


    public static final DeferredHolder<DungeonClass, DungeonClass> CLASSLESS = CLASSES.register("classless",
            Classless::new);

    public static final DeferredHolder<DungeonClass, DungeonClass> TANK = CLASSES.register("tank",
            TankClass::new);

    public static void register(IEventBus eventBus) {
        CLASSES.register(eventBus);
    }
}
