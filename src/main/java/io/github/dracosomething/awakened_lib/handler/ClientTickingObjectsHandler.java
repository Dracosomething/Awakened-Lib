package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.capability.ObjectsCapability;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = Awakened_lib.MODID)
public class ClientTickingObjectsHandler {
    @SubscribeEvent
    public static void onJoin(LevelEvent.Load event) {
        if (event.getLevel().getServer() != null) {
            MinecraftServer server = event.getLevel().getServer();
            server.getAllLevels().forEach((level) -> {
                level.getCapability(ObjectsCapability.CAPABILITY).ifPresent((cap) -> {

                });
            });
        }
    }
}
