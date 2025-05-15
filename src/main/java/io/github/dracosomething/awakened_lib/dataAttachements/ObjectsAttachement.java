package io.github.dracosomething.awakened_lib.dataAttachements;

import io.github.dracosomething.awakened_lib.handler.CapabilitiesHandler;
import io.github.dracosomething.awakened_lib.helper.NBTHelper;
import io.github.dracosomething.awakened_lib.library.ObjectType;
import io.github.dracosomething.awakened_lib.library.TickingObject;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import io.github.dracosomething.awakened_lib.registry.object.objectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.HashMap;
import java.util.UUID;

public class ObjectsAttachement implements IObjects {
    private static HashMap<UUID, TickingObject> OBJECTS = new HashMap<>();

    public static boolean addObject(UUID objectUUID, TickingObject object, LevelChunk level) {
        ObjectsAttachement objects = level.getData(DataAttachmentRegistry.OBJECTS);
        if (objects == null) return false;
        objects.addObject(objectUUID, object);
        level.setUnsaved(true);
        return contains(objectUUID, object, level);
    }

    public static boolean contains(UUID objectUUID, TickingObject object, LevelChunk level) {
        ObjectsAttachement objects = level.getData(DataAttachmentRegistry.OBJECTS);
        if (objects == null) return false;
        level.setUnsaved(true);
        return OBJECTS.containsKey(objectUUID) && OBJECTS.containsValue(object);
    }

    public static boolean removeObject(UUID objectUUID, TickingObject object, LevelChunk level) {
        ObjectsAttachement objects = level.getData(DataAttachmentRegistry.OBJECTS);
        if (objects == null) return false;
        objects.removeObject(objectUUID);
        level.setUnsaved(true);
        return !contains(objectUUID, object, level);
    }

    public void addObject(UUID objectUUID, TickingObject object) {
        OBJECTS.put(objectUUID, object);
    }

    public void removeObject(UUID objectUUID) {
        OBJECTS.remove(objectUUID);
    }

    public HashMap<UUID, TickingObject> getOBJECTS() {
        return OBJECTS;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        CompoundTag objects = new CompoundTag();
        OBJECTS.forEach((UUID, object) -> {
            objects.put(UUID.toString(), object.serializeNBT(provider));
        });
        tag.put("entries", objects);
        return tag;    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        CompoundTag entries = tag.getCompound("entries");
        entries.getAllKeys().forEach((uuid) -> {
            CompoundTag object = entries.getCompound(uuid);
            CompoundTag locationTag = object.getCompound("level").getCompound("location");
            ResourceLocation location = NBTHelper.parseTagToLocation(locationTag);
            ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, location);
            Holder.Reference<Level> reference = provider.lookupOrThrow(Registries.DIMENSION).getOrThrow(key);
            if (reference.value() instanceof Level) {
                Level level = provider.lookupOrThrow(Registries.DIMENSION).getOrThrow(key).value();
                int life = tag.getInt("life");
                BlockPos pos = BlockPos.of(tag.getLong("pos"));
                ObjectType<?> object1 = objectRegistry.OBJECTS_REGISTRY.get(ResourceLocation.parse(object.getString("Location")));
                TickingObject obj = object1.spawn(life, level, pos);
                OBJECTS.put(UUID.fromString(uuid), obj);
            }
        });
    }
}
