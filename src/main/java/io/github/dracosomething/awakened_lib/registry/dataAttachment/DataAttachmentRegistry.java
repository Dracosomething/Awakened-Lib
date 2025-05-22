package io.github.dracosomething.awakened_lib.registry.dataAttachment;

import com.mojang.serialization.Codec;
import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.dataAttachements.ChunkObjectsProvider;
import io.github.dracosomething.awakened_lib.dataAttachements.ObjectsAttachement;
import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.Data.api.ManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.Data.chunk.ChunkManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.Data.chunk.ChunkManaProvider;
import io.github.dracosomething.awakened_lib.manaSystem.Data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.Data.entity.EntityManaProvider;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.IManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.ManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.ManaSystemHolder;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class DataAttachmentRegistry {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES;
    public static final Supplier<AttachmentType<ObjectsAttachement>> OBJECTS;
    private static final Map<IManaSystem, Supplier<AttachmentType<EntityManaHolder>>> ENTITIE_SYSTEMS = new HashMap<>();
    private static final Map<IManaSystem, Supplier<AttachmentType<ChunkManaHolder>>> CHUNK_SYSTEMS = new HashMap<>();

    public static Supplier<AttachmentType<EntityManaHolder>> getEntity(IManaSystem system) {
        return ENTITIE_SYSTEMS.get(system);
    }

    public static void forEachEntity(BiConsumer<IManaSystem, Supplier<AttachmentType<EntityManaHolder>>> consumer) {
        ENTITIE_SYSTEMS.forEach(consumer);
    }

    public static Supplier<AttachmentType<ChunkManaHolder>> getChunk(IManaSystem system) {
        return CHUNK_SYSTEMS.get(system);
    }

    public static void forEachChunk(BiConsumer<IManaSystem, Supplier<AttachmentType<ChunkManaHolder>>> consumer) {
        CHUNK_SYSTEMS.forEach(consumer);
    }

    public static void register(IEventBus eventBus) {
        StartUpHandler.getMANAGER().foreach((id, system) -> {
            ManaSystemHolder holder = new ManaSystemHolder(system);
            Supplier<AttachmentType<EntityManaHolder>> entitySupplier = ATTACHMENT_TYPES.register(
                    id + "_entity", () -> AttachmentType.builder(() -> new EntityManaHolder(holder)).serialize(new EntityManaProvider()).build()
            );
            ENTITIE_SYSTEMS.put(system, entitySupplier);
            Supplier<AttachmentType<ChunkManaHolder>> chunkSupplier = ATTACHMENT_TYPES.register(
                    id + "_chunk", () -> AttachmentType.builder(() -> new ChunkManaHolder(holder)).serialize(new ChunkManaProvider()).build()
            );
            CHUNK_SYSTEMS.put(system, chunkSupplier);
        });
        ATTACHMENT_TYPES.register(eventBus);
    }

    static {
        ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Awakened_lib.MODID);
        OBJECTS = ATTACHMENT_TYPES.register(
                "objects", () -> AttachmentType.builder(() -> new ObjectsAttachement()).serialize(new ChunkObjectsProvider()).build()
        );
    }
}
