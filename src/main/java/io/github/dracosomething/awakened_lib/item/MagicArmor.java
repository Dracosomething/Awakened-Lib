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

    default void fullSetBonus(ItemStack[] stacks, LivingEntity entity) {
    }

    default void activateSpell(ItemStack stack, LivingEntity entity) {
        if (stack.getItem() instanceof ArmorItem item) {
            ItemManaHolder holder = stack.get(dataComponentsRegistry.getItem(this.getSystem()));
            if (holder != null) {
                if (holder.getCurrent() - this.getCost() >= 0) {
                    holder.setCurrent(holder.getCurrent() - this.getCost());
                    this.onActivate(stack, item, entity);
                }
            }
        }
    }
}
