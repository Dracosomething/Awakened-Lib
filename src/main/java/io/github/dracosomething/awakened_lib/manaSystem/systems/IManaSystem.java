package io.github.dracosomething.awakened_lib.manaSystem.systems;

public interface IManaSystem {
    double getMax();

    double getRegen();

    String getName();

    RegenOn getRegenerator();

    int getRegenRate();
}
