package io.github.dracosomething.awakened_lib.manaSystem.data.blockEntity;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.data.api.ManaProvider;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

public class BlockManaProvider extends ManaProvider<BlockEntity, BlockManaHolder> {
    @Override
    public BlockManaHolder read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
        IManaSystem system = StartUpHandler.getMANAGER().get(tag.getString("system"));
        BlockManaHolder mana = holder.getData(DataAttachmentRegistry.getBlock(system));
        mana.deserializeNBT(provider, tag);
        return mana;
    }

    @Override
    public @Nullable CompoundTag write(BlockManaHolder manaHolder, HolderLookup.Provider provider) {
        return manaHolder.serializeNBT(provider);
    }
}
