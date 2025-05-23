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
    private double current;

    public EntityManaHolder(ManaSystemHolder holder) {
        super(holder);
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getCurrent() {
        return current;
    }

    public void tick(Entity entity) {
        boolean flag = this.system.getSystem().getRegenerator() == RegenOn.PLAYER;
        if (flag) {
            this.setCurrent(this.getCurrent() + this.system.getSystem().getRegen());
            this.sync(entity);
        }
    }

    @Override
    public void sync(Entity entity) {
        if (!entity.level().isClientSide) {
            if (entity instanceof ServerPlayer player) {
                PacketDistributor.sendToPlayer(player, new SyncEntityManaSystem(
                        this.serializeNBT(player.registryAccess()), player.getId()
                ));
            }
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
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        ManaSystemHolder holder = new ManaSystemHolder(StartUpHandler.getMANAGER().get(tag.getString("system")));
        this.setSystem(holder);
        this.current = tag.getDouble("current");
    }
}
