package io.github.dracosomething.awakened_lib.manaSystem.data.api;

import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystemHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public abstract class ManaHolder<T extends IAttachmentHolder> implements INBTSerializable<CompoundTag> {
    private T attacher;
    protected ManaSystemHolder system;
    private boolean initialized = false;
    protected double max;
    protected double current;

    public ManaHolder (ManaSystemHolder holder) {
        this.system = holder;
    }

    protected void initialize(T attacher, ManaSystemHolder system) {
        if (attacher == null) {
            throw new NullPointerException("holder is marked non-null but is null");
        } else {
            this.attacher = attacher;
            this.system = system;
            this.initialized = true;
        }
    }

    public void sync(T holder) {};

    public abstract ManaHolder<T> getFrom(T holder);

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        if (this.max > this.getISystem().getMax()) {
            this.max = this.getISystem().getMax();
        } else {
            this.max = max;
        }
    }

    public ManaSystemHolder getSystem() {
        return system;
    }

    protected void setSystem(ManaSystemHolder system) {
        this.system = system;
    }

    public IManaSystem getISystem() {
        return this.system.getSystem();
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        if (current < 0)
            this.current = 0;
        else if (current >= this.getMax())
            this.current = this.getMax();
        else
            this.current = current;
    }

    public boolean canSetCurrent(double cost) {
        return this.current >= cost;
    }

    public boolean updateCurrent(double cost) {
        if (canSetCurrent(cost)) {
            this.setCurrent(this.getCurrent() - cost);
            return true;
        }
        return false;
    }

    public T getAttacher() {
        return attacher;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public abstract @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider);

    public abstract void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag);
}
