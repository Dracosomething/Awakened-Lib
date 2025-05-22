package io.github.dracosomething.awakened_lib.manaSystem.Systems;

public class ManaSystemHolder {
    private ManaSystem system;

    public <T extends ManaSystem> ManaSystemHolder(T system) {
        this.system = system;
    }

    public ManaSystem getSystem() {
        return system;
    }
}
