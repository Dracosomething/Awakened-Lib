package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.helper.ArmorHelper;
import io.github.dracosomething.awakened_lib.helper.ClassHelper;
import io.github.dracosomething.awakened_lib.item.util.MagicArmor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.Arrays;

@EventBusSubscriber(modid = Awakened_lib.MODID)
public class MagicItemHandler {
    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (ClassHelper.hasInterface(event.getTo().getItem().getClass(), MagicArmor.class)) {
            MagicArmor magicArmor = (MagicArmor)event.getTo().getItem();
            if (magicArmor.hasFullSetBonus()) {
                LivingEntity entity = event.getEntity();
                Item[] fullSet = magicArmor.getFullSet();
                if (ArmorHelper.isEntityWearingAll(entity, fullSet)) {
                    ItemStack[] arr = ArmorHelper.getArmorSet(entity);
                    magicArmor.fullSetBonus(arr, entity);
                }
            }
        }
    }
}
