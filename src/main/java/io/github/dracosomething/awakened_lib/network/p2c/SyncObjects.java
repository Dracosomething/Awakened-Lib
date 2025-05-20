package io.github.dracosomething.awakened_lib.network.p2c;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.network.Packet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class SyncObjects implements CustomPacketPayload {
    private static Type<SyncObjects> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Awakened_lib.MODID, "syncObjects"));
    private static StreamCodec<RegistryFriendlyByteBuf, SyncObjects> CODEC;
    public static Packet<SyncObjects> PACKET = new Packet<>(

    )

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
