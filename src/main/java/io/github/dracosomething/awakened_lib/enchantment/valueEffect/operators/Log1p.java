package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.LevelValue;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record Log1p(
        LevelBasedValue level
) implements LevelBasedValue {
    public static final MapCodec<Log1p> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.optionalFieldOf("level", new LevelValue()).forGetter(Log1p::level)
    ).apply(builder, Log1p::new));

    @Override
    public float calculate(int i) {
        return (float) Math.log1p(level().calculate(i));
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
