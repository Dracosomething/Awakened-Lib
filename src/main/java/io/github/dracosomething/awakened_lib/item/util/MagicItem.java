package io.github.dracosomething.awakened_lib.item.util;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.data.item.ItemManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import io.github.dracosomething.awakened_lib.registry.dataComponents.DataComponentsRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface MagicItem {
    default IManaSystem getSystem() {
        return StartUpHandler.DEFAULT;
    }

    double getCost();

    void onUse(ItemStack stack, LivingEntity entity);

    default void activateSpell(ItemStack stack, LivingEntity entity) {
        ItemManaHolder holder = stack.get(DataComponentsRegistry.getItem(this.getSystem()));
        if (holder != null) {
            if (holder.getCurrent() - this.getCost() >= 0) {
                holder.setCurrent(holder.getCurrent() - this.getCost());
                this.onUse(stack, entity);
            }
        }
    }
}
