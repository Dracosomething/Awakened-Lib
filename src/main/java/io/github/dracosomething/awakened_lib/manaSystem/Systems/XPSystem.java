package io.github.dracosomething.awakened_lib.manaSystem.Systems;

public class XPSystem implements IManaSystem {
    public double getMax() {
        return -1;
    }

    public double getRegen() {
        return 0;
    }

    public String getName() {
        return "xp";
    }

    public RegenOn getRegenerator() {
        return RegenOn.NO;
    }
}
