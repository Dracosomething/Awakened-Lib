package io.github.dracosomething.awakened_lib.events;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class SoulBoundItemsSetupEvent extends Event {
    private List<Item> items;

    public SoulBoundItemsSetupEvent(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }
}
