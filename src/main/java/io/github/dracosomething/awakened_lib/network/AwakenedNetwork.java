package io.github.dracosomething.awakened_lib.network;

import com.google.common.graph.NetworkBuilder;
import io.github.dracosomething.awakened_lib.Awakened_lib;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.*;

public class AwakenedNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE;

    public static void register() {
        int i = 0;

    }

    static {
        ChannelBuilder builder = ChannelBuilder.named(ResourceLocation.fromNamespaceAndPath(Awakened_lib.MODID, "channnel")).networkProtocolVersion(1);
        builder.
        INSTANCE = builder.channel();
    }
}
