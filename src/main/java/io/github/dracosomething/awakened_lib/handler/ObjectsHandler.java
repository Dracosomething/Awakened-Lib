package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.capability.IObjects;
import io.github.dracosomething.awakened_lib.capability.ObjectsCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ObjectsHandler {
    @SubscribeEvent
    public static void save(PlayerEvent.PlayerLoggedOutEvent event) {
    }
}
