package io.github.dracosomething.awakened_lib.registry.payload;

import io.github.dracosomething.awakened_lib.network.AwakenedNetwork;
import io.github.dracosomething.awakened_lib.network.Origin;
import io.github.dracosomething.awakened_lib.network.p2c.*;

public class ClientPayloadRegistry {
    public static void register() {
        AwakenedNetwork.registerPayload(SyncObjects.PACKET, Origin.PLAY);
        AwakenedNetwork.registerPayload(SyncEntityManaSystem.PACKET, Origin.PLAY);
        AwakenedNetwork.registerPayload(SyncChunkManaSystem.PACKET, Origin.PLAY);
        AwakenedNetwork.registerPayload(SyncXpManaSystem.PACKET, Origin.PLAY);
        AwakenedNetwork.registerPayload(SyncBlockManaSystem.PACKET, Origin.PLAY);
    }
}
