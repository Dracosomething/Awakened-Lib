package io.github.dracosomething.awakened_lib.lootConditions;

import net.minecraft.util.StringRepresentable;

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
