package io.github.dracosomething.awakened_lib.network.p2c;

import io.github.dracosomething.awakened_lib.capability.IObjects;
import io.github.dracosomething.awakened_lib.network.Packet;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkContext;
import net.minecraftforge.network.packets.Acknowledge;

import java.util.function.Supplier;

public class SyncObjectsCapability extends Packet<SyncObjectsCapability> {
    public static StreamCodec<FriendlyByteBuf, SyncObjectsCapability> STREAM_CODEC = StreamCodec.ofMember(SyncObjectsCapability::toBytes, SyncObjectsCapability::new);
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

    public void handle(Supplier<CustomPayloadEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
                return () -> {
                    ServerPlayer player = ctx.get().getSender();
                };
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public StreamCodec<FriendlyByteBuf, SyncObjectsCapability> getCoded() {
        return STREAM_CODEC;
    }
}
