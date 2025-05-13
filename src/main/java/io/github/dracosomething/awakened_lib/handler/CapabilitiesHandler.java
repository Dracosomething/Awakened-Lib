package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.capability.ObjectsCapability;
import io.github.dracosomething.awakened_lib.capability.ObjectsProvider;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Awakened_lib.MODID)
public class CapabilitiesHandler {
    @SubscribeEvent
    public static void registerLevelCaps(AttachCapabilitiesEvent<Level> event) {
        event.addCapability(ObjectsCapability.ID, ObjectsProvider.class);
    }
}
