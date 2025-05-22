package io.github.dracosomething.awakened_lib.manaSystem.Data.xp;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.Data.api.ManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.ManaSystemHolder;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.RegenOn;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.XPSystem;
import io.github.dracosomething.awakened_lib.network.p2c.SyncEntityManaSystem;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.UnknownNullability;

public class XPManaHolder extends ManaHolder<Player> {
    private double current;

    public XPManaHolder() {
        super(new ManaSystemHolder(new XPSystem()));
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getCurrent() {
        return current;
    }

    public void tick(Player entity) {
        boolean flag = this.system.getSystem().getRegenerator() == RegenOn.PLAYER;
        if (flag) {
            this.current = Mth.clamp(entity.totalExperience, 0, Integer.MAX_VALUE);
            this.sync(entity);
        }
    }

    @Override
    public void sync(Player entity) {
        if (!entity.level().isClientSide) {
            if (entity instanceof ServerPlayer player) {
                PacketDistributor.sendToPlayer(player, new SyncEntityManaSystem(
                        this.serializeNBT(player.registryAccess()), player.getId()
                ));
            }
        }
    }

    @Override
    public ManaHolder<Player> getFrom(Player holder) {
        return null;
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
