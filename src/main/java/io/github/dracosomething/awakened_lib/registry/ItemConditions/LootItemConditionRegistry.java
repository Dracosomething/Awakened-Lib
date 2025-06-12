package io.github.dracosomething.awakened_lib.registry.ItemConditions;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.lootConditions.ManaCondition;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LootItemConditionRegistry {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES;
    public static final Supplier<LootItemConditionType> MANA_CONDITION;

    public static void register(IEventBus bus) {
        LOOT_CONDITION_TYPES.register(bus);
    }

    static {
        LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Awakened_lib.MODID);
        MANA_CONDITION = LOOT_CONDITION_TYPES.register("mana_condition", () -> new LootItemConditionType(ManaCondition.CODEC));
    }
}
