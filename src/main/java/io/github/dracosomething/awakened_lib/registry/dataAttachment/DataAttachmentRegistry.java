package io.github.dracosomething.awakened_lib.registry.dataAttachment;

import com.mojang.serialization.Codec;
import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.dataAttachements.ChunkObjectsProvider;
import io.github.dracosomething.awakened_lib.dataAttachements.ObjectsAttachement;
import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.Data.api.ManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.Data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.Data.entity.EntityManaProvider;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.ManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.ManaSystemHolder;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DataAttachmentRegistry {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES;
    public static final Supplier<AttachmentType<ObjectsAttachement>> OBJECTS;
    private static final Map<ManaSystem, Supplier<AttachmentType<EntityManaHolder>>> ENTITIE_SYSTEMS = new HashMap<>();

    public static Supplier<AttachmentType<EntityManaHolder>> getEntity(ManaSystem system) {
        return ENTITIE_SYSTEMS.get(system);
    }

    public static void register(IEventBus eventBus) {
        StartUpHandler.getMANAGER().foreach((id, system) -> {
            ManaSystemHolder holder = new ManaSystemHolder(system);
            Supplier<AttachmentType<EntityManaHolder>> supplier = ATTACHMENT_TYPES.register(
                    id, () -> AttachmentType.builder(() -> new EntityManaHolder(holder)).serialize(new EntityManaProvider()).build()
            );
            ENTITIE_SYSTEMS.put(system, supplier);
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
