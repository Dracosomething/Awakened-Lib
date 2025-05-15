package io.github.dracosomething.awakened_lib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public abstract class Packet<T> {
    public abstract StreamCodec<FriendlyByteBuf, T> getCoded();
}
