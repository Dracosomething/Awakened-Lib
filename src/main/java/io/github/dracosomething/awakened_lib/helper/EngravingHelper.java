package io.github.dracosomething.awakened_lib.helper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Map;

public class EngravingHelper {
    /**
     * removes 1 enchantment from an item.
     * @param itemStack - item with the enchant.
     * @param toClear - enchantment that should be removed.
     * @return - returns the item with the enchantment removed
     */
    public static ItemStack RemoveEnchantments(ItemStack itemStack, Enchantment toClear) {
        EnchantmentHelper.updateEnchantments(itemStack, (enchantments) -> {
            enchantments.removeIf((enchantment) -> {
                return enchantment.get().equals(toClear);
            });
        });

        return itemStack;
    }

    /**
     * removes all enchantment from an item
     * @param itemStack - the item of which the enchantments should be cleared
     * @return - the ItemStack with the enchantments removed
     */
    public static ItemStack RemoveAllEnchantments(ItemStack itemStack) {
        EnchantmentHelper.updateEnchantments(itemStack, (enchantments) -> {
            enchantments.removeIf((enchantment) -> {
                return  true;
            });
        });

        return itemStack;
    }
}
