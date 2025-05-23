package io.github.dracosomething.awakened_lib.registry.dataAttachment;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.dataAttachements.ChunkObjectsProvider;
import io.github.dracosomething.awakened_lib.dataAttachements.ObjectsAttachement;
import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.data.blockEntity.BlockManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.blockEntity.BlockManaProvider;
import io.github.dracosomething.awakened_lib.manaSystem.data.chunk.ChunkManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.chunk.ChunkManaProvider;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaProvider;
import io.github.dracosomething.awakened_lib.manaSystem.data.xp.XPManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.xp.XPManaProvider;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystemHolder;
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
    public static final Supplier<AttachmentType<XPManaHolder>> EXPERIENCE;
    private static final Map<IManaSystem, Supplier<AttachmentType<EntityManaHolder>>> ENTITIE_SYSTEMS = new HashMap<>();
    private static final Map<IManaSystem, Supplier<AttachmentType<ChunkManaHolder>>> CHUNK_SYSTEMS = new HashMap<>();
    private static final Map<IManaSystem, Supplier<AttachmentType<BlockManaHolder>>> BLOCK_SYSTEMS = new HashMap<>();

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

    public static Supplier<AttachmentType<BlockManaHolder>> getBlock(IManaSystem system) {
        return BLOCK_SYSTEMS.get(system);
    }

    public static void forEachBlock(BiConsumer<IManaSystem, Supplier<AttachmentType<BlockManaHolder>>> consumer) {
        BLOCK_SYSTEMS.forEach(consumer);
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
            Supplier<AttachmentType<BlockManaHolder>> blockSupplier = ATTACHMENT_TYPES.register(
                    id + "_block", () -> AttachmentType.builder(() -> new BlockManaHolder(holder)).serialize(new BlockManaProvider()).build()
            );
            BLOCK_SYSTEMS.put(system, blockSupplier);
        });
        ATTACHMENT_TYPES.register(eventBus);
    }

    static {
        ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Awakened_lib.MODID);
        OBJECTS = ATTACHMENT_TYPES.register(
                "objects", () -> AttachmentType.builder(() -> new ObjectsAttachement()).serialize(new ChunkObjectsProvider()).build()
        );
        EXPERIENCE = ATTACHMENT_TYPES.register(
                "experience", () -> AttachmentType.builder(() -> new XPManaHolder()).serialize(new XPManaProvider()).build()
        );
    }
}
