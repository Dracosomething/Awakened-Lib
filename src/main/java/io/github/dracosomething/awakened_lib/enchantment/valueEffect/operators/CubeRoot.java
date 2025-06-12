package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.LevelValue;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record CubeRoot(
        LevelBasedValue level
) implements LevelBasedValue {
    public static final MapCodec<CubeRoot> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.optionalFieldOf("level", new LevelValue()).forGetter(CubeRoot::level)
    ).apply(builder, CubeRoot::new));

    @Override
    public float calculate(int i) {
        return (float) Math.cbrt(level.calculate(i));
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
