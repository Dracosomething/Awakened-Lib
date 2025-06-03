package io.github.dracosomething.awakened_lib.registry.manaSystem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystem;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = Awakened_lib.MODID)
public class manaSystemRegistry {
    public static final ResourceKey<Registry<ManaSystem>> KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Awakened_lib.MODID, "mana_system"));
    public static final Codec<Holder<ManaSystem>> REGISTRY_CODEC = RegistryFixedCodec.create(KEY);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ManaSystem>> UPGRADE_ORB_REGISTRY_STREAM_CODEC = ByteBufCodecs.holderRegistry(KEY);

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                KEY,

        );
    }
}
