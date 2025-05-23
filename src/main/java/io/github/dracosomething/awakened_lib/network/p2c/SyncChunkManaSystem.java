package io.github.dracosomething.awakened_lib.network.p2c;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.network.Packet;
import io.github.dracosomething.awakened_lib.network.Side;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncChunkManaSystem implements CustomPacketPayload {
    private static Type<SyncChunkManaSystem> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Awakened_lib.MODID, "sync_chunk_system"));
    private static StreamCodec<RegistryFriendlyByteBuf, SyncChunkManaSystem> CODEC = StreamCodec.of(SyncChunkManaSystem::toBytes, SyncChunkManaSystem::new);
    public static Packet<SyncChunkManaSystem> PACKET = new Packet<>(
        TYPE, CODEC, SyncChunkManaSystem::handle, Side.CLIENT
    );
    private CompoundTag tag;
    private ChunkPos pos;

    public SyncChunkManaSystem(CompoundTag tag, ChunkPos pos) {
        this.pos = pos;
        this.tag = tag;
    }

    public SyncChunkManaSystem(FriendlyByteBuf buf) {
        this.pos = buf.readChunkPos();
        this.tag = buf.readNbt();
    }

    public static void toBytes(RegistryFriendlyByteBuf buf, SyncChunkManaSystem objects) {
        buf.writeChunkPos(objects.pos);
        buf.writeNbt(objects.tag);
    }

    public static void handle(SyncChunkManaSystem packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                LevelChunk chunk = Minecraft.getInstance().level.getChunk(packet.pos.x, packet.pos.z);
                DataAttachmentRegistry.forEachEntity((system, mana) -> {
                    EntityManaHolder holder = chunk.getData(DataAttachmentRegistry.getEntity(system).get());
                    RegistryAccess access = chunk.getLevel().registryAccess();
                    holder.deserializeNBT(access, packet.tag);
                });
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
