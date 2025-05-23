package io.github.dracosomething.awakened_lib;

import com.mojang.logging.LogUtils;
import io.github.dracosomething.awakened_lib.handler.SoulBoundItemsHandler;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.systems.RegenOn;
import io.github.dracosomething.awakened_lib.registry.mainRegistry;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import java.util.Arrays;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Awakened_lib.MODID)
public class Awakened_lib {
    public static final String MODID = "awakened_lib";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static ModContainer container;

    public Awakened_lib(IEventBus modEventBus, ModContainer modContainer) {
        mainRegistry.register(modEventBus);
        new ManaSystem("e", 1, 1, RegenOn.NO, 1);

        modEventBus.addListener(SoulBoundItemsHandler::onClientSetup);
//        NeoForge.EVENT_BUS.register(this);
        container = modContainer;
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public static ModContainer getContainer() {
        return container;
    }
}
