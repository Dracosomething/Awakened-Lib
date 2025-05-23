package io.github.dracosomething.awakened_lib.network.p2c;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.manaSystem.data.blockEntity.BlockManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.network.Packet;
import io.github.dracosomething.awakened_lib.network.Side;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncBlockManaSystem implements CustomPacketPayload {
    private static Type<SyncBlockManaSystem> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Awakened_lib.MODID, "sync_block_system"));
    private static StreamCodec<RegistryFriendlyByteBuf, SyncBlockManaSystem> CODEC = StreamCodec.of(SyncBlockManaSystem::toBytes, SyncBlockManaSystem::new);
    public static Packet<SyncBlockManaSystem> PACKET = new Packet<>(
        TYPE, CODEC, SyncBlockManaSystem::handle, Side.CLIENT
    );
    private CompoundTag tag;
    private BlockPos pos;

    public SyncBlockManaSystem(CompoundTag tag, BlockPos pos) {
        this.pos = pos;
        this.tag = tag;
    }

    public SyncBlockManaSystem(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.tag = buf.readNbt();
    }

    public static void toBytes(RegistryFriendlyByteBuf buf, SyncBlockManaSystem objects) {
        buf.writeBlockPos(objects.pos);
        buf.writeNbt(objects.tag);
    }

    public static void handle(SyncBlockManaSystem packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Level level = Minecraft.getInstance().level;
                BlockEntity block = level.getBlockEntity(packet.pos);
                DataAttachmentRegistry.forEachEntity((system, mana) -> {
                    BlockManaHolder holder = block.getData(DataAttachmentRegistry.getBlock(system));
                    RegistryAccess access = level.registryAccess();
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
