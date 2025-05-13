package io.github.dracosomething.awakened_lib.helper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class NBTHelper {
    public static CompoundTag parseResourceKey(ResourceKey<?> key) {
        CompoundTag tag = new CompoundTag();
        tag.put("registryName", parseResourceLocation(key.registry()));
        tag.put("location", parseResourceLocation(key.location()));
        return tag;
    }

    public static CompoundTag parseResourceLocation(ResourceLocation location) {
        CompoundTag tag = new CompoundTag();
        tag.putString("namespace", location.getNamespace());
        tag.putString("path", location.getPath());
        return tag;
    }

    public static ResourceLocation parseTagToLocation(CompoundTag tag) {
        return ResourceLocation.tryBuild(tag.getString("namespace"), tag.getString("path"));
    }
}
