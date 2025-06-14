package io.github.dracosomething.awakened_lib.manaSystem.data.chunk;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.data.api.ManaProvider;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystem;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

public class ChunkManaProvider extends ManaProvider<ChunkAccess, ChunkManaHolder> {
    @Override
    public ChunkManaHolder read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
        IManaSystem system = StartUpHandler.getMANAGER().get(tag.getString("system"));
        ChunkManaHolder mana = holder.getData(DataAttachmentRegistry.getChunk(system));
        mana.deserializeNBT(provider, tag);
        return mana;
    }

    @Override
    public @Nullable CompoundTag write(ChunkManaHolder manaHolder, HolderLookup.Provider provider) {
        CompoundTag tag = manaHolder.serializeNBT(provider);
        return tag;
    }
}
