package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.events.ManaSystemEvent.*;
import io.github.dracosomething.awakened_lib.manaSystem.Data.api.ManaManager;
import io.github.dracosomething.awakened_lib.manaSystem.Systems.ManaSystem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Awakened_lib.MODID, bus = EventBusSubscriber.Bus.MOD)
public class StartUpHandler {
    private static ManaManager MANAGER;
    private static final Map<String, ManaSystem> SYSTEMS = new HashMap<>();

    public static void registerSystem(String id, ManaSystem system) {
        SYSTEMS.put(id, system);
    }

    public static ManaManager getMANAGER() {
        if (MANAGER == null) return new ManaManager();
        return MANAGER;
    }

    @SubscribeEvent
    public static void startUp(FMLConstructModEvent event) {
        event.enqueueWork(() -> {
            ManaSystemGatherEvent event2 = new ManaSystemGatherEvent();
            event2.getSystems().forEach(StartUpHandler::registerSystem);
            ManaSystemSetupEvent event1 = new ManaSystemSetupEvent(SYSTEMS);
            NeoForge.EVENT_BUS.post(event1);
            ManaManager manager = new ManaManager();
            manager.start(event1);
            MANAGER = manager;
        });
    }
}
