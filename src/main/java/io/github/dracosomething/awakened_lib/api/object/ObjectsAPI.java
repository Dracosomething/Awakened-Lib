package io.github.dracosomething.awakened_lib.api.object;

import io.github.dracosomething.awakened_lib.objects.api.ObjectType;
import io.github.dracosomething.awakened_lib.registry.object.objectRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ObjectsAPI {
    public static Registry<ObjectType<?>> getRegistry() {
        return objectRegistry.OBJECTS_REGISTRY;
    }

    public static ResourceKey<Registry<ObjectType<?>>> getRegistryKey() {
        return objectRegistry.KEY;
    }
}
