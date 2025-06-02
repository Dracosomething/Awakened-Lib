package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.helper.ClassHelper;
import io.github.dracosomething.awakened_lib.item.util.MagicItem;
import io.github.dracosomething.awakened_lib.item.util.SoulBoundItem;
import io.github.dracosomething.awakened_lib.manaSystem.data.chunk.ChunkManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.item.ItemManaHolder;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import io.github.dracosomething.awakened_lib.registry.dataComponents.dataComponentsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Awakened_lib.MODID)
public class ManaSystemHandler {
    @SubscribeEvent
    public static void tick(EntityTickEvent.Pre event) {
        StartUpHandler.getMANAGER().foreach((id, system) -> {
            boolean canTick = event.getEntity().tickCount % system.getRegenRate() == 0;
            if (canTick) {
                Entity entity = event.getEntity();
                switch (system.getRegenerator()) {
                    case CHUNK -> {
                        BlockPos pos = entity.blockPosition();
                        Level level = entity.level();
                        ChunkAccess chunk = level.getChunkAt(pos);
                        ChunkManaHolder holder = chunk.getData(DataAttachmentRegistry.getChunk(system));
                        holder.tick(entity);
                    }
                    case PLAYER -> {
                        EntityManaHolder holder = entity.getData(DataAttachmentRegistry.getEntity(system));
                        holder.tick(entity);
                    }
                }
                if (entity.getWeaponItem() != null) {
                    ItemStack item = entity.getWeaponItem();
                    if (ClassHelper.hasInterface(item.getItem().getClass(), MagicItem.class) ||
                            ClassHelper.isAnotatedWith(item.getItem().getClass(), SoulBoundItem.class)) {
                        ItemManaHolder holder = item.get(dataComponentsRegistry.getItem(system));
                        if (holder != null) {
                            holder.tick(item, event.getEntity());
                        } else {
                            item.set(dataComponentsRegistry.getItem(system), new ItemManaHolder(system));
                        }
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void updateData(PlayerTickEvent.Post event) {
        StartUpHandler.getMANAGER().foreach((id, system) -> {
            boolean canTick = event.getEntity().tickCount % system.getRegenRate() == 0;
            if (canTick) {
                event.getEntity().getInventory().items.forEach((item) -> {
                    if (ClassHelper.hasInterface(item.getItem().getClass(), MagicItem.class) ||
                            ClassHelper.isAnotatedWith(item.getItem().getClass(), SoulBoundItem.class)) {
                        ItemManaHolder holder = item.get(dataComponentsRegistry.getItem(system));
                        if (holder != null) {
                            holder.tick(item, event.getEntity());
                        } else {
                            item.set(dataComponentsRegistry.getItem(system), new ItemManaHolder(system));
                        }
                    }
                });
            }
        });
    }

    @SubscribeEvent
    public static void updateXPSystem(PlayerXpEvent.XpChange event) {
        event.getEntity().getData(DataAttachmentRegistry.EXPERIENCE).tick(event.getEntity());
    }
}
