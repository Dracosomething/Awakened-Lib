package io.github.dracosomething.awakened_lib.api.ability;

import io.github.dracosomething.awakened_lib.ability.Ability;
import io.github.dracosomething.awakened_lib.objects.api.ObjectType;
import io.github.dracosomething.awakened_lib.registry.ability.abilityRegistry;
import io.github.dracosomething.awakened_lib.registry.object.objectRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class AbilityAPI {
    public static Registry<Ability> getRegistry() {
        return abilityRegistry.ABILITIES_REGISTRY;
    }

    public static ResourceKey<Registry<Ability>> getRegistryKey() {
        return abilityRegistry.KEY;
    }
}
