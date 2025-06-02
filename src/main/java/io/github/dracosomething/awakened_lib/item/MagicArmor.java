package io.github.dracosomething.awakened_lib.item;

import io.github.dracosomething.awakened_lib.manaSystem.data.item.ItemManaHolder;
import io.github.dracosomething.awakened_lib.registry.dataComponents.dataComponentsRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public interface MagicArmor extends MagicItem {
    void onActivate(ItemStack stack, ArmorItem item, LivingEntity entity);

    default boolean hasFullSetBonus() {
        return false;
    };

    default ArmorItem[] getFullSet() {
        return new ArmorItem[]{};
    }

    default void fullSetBonus(ItemStack[] stacks, LivingEntity entity) {
    }
}
