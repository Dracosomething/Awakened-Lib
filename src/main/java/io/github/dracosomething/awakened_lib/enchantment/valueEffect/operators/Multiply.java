package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.LevelValue;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record Multiply(
        LevelBasedValue value1,
        LevelBasedValue value2
) implements LevelBasedValue {
    public static final MapCodec<Multiply> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("value_1").forGetter(Multiply::value1),
            LevelBasedValue.CODEC.optionalFieldOf("value_2", new LevelValue()).forGetter(Multiply::value2)
    ).apply(builder, Multiply::new));

    @Override
    public float calculate(int i) {
        return value1.calculate(i) * value2.calculate(i);
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
