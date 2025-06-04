package io.github.dracosomething.awakened_lib.item;

import io.github.dracosomething.awakened_lib.item.util.MagicArmor;
import io.github.dracosomething.awakened_lib.item.util.SoulBoundItem;
import io.github.dracosomething.awakened_lib.registry.items.itemRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Optional;

public class TestArmor extends ArmorItem implements MagicArmor {
    public TestArmor(Type type) {
        super(ArmorMaterials.DIAMOND, type, new Properties());
    }

    @Override
    public boolean hasFullSetBonus() {
        return true;
    }

    @Override
    public Item[] getFullSet() {
        return new Item[]{itemRegistry.ARMOR_HEAD.get(), itemRegistry.ARMOR_CHEST.get(), itemRegistry.ARMOR_LEGS.get(), itemRegistry.ARMOR_FEET.get()};
    }

    @Override
    public void fullSetBonus(ItemStack[] stacks, LivingEntity entity) {
        System.out.println("activated");
        for (ItemStack stack : stacks) {
            RegistryAccess access = entity.registryAccess();
            Optional<Holder.Reference<Enchantment>> prot = access.registryOrThrow(Registries.ENCHANTMENT).getHolder(Enchantments.FIRE_PROTECTION);
            prot.ifPresent((ench) -> {
                stack.enchant(ench, 1);
            });
        }
    }

    @Override
    public double getCost() {
        return 0;
    }
}
