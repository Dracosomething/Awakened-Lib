package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.events.SoulBoundItemsSetupEvent;
import io.github.dracosomething.awakened_lib.helper.ClassHelper;
import io.github.dracosomething.awakened_lib.helper.EnchantmentHelper;
import io.github.dracosomething.awakened_lib.library.SoulBoundItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Awakened_lib.MODID, bus = EventBusSubscriber.Bus.GAME)
public class SoulBoundItemsHandler {
    private static List<Item> SOULBOUNDITEMS = new ArrayList<>();
    private static List<ItemStack> items = new ArrayList<>();

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            List<Item> items = BuiltInRegistries.ITEM.stream().filter((item) -> {
                return ClassHelper.isAnotatedWith(item.getClass(), SoulBoundItem.class);
            }).toList();
            SoulBoundItemsSetupEvent event1 = new SoulBoundItemsSetupEvent(items);
            items = event1.getItems();
            SOULBOUNDITEMS = items;
        });
    }

    private static boolean isSoulBoundItem(Item item) {
        return SOULBOUNDITEMS.contains(item) && item != Items.AIR;
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.getInventory().items.forEach((item) -> {
                if (ClassHelper.isAnotatedWith(item.getItem().getClass(), SoulBoundItem.class)) {
                    SoulBoundItem itemData = (SoulBoundItem) item.getItem();
                    if (item.getDamageValue() >= itemData.minimumDurability() &&
                            player.experienceLevel >= itemData.getXPRequirement()) {
                        item.setDamageValue(item.getDamageValue() - itemData.getDurabilityCost());
                    }
                }
                items.add(item);
            });
        }
    }

    @SubscribeEvent
    public static void onDrop(LivingDropsEvent event) {
        event.getDrops().removeAll(items);
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        items.forEach((item) -> {
            boolean bool = true;
            if (ClassHelper.isAnotatedWith(item.getItem().getClass(), SoulBoundItem.class)) {
                SoulBoundItem itemData = (SoulBoundItem) item.getItem();
                bool = itemData.keepsEnchantments();
            }
            if (!bool) {
                EnchantmentHelper.RemoveAllEnchantments(item);
            }
            event.getEntity().getInventory().add(item);
        });
    }

    @SubscribeEvent
    public static void onPlayerDropitem(ItemTossEvent event) {
        if (isSoulBoundItem(event.getEntity().getItem().getItem())) {
            Item item = event.getEntity().getItem().getItem();
            boolean bool = false;
            if (ClassHelper.isAnotatedWith(item.getClass(), SoulBoundItem.class)) {
                SoulBoundItem itemData = (SoulBoundItem) event.getEntity().getItem().getItem();
                bool = itemData.canBeDropped();
            }
            if (!bool) {
                ItemStack stack = event.getEntity().getItem();
                event.getPlayer().getInventory().add(stack);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void keepItems(PlayerTickEvent.Post event) {
        event.getEntity().getInventory().items.forEach((item) -> {
            if (isSoulBoundItem(item.getItem())) {
                CustomData data = item.get(DataComponents.CUSTOM_DATA);
                if (data == null) return;
                data.update(tag -> {
                    if (!tag.hasUUID("owner")) {
                        tag.putUUID("owner", event.getEntity().getUUID());
                    }
                });
                item.set(DataComponents.CUSTOM_DATA, data);
                CompoundTag tag = data.copyTag();
                if (event.getEntity().getUUID() != tag.getUUID("owner")) {
                    event.getEntity().level().getServer().getAllLevels().forEach((level) -> {
                        Entity entity = level.getEntity(tag.getUUID("owner"));
                        if (entity instanceof Player player) {
                            player.getInventory().add(item);
                            event.getEntity().getInventory().removeItem(item);
                        }
                    });
                }
            }
        });
    }
}
