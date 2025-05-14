package io.github.dracosomething.awakened_lib.network.p2c;

import io.github.dracosomething.awakened_lib.capability.IObjects;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkContext;

import java.util.function.Supplier;

public class SyncObjectsCapability {
    private final CompoundTag tag;
    private final ResourceKey<Level> level;

    public SyncObjectsCapability(FriendlyByteBuf buf) {
        this.tag = buf.readNbt();
        this.level = buf.readResourceKey(Registries.DIMENSION);
    }

    public SyncObjectsCapability(CompoundTag tag, ResourceKey<Level> entityId) {
        this.tag = tag;
        this.level = entityId;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
        buf.writeResourceKey(this.level);
    }

    public void handle(Supplier<NetworkContext> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
                return () -> {
                    ClientAccess.updatePlayerCapability(this.entityId, this.tag);
                };
            });
        });
        ((NetworkEvent.Context)ctx.get()).setPacketHandled(true);
    }
}
