package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.LevelValue;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record Subtract(
        LevelBasedValue value2,
        LevelBasedValue value1
) implements LevelBasedValue {
    public static final MapCodec<Subtract> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("value_2").forGetter(Subtract::value2),
            LevelBasedValue.CODEC.optionalFieldOf("value_1", new LevelValue()).forGetter(Subtract::value1)
    ).apply(builder, Subtract::new));

    @Override
    public float calculate(int i) {
        return value1.calculate(i) - value2.calculate(i);
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
