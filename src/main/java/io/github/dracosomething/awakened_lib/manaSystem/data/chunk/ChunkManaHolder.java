package io.github.dracosomething.awakened_lib.manaSystem.data.chunk;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.data.api.ManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystemHolder;
import io.github.dracosomething.awakened_lib.manaSystem.systems.RegenOn;
import io.github.dracosomething.awakened_lib.network.p2c.SyncChunkManaSystem;
import io.github.dracosomething.awakened_lib.network.p2c.SyncEntityManaSystem;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.UnknownNullability;

public class ChunkManaHolder extends ManaHolder<ChunkAccess> {
    private int multiplier = RandomSource.create().nextInt(0, 100);

    public ChunkManaHolder(ManaSystemHolder holder) {
        super(holder);
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void tick(Entity entity) {
        boolean flag = this.getISystem().getRegenerator() == RegenOn.CHUNK;
        if (flag) {
            EntityManaHolder holder = entity.getData(DataAttachmentRegistry.getEntity(getISystem()));
            double newCurr = holder.getCurrent() + (this.getISystem().getRegen() * this.multiplier);
            if (newCurr >= holder.getISystem().getMax()) {
                newCurr = holder.getISystem().getMax();
            }
            holder.setCurrent(newCurr);
            holder.sync(entity);
        }
    }

    public void sync(ChunkAccess chunk) {
        if (chunk.getLevel() != null) {
            if (!chunk.getLevel().isClientSide) {
                PacketDistributor.sendToAllPlayers(new SyncChunkManaSystem(
                        this.serializeNBT(chunk.getLevel().registryAccess()), chunk.getPos()
                ));
            }
        }
    }

    @Override
    public ManaHolder<ChunkAccess> getFrom(ChunkAccess holder) {
        return holder.getData(DataAttachmentRegistry.getChunk(this.system.getSystem()).get());
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putString("system", this.system.getSystem().getName());
        tag.putInt("multiplier", this.multiplier);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        ManaSystemHolder holder = new ManaSystemHolder(StartUpHandler.getMANAGER().get(tag.getString("system")));
        this.setSystem(holder);
        this.multiplier = tag.getInt("multiplier");
    }
}
