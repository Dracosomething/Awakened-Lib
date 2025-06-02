package io.github.dracosomething.awakened_lib.helper;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class ArmorHelper {
    public static boolean isEntityWearingAll(LivingEntity entity, Item[] armorItems) {
        int i = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND) {
                if (Arrays.stream(armorItems).toList().contains(entity.getItemBySlot(slot).getItem())) i++;
                if (i >= armorItems.length)
                    return true;
            }
        }
        return false;
    }

    public static ItemStack[] getArmorSet(LivingEntity entity) {
        ItemStack[] arr = new ItemStack[4];
        int i = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND) {
                if (i < 4) {
                    arr[i] = entity.getItemBySlot(slot);
                    i++;
                }
            }
        }
        return arr;
    }
}
