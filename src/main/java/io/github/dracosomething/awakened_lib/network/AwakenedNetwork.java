package io.github.dracosomething.awakened_lib.network;

import com.google.common.graph.NetworkBuilder;
import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.network.p2c.SyncObjectsCapability;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleFlow;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AwakenedNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final EventNetworkChannel INSTANCE;

    public static <T extends Packet<T>> void sendToServer(T packet) {
        Connection connection = Minecraft.getInstance().getConnection().getConnection();
        if (INSTANCE.isRemotePresent(connection)) {
            FriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), Minecraft.getInstance().player.registryAccess());
            packet.getCoded().encode(buf, packet);
            INSTANCE.send(buf, connection);
        }
    }

    public <T extends Packet<T>> void sendToClient(T packet, ServerPlayer player) {
        Connection connection = player.connection.getConnection();
        if (INSTANCE.isRemotePresent(connection)) {
            FriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.server.registryAccess());
            packet.getCoded().encode(buf, packet);
            INSTANCE.send(buf, connection);
        }

    }

    static {
        ChannelBuilder builder = ChannelBuilder.named(ResourceLocation.fromNamespaceAndPath(Awakened_lib.MODID, "channnel")).networkProtocolVersion(1);
        INSTANCE = builder.eventNetworkChannel();
    }
}
