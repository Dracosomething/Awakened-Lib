package io.github.dracosomething.awakened_lib.capability;

import io.github.dracosomething.awakened_lib.library.ClientTickingObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.HashMap;
import java.util.UUID;

public class ObjectsCapability implements IObjects {
    public static final Capability<IObjects> CAPABILITY = CapabilityManager.get(new CapabilityToken<IObjects>() {});
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
        CompoundTag objects = new CompoundTag();
        this.objects.forEach((UUID, object) -> {
            objects.put(UUID.toString(), object.toNBT());
        });
        tag.put("entries", objects);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        CompoundTag entries = tag.getCompound("entries");
        entries.getAllKeys().forEach((uuid) -> {
            CompoundTag object = entries.getCompound(uuid);
            ClientTickingObject object1 = new ClientTickingObject() {
            };
            object1 = object1.fromNBT(object);
            objects.put(UUID.fromString(uuid), object1);
        });
    }
}
