package io.github.dracosomething.awakened_lib.enchantment;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;

public enum ManaSource implements StringRepresentable {
    PLAYER,
    CHUNK,
    ITEM;
    public static final StringRepresentable.EnumCodec<ManaSource> CODEC = StringRepresentable.fromEnum(ManaSource::values);

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
