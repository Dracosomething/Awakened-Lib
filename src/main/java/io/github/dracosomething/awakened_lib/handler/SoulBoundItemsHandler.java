package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.events.SoulBoundItemsSetupEvent;
import io.github.dracosomething.awakened_lib.helper.ClassHelper;
import io.github.dracosomething.awakened_lib.helper.EnchantmentHelper;
import io.github.dracosomething.awakened_lib.helper.MagicItemHelper;
import io.github.dracosomething.awakened_lib.item.util.MagicItem;
import io.github.dracosomething.awakened_lib.item.util.SoulBoundItem;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.neoforged.neoforge.attachment.AttachmentType;
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

    public static List<Item> getSOULBOUNDITEMS() {
        return SOULBOUNDITEMS;
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            List<Item> items = BuiltInRegistries.ITEM.stream().filter((item) -> {
                return (ClassHelper.isAnotatedWith(item.getClass(), SoulBoundItem.class) || ClassHelper.hasInterface(item.getClass(), MagicItem.class)) && item !=  Items.AIR;
            }).toList();
            SoulBoundItemsSetupEvent event1 = new SoulBoundItemsSetupEvent(items);
            items = event1.getItems();
            SOULBOUNDITEMS = items;
        });
    }

    private static SoulBoundItem getAnotation(Class<?> obj) {
        return ClassHelper.getAnotation(obj, SoulBoundItem.class);
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.getInventory().items.forEach((item) -> {
                if (ClassHelper.isAnotatedWith(item.getItem().getClass(), SoulBoundItem.class)) {
                    SoulBoundItem itemData = getAnotation(item.getItem().getClass());
                    if (itemData == null) return;
                    IManaSystem system = StartUpHandler.getMANAGER().get(itemData.getSystemID());
                    AttachmentType<EntityManaHolder> holder = DataAttachmentRegistry.getEntity(system).get();
                    if (player.hasData(holder)) {
                        if (item.getDamageValue() >= itemData.minimumDurability() &&
                                player.getData(holder).getCurrent() >= itemData.getRequiredAmount()) {
                            item.setDamageValue(item.getDamageValue() - itemData.getDurabilityCost());
                        }
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
                SoulBoundItem itemData = getAnotation(item.getClass());
                if (itemData == null) return;
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
        if (MagicItemHelper.isSoulBoundItem(event.getEntity().getItem())) {
            Item item = event.getEntity().getItem().getItem();
            boolean bool = true;
            if (ClassHelper.isAnotatedWith(item.getClass(), SoulBoundItem.class)) {
                SoulBoundItem itemData = getAnotation(item.getClass());
                if (itemData == null) return;
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
            if (MagicItemHelper.isSoulBoundItem(item)) {
                boolean bool = true;
                if (ClassHelper.isAnotatedWith(item.getClass(), SoulBoundItem.class)) {
                    SoulBoundItem itemData = getAnotation(item.getClass());
                    if (itemData == null) return;
                    bool = itemData.canBeDropped();
                }
                if (!bool) {
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
                            if (entity instanceof Player player && player.getInventory().contains(item)) {
                                player.getInventory().add(item);
                                event.getEntity().getInventory().removeItem(item);
                            }
                        });
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void initSoulBoundItems(SoulBoundItemsSetupEvent event) {
        event.addItem(Items.DIAMOND_SWORD);
    }
}
