package io.github.dracosomething.awakened_lib.helper;

import io.github.dracosomething.awakened_lib.handler.SoulBoundItemsHandler;
import io.github.dracosomething.awakened_lib.item.util.MagicItem;
import io.github.dracosomething.awakened_lib.registry.dataComponents.DataComponentsRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.concurrent.atomic.AtomicBoolean;

public class MagicItemHelper {
    public static boolean isSoulBoundItem(ItemStack item) {
        AtomicBoolean bool = new AtomicBoolean(false);
        net.minecraft.world.item.enchantment.EnchantmentHelper.runIterationOnItem(item, (enchantmentHolder, enchantLevel) -> {
            bool.set(enchantmentHolder.value().effects().has(DataComponentsRegistry.SOUL_BOUND.get()));
        });
        return (SoulBoundItemsHandler.getSOULBOUNDITEMS().contains(item.getItem()) || bool.get()) && item.getItem() != Items.AIR;
    }

    public static boolean isMagicItem(ItemStack stack) {
        AtomicBoolean bool = new AtomicBoolean(false);
        net.minecraft.world.item.enchantment.EnchantmentHelper.runIterationOnItem(stack, (enchantmentHolder, enchantLevel) -> {
            bool.set(enchantmentHolder.value().effects().has(DataComponentsRegistry.MAGIC_ENCHANTMENT.get()));
        });
        return ClassHelper.hasInterface(stack.getItem().getClass(), MagicItem.class) || MagicItemHelper.isSoulBoundItem(stack) || bool.get();
    }
}
