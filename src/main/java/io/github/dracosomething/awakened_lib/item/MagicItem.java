package io.github.dracosomething.awakened_lib.item;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import net.neoforged.bus.api.Event;

public interface MagicItem {
    default IManaSystem getSystem() {
        return StartUpHandler.DEFAULT;
    }

    double getCost();

    void activateSpell();
}
