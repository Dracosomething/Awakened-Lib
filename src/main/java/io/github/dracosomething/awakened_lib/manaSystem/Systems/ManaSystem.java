package io.github.dracosomething.awakened_lib.manaSystem.Systems;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;

public class ManaSystem implements IManaSystem {
    private final String name;
    private final double max;
    private final double regen;
    private final RegenOn on;

    public ManaSystem(String name, double max, double regen, RegenOn on) {
        this.name = name;
        this.max = max;
        this.regen = regen;
        this.on = on;
        StartUpHandler.getMANAGER().registerSystem(this.name, this);
    }

    public String getName() {
        return name;
    }

    public RegenOn getRegenerator() {
        return this.on;
    }

    public double getMax() {
        return this.max;
    }

    public double getRegen() {
        return this.regen;
    }
}
