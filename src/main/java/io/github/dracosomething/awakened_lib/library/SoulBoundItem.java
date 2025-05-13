package io.github.dracosomething.awakened_lib.library;

public @interface SoulBoundItem {
    boolean canBeDropped() default false;

    int getXPRequirement() default 0;

    boolean keepsEnchantments() default true;

    int getDurabilityCost() default 0;

    double minimumDurability() default 0;
}
