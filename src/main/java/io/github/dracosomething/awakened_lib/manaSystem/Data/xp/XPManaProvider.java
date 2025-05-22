package io.github.dracosomething.awakened_lib.manaSystem.Data.xp;

import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.Data.api.ManaProvider;
import io.github.dracosomething.awakened_lib.manaSystem.Data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.ManaSystem;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

public class XPManaProvider extends ManaProvider<Player, XPManaHolder> {
    @Override
    public XPManaHolder read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
        XPManaHolder mana = holder.getData(DataAttachmentRegistry.EXPERIENCE);
        mana.deserializeNBT(provider, tag);
        return mana;
    }

    @Override
    public @Nullable CompoundTag write(XPManaHolder manaHolder, HolderLookup.Provider provider) {
        CompoundTag tag = manaHolder.serializeNBT(provider);
        return tag;
    }
}
