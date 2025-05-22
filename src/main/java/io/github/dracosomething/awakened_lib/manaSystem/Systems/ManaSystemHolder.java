package io.github.dracosomething.awakened_lib.manaSystem.Systems;

public class ManaSystemHolder {
    private IManaSystem system;

    public <T extends IManaSystem> ManaSystemHolder(T system) {
        this.system = system;
    }

    public IManaSystem getSystem() {
        return system;
    }
}
