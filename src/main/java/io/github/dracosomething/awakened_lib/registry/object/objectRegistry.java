package io.github.dracosomething.awakened_lib.registry.object;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.library.ObjectType;
import io.github.dracosomething.awakened_lib.objects.TestObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class objectRegistry {
    public static final ResourceLocation OBJECTS_KEY = ResourceLocation.fromNamespaceAndPath(Awakened_lib.MODID, "ojects");
    public static final DeferredRegister<ObjectType<?>> OBJECTS;
    public static final Supplier<IForgeRegistry<ObjectType<?>>> REGISTRY;
    public static final RegistryObject<ObjectType<TestObject>> EXAMPLE;

    public static void register(IEventBus bus) {
        OBJECTS.register(bus);
    }

    static {
        OBJECTS = DeferredRegister.create(OBJECTS_KEY, Awakened_lib.MODID);
        REGISTRY = OBJECTS.makeRegistry(() -> {
            RegistryBuilder<ObjectType<?>> builder =new RegistryBuilder<>();
            builder = builder.hasWrapper();
            return builder;
        });
        EXAMPLE = OBJECTS.register("example", () -> new ObjectType<TestObject>(TestObject::new, new AABB(1, 1, 1, 2, 2, 2)));
    }
}
