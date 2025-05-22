package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.dataAttachements.ObjectsAttachement;
import io.github.dracosomething.awakened_lib.manaSystem.Data.api.ManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.Data.chunk.ChunkManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.Data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.network.p2c.SyncObjects;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Awakened_lib.MODID)
public class CapabilitiesHandler {
    @SubscribeEvent
    public static void registerLevelCaps(ChunkEvent.Load event) {
        ObjectsAttachement.get(event.getChunk());
        DataAttachmentRegistry.forEachEntity((system, supplier) -> {
            AttachmentType<ChunkManaHolder> type = DataAttachmentRegistry.getChunk(system).get();
            ChunkManaHolder holder = event.getChunk().getData(type);
            holder.sync(event.getChunk());
        });
    }

    @SubscribeEvent
    public static void unloadChunk(ChunkEvent.Unload event) {
        ObjectsAttachement.get(event.getChunk());
        DataAttachmentRegistry.forEachEntity((system, supplier) -> {
            AttachmentType<ChunkManaHolder> type = DataAttachmentRegistry.getChunk(system).get();
            ChunkManaHolder holder = event.getChunk().getData(type);
            holder.sync(event.getChunk());
        });
    }

    @SubscribeEvent
    public static void watchChunk(ChunkWatchEvent.Sent event) {
        LevelChunk chunk = event.getChunk();
        HolderLookup.Provider provider = chunk.getLevel().registryAccess();
        CompoundTag tag = chunk.getData(DataAttachmentRegistry.OBJECTS).serializeNBT(provider);
        PacketDistributor.sendToPlayer(event.getPlayer(), new SyncObjects(tag, chunk.getPos()));
        DataAttachmentRegistry.forEachEntity((system, supplier) -> {
            AttachmentType<ChunkManaHolder> type = DataAttachmentRegistry.getChunk(system).get();
            ChunkManaHolder holder = event.getChunk().getData(type);
            holder.sync(event.getChunk());
        });
    }

    @SubscribeEvent
    public static void registerPlayerCaps(EntityJoinLevelEvent event) {
        DataAttachmentRegistry.forEachEntity((system, supplier) -> {
            AttachmentType<EntityManaHolder> type = DataAttachmentRegistry.getEntity(system).get();
            EntityManaHolder holder = event.getEntity().getData(type);
            holder.sync(event.getEntity());
        });
    }

    @SubscribeEvent
    public static void onLeave(EntityLeaveLevelEvent event) {
        DataAttachmentRegistry.forEachEntity((system, supplier) -> {
            AttachmentType<EntityManaHolder> type = DataAttachmentRegistry.getEntity(system).get();
            EntityManaHolder holder = event.getEntity().getData(type);
            holder.sync(event.getEntity());
        });
    }

    @SubscribeEvent
    public static void onTrack(PlayerEvent.StartTracking event) {
        DataAttachmentRegistry.forEachEntity((system, supplier) -> {
            AttachmentType<EntityManaHolder> type = DataAttachmentRegistry.getEntity(system).get();
            EntityManaHolder holder = event.getEntity().getData(type);
            holder.sync(event.getEntity());
            holder = event.getTarget().getData(type);
            holder.sync(event.getTarget());
        });
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        DataAttachmentRegistry.forEachEntity((system, supplier) -> {
            AttachmentType<EntityManaHolder> type = DataAttachmentRegistry.getEntity(system).get();
            EntityManaHolder holderOld = event.getEntity().getData(type);
            EntityManaHolder holderNew = event.getOriginal().getData(type);
            RegistryAccess access = event.getEntity().registryAccess();
            holderNew.deserializeNBT(access, holderOld.serializeNBT(access));
        });
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        DataAttachmentRegistry.forEachEntity((system, supplier) -> {
            AttachmentType<EntityManaHolder> type = DataAttachmentRegistry.getEntity(system).get();
            EntityManaHolder holder = event.getEntity().getData(type);
            holder.sync(event.getEntity());
        });
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        DataAttachmentRegistry.forEachEntity((system, supplier) -> {
            AttachmentType<EntityManaHolder> type = DataAttachmentRegistry.getEntity(system).get();
            EntityManaHolder holder = event.getEntity().getData(type);
            holder.sync(event.getEntity());
        });
    }
}
