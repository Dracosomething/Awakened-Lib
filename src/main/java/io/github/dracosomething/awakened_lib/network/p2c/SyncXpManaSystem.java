package io.github.dracosomething.awakened_lib.network.p2c;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.xp.XPManaHolder;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncXpManaSystem implements CustomPacketPayload {
    private static CustomPacketPayload.Type<SyncXpManaSystem> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Awakened_lib.MODID, "sync_xp_mana"));
    private static StreamCodec<RegistryFriendlyByteBuf, SyncXpManaSystem> CODEC = StreamCodec.of(SyncXpManaSystem::toBytes, SyncXpManaSystem::new);
    public static Packet<SyncXpManaSystem> PACKET = new Packet<>(
            TYPE, CODEC, SyncXpManaSystem::handle, Side.CLIENT
    );
    private CompoundTag tag;
    private int id;

    public SyncXpManaSystem(CompoundTag tag, int id) {
        this.id = id;
        this.tag = tag;
    }

    public SyncXpManaSystem(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        this.tag = buf.readNbt();
    }

    public static void toBytes(RegistryFriendlyByteBuf buf, SyncXpManaSystem objects) {
        buf.writeInt(objects.id);
        buf.writeNbt(objects.tag);
    }

    public static void handle(SyncXpManaSystem packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Entity entity = Minecraft.getInstance().level.getEntity(packet.id);
                if (entity instanceof Player player) {
                    XPManaHolder holder = player.getData(DataAttachmentRegistry.EXPERIENCE);
                    RegistryAccess access = player.registryAccess();
                    holder.deserializeNBT(access, packet.tag);
                    entity.refreshDimensions();
                }
            }
        });
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
