package io.github.dracosomething.awakened_lib.enchantment;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record Exponential(
        float base,
        float scaleFactor
) implements LevelBasedValue {

    @Override
    public float calculate(int i) {
        return (float) (Math.pow(scaleFactor, i) * base);
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return null;
    }
}
