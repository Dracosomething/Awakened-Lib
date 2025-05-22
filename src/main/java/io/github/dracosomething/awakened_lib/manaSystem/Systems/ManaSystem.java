package io.github.dracosomething.awakened_lib.manaSystem.Systems;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.UnknownNullability;

public class ManaSystem implements IManaSystem {
    private final String name;
    private final double max;
    private final double regen;

    public ManaSystem(String name, double max, double regen) {
        this.name = name;
        this.max = max;
        this.regen = regen;
        StartUpHandler.registerSystem(this.name, this);
    }

    public String getName() {
        return name;
    }

    @Override
    public double getMax() {
        return this.max;
    }

    @Override
    public double getRegen() {
        return this.regen;
    }
}
