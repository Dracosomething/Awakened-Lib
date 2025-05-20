package io.github.dracosomething.awakened_lib.network.p2c;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.dataAttachements.ChunkObjectsProvider;
import io.github.dracosomething.awakened_lib.dataAttachements.ObjectsAttachement;
import io.github.dracosomething.awakened_lib.network.Packet;
import io.github.dracosomething.awakened_lib.network.Side;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncObjects implements CustomPacketPayload {
    private static Type<SyncObjects> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Awakened_lib.MODID, "syncobjects"));
    private static StreamCodec<RegistryFriendlyByteBuf, SyncObjects> CODEC = StreamCodec.of(SyncObjects::toBytes, SyncObjects::new);
    public static Packet<SyncObjects> PACKET = new Packet<>(
        TYPE, CODEC, SyncObjects::handle, Side.CLIENT
    );
    private CompoundTag tag;
    private ChunkPos pos;

    public SyncObjects(CompoundTag tag, ChunkPos pos) {
        this.pos = pos;
        this.tag = tag;
    }

    public SyncObjects(FriendlyByteBuf buf) {
        this.pos = buf.readChunkPos();
        this.tag = buf.readNbt();
    }

    public static void toBytes(RegistryFriendlyByteBuf buf, SyncObjects objects) {
        buf.writeChunkPos(objects.pos);
        buf.writeNbt(objects.tag);
    }

    public static void handle(SyncObjects packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                LevelChunk chunk = Minecraft.getInstance().level.getChunk(packet.pos.x, packet.pos.z);
                ObjectsAttachement attachement = chunk.getData(DataAttachmentRegistry.OBJECTS);
                HolderLookup.Provider provider = chunk.getLevel().registryAccess();
                attachement.deserializeNBT(provider, packet.tag);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
