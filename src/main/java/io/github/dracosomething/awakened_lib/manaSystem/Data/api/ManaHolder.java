package io.github.dracosomething.awakened_lib.manaSystem.Data.api;

import io.github.dracosomething.awakened_lib.manaSystem.Systems.ManaSystemHolder;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public abstract class ManaHolder<T extends IAttachmentHolder> implements INBTSerializable<CompoundTag> {
    private T attacher;
    protected ManaSystemHolder system;
    private boolean initialized = false;

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

    public ManaSystemHolder getSystem() {
        return system;
    }

    protected void setSystem(ManaSystemHolder system) {
        this.system = system;
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
