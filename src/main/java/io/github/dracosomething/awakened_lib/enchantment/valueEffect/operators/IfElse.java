package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.enchantment.valueEffect.LevelValue;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record IfElse(
        LevelBasedValue firstValue,
        LevelBasedValue secondValue,
        LevelBasedValue If,
        LevelBasedValue Else,
        Operators operator
) implements LevelBasedValue {
    public static final MapCodec<IfElse> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            LevelBasedValue.CODEC.fieldOf("value_1").forGetter(IfElse::firstValue),
            LevelBasedValue.CODEC.fieldOf("value_2").forGetter(IfElse::secondValue),
            LevelBasedValue.CODEC.fieldOf("if").forGetter(IfElse::If),
            LevelBasedValue.CODEC.fieldOf("else").forGetter(IfElse::Else),
            Operators.CODEC.fieldOf("operator").forGetter(IfElse::operator)
    ).apply(builder, IfElse::new));

    @Override
    public float calculate(int i) {
        float firstVal = firstValue.calculate(i);
        float secondVal = secondValue.calculate(i);
        float then = If().calculate(i);
        float Else = Else().calculate(i);
        switch (operator) {
            case ABOVE -> {
                if (firstVal > secondVal) {
                    return then;
                } else {
                    return Else;
                }
            }
            case BELOW -> {
                if (firstVal < secondVal) {
                    return then;
                } else {
                    return Else;
                }
            }
            case EQUAL -> {
                if (firstVal == secondVal) {
                    return then;
                } else {
                    return Else;
                }
            }
            case EQUAL_OR_ABOVE -> {
                if (firstVal >= secondVal) {
                    return then;
                } else {
                    return Else;
                }
            }
            case EQUAL_OR_BELOW -> {
                if (firstVal <= secondVal) {
                    return then;
                } else {
                    return Else;
                }
            }
            default -> {
                return Else;
            }
        }
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
        return null;
    }
}
