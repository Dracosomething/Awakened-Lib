package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.dataAttachements.ObjectsAttachement;
import io.github.dracosomething.awakened_lib.manaSystem.data.blockEntity.BlockManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.chunk.ChunkManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.xp.XPManaHolder;
import io.github.dracosomething.awakened_lib.network.p2c.SyncObjects;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Awakened_lib.MODID)
public class CapabilitiesHandler {
    @SubscribeEvent
    public static void registerLevelCaps(ChunkEvent.Load event) {
        ObjectsAttachement attachement = ObjectsAttachement.get(event.getChunk());
        PacketDistributor.sendToAllPlayers(new SyncObjects(attachement.serializeNBT(event.getChunk().getLevel().registryAccess()), event.getChunk().getPos()));
        DataAttachmentRegistry.forEachEntity((system, supplier) -> {
            AttachmentType<ChunkManaHolder> type = DataAttachmentRegistry.getChunk(system).get();
            ChunkManaHolder holder = event.getChunk().getData(type);
            holder.sync(event.getChunk());
        });
    }

    @SubscribeEvent
    public static void unloadChunk(ChunkEvent.Unload event) {
        ObjectsAttachement attachement = ObjectsAttachement.get(event.getChunk());
        PacketDistributor.sendToAllPlayers(new SyncObjects(attachement.serializeNBT(event.getChunk().getLevel().registryAccess()), event.getChunk().getPos()));
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
        PacketDistributor.sendToAllPlayers(new SyncObjects(tag, chunk.getPos()));
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
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        XPManaHolder holder = event.getEntity().getData(DataAttachmentRegistry.EXPERIENCE);
        holder.sync(event.getEntity());
    }

    @SubscribeEvent
    public static void attachDataBlocks(BlockEvent.EntityPlaceEvent event) {
        BlockPos pos = event.getPos();
        LevelAccessor level = event .getLevel();
        if (level.getBlockEntity(pos) != null) {
            BlockEntity entity = level.getBlockEntity(pos);
            DataAttachmentRegistry.forEachEntity((system, supplier) -> {
                AttachmentType<BlockManaHolder> type = DataAttachmentRegistry.getBlock(system).get();
                BlockManaHolder holder = entity.getData(type);
                holder.sync(entity);
            });
        }
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
        if (event.getTarget() instanceof Player player) {
            XPManaHolder holder = player.getData(DataAttachmentRegistry.EXPERIENCE);
            holder.sync(player);
        }
        XPManaHolder holder = event.getEntity().getData(DataAttachmentRegistry.EXPERIENCE);
        holder.sync(event.getEntity());
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
        XPManaHolder holderOld = event.getEntity().getData(DataAttachmentRegistry.EXPERIENCE);
        XPManaHolder holderNew = event.getOriginal().getData(DataAttachmentRegistry.EXPERIENCE);
        RegistryAccess access = event.getEntity().registryAccess();
        holderNew.deserializeNBT(access, holderOld.serializeNBT(access));
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        DataAttachmentRegistry.forEachEntity((system, supplier) -> {
            AttachmentType<EntityManaHolder> type = DataAttachmentRegistry.getEntity(system).get();
            EntityManaHolder holder = event.getEntity().getData(type);
            holder.sync(event.getEntity());
        });
        XPManaHolder holder = event.getEntity().getData(DataAttachmentRegistry.EXPERIENCE);
        holder.sync(event.getEntity());
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        DataAttachmentRegistry.forEachEntity((system, supplier) -> {
            AttachmentType<EntityManaHolder> type = DataAttachmentRegistry.getEntity(system).get();
            EntityManaHolder holder = event.getEntity().getData(type);
            holder.sync(event.getEntity());
        });
        XPManaHolder holder = event.getEntity().getData(DataAttachmentRegistry.EXPERIENCE);
        holder.sync(event.getEntity());
    }
}
