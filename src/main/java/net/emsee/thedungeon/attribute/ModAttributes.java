package net.emsee.thedungeon.attribute;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, TheDungeon.MOD_ID);

    /** the flat damage reduction for a player*/ // can be reworked to work for entities too
    public static final Holder<Attribute> INCOMING_DAMAGE_REDUCTION = register("dungeon.incoming_damage_reduction", new RangedAttribute("attribute.thedungeon.name.generic.incoming_damage_reduction", 0, 0, 2048).setSyncable(true));
    /** the attack reach of a given dungeon mob in blocks */
    public static final Holder<Attribute> DUNGEON_MOB_REACH = register("dungeon.mob_reach", new RangedAttribute("attribute.thedungeon.name.generic.mob_reach", 0, 0, 2048).setSyncable(true));
    /** the amount of player agro the enemies see towards the player */
    public static final Holder<Attribute> DUNGEON_AGGRO_TO_ENEMY = register("dungeon.aggro_to_enemy", new RangedAttribute("attribute.thedungeon.name.generic.aggro_to_enemy", 500, 50, 2048).setSyncable(true));
    /** any player aggro below this value is halved */
    public static final Holder<Attribute> MIN_PERCEPTION = register("dungeon.min_perception", new RangedAttribute("attribute.thedungeon.name.generic.min_aggro", 250, 0, 2048).setSyncable(true));
    /** any player aggro above this value is doubled */
    public static final Holder<Attribute> MAX_PERCEPTION = register("dungeon.max_perception", new RangedAttribute("attribute.thedungeon.name.generic.min_aggro", 750, 0, 2048).setSyncable(true));

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    private static Holder<Attribute> register(String name, Attribute attribute) {
        return ATTRIBUTES.register(name, () -> attribute);
    }
}
