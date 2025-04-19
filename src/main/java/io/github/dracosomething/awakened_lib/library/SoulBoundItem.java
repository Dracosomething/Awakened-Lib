package io.github.dracosomething.awakened_lib.library;

public interface SoulBoundItem {
    boolean canBeDropped();

    default int getXPRequirement() {
        return 0;
    }

    default boolean keepsEnchantments() {
        return true;
    }

    default int getDurabilityCost() {
        return 0;
    }

    default double minimumDurability() {
        return 0;
    }
}
