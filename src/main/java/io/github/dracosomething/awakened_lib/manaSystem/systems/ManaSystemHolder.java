package io.github.dracosomething.awakened_lib.manaSystem.systems;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;

public class ManaSystemHolder {
    private IManaSystem system;

    public <T extends IManaSystem> ManaSystemHolder(T system) {
        this.system = system;
    }

    public IManaSystem getSystem() {
        if (system == null) return StartUpHandler.DEFAULT;
        return system;
    }
}
