package io.github.dracosomething.awakened_lib.manaSystem.data.entity;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.data.api.ManaProvider;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystem;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

public class EntityManaProvider extends ManaProvider<Entity, EntityManaHolder> {
    @Override
    public EntityManaHolder read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
        ManaSystem system = StartUpHandler.getMANAGER().get(tag.getString("system"));
        EntityManaHolder mana = holder.getData(DataAttachmentRegistry.getEntity(system));
        mana.deserializeNBT(provider, tag);
        return mana;
    }

    @Override
    public @Nullable CompoundTag write(EntityManaHolder manaHolder, HolderLookup.Provider provider) {
        CompoundTag tag = manaHolder.serializeNBT(provider);
        return tag;
    }
}
