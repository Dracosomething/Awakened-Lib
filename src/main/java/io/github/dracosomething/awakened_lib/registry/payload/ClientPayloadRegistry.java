package io.github.dracosomething.awakened_lib.registry.payload;

import io.github.dracosomething.awakened_lib.network.AwakenedNetwork;
import io.github.dracosomething.awakened_lib.network.Origin;
import io.github.dracosomething.awakened_lib.network.p2c.SyncObjects;

public class ClientPayloadRegistry {
    public static void register() {
        AwakenedNetwork.registerPayload(SyncObjects.PACKET, Origin.PLAY);
    }
}
