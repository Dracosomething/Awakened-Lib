package io.github.dracosomething.awakened_lib.dataAttachements;

import io.github.dracosomething.awakened_lib.library.TickingObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;
import java.util.UUID;

public class ObjectsAttachement implements IObjects{


    @Override
    public HashMap<UUID, TickingObject> getOBJECTS() {
        return null;
    }

    @Override
    public void addObject(UUID objectUUID, TickingObject object) {

    }

    @Override
    public void removeObject(UUID objectUUID) {

    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {

    }
}
