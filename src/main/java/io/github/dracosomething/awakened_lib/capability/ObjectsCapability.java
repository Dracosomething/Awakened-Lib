package io.github.dracosomething.awakened_lib.capability;

import io.github.dracosomething.awakened_lib.library.ClientTickingObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.UUID;

public class ObjectsCapability implements IObjects {
    private HashMap<UUID, ClientTickingObject> objects = new HashMap<>();

    public void addObject(UUID objectUUID, ClientTickingObject object) {
        this.objects.put(objectUUID, object);
    }

    public void removeObject(UUID objectUUID) {
        this.objects.remove(objectUUID);
    }

    @Override
    public HashMap<UUID, ClientTickingObject> getObjects() {
        return objects;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag objects = new ListTag();
        this.objects.forEach((UUID, object) -> {
            CompoundTag entry = new CompoundTag();
            entry.put("object", object.toNBT());
            entry.putUUID("UUID", UUID);
            objects.add(entry);
        });
        tag.put("entries", objects);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {

    }
}
