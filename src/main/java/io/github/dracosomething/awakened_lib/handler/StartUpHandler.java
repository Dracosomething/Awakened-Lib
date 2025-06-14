package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.events.ManaSystemEvent.*;
import io.github.dracosomething.awakened_lib.manaSystem.data.api.ManaManager;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.systems.XPSystem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = Awakened_lib.MODID, bus = EventBusSubscriber.Bus.MOD)
public class StartUpHandler {
    private static ManaManager MANAGER = new ManaManager();
    public static final IManaSystem DEFAULT = new XPSystem();

    public static ManaManager getMANAGER() {
        return MANAGER;
    }

    @SubscribeEvent
    public static void startUp(FMLConstructModEvent event) {
        event.enqueueWork(StartUpHandler::reload);
    }

    @SubscribeEvent
    public static void reloadClientStartup(FMLClientSetupEvent event) {
        event.enqueueWork(StartUpHandler::reload);
    }

    @SubscribeEvent
    public static void reloadCommonStartup(FMLCommonSetupEvent event) {
        event.enqueueWork(StartUpHandler::reload);
    }

    private static void reload() {
        ManaSystemGatherEvent event2 = new ManaSystemGatherEvent();
        NeoForge.EVENT_BUS.post(event2);
        event2.getSystems().forEach(getMANAGER()::registerSystem);
        ManaSystemSetupEvent event1 = new ManaSystemSetupEvent(MANAGER);
        NeoForge.EVENT_BUS.post(event1);
        MANAGER.start(event1);
        MANAGER.foreach((id, system) -> {
            Awakened_lib.getLOGGER().info(id + ":" + system.getName());
        });
    }
}
