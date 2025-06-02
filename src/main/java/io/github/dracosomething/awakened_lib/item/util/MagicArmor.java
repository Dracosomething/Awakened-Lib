package io.github.dracosomething.awakened_lib.item.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface MagicArmor extends MagicItem {
    default boolean hasFullSetBonus() {
        return false;
    };

    default Item[] getFullSet() {
        return new Item[]{};
    }

    default void fullSetBonus(ItemStack[] stacks, LivingEntity entity) {
    }

    default void onUse(ItemStack stack, LivingEntity entity) {

    }
}
