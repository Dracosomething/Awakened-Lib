package io.github.dracosomething.awakened_lib.capability;

import io.github.dracosomething.awakened_lib.library.TickingObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.UUID;

@AutoRegisterCapability
public interface IObjects extends INBTSerializable<CompoundTag> {
    HashMap<UUID, TickingObject> getObjects();

    void addObject(UUID objectUUID, TickingObject object);

    public void removeObject(UUID objectUUID);
}