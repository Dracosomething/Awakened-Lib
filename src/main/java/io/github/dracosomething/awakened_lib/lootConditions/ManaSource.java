package io.github.dracosomething.awakened_lib.lootConditions;

import net.minecraft.util.StringRepresentable;
import net.neoforged.fml.common.asm.enumextension.ExtensionInfo;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;

public enum ManaSource implements StringRepresentable, IExtensibleEnum {
    PLAYER,
    CHUNK,
    ITEM;
    public static final StringRepresentable.EnumCodec<ManaSource> CODEC = StringRepresentable.fromEnum(ManaSource::values);

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }

    public static ExtensionInfo getExtensionInfo() {
        return ExtensionInfo.nonExtended(ManaSource.class);
    }
}
