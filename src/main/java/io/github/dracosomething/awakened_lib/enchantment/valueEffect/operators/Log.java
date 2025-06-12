package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.LevelValue;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record Log(
        LevelBasedValue base,
        LevelBasedValue exponent
) implements LevelBasedValue {
    public static final MapCodec<Log> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("base").forGetter(Log::base),
            LevelBasedValue.CODEC.optionalFieldOf("exponent", new LevelValue()).forGetter(Log::exponent)
    ).apply(builder, Log::new));

    @Override
    public float calculate(int i) {
        return (float) ((float) (Math.log(base.calculate(i))) / (Math.log(exponent.calculate(i))));
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
