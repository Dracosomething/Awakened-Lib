package io.github.dracosomething.awakened_lib.helper;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TimeStopHelper {
    public static List<MobEffect> timeStops = new ArrayList<>();
    public static List<MobEffect> timeStopCores = new ArrayList<>();
    public static boolean TimeStopped = false;

    public static void onServerSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ForgeRegistries.MOB_EFFECTS.forEach((effect) -> {
                if (effect.getDisplayName().getString().contains("time_stop")) {
                    timeStops.add(effect);
                }
                if (effect.getDisplayName().getString().contains("time_stop_core")) {
                    timeStopCores.add(effect);
                }
            });
        });
    }

    public static boolean containsTimeStop(Collection<MobEffect> effects) {
        return !Collections.disjoint(effects, timeStops);
    }

    public static boolean containsTimeStopCore(Collection<MobEffect> effects) {
        return !Collections.disjoint(effects, timeStopCores);
    }

    public static boolean containsTimeStop(MobEffect effect) {
        return timeStops.contains(effect);
    }

    public static boolean containsTimeStopCore(MobEffect effect) {
        return timeStopCores.contains(effect);
    }

    public static boolean hasTimeStop(@Nullable LivingEntity entity) {
        if (entity != null) {
            Collection<MobEffect> effects = entity.getActiveEffectsMap().keySet().stream().toList();
            return (containsTimeStop(effects) || containsTimeStopCore(effects)) &&
                    !entity.isSpectator() &&
                    !(entity instanceof Player player && player.isCreative()) &&
                    !entity.isDeadOrDying();
        }
        return false;
    }
}
