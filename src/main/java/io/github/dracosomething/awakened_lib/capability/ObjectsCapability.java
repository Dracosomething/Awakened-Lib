package io.github.dracosomething.awakened_lib.capability;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.helper.NBTHelper;
import io.github.dracosomething.awakened_lib.library.TickingObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.UUID;

public class ObjectsCapability implements IObjects {
    public static final Capability<IObjects> CAPABILITY = CapabilityManager.get(new CapabilityToken<IObjects>() {});
    public static final ResourceLocation ID = ResourceLocation.tryBuild(Awakened_lib.MODID, "objects");
    private static HashMap<UUID, TickingObject> objects = new HashMap<>();

    public void addObject(UUID objectUUID, TickingObject object) {
        this.objects.put(objectUUID, object);
    }

    public void removeObject(UUID objectUUID) {
        this.objects.remove(objectUUID);
    }

    public HashMap<UUID, TickingObject> getObjects() {
        return objects;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        CompoundTag objects = new CompoundTag();
        this.objects.forEach((UUID, object) -> {
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
            Level level = provider.lookupOrThrow(Registries.DIMENSION).getOrThrow(key).get();
            TickingObject object1 = new TickingObject(1, level, new AABB(0,0,0,0,0,0), BlockPos.ZERO) {
            };
            object1.load(object, provider);
            objects.put(UUID.fromString(uuid), object1);
        });
    }
}
