package io.github.dracosomething.awakened_lib.api;

import io.github.dracosomething.awakened_lib.library.ObjectType;
import io.github.dracosomething.awakened_lib.registry.object.objectRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class ObjectsAPI {
    public static IForgeRegistry<ObjectType<?>> getRegistry() {
        return objectRegistry.REGISTRY.get();
    }

    public static ResourceLocation getRegistryKey() {
        return objectRegistry.OBJECTS_KEY;
    }
}
