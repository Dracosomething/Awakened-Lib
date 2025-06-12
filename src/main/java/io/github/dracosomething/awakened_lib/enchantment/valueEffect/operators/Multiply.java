package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.LevelValue;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record Multiply(
        LevelBasedValue value,
        LevelBasedValue level
) implements LevelBasedValue {
    public static final MapCodec<Multiply> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("value").forGetter(Multiply::value),
            LevelBasedValue.CODEC.optionalFieldOf("level", new LevelValue()).forGetter(Multiply::level)
    ).apply(builder, Multiply::new));

    @Override
    public float calculate(int i) {
        return level.calculate(i) * value.calculate(i);
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
