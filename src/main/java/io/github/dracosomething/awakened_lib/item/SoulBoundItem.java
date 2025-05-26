package io.github.dracosomething.awakened_lib.item;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SoulBoundItem {
    boolean canBeDropped() default false;

    int getXPRequirement() default 0;

    boolean keepsEnchantments() default true;

    int getDurabilityCost() default 0;

    double minimumDurability() default 0;
}
