package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.LevelValue;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record CubeRoot(
        LevelBasedValue value
) implements LevelBasedValue {
    public static final MapCodec<CubeRoot> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.optionalFieldOf("value", new LevelValue()).forGetter(CubeRoot::value)
    ).apply(builder, CubeRoot::new));

    @Override
    public float calculate(int i) {
        return (float) Math.cbrt(value.calculate(i));
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
