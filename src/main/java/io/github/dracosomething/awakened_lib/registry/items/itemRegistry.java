package io.github.dracosomething.awakened_lib.registry.items;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.item.TestArmor;
import io.github.dracosomething.awakened_lib.item.TestItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class itemRegistry {
    private static DeferredRegister<Item> ITEMS;
    private static DeferredHolder<Item, Item> TEST;
    public static final DeferredHolder<Item, Item> ARMOR_LEGS;
    public static final DeferredHolder<Item, Item> ARMOR_HEAD;
    public static final DeferredHolder<Item, Item> ARMOR_CHEST;
    public static final DeferredHolder<Item, Item> ARMOR_FEET;

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    static {
        ITEMS = DeferredRegister.create(Registries.ITEM, Awakened_lib.MODID);
        TEST = ITEMS.register("test", () -> new TestItem(new Item.Properties()));
        ARMOR_LEGS = ITEMS.register("legs", () -> new TestArmor(ArmorItem.Type.LEGGINGS));
        ARMOR_HEAD = ITEMS.register("head", () -> new TestArmor(ArmorItem.Type.HELMET));
        ARMOR_CHEST = ITEMS.register("chest", () -> new TestArmor(ArmorItem.Type.CHESTPLATE));
        ARMOR_FEET = ITEMS.register("feet", () -> new TestArmor(ArmorItem.Type.BOOTS));
    }
}
