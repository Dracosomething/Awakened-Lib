package io.github.dracosomething.awakened_lib.network;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Awakened_lib.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AwakenedNetwork {
    private static final Map<Packet, Origin> PAYLOADS = new HashMap<>();

    public static <T extends Packet> void registerPayload(T packet, Origin origin) {
        PAYLOADS.put(packet, origin);
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar payloadRegistrar = event.registrar(Awakened_lib.MODID).versioned("1.0.0").optional();

        PAYLOADS.forEach((packet, origin) -> {
            switch (origin) {
                case PLAY: {
                    switch (packet.side()) {
                        case CLIENT -> payloadRegistrar.playToClient(packet.type(), packet.codec(), packet.handler());
                        case COMMON -> payloadRegistrar.playBidirectional(packet.type(), packet.codec(), packet.handler());
                        case SERVER -> payloadRegistrar.playToServer(packet.type(), packet.codec(), packet.handler());
                    }
                }
                case COMMON: {
                    switch (packet.side()) {
                        case CLIENT -> payloadRegistrar.commonToClient(packet.type(), packet.codec(), packet.handler());
                        case COMMON -> payloadRegistrar.commonBidirectional(packet.type(), packet.codec(), packet.handler());
                        case SERVER -> payloadRegistrar.commonToServer(packet.type(), packet.codec(), packet.handler());
                    }
                }
                case CONFIG: {
                    switch (packet.side()) {
                        case CLIENT -> payloadRegistrar.configurationToClient(packet.type(), packet.codec(), packet.handler());
                        case COMMON -> payloadRegistrar.configurationBidirectional(packet.type(), packet.codec(), packet.handler());
                        case SERVER -> payloadRegistrar.configurationToServer(packet.type(), packet.codec(), packet.handler());
                    }
                }
            }
        });
    }
}
