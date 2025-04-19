 package io.github.dracosomething.awakened_lib.registry;

 import io.github.dracosomething.awakened_lib.Awakened_lib;
 import io.github.dracosomething.awakened_lib.mobEffects.EmptyEffect;
 import io.github.dracosomething.awakened_lib.mobEffects.TimeStopCoreEffect;
 import net.minecraft.world.effect.MobEffect;
 import net.minecraft.world.effect.MobEffectCategory;
 import net.minecraftforge.eventbus.api.IEventBus;
 import net.minecraftforge.registries.DeferredRegister;
 import net.minecraftforge.registries.ForgeRegistries;
 import net.minecraftforge.registries.RegistryObject;

public class EffectRegistry {
    private static final DeferredRegister<MobEffect> registry;
    public static final RegistryObject<MobEffect> TIMESTOP;
    public static final RegistryObject<MobEffect> TIMESTOP_CORE;

    public EffectRegistry(){}

    public static void init(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Awakened_lib.MODID);
        TIMESTOP = registry.register("time_stop", () -> new EmptyEffect(MobEffectCategory.NEUTRAL, 14914605));
        TIMESTOP_CORE = registry.register("time_stop_core", () -> new TimeStopCoreEffect(MobEffectCategory.BENEFICIAL, 14914605));
    }
}
