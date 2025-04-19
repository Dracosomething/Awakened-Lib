package io.github.dracosomething.awakened_lib.capability;

import net.minecraft.core.Direction;
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

    public CompoundTag serializeNBT() {
        return (CompoundTag)this.defaultCapability.serializeNBT();
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.defaultCapability.deserializeNBT(nbt);
    }
}
