package io.github.dracosomething.awakened_lib.registry.items;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.item.TestItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class itemRegistry {
    private static DeferredRegister<Item> ITEMS;
    private static DeferredHolder<Item, Item> TEST;

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    static {
        ITEMS = DeferredRegister.create(Registries.ITEM, Awakened_lib.MODID);
        TEST = ITEMS.register("test", () -> new TestItem(new Item.Properties()));
    }
}
