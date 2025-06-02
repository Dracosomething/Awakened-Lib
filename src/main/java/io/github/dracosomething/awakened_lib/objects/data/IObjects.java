package io.github.dracosomething.awakened_lib.objects.data;

import io.github.dracosomething.awakened_lib.objects.api.TickingObject;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.UUID;

public interface IObjects extends INBTSerializable<CompoundTag> {
    HashMap<UUID, TickingObject> getOBJECTS();

    void addObject(UUID objectUUID, TickingObject object);

    public void removeObject(UUID objectUUID);
}