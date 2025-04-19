package io.github.dracosomething.awakened_lib.capability;

import io.github.dracosomething.awakened_lib.library.ClientTickingObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.UUID;

public interface IObjects extends INBTSerializable<CompoundTag> {
    HashMap<UUID, ClientTickingObject> getObjects();
}
