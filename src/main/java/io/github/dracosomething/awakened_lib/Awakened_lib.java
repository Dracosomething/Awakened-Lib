package io.github.dracosomething.awakened_lib;

import com.mojang.logging.LogUtils;
import io.github.dracosomething.awakened_lib.handler.SoulBoundItemsHandler;
import io.github.dracosomething.awakened_lib.registry.mainRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Awakened_lib.MODID)
public class Awakened_lib {
    public static final String MODID = "awakened_lib";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Awakened_lib(IEventBus modEventBus, ModContainer modContainer) {
        mainRegistry.register(modEventBus);

        modEventBus.addListener(SoulBoundItemsHandler::onClientSetup);
        NeoForge.EVENT_BUS.register(this);
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }
}
