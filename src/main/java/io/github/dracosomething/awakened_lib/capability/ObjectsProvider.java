package io.github.dracosomething.awakened_lib.capability;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjectsProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    private final IObjects defaultCapability = new ObjectsCapability();
    private final LazyOptional<IObjects> cap = LazyOptional.of(() -> {
        return this.defaultCapability;
    });

    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ObjectsCapability.CAPABILITY ? this.cap.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return (CompoundTag)this.defaultCapability.serializeNBT(provider);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.defaultCapability.deserializeNBT(provider, tag);

    }
}
