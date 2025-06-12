package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.LevelValue;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record Subtract(
        LevelBasedValue value,
        LevelBasedValue level
) implements LevelBasedValue {
    public static final MapCodec<Subtract> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("value").forGetter(Subtract::value),
            LevelBasedValue.CODEC.optionalFieldOf("level", new LevelValue()).forGetter(Subtract::level)
    ).apply(builder, Subtract::new));

    @Override
    public float calculate(int i) {
        return level().calculate(i) - value.calculate(i);
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
