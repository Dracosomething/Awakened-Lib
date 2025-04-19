package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.capability.ObjectsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Awakened_lib.MODID)
public class CapabilityHandler {
    @SubscribeEvent
    public static void attachLevelCapabilties(AttachCapabilitiesEvent<Level> event) {
        event.addCapability(new ResourceLocation(Awakened_lib.MODID, "client_ticking_objects"), new ObjectsProvider());
    }
}
