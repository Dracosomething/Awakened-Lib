package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.LevelValue;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record Power(
        LevelBasedValue base,
        LevelBasedValue exponent
) implements LevelBasedValue {
    public static final MapCodec<Power> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("base").forGetter(Power::base),
            LevelBasedValue.CODEC.optionalFieldOf("exponent", new LevelValue()).forGetter(Power::exponent)
    ).apply(builder, Power::new));

    @Override
    public float calculate(int i) {
        return (float) Math.pow(base.calculate(i), exponent.calculate(i));
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
