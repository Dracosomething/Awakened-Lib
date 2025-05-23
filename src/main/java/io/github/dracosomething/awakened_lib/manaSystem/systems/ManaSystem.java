package io.github.dracosomething.awakened_lib.manaSystem.systems;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;

public class ManaSystem implements IManaSystem {
    private final String name;
    private final double max;
    private final double regen;
    private final RegenOn on;
    private final int regenRate;

    public ManaSystem(String name, double max, double regen, RegenOn on, int regenRate) {
        this.name = name;
        this.max = max;
        this.regen = regen;
        this.on = on;
        this.regenRate = regenRate;
        StartUpHandler.getMANAGER().registerSystem(this.name, this);
    }

    public String getName() {
        return name;
    }

    public RegenOn getRegenerator() {
        return this.on;
    }

    public int getRegenRate() {
        return this.regenRate;
    }

    public double getMax() {
        return this.max;
    }

    public double getRegen() {
        return this.regen;
    }
}
