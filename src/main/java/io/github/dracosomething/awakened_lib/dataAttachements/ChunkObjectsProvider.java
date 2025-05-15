package io.github.dracosomething.awakened_lib.dataAttachements;

import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

public class ChunkObjectsProvider implements IAttachmentSerializer<CompoundTag, ObjectsAttachement> {
    @Override
    public ObjectsAttachement read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
        ObjectsAttachement data = holder.getData(DataAttachmentRegistry.OBJECTS);
        data.serializeNBT(provider);
        return data;
    }

    @Override
    public @Nullable CompoundTag write(ObjectsAttachement data, HolderLookup.Provider provider) {
        CompoundTag tag = data.serializeNBT(provider);
        return tag;
    }
}
