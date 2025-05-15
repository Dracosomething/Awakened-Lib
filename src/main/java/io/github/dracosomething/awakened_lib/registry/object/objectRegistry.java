package io.github.dracosomething.awakened_lib.registry.object;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.objects.api.ObjectType;
import io.github.dracosomething.awakened_lib.objects.TestObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class objectRegistry {
    private static final ResourceLocation OBJECTS_KEY = ResourceLocation.fromNamespaceAndPath(Awakened_lib.MODID, "ojects");
    public static final ResourceKey<Registry<ObjectType<?>>> KEY = ResourceKey.createRegistryKey(OBJECTS_KEY);
    public static final Registry<ObjectType<?>> OBJECTS_REGISTRY;
    public static final DeferredRegister<ObjectType<?>> OBJECTS;
    public static final DeferredHolder<ObjectType<?>, ObjectType<TestObject>> EXAMPLE;

    public static void register(IEventBus bus) {
        OBJECTS.register(bus);
    }

    static {
        OBJECTS = DeferredRegister.create(OBJECTS_KEY, Awakened_lib.MODID);
        OBJECTS_REGISTRY = new RegistryBuilder<>(KEY)
                .sync(true)
                .defaultKey(ResourceLocation.fromNamespaceAndPath(Awakened_lib.MODID, "example"))
                .create();
        EXAMPLE = OBJECTS.register("example", () -> new ObjectType<>(TestObject::new, new AABB(1, 1, 1, 2, 2, 2)));
    }
}
