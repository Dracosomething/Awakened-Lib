package io.github.dracosomething.awakened_lib.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.data.chunk.ChunkManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.item.ItemManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import io.github.dracosomething.awakened_lib.registry.dataComponents.DataComponentsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;

public record MagicEnchantment<T>(
        String systemId,
        int requirement,
        ManaSource source,
        T effects
) {
    public static <S> Codec<MagicEnchantment<S>> codec(Codec<S> codec){
        return RecordCodecBuilder.create(
                builder -> builder.group(
                                Codec.STRING.optionalFieldOf("system_id", "xp").forGetter(MagicEnchantment::systemId),
                                Codec.INT.fieldOf("required_amount").forGetter(MagicEnchantment::requirement),
                                ManaSource.CODEC.fieldOf("take_from").forGetter(MagicEnchantment::source),
                                codec.fieldOf("effect").forGetter(MagicEnchantment::effects)
                        )
                        .apply(builder, MagicEnchantment::new)
        );
    }

    public boolean canActivateEffects(ServerLevel level, int enchLevel, EnchantedItemInUse item,
                             Entity user, Vec3 pos) {
        IManaSystem system = StartUpHandler.getMANAGER().get(this.systemId);
        double req = this.requirement - (enchLevel * 2);
        if (system != null) {
            switch (source) {
                case PLAYER -> {
                    return checkEntity(system, user, req);
                }
                case CHUNK -> {
                    return checkChunk(system, user, req, level);
                }
                case ITEM -> {
                    return checkItem(system, item, req);
                }
            }
        }
        return false;
    }

    private boolean checkEntity(IManaSystem system, Entity user, double req) {
        AttachmentType<EntityManaHolder> attachmentType = DataAttachmentRegistry.getEntity(system).get();
        if (attachmentType != null) {
            EntityManaHolder holder = user.getData(attachmentType);
            if (holder.canSetCurrent(req)) {
                if (holder.updateCurrent(req)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkItem(IManaSystem system, EnchantedItemInUse item, double req) {
        DataComponentType<ItemManaHolder> attachmentType = DataComponentsRegistry.getItem(system).get();
        if (attachmentType != null) {
            ItemStack stack = item.itemStack();
            ItemManaHolder holder = stack.get(attachmentType);
            if (holder != null) {
                if (holder.canSetCurrent(req)) {
                    if (holder.updateCurrent(req)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkChunk(IManaSystem system, Entity user, double req, ServerLevel level) {
        AttachmentType<ChunkManaHolder> attachmentType = DataAttachmentRegistry.getChunk(system).get();
        if (attachmentType != null) {
            BlockPos pos1 = user.blockPosition();
            LevelChunk chunk = level.getChunkAt(pos1);
            ChunkManaHolder holder = chunk.getData(attachmentType);
            if (holder.canSetCurrent(req)) {
                if (holder.updateCurrent(req)) {
                    return true;
                }
            }
        }
        return false;
    }

    public IManaSystem getSystem() {
        IManaSystem system = StartUpHandler.getMANAGER().get(this.systemId);
        if (system != null) {
            return system;
        }
        return StartUpHandler.DEFAULT;
    }
}
