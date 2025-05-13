package io.github.dracosomething.awakened_lib.capability;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.handler.CapabilitiesHandler;
import io.github.dracosomething.awakened_lib.helper.NBTHelper;
import io.github.dracosomething.awakened_lib.library.TickingObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class ObjectsCapability implements IObjects {
    public static final Capability<IObjects> CAPABILITY = CapabilityManager.get(new CapabilityToken<IObjects>() {});
    public static final ResourceLocation ID = ResourceLocation.tryBuild(Awakened_lib.MODID, "objects");
    private static HashMap<UUID, TickingObject> OBJECTS = new HashMap<>();

    public static boolean addObject(UUID objectUUID, TickingObject object, Level level) {
        IObjects objects = CapabilitiesHandler.getCapability(level, CAPABILITY);
        if (objects == null) return false;
        objects.addObject(objectUUID, object);
        return contains(objectUUID, object, level);
    }

    public static boolean contains(UUID objectUUID, TickingObject object, Level level) {
        IObjects objects = CapabilitiesHandler.getCapability(level, CAPABILITY);
        if (objects == null) return false;
        return OBJECTS.containsKey(objectUUID) && OBJECTS.containsValue(object);
    }

    public static boolean removeObject(UUID objectUUID, TickingObject object, Level level) {
        IObjects objects = CapabilitiesHandler.getCapability(level, CAPABILITY);
        if (objects == null) return false;
        objects.removeObject(objectUUID);
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
            Level level = provider.lookupOrThrow(Registries.DIMENSION).getOrThrow(key).get();
            TickingObject object1 = new TickingObject(1, level, new AABB(0,0,0,0,0,0), BlockPos.ZERO) {
            };
            object1.load(object, provider);
            OBJECTS.put(UUID.fromString(uuid), object1);
        });
    }

    public static LazyOptional<IObjects> getFrom(Level level) {
        @NotNull LazyOptional<IObjects> lazyCap = level.getCapability(CAPABILITY);

        return lazyCap;
    }
}
