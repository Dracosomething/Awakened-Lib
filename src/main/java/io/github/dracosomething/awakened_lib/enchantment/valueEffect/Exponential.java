package io.github.dracosomething.awakened_lib.enchantment.valueEffect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record Exponential(
        LevelBasedValue base,
        LevelBasedValue scaleFactor,
        LevelBasedValue value
) implements LevelBasedValue {
    public static final MapCodec<Exponential> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("base").forGetter(Exponential::base),
            LevelBasedValue.CODEC.fieldOf("scaleFactor").forGetter(Exponential::scaleFactor),
            LevelBasedValue.CODEC.optionalFieldOf("value", new LevelValue()).forGetter(Exponential::value)
    ).apply(builder, Exponential::new));

    @Override
    public float calculate(int i) {
        return (float) (Math.pow(scaleFactor.calculate(i), value.calculate(i)) * base.calculate(i));
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
