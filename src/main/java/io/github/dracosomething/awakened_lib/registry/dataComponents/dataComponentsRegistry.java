package io.github.dracosomething.awakened_lib.registry.dataComponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.data.chunk.ChunkManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.chunk.ChunkManaProvider;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaProvider;
import io.github.dracosomething.awakened_lib.manaSystem.data.item.ItemManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystemHolder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class dataComponentsRegistry {
    public static final DeferredRegister.DataComponents COMPONENTS;
    public static final DeferredRegister<DataComponentType<?>> ENCHANTMENT_COMPONENTS = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Awakened_lib.MODID);
    private static final Map<IManaSystem, DeferredHolder<DataComponentType<?>, DataComponentType<ItemManaHolder>>> ITEM_SYSTEMS = new HashMap<>();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> SOUL_BOUND = ENCHANTMENT_COMPONENTS.register("soul_bound", () ->
        DataComponentType.<Unit>builder().persistent(Unit.CODEC).build()
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
