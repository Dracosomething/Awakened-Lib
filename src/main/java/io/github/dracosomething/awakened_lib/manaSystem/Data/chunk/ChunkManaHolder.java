package io.github.dracosomething.awakened_lib.manaSystem.Data.chunk;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.Data.api.ManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.Data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.ManaSystemHolder;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.RegenOn;
import io.github.dracosomething.awakened_lib.network.p2c.SyncChunkManaSystem;
import io.github.dracosomething.awakened_lib.network.p2c.SyncEntityManaSystem;
import io.github.dracosomething.awakened_lib.network.p2c.SyncObjects;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.UnknownNullability;

public class ChunkManaHolder extends ManaHolder<ChunkAccess> {
    public ChunkManaHolder(ManaSystemHolder holder) {
        super(holder);
    }

    public void tick(Entity entity) {
        boolean flag = this.system.getSystem().getRegenerator() == RegenOn.CHUNK;
        if (flag) {
            EntityManaHolder holder = entity.getData(DataAttachmentRegistry.getEntity(system.getSystem()));
            holder.setCurrent(holder.getCurrent() + this.system.getSystem().getRegen());
            holder.sync(entity);
        }
    }

    public void sync(ChunkAccess chunk, ServerPlayer entity) {
        HolderLookup.Provider provider = chunk.getLevel().registryAccess();
        CompoundTag tag = chunk.getData(DataAttachmentRegistry.OBJECTS).serializeNBT(provider);
        PacketDistributor.sendToPlayer(entity, new SyncChunkManaSystem(tag, chunk.getPos()));
    }

    @Override
    public ManaHolder<ChunkAccess> getFrom(ChunkAccess holder) {
        return holder.getData(DataAttachmentRegistry.getChunk(this.system.getSystem()).get());
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putString("system", this.system.getSystem().getName());
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        ManaSystemHolder holder = new ManaSystemHolder(StartUpHandler.getMANAGER().get(tag.getString("system")));
        this.setSystem(holder);
    }
}
