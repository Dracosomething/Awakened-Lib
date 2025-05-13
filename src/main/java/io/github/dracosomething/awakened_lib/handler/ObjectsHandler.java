package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.capability.ObjectsCapability;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ObjectsHandler {
    @SubscribeEvent
    public static void onJoin(LevelEvent.Load event) {
        if (event.getLevel().getServer() != null) {
            MinecraftServer server = event.getLevel().getServer();
            server.getAllLevels().forEach((level) -> {
                level.getCapability(ObjectsCapability.CAPABILITY).ifPresent((cap) -> {
                    cap.serializeNBT(level.registryAccess());
                });
            });
        }
    }

    @SubscribeEvent
    public static void onSave(LevelEvent.Save event) {
        if (event.getLevel().getServer() != null) {
            MinecraftServer server = event.getLevel().getServer();
            server.getAllLevels().forEach((level) -> {
                ObjectsCapability.getFrom(level).ifPresent((data) -> {
                    System.out.println("erwerewrwe");
                    data.deserializeNBT(level.registryAccess(), data.serializeNBT(level.registryAccess()));
                });
            });
        }
    }
}
