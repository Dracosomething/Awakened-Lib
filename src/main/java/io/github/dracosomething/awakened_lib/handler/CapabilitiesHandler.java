package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.dataAttachements.ChunkObjectsProvider;
import io.github.dracosomething.awakened_lib.dataAttachements.ObjectsAttachement;
import io.github.dracosomething.awakened_lib.network.AwakenedNetwork;
import io.github.dracosomething.awakened_lib.network.p2c.SyncObjects;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = Awakened_lib.MODID)
public class CapabilitiesHandler {
    @SubscribeEvent
    public static void registerLevelCaps(ChunkEvent.Load event) {
        ObjectsAttachement.get(event.getChunk());
    }

    @SubscribeEvent
    public static void watchChunk(ChunkWatchEvent.Sent event) {
        LevelChunk chunk = event.getChunk();
        HolderLookup.Provider provider = chunk.getLevel().registryAccess();
        CompoundTag tag = chunk.getData(DataAttachmentRegistry.OBJECTS).serializeNBT(provider);
        PacketDistributor.sendToPlayer(event.getPlayer(), new SyncObjects(tag, chunk.getPos()));
    }
}
