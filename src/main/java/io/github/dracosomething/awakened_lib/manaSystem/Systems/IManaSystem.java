package io.github.dracosomething.awakened_lib.manaSystem.Systems;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface IManaSystem {
    double getMax();

    double getRegen();
}
