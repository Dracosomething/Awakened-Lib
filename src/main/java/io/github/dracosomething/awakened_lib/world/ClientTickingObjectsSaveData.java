package io.github.dracosomething.awakened_lib.world;

import io.github.dracosomething.awakened_lib.library.ClientTickingObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientTickingObjectsSaveData extends SavedData {
    private final HashMap<UUID, ClientTickingObject> objects;

    public static ClientTickingObjectsSaveData get(ServerLevel overworld) {
        System.out.println("computing");
        return (ClientTickingObjectsSaveData)overworld.getDataStorage().computeIfAbsent(ClientTickingObjectsSaveData::new, ClientTickingObjectsSaveData::new, "client_ticking_objects");
    }

    private ClientTickingObjectsSaveData() {
        this.objects = new HashMap();
    }

    private ClientTickingObjectsSaveData(CompoundTag tag) {
        this();
        if (tag.contains("entries")) {
            ListTag skillList = tag.getList("entries", 10);
            skillList.forEach((e) -> {
                CompoundTag entry = (CompoundTag)e;
                ClientTickingObject object = new ClientTickingObject() {
                };
                object = object.fromNBT(entry.getCompound("object"));
                UUID uuid = entry.getUUID("UUID");
                this.objects.put(uuid, object);
            });
        }

    }

    public void addObject(UUID objectUUID, ClientTickingObject object) {
        this.objects.put(objectUUID, object);
        this.setDirty();
    }

    public void removeObject(UUID objectUUID) {
        this.objects.remove(objectUUID);
        this.setDirty();
    }

    public CompoundTag save(CompoundTag tag) {
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

    public HashMap<UUID, ClientTickingObject> getObjects() {
        return objects;
    }
}
