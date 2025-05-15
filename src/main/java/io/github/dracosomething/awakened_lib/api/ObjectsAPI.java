package io.github.dracosomething.awakened_lib.api;

import io.github.dracosomething.awakened_lib.library.ObjectType;
import io.github.dracosomething.awakened_lib.registry.object.objectRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ObjectsAPI {
    public static Registry<ObjectType<?>> getRegistry() {
        return objectRegistry.OBJECTS_REGISTRY;
    }

    public static ResourceKey<Registry<ObjectType<?>>> getRegistryKey() {
        return objectRegistry.KEY;
    }
}
