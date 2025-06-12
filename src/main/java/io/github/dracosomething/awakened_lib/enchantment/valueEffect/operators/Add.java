package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.LevelValue;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record Add(
        LevelBasedValue value2,
        LevelBasedValue value1
) implements LevelBasedValue {
    public static final MapCodec<Add> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("value_1").forGetter(Add::value1),
            LevelBasedValue.CODEC.optionalFieldOf("value_2", new LevelValue()).forGetter(Add::value2)
    ).apply(builder, Add::new));

    @Override
    public float calculate(int i) {
        return value1.calculate(i) + value2.calculate(i);
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
