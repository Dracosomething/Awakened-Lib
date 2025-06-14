package io.github.dracosomething.awakened_lib.manaSystem.data.entity;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.data.api.ManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystemHolder;
import io.github.dracosomething.awakened_lib.manaSystem.systems.RegenOn;
import io.github.dracosomething.awakened_lib.network.p2c.SyncEntityManaSystem;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.UnknownNullability;

public class EntityManaHolder extends ManaHolder<Entity> {
    public EntityManaHolder(ManaSystemHolder holder) {
        super(holder);
    }

    public void tick(Entity entity) {
        if (this.max > this.getISystem().getMax())
            this.max = this.getISystem().getMax();
        boolean flag = this.getISystem().getRegenerator() == RegenOn.PLAYER;
        if (flag) {
            double newCurr = this.getCurrent() + this.getISystem().getRegen();
            if (newCurr >= this.getMax()) {
                newCurr = this.getMax();
            }
            this.setCurrent(newCurr);
            this.sync(entity);
        }
    }

    @Override
    public void sync(Entity entity) {
        if (!entity.level().isClientSide) {
            PacketDistributor.sendToAllPlayers(new SyncEntityManaSystem(
                    this.serializeNBT(entity.registryAccess()), entity.getId()
            ));
        }
    }

    @Override
    public ManaHolder<Entity> getFrom(Entity holder) {
        return holder.getData(DataAttachmentRegistry.getEntity(this.system.getSystem()).get());
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("current", this.current);
        tag.putString("system", this.system.getSystem().getName());
        tag.putDouble("max", this.max);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        ManaSystemHolder holder = new ManaSystemHolder(StartUpHandler.getMANAGER().get(tag.getString("system")));
        this.setSystem(holder);
        this.current = tag.getDouble("current");
        this.max = tag.getDouble("max");
    }
}
