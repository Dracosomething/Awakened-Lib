package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.enchantment.MagicEnchantment;
import io.github.dracosomething.awakened_lib.helper.ArmorHelper;
import io.github.dracosomething.awakened_lib.helper.ClassHelper;
import io.github.dracosomething.awakened_lib.item.util.MagicArmor;
import io.github.dracosomething.awakened_lib.registry.dataComponents.DataComponentsRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

@EventBusSubscriber(modid = Awakened_lib.MODID)
public class MagicItemHandler {
    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        applyFullSetBonus(event);
        applyFireResistant(event);
        removeFireResistant(event);
    }

    private static void applyFullSetBonus(LivingEquipmentChangeEvent event) {
        if (ClassHelper.hasInterface(event.getTo().getItem().getClass(), MagicArmor.class)) {
            MagicArmor magicArmor = (MagicArmor) event.getTo().getItem();
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

    private static void applyFireResistant(LivingEquipmentChangeEvent event) {
        EnchantmentHelper.runIterationOnItem(
                event.getTo(),
                (ench, lvl) -> {
                    Enchantment enchantment = ench.value();
                    if (enchantment.effects().has(DataComponentsRegistry.FIRE_PROOF.get())) {
                        event.getTo().set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
                        CustomData data = event.getTo().get(DataComponents.CUSTOM_DATA);
                        if (data == null) return;
                        data.update(tag -> {
                            if (!tag.contains("awakened_lib:originalFireProof")) {
                                tag.putBoolean("awakened_lib:originalFireProof", event.getFrom().has(DataComponents.FIRE_RESISTANT));
                            }
                        });
                        event.getTo().set(DataComponents.CUSTOM_DATA, data);
                    }
                }
        );
    }

    private static void removeFireResistant(LivingEquipmentChangeEvent event) {
        EnchantmentHelper.runIterationOnItem(
                event.getFrom(),
                (ench, lvl) -> {
                    Enchantment enchantment = ench.value();
                    if (enchantment.effects().has(DataComponentsRegistry.FIRE_PROOF.get())) {
                        CustomData data = event.getFrom().get(DataComponents.CUSTOM_DATA);
                        if (data == null) return;
                        CompoundTag tag = data.copyTag();
                        if (tag.contains("awakened_lib:originalFireProof") && tag.getBoolean("awakened_lib:originalFireProof")) {
                            event.getTo().set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
                        } else {
                            event.getTo().remove(DataComponents.FIRE_RESISTANT);
                        }
                        data.update(compoundTag -> {
                            if (compoundTag.contains("awakened_lib:originalFireProof")) {
                                compoundTag.remove("awakened_lib:originalFireProof");
                            }
                        });
                        event.getTo().set(DataComponents.CUSTOM_DATA, data);
                    }
                }
        );
    }

    private static void removeManaSystem(LivingEquipmentChangeEvent event) {
        EnchantmentHelper.runIterationOnItem(
                event.getFrom(),
                (ench, lvl) -> {
                    Enchantment enchantment = ench.value();
                    if (enchantment.effects().has(DataComponentsRegistry.MAGIC_ENCHANTMENT.get())) {
                        MagicEnchantment magic = enchantment.effects().get(DataComponentsRegistry.MAGIC_ENCHANTMENT.get());
                        CustomData data = event.getFrom().get(DataComponents.CUSTOM_DATA);
                        if (data == null) return;
                        CompoundTag tag = data.copyTag();
                        if (tag.contains("awakened_lib:originalMagic") && tag.getBoolean("awakened_lib:originalMagic")) {
                            event.getTo().set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
                        } else {
                            event.getTo().remove(DataComponents.FIRE_RESISTANT);
                        }
                        data.update(compoundTag -> {
                            if (compoundTag.contains("awakened_lib:originalFireProof")) {
                                compoundTag.remove("awakened_lib:originalFireProof");
                            }
                        });
                        event.getTo().set(DataComponents.CUSTOM_DATA, data);
                    }
                }
        );
    }
}
