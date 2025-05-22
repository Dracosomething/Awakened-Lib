package io.github.dracosomething.awakened_lib.network.p2c;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.dataAttachements.ObjectsAttachement;
import io.github.dracosomething.awakened_lib.manaSystem.Data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.network.Packet;
import io.github.dracosomething.awakened_lib.network.Side;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncEntityManaSystem implements CustomPacketPayload {
    private static Type<SyncEntityManaSystem> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Awakened_lib.MODID, "sync_mana_entity"));
    private static StreamCodec<RegistryFriendlyByteBuf, SyncEntityManaSystem> CODEC = StreamCodec.of(SyncEntityManaSystem::toBytes, SyncEntityManaSystem::new);
    public static Packet<SyncEntityManaSystem> PACKET = new Packet<>(
            TYPE, CODEC, SyncEntityManaSystem::handle, Side.CLIENT
    );
    private CompoundTag tag;
    private int id;

    public SyncEntityManaSystem(CompoundTag tag, int id) {
        this.id = id;
        this.tag = tag;
    }

    public SyncEntityManaSystem(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        this.tag = buf.readNbt();
    }

    public static void toBytes(RegistryFriendlyByteBuf buf, SyncEntityManaSystem objects) {
        buf.writeInt(objects.id);
        buf.writeNbt(objects.tag);
    }

    public static void handle(SyncEntityManaSystem packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Entity entity = Minecraft.getInstance().level.getEntity(packet.id);
                if (entity != null) {
                    DataAttachmentRegistry.forEachEntity((system, mana) -> {
                        EntityManaHolder holder = entity.getData(DataAttachmentRegistry.getEntity(system).get());
                        RegistryAccess access = entity.registryAccess();
                        holder.deserializeNBT(access, packet.tag);
                    });
                    entity.refreshDimensions();
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
