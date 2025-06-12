package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.LevelValue;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record Power(
        LevelBasedValue value,
        LevelBasedValue level
) implements LevelBasedValue {
    public static final MapCodec<Power> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("value").forGetter(Power::value),
            LevelBasedValue.CODEC.optionalFieldOf("level", new LevelValue()).forGetter(Power::level)
    ).apply(builder, Power::new));

    @Override
    public float calculate(int i) {
        return (float) Math.pow(level.calculate(i), value.calculate(i));
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
