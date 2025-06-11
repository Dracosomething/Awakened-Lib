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

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Post event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (entity.level() instanceof ServerLevel level) {
            if (source.getEntity() instanceof LivingEntity livingentity) {
                ItemStack stack = livingentity.getWeaponItem();
                EnchantmentHelper.runIterationOnEquipment(
                        livingentity,
                        (ench, lvl, item) -> {
                            Enchantment enchantment = ench.value();
                            List<MagicEnchantment<AllOf.EntityEffects>> list = enchantment.getEffects(DataComponentsRegistry.MAGIC_ENCHANTMENT.get());
                            list.forEach((enchantment1) -> {
                                if (enchantment1.canActivateEffects(level, lvl, item, entity, entity.position())) {
                                    enchantment.doPostAttack(level, lvl, item, EnchantmentTarget.VICTIM, entity, source);
                                }
                            });
                        }
                );
                EnchantmentHelper.runIterationOnItem(
                        stack,
                        EquipmentSlot.MAINHAND,
                        livingentity,
                        (ench, lvl, item) -> {
                            Enchantment enchantment = ench.value();
                            List<MagicEnchantment<AllOf.EntityEffects>> list = enchantment.getEffects(DataComponentsRegistry.MAGIC_ENCHANTMENT.get());
                            list.forEach((enchantment1) -> {
                                if (enchantment1.canActivateEffects(level, lvl, item, entity, entity.position())) {
                                    enchantment.doPostAttack(level, lvl, item, EnchantmentTarget.ATTACKER, entity, source);
                                }
                            });
                        }
                );
            }
        }
    }

    @SubscribeEvent
    public static void onTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (entity.level() instanceof ServerLevel level) {
            if (entity instanceof LivingEntity living) {
                EnchantmentHelper.runIterationOnEquipment(
                        living,
                        (ench, lvl, item) -> {
                            Enchantment enchantment = ench.value();
                            List<MagicEnchantment<AllOf.EntityEffects>> list = enchantment.getEffects(DataComponentsRegistry.MAGIC_ENCHANTMENT.get());
                            list.forEach((enchantment1) -> {
                                if (enchantment1.canActivateEffects(level, lvl, item, entity, entity.position())) {
                                    enchantment.tick(level, lvl, item, entity);
                                }
                            });
                        }
                );
            }
        }
    }

    @SubscribeEvent
    public static void onProjectileSpawned(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity.level() instanceof ServerLevel level) {
            if (entity instanceof Projectile projectile) {
                Entity owner = projectile.getOwner();
                if (owner instanceof LivingEntity living) {
                    if (projectile instanceof AbstractArrow abstractArrow) {
                        ItemStack stack = abstractArrow.getWeaponItem();
                        if (stack != null) {
                            EnchantmentHelper.runIterationOnItem(
                                    stack,
                                    (ench, lvl) -> {
                                        if (stack.getEquipmentSlot() != null) {
                                            Enchantment enchantment = ench.value();
                                            EnchantedItemInUse itemInUse = new EnchantedItemInUse(stack, stack.getEquipmentSlot(), living);
                                            List<MagicEnchantment<AllOf.EntityEffects>> list = enchantment.getEffects(DataComponentsRegistry.MAGIC_ENCHANTMENT.get());
                                            list.forEach((enchantment1) -> {
                                                if (enchantment1.canActivateEffects(level, lvl, itemInUse, entity, entity.position())) {
                                                    enchantment.onProjectileSpawned(level, lvl, itemInUse, projectile);
                                                }
                                            });
                                        }
                                    }
                            );
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onTick(VanillaGameEvent event) {
        if (event.getVanillaEvent().value().equals(GameEvent.PROJECTILE_LAND.value())) {
            Vec3 pos = event.getEventPosition();
            Entity entity = event.getCause();
            Level level = event.getLevel();
            BlockState state = event.getContext().affectedState();
            if (level instanceof ServerLevel serverLevel) {
                if (entity instanceof AbstractArrow abstractArrow) {
                    Entity owner = abstractArrow.getOwner();
                    if (owner instanceof LivingEntity living) {
                        ItemStack stack = abstractArrow.getWeaponItem();
                        if (stack != null) {
                            if (stack.getEquipmentSlot() != null) {
                                if (state != null) {
                                    EquipmentSlot slot = stack.getEquipmentSlot();
                                    EnchantmentHelper.runIterationOnItem(
                                            stack,
                                            (ench, lvl) -> {
                                                Enchantment enchantment = ench.value();
                                                EnchantedItemInUse itemInUse = new EnchantedItemInUse(stack, stack.getEquipmentSlot(), living, (item) -> {
                                                    if (living.hasItemInSlot(slot) && living.getItemBySlot(slot).equals(stack)) {
                                                        living.setItemSlot(slot, ItemStack.EMPTY);
                                                    }
                                                });
                                                List<MagicEnchantment<AllOf.EntityEffects>> list = enchantment.getEffects(DataComponentsRegistry.MAGIC_ENCHANTMENT.get());
                                                list.forEach((enchantment1) -> {
                                                    if (enchantment1.canActivateEffects(serverLevel, lvl, itemInUse, entity, entity.position())) {
                                                        enchantment.onHitBlock(serverLevel, lvl, itemInUse, abstractArrow, pos, state);
                                                    }
                                                });
                                            }
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onHitBlock(PlayerInteractEvent.LeftClickBlock event) {
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        BlockPos blockPos = event.getPos();
        Vec3 pos = Vec3.atCenterOf(blockPos);
        Level level = event.getLevel();
        if (level instanceof ServerLevel serverLevel) {
            BlockState state = serverLevel.getBlockState(blockPos);
            EnchantmentHelper.runIterationOnItem(
                    stack,
                    (ench, lvl) -> {
                        Enchantment enchantment = ench.value();
                        EnchantedItemInUse itemInUse = new EnchantedItemInUse(stack, stack.getEquipmentSlot(), player, (item) -> {
                            player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND);
                        });
                        List<MagicEnchantment<AllOf.EntityEffects>> list = enchantment.getEffects(DataComponentsRegistry.MAGIC_ENCHANTMENT.get());
                        list.forEach((enchantment1) -> {
                            if (enchantment1.canActivateEffects(serverLevel, lvl, itemInUse, player, pos)) {
                                enchantment.onHitBlock(serverLevel, lvl, itemInUse, player, pos, state);
                            }
                        });
                    }
            );
        }
    }
}
