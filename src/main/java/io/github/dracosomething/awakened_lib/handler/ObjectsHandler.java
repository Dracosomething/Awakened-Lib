package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.objects.TestProjectile;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import io.github.dracosomething.awakened_lib.registry.object.objectRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = Awakened_lib.MODID)
public class ObjectsHandler {
    @SubscribeEvent
    public static void create(PlayerInteractEvent.RightClickBlock event) {
        objectRegistry.EXAMPLE.get().spawn(100, event.getLevel(), event.getPos().getCenter());
    }

    @SubscribeEvent
    public static void fireProjectile(PlayerInteractEvent.LeftClickEmpty event) {
        Vec2 rot = event.getEntity().getRotationVector();
        TestProjectile projectile = objectRegistry.EXAMPLE_PROJECTILE.get().create(1000, event.getLevel(), event.getPos().getCenter());
        projectile.shootFromRotation((float) rot.x, (float) rot.y, (float) 0.0F, 1, 0.1F);
    }
}
