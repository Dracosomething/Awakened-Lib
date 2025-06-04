package io.github.dracosomething.awakened_lib.registry.dataComponents;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.enchantment.SoulBound;
import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.data.item.ItemManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class dataComponentsRegistry {
    public static final DeferredRegister.DataComponents COMPONENTS;
    public static final DeferredRegister<DataComponentType<?>> ENCHANTMENT_COMPONENTS = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Awakened_lib.MODID);
    private static final Map<IManaSystem, DeferredHolder<DataComponentType<?>, DataComponentType<ItemManaHolder>>> ITEM_SYSTEMS = new HashMap<>();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SoulBound>> SOUL_BOUND = ENCHANTMENT_COMPONENTS.register("soul_bound", () ->
        DataComponentType.<SoulBound>builder().persistent(SoulBound.CODEC).build()
    );
    public static DeferredHolder<DataComponentType<?>, DataComponentType<ItemManaHolder>> getItem(IManaSystem system) {
        return ITEM_SYSTEMS.get(system);
    }

    public static void forEachItem(BiConsumer<IManaSystem, DeferredHolder<DataComponentType<?>, DataComponentType<ItemManaHolder>>> consumer) {
        ITEM_SYSTEMS.forEach(consumer);
    }

    public static void register(IEventBus bus) {
        loadSystems();
        COMPONENTS.register(bus);
        ENCHANTMENT_COMPONENTS.register(bus);
    }

    public static void loadSystems() {
        StartUpHandler.getMANAGER().foreach((id, system) -> {
            DeferredHolder<DataComponentType<?>, DataComponentType<ItemManaHolder>> itemSupplier = COMPONENTS.registerComponentType(
                    id + "_item", (builder) -> builder
                            .persistent(ItemManaHolder.CODEC)
                            .networkSynchronized(ItemManaHolder.BASIC_STREAM_CODEC)
            );
            ITEM_SYSTEMS.put(system, itemSupplier);
        });
    }

    static {
        COMPONENTS = DeferredRegister.createDataComponents(Awakened_lib.MODID);
    }
}
