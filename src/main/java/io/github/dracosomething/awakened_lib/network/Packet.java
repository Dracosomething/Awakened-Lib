package io.github.dracosomething.awakened_lib.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import org.jetbrains.annotations.NotNull;

public record Packet<T extends CustomPacketPayload>(
    CustomPacketPayload.Type<T> type,
    StreamCodec<RegistryFriendlyByteBuf, T> codec,
    IPayloadHandler<T> handler,
    Side side
) implements CustomPacketPayload {
    public @NotNull Type<T> type() {
        return this.type;
    }
}
