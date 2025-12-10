package net.emsee.thedungeon.dungeonClass;

import io.netty.util.Attribute;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeonClass.mainClass.Classless;
import net.emsee.thedungeon.dungeonClass.mainClass.TankClass;
import net.emsee.thedungeon.item.ModSpawnEggs;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModClasses {
    public static final DeferredRegister<DungeonClass> CLASSES =
            DeferredRegister.create(ResourceKey.createRegistryKey(TheDungeon.defaultResourceLocation("dungeon_classes")),TheDungeon.MOD_ID);


    public static final DeferredHolder<DungeonClass, DungeonClass> CLASSLESS = CLASSES.register("classless",
            Classless::new);

    public static final DeferredHolder<DungeonClass, DungeonClass> TANK = CLASSES.register("tank",
            TankClass::new);

    public static void register(IEventBus eventBus) {
        CLASSES.register(eventBus);
    }
}
