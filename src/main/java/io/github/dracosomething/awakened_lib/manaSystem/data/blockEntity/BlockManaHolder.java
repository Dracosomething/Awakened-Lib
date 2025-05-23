package io.github.dracosomething.awakened_lib.manaSystem.data.blockEntity;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.data.api.ManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystemHolder;
import io.github.dracosomething.awakened_lib.network.p2c.SyncBlockManaSystem;
import io.github.dracosomething.awakened_lib.network.p2c.SyncChunkManaSystem;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.UnknownNullability;

public class BlockManaHolder extends ManaHolder<BlockEntity> {
    private double current;

    public BlockManaHolder(ManaSystemHolder holder) {
        super(holder);
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        if (current < 0)
            this.current = 0;
        else
            this.current = current;
    }

    public void sync(BlockEntity holder) {
        PacketDistributor.sendToAllPlayers(new SyncBlockManaSystem(
                this.serializeNBT(holder.getLevel().registryAccess()), holder.getBlockPos()
        ));
    }

    @Override
    public ManaHolder<BlockEntity> getFrom(BlockEntity holder) {
        return null;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("current", this.current);
        tag.putString("system", this.system.getSystem().getName());
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        ManaSystemHolder holder = new ManaSystemHolder(StartUpHandler.getMANAGER().get(tag.getString("system")));
        this.setSystem(holder);
        this.current = tag.getDouble("current");
    }
}
