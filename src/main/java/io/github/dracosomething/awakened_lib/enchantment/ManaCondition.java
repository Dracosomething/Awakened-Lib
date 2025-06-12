package io.github.dracosomething.awakened_lib.enchantment;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.data.api.ManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.chunk.ChunkManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.item.ItemManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import io.github.dracosomething.awakened_lib.registry.ItemConditions.LootItemConditionRegistry;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import io.github.dracosomething.awakened_lib.registry.dataComponents.DataComponentsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.Set;

public record ManaCondition(
        float requirement,
        float min,
        float max,
        String systemID,
        ManaSource source,
        LootContext.EntityTarget entityTarget
) implements LootItemCondition {
    public static final MapCodec<ManaCondition> CODEC = RecordCodecBuilder.mapCodec(
            (instance) -> instance.group(
                    Codec.FLOAT.fieldOf("requirement").forGetter(ManaCondition::requirement),
                    Codec.FLOAT.optionalFieldOf("min", 0f).forGetter(ManaCondition::min),
                    Codec.FLOAT.optionalFieldOf("max", Float.MAX_VALUE-1).forGetter(ManaCondition::max),
                    Codec.STRING.optionalFieldOf("system_id", "xp").forGetter(ManaCondition::systemID),
                    ManaSource.CODEC.fieldOf("take_from").forGetter(ManaCondition::source),
                    LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(ManaCondition::entityTarget)
            ).apply(instance, ManaCondition::new)
    );

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(this.entityTarget.getParam());
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditionRegistry.MANA_CONDITION.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        IManaSystem system = StartUpHandler.getMANAGER().get(systemID);
        Entity entity = lootContext.getParamOrNull(this.entityTarget.getParam());
        switch (source) {
            case ITEM -> {
                if (entity != null) {
                    ItemStack stack = entity.getWeaponItem();
                    if (stack != null) {
                        DataComponentType<ItemManaHolder> type = DataComponentsRegistry.getItem(system).get();
                        ItemManaHolder holder = stack.get(type);
                        if (holder != null) {
                            return matchItemConditions(holder);
                        }
                    }
                }
            }
            case CHUNK -> {
                if (entity != null) {
                    BlockPos pos = entity.blockPosition();
                    ServerLevel level = lootContext.getLevel();
                    LevelChunk chunk = level.getChunkAt(pos);
                    AttachmentType<ChunkManaHolder> type = DataAttachmentRegistry.getChunk(system).get();
                    ChunkManaHolder holder = chunk.getData(type);
                    return matchConditions(holder);
                }
            }
            case PLAYER -> {
                if (entity != null) {
                    AttachmentType<EntityManaHolder> type = DataAttachmentRegistry.getEntity(system).get();
                    EntityManaHolder holder = entity.getData(type);
                    return matchConditions(holder);
                }
            }
        }
        return false;
    }

    private <T extends ManaHolder> boolean matchConditions(T holder) {
        if (holder.getCurrent() >= min && holder.getCurrent() <= max) {
            if (holder.canSetCurrent(requirement)) {
                holder.updateCurrent(requirement);
                return true;
            }
        }
        return false;
    }

    private boolean matchItemConditions(ItemManaHolder holder) {
        if (holder.getCurrent() >= min && holder.getCurrent() <= max) {
            if (holder.canSetCurrent(requirement)) {
                holder.updateCurrent(requirement);
                return true;
            }
        }
        return false;
    }
}
