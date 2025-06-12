package io.github.dracosomething.awakened_lib.enchantment.valueEffect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record LevelValue() implements LevelBasedValue {
    public static final MapCodec<LevelValue> CODEC = MapCodec.unit(LevelValue::new);

    @Override
    public float calculate(int i) {
        return i;
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return CODEC;
    }
}
