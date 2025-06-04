package io.github.dracosomething.awakened_lib.item.util;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface SoulBoundItem {
    String getSystemID() default "xp";

    boolean canBeDropped() default true;

    int getRequiredAmount() default 0;

    boolean keepsEnchantments() default true;

    int getDurabilityCost() default 0;

    double minimumDurability() default 0;
}
