package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.world.ClientTickingObjectsSaveData;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = Awakened_lib.MODID)
public class ClientTickingObjectsHandler {
    @SubscribeEvent
    public static void onJoin(LevelEvent.Load event) throws IOException {
        if (event.getLevel().getServer() != null) {
            MinecraftServer server = event.getLevel().getServer();
            if (server.overworld() != null) {
                ClientTickingObjectsSaveData data = ClientTickingObjectsSaveData.get(server.overworld());
                System.out.println(server.overworld().getDataStorage().readTagFromDisk("client_ticking_objects", 1));
                System.out.println("placing objects");
                data.getObjects().forEach((uuid, object) -> {
                    object.place();
                });
            }
        }
    }
}
