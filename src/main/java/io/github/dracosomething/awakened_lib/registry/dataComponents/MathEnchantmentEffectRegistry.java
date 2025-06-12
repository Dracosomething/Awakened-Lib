package io.github.dracosomething.awakened_lib.registry.dataComponents;

import com.mojang.serialization.MapCodec;
import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.enchantment.SoulBound;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.Exponential;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.Logistic;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators.*;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MathEnchantmentEffectRegistry {
    public static final DeferredRegister<MapCodec<? extends LevelBasedValue>> ENCHANTMENT_COMPONENTS;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<Abs>> ABS;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<Add>> ADD;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<CubeRoot>> CUBE_ROOT;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<Divide>> DIVIDE;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<Log>> LOG;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<Log10>> LOG_10;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<Log1p>> LOG_1P;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<Multiply>> MULTIPLY;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<Power>> POWER;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<Round>> ROUND;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<SquareRoot>> SQUARE_ROOT;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<Subtract>> SUBSTRACT;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<Exponential>> EXPONENTIAL;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<Logistic>> LOGISTIC;
    public static final DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<Invert>> INVERT;

    public static void register(IEventBus bus) {
        ENCHANTMENT_COMPONENTS.register(bus);
    }

    static {
        ENCHANTMENT_COMPONENTS = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT_LEVEL_BASED_VALUE_TYPE, Awakened_lib.MODID);
        ABS = ENCHANTMENT_COMPONENTS.register("abs", () -> Abs.CODEC);
        ADD = ENCHANTMENT_COMPONENTS.register("add", () -> Add.CODEC);
        CUBE_ROOT = ENCHANTMENT_COMPONENTS.register("cube_root", () -> CubeRoot.CODEC);
        DIVIDE = ENCHANTMENT_COMPONENTS.register("divide", () -> Divide.CODEC);
        LOG = ENCHANTMENT_COMPONENTS.register("log", () -> Log.CODEC);
        LOG_10 = ENCHANTMENT_COMPONENTS.register("log_10", () -> Log10.CODEC);
        LOG_1P = ENCHANTMENT_COMPONENTS.register("log_1p", () -> Log1p.CODEC);
        MULTIPLY = ENCHANTMENT_COMPONENTS.register("multiply", () -> Multiply.CODEC);
        POWER = ENCHANTMENT_COMPONENTS.register("power", () -> Power.CODEC);
        ROUND = ENCHANTMENT_COMPONENTS.register("round", () -> Round.CODEC);
        SQUARE_ROOT = ENCHANTMENT_COMPONENTS.register("square_root", () -> SquareRoot.CODEC);
        SUBSTRACT = ENCHANTMENT_COMPONENTS.register("subtract", () -> Subtract.CODEC);
        EXPONENTIAL = ENCHANTMENT_COMPONENTS.register("exponential", () -> Exponential.CODEC);
        LOGISTIC = ENCHANTMENT_COMPONENTS.register("logistic", () -> Logistic.CODEC);
        INVERT = ENCHANTMENT_COMPONENTS.register("invert", () -> Invert.CODEC);
    }
}
