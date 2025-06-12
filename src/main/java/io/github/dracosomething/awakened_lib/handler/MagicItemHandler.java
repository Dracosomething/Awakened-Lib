package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.enchantment.MagicEnchantment;
import io.github.dracosomething.awakened_lib.helper.ArmorHelper;
import io.github.dracosomething.awakened_lib.helper.ClassHelper;
import io.github.dracosomething.awakened_lib.item.util.MagicArmor;
import io.github.dracosomething.awakened_lib.registry.dataComponents.DataComponentsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.effects.AllOf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.VanillaGameEvent;
import net.neoforged.neoforge.event.enchanting.EnchantmentLevelSetEvent;
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.List;

@EventBusSubscriber(modid = Awakened_lib.MODID)
public class MagicItemHandler {
    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
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
}
