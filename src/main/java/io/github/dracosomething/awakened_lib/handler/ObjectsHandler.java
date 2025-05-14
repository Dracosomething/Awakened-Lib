package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.capability.ObjectsCapability;
import io.github.dracosomething.awakened_lib.registry.object.objectRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
                    cap.deserializeNBT(level.registryAccess(), cap.serializeNBT(level.registryAccess()));
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
                    data.deserializeNBT(level.registryAccess(), data.serializeNBT(level.registryAccess()));
                });
            });
        }
    }

    @SubscribeEvent
    public static void onUnload(LevelEvent.Unload event) {
        if (event.getLevel().getServer() != null) {
            MinecraftServer server = event.getLevel().getServer();
            server.getAllLevels().forEach((level) -> {
                ObjectsCapability.getFrom(level).ifPresent((data) -> {
                    data.deserializeNBT(level.registryAccess(), data.serializeNBT(level.registryAccess()));
                });
            });
        }
    }

    @SubscribeEvent
    public static void create(PlayerInteractEvent.LeftClickEmpty event) {
        objectRegistry.EXAMPLE.get().spawn(100, event.getLevel(), event.getPos());
        System.out.println("Succesfully created new object");
    }
}
