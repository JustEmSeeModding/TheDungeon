package net.emsee.thedungeon.dungeonClass;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeonClass.mainClass.Classless;
import net.emsee.thedungeon.dungeonClass.mainClass.TankClass;
import net.emsee.thedungeon.dungeonClass.subClass.SubClassless;
import net.emsee.thedungeon.dungeonClass.subClass.VanguardTankSubClass;
import net.emsee.thedungeon.item.ModSpawnEggs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ModSubClasses {
    public static ResourceKey<Registry<DungeonSubClass<?>>> SUBCLASS_REGISTRY_KEY = ResourceKey.createRegistryKey(TheDungeon.defaultResourceLocation("dungeon_subclasses"));

    public static final Registry<DungeonSubClass<?>> SUBCLASS_REGISTRY =
            new RegistryBuilder<>(SUBCLASS_REGISTRY_KEY)
                    .sync(true)
                    .defaultKey(TheDungeon.defaultResourceLocation("empty"))
                    .create();

    public static final DeferredRegister<DungeonSubClass<?>> SUBCLASSES =
            DeferredRegister.create(SUBCLASS_REGISTRY,TheDungeon.MOD_ID);


    public static final DeferredHolder<DungeonSubClass<?>, SubClassless> CLASSLESS = SUBCLASSES.register("classless",
            SubClassless::new);

    public static final DeferredHolder<DungeonSubClass<?>, VanguardTankSubClass> VANGUARD = SUBCLASSES.register("vanguard",
            VanguardTankSubClass::new);


    public static void register(IEventBus eventBus) {
        SUBCLASSES.register(eventBus);
    }


}
