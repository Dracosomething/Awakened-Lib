package io.github.dracosomething.awakened_lib.enchantment.valueEffect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record Logistic(
        LevelBasedValue base,
        LevelBasedValue scaleFactor,
        LevelBasedValue limiter,
        LevelBasedValue exponent
) implements LevelBasedValue {
    public static final MapCodec<Logistic> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("base").forGetter(Logistic::base),
            LevelBasedValue.CODEC.fieldOf("scale_factor").forGetter(Logistic::scaleFactor),
            LevelBasedValue.CODEC.fieldOf("limiter").forGetter(Logistic::limiter),
            LevelBasedValue.CODEC.optionalFieldOf("exponent", new LevelValue()).forGetter(Logistic::exponent)
    ).apply(builder, Logistic::new));

    @Override
    public float calculate(int i) {
        return (float) ((limiter.calculate(i)) / (1 + (base.calculate(i) * Math.pow(scaleFactor.calculate(i), exponent.calculate(i * -1)))));
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}

