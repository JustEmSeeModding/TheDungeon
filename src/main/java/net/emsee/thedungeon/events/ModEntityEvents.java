package net.emsee.thedungeon.events;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.attribute.ModAttributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = TheDungeon.MOD_ID)
public class ModEntityEvents {
    @SubscribeEvent
    public static void entityDamaged(LivingDamageEvent.Pre event) {
        if (event.getEntity().getAttributes().hasAttribute(ModAttributes.INCOMING_DAMAGE_REDUCTION)) {
            float reduction = (float) event.getEntity().getAttributeValue(ModAttributes.INCOMING_DAMAGE_REDUCTION);
            if (reduction > 0) {
                event.setNewDamage(event.getNewDamage() - reduction);
            }
        }
    }
}
