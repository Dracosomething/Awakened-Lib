package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.helper.ClassHelper;
import io.github.dracosomething.awakened_lib.item.MagicArmor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

@EventBusSubscriber(modid = Awakened_lib.MODID)
public class MagicItemHandler {
    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (ClassHelper.hasInterface(event.getFrom().getItem().getClass(), MagicArmor.class)) {
            MagicArmor magicArmor = (MagicArmor)event.getFrom().getItem();
            
        }
    }
}
