package net.emsee.thedungeon.villager;

import com.google.common.collect.ImmutableSet;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, TheDungeon.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, TheDungeon.MOD_ID);

    public static final Holder<PoiType> PORTAL_POI = POI_TYPES.register("portal_poi",
            ()-> new PoiType(ImmutableSet.copyOf(ModBlocks.DUNGEON_PORTAL.get().getStateDefinition().getPossibleStates()),1,1));

    public static final Holder<VillagerProfession> DUNGEON_SCHOLAR = VILLAGER_PROFESSIONS.register("dungeon_scholar",
            () -> new VillagerProfession("dungeon_scholar", holder -> holder.value() == PORTAL_POI.value(), holder -> holder.value() == PORTAL_POI.value(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.END_PORTAL_FRAME_FILL));


    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
