package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.LevelValue;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record Abs(
        LevelBasedValue value
) implements LevelBasedValue {
    public static final MapCodec<Abs> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.optionalFieldOf("value", new LevelValue()).forGetter(Abs::value)
    ).apply(builder, Abs::new));

    @Override
    public float calculate(int i) {
        return Math.abs(value.calculate(i));
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
