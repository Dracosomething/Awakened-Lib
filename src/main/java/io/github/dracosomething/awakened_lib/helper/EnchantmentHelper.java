package io.github.dracosomething.awakened_lib.helper;

import io.github.dracosomething.awakened_lib.enchantment.MagicEnchantment;
import io.github.dracosomething.awakened_lib.registry.dataComponents.DataComponentsRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.effects.AllOf;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class EnchantmentHelper {
    /**
     * removes 1 enchantment from an item.
     * @param itemStack - item with the enchant.
     * @param toClear - enchantment that should be removed.
     * @return - returns the item with the enchantment removed
     */
    public static ItemStack RemoveEnchantments(ItemStack itemStack, Enchantment toClear) {
        net.minecraft.world.item.enchantment.EnchantmentHelper.updateEnchantments(itemStack, (enchantments) -> {
            enchantments.removeIf((enchantment) -> {
                return enchantment.value().equals(toClear);
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
        net.minecraft.world.item.enchantment.EnchantmentHelper.updateEnchantments(itemStack, (enchantments) -> {
            enchantments.removeIf((enchantment) -> {
                return  true;
            });
        });

        return itemStack;
    }
}
