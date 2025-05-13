package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.events.SoulBoundItemsSetupEvent;
import io.github.dracosomething.awakened_lib.helper.ClassHelper;
import io.github.dracosomething.awakened_lib.helper.EnchantmentHelper;
import io.github.dracosomething.awakened_lib.library.SoulBoundItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Awakened_lib.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoulBoundItemsHandler {
    private static List<Item> SOULBOUNDITEMS = new ArrayList<>();
    private static List<ItemStack> items = new ArrayList<>();

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            List<Item> items = ForgeRegistries.ITEMS.getValues().stream().filter((item) -> {
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
                if (isSoulBoundItem(item.getItem())) {
                    SoulBoundItem soulBoundItem = (SoulBoundItem) item.getItem();
                    if (item.getDamageValue() >= soulBoundItem.minimumDurability() &&
                    player.experienceLevel >= soulBoundItem.getXPRequirement()) {
                        item.setDamageValue(item.getDamageValue() - soulBoundItem.getDurabilityCost());
                        items.add(item);
                    }
                }
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
            SoulBoundItem soulBoundItem = (SoulBoundItem) item.getItem();
            if (!soulBoundItem.keepsEnchantments()) {
                EnchantmentHelper.RemoveAllEnchantments(item);
            }
            event.getEntity().getInventory().add(item);
        });
    }

    @SubscribeEvent
    public static void onPlayerDropitem(ItemTossEvent event) {
        if (isSoulBoundItem(event.getEntity().getItem().getItem())) {
            SoulBoundItem item = (SoulBoundItem) event.getEntity().getItem().getItem();
            if (!item.canBeDropped()) {
                ItemStack stack = event.getEntity().getItem();
                event.getPlayer().getInventory().add(stack);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void keepItems(TickEvent.PlayerTickEvent event) {
            event.player.getInventory().items.forEach((item) -> {
                if (isSoulBoundItem(item.getItem())) {
                    CustomData data = item.get(DataComponents.CUSTOM_DATA);
                    if (data == null) return;
                    data.update(tag -> {
                        if (!tag.hasUUID("owner")) {
                            tag.putUUID("owner", event.player.getUUID());
                        }
                    });
                    item.set(DataComponents.CUSTOM_DATA, data);
                    CompoundTag tag = data.copyTag();
                    if (event.player.getUUID() != tag.getUUID("owner")) {
                        event.player.level().getServer().getAllLevels().forEach((level) -> {
                            Entity entity = level.getEntity(tag.getUUID("owner"));
                            if (entity instanceof Player player) {
                                player.getInventory().add(item);
                                event.player.getInventory().removeItem(item);
                            }
                        });
                    }
                }
            });
    }
}
