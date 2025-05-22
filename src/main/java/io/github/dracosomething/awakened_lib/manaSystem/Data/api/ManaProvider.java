package io.github.dracosomething.awakened_lib.manaSystem.Data.api;

import io.github.dracosomething.awakened_lib.manaSystem.Systems.ManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.ManaSystemHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

public abstract class ManaProvider<S extends IAttachmentHolder, T extends ManaHolder<S>> implements IAttachmentSerializer<CompoundTag, T> {
    @Override
    public abstract T read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider);

    @Override
    public abstract @Nullable CompoundTag write(T manaHolder, HolderLookup.Provider provider);
}
