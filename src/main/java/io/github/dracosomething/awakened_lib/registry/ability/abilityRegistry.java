package io.github.dracosomething.awakened_lib.registry.ability;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.ability.Ability;
import io.github.dracosomething.awakened_lib.objects.TestObject;
import io.github.dracosomething.awakened_lib.objects.TestProjectile;
import io.github.dracosomething.awakened_lib.objects.api.ObjectType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class abilityRegistry {
    private static final ResourceLocation ABILITIES_KEY = ResourceLocation.fromNamespaceAndPath(Awakened_lib.MODID, "abilities");
    public static final ResourceKey<Registry<Ability>> KEY = ResourceKey.createRegistryKey(ABILITIES_KEY);
    public static final Registry<Ability> ABILITIES_REGISTRY;
    public static final DeferredRegister<Ability> ABILITIES;

    public static void register(IEventBus bus) {
        ABILITIES.register(bus);
    }

    static {
        ABILITIES = DeferredRegister.create(ABILITIES_KEY, Awakened_lib.MODID);
        ABILITIES_REGISTRY = new RegistryBuilder<>(KEY)
                .sync(true)
                .defaultKey(ResourceLocation.fromNamespaceAndPath(Awakened_lib.MODID, "example"))
                .create();
    }
}
