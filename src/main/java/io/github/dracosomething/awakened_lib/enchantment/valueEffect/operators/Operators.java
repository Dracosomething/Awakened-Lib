package io.github.dracosomething.awakened_lib.enchantment.valueEffect.operators;

import io.github.dracosomething.awakened_lib.lootConditions.ManaSource;
import net.minecraft.util.StringRepresentable;
import net.neoforged.fml.common.asm.enumextension.ExtensionInfo;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;
import net.neoforged.fml.common.asm.enumextension.NamedEnum;

public enum Operators implements StringRepresentable {
    EQUAL("=="),
    ABOVE(">"),
    BELOW("<"),
    EQUAL_OR_ABOVE(">="),
    EQUAL_OR_BELOW("<=");
    public static final StringRepresentable.EnumCodec<Operators> CODEC = StringRepresentable.fromEnum(Operators::values);
    String value;

    Operators(String value) {
        this.value = value;
    }

    @Override
    public String getSerializedName() {
        return this.value;
    }
}
