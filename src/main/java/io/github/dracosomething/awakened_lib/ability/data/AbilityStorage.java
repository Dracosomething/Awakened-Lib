package io.github.dracosomething.awakened_lib.ability.data;

import io.github.dracosomething.awakened_lib.ability.Ability;
import io.github.dracosomething.awakened_lib.ability.AbilityInstance;
import io.github.dracosomething.awakened_lib.api.ability.AbilityAPI;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;

public class AbilityStorage {
    private Map<ResourceLocation, AbilityInstance> abilities;

    public AbilityStorage(AbilityInstance... abilities) {
        this();
        for (AbilityInstance instance : abilities) {
            ResourceLocation location = instance.getAbilityId();
            this.abilities.put(location, instance);
        }
    }

    public AbilityStorage() {
        this.abilities = new HashMap<>();
    }

    public void forEachAbility(BiConsumer<? super ResourceLocation, ? super AbilityInstance> action) {
        abilities.forEach(action);
    }

    public boolean addAbility(Ability ability) {
        AbilityInstance instance = new AbilityInstance(ability);
        return addAbility(instance, instance.getAbilityId());
    }

    public boolean addAbility(AbilityInstance ability) {
        return addAbility(ability, ability.getAbilityId());
    }

    public boolean addAbility(ResourceLocation location) {
        Ability ability = AbilityAPI.getAbility(location);
        AbilityInstance instance = new AbilityInstance(ability);
        return addAbility(instance, location);
    }

    public boolean addAbility(AbilityInstance ability, ResourceLocation location) {
        abilities.put(location, ability);
        return abilities.containsKey(location) && abilities.containsValue(ability);
    }

    public Set<ResourceLocation> getKeys() {
        return this.abilities.keySet();
    }

    public Collection<AbilityInstance> getAbilities() {
        return this.abilities.values();
    }

    public int size() {
        return this.abilities.size();
    }

    public Optional<AbilityInstance> getAbility(@NotNull ResourceLocation location) {
        return Optional.ofNullable(this.abilities.get(location));
    }

    public boolean removeAbility(ResourceLocation location) {
        Ability ability = AbilityAPI.getAbility(location);
        AbilityInstance instance = new AbilityInstance(ability);
        return removeAbility(instance, location);
    }

    public boolean removeAbility(Ability ability) {
        AbilityInstance instance = new AbilityInstance(ability);
        return removeAbility(instance, instance.getAbilityId());
    }

    public boolean removeAbility(AbilityInstance instance) {
        return removeAbility(instance, instance.getAbilityId());
    }

    public boolean removeAbility(AbilityInstance instance, ResourceLocation location) {
        return abilities.remove(location, instance);
    }

    public boolean isEmpty() {
        return this.abilities.isEmpty();
    }
}
