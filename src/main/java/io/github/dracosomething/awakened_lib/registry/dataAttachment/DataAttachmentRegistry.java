package io.github.dracosomething.awakened_lib.registry.dataAttachment;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.dataAttachements.ChunkObjectsProvider;
import io.github.dracosomething.awakened_lib.dataAttachements.ObjectsAttachement;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class DataAttachmentRegistry {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES;
    public static final Supplier<AttachmentType<ObjectsAttachement>> OBJECTS;

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }

    static {
        ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Awakened_lib.MODID);
        OBJECTS = ATTACHMENT_TYPES.register(
                "objects", () -> AttachmentType.builder(() -> new ObjectsAttachement()).serialize(new ChunkObjectsProvider()).build()
        );
    }
}
