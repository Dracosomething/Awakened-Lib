package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.LevelValue;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record SquareRoot(
        LevelBasedValue value
) implements LevelBasedValue {
    public static final MapCodec<SquareRoot> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.optionalFieldOf("value", new LevelValue()).forGetter(SquareRoot::value)
    ).apply(builder, SquareRoot::new));

    @Override
    public float calculate(int i) {
        return (float) Math.sqrt(value.calculate(i));
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
