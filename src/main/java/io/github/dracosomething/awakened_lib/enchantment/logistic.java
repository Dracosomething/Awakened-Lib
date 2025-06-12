package io.github.dracosomething.awakened_lib.enchantment;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record logistic(
        float base,
        float scaleFactor,
        float limiter
) implements LevelBasedValue {

    @Override
    public float calculate(int i) {
        return (float) ((limiter) / (1 + (base * Math.pow(scaleFactor, -1))));
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return null;
    }
}

