package io.github.dracosomething.awakened_lib.manaSystem.Data.entity;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.Data.api.ManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.ManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.ManaSystemHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.UnknownNullability;

public class EntityManaHolder extends ManaHolder<Entity> {
    private double current;

    public EntityManaHolder(ManaSystemHolder holder) {
        super(holder);
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("current", this.current);
        tag.putString("system", this.system.getSystem().getName());
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        ManaSystemHolder holder = new ManaSystemHolder(StartUpHandler.getMANAGER().get(tag.getString("system")));
        this.setSystem(holder);
        this.current = tag.getDouble("current");
    }
}
