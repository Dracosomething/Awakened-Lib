package io.github.dracosomething.awakened_lib.helper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

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

    public static CompoundTag parseVec3(Vec3 vec3) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("x", vec3.x);
        tag.putDouble("y", vec3.y);
        tag.putDouble("z", vec3.z);
        return tag;
    }

    public static Vec3 parseTagToVec3(CompoundTag tag) {
        return new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
    }
}
