package io.github.dracosomething.awakened_lib.item;

import io.github.dracosomething.awakened_lib.library.SoulBoundItem;
import net.minecraft.world.item.Item;

@SoulBoundItem(
        canBeDropped = true
)
public class TestItem extends Item {
    public TestItem(Properties properties) {
        super(properties);
    }
}
