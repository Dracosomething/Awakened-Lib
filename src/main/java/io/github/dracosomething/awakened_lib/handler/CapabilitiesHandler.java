package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.dataAttachements.ObjectsCapability;
import io.github.dracosomething.awakened_lib.dataAttachements.ObjectsProvider;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Awakened_lib.MODID)
public class CapabilitiesHandler {
    @SubscribeEvent
    public static void registerLevelCaps(AttachCapabilitiesEvent<Level> event) {
        event.addCapability(ObjectsCapability.ID, new ObjectsProvider());
    }

    @Nullable
    public static <T> T getCapability(Level level, Capability<T> capability) {
        return level.getCapability(capability).isPresent() ? level.getCapability(capability).orElseThrow(() ->
                new IllegalArgumentException("Lazy optional must not be empty")) : null;
    }
}
