package io.github.dracosomething.awakened_lib.manaSystem.systems;

import io.github.dracosomething.awakened_lib.lootConditions.ManaSource;
import net.minecraft.util.Unit;
import net.neoforged.fml.common.asm.enumextension.ExtensionInfo;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;

public enum RegenOn implements IExtensibleEnum {
    PLAYER,
    CHUNK,
    NO;

    public static ExtensionInfo getExtensionInfo() {
        return ExtensionInfo.nonExtended(ManaSource.class);
    }
}
