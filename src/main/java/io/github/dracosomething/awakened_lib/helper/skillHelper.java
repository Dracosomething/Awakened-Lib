package io.github.dracosomething.awakened_lib.helper;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class skillHelper {
    public static List<Entity> DrawSphereAndGetEntitiesInIt(LivingEntity entity, Level level, Vec3 pos, int radius, boolean areNotAllie){
        AABB aabb = new AABB((double) (pos.x - radius), (double) (pos.y - radius), (double) (pos.z - radius), (double) (pos.x + radius), (double) (pos.y + radius), (double) (pos.z + radius));
        List<Entity> entities = level.getEntities((Entity) null, aabb, Entity::isAlive);
        List<Entity> ret = new ArrayList();
        Iterator var16 = entities.iterator();

        while (var16.hasNext()) {
            Entity entity2 = (Entity) var16.next();

            double x = entity2.getX();
            double y = entity2.getY();
            double z = entity2.getZ();
            double cmp = (double) (radius * radius) - ((double) pos.x - x) * ((double) pos.x - x) - ((double) pos.y - y) * ((double) pos.y - y) - ((double) pos.z - z) * ((double) pos.z - z);
            if (cmp > 0.0) {
                if(areNotAllie) {
                    if (!entity2.isAlliedTo(entity)) {
                        ret.add(entity2);
                    }
                } else {
                    ret.add(entity2);
                }
            }
        }

        return ret;
    }

    public static List<Entity> DrawSphereAndGetEntitiesInIt(LivingEntity entity, int radius, boolean areNotAllie){
        return DrawSphereAndGetEntitiesInIt(entity, entity.level, entity.position(), radius, areNotAllie);
    }

    public static List<Entity> DrawSphereAndGetEntitiesInIt(LivingEntity entity, Level level, int radius, boolean areNotAllie){
        return DrawSphereAndGetEntitiesInIt(entity, level, entity.position(), radius, areNotAllie);
    }

    public static List<Entity> DrawSphereAndGetEntitiesInIt(LivingEntity entity, Vec3 pos, int radius, boolean areNotAllie){
        return DrawSphereAndGetEntitiesInIt(entity, entity.level, pos, radius, areNotAllie);
    }

    public static List<Entity> DrawSphereAndGetEntitiesInIt(LivingEntity entity, int radius){
        return DrawSphereAndGetEntitiesInIt(entity, entity.level, entity.position(), radius, false);
    }

    public static List<LivingEntity> GetLivingEntitiesInRange(LivingEntity entity, int radius, boolean areNotAllie) {
        List<LivingEntity> list = new ArrayList<>();
        DrawSphereAndGetEntitiesInIt(entity, radius, areNotAllie).stream().filter((entity1 -> entity1 instanceof LivingEntity)).toList().forEach((living) -> list.add((LivingEntity) living));
        return list;
    }

    public static List<LivingEntity> GetLivingEntitiesInRange(LivingEntity entity, Level level, Vec3 pos, int radius, boolean areNotAllie) {
        List<LivingEntity> list = new ArrayList<>();
        DrawSphereAndGetEntitiesInIt(entity, level, pos, radius, areNotAllie).stream().filter((entity1 -> entity1 instanceof LivingEntity)).toList().forEach((living) -> list.add((LivingEntity) living));
        return list;
    }

    public static List<LivingEntity> GetLivingEntitiesInRange(LivingEntity entity, int radius) {
        List<LivingEntity> list = new ArrayList<>();
        DrawSphereAndGetEntitiesInIt(entity, entity.level, entity.position(), radius, false).stream().filter((entity1 -> entity1 instanceof LivingEntity)).toList().forEach((living) -> list.add((LivingEntity) living));
        return list;
    }

    public static List<LivingEntity> GetLivingEntitiesInRange(LivingEntity entity, Vec3 pos, int radius) {
        List<LivingEntity> list = new ArrayList<>();
        DrawSphereAndGetEntitiesInIt(entity, entity.level, pos, radius, false).stream().filter((entity1 -> entity1 instanceof LivingEntity)).toList().forEach((living) -> list.add((LivingEntity) living));
        return list;
    }

    public static List<Player> getPlayersInRange(LivingEntity entity, Vec3 pos, int radius, Predicate<? super Player> predicate) {
        List<Player> list = new ArrayList<>();
        List<Player> finalList = list;
        DrawSphereAndGetEntitiesInIt(entity, radius, false)
                .stream()
                .filter(entity1 -> entity1 instanceof Player)
                .toList()
                .forEach((entity1) -> {
                    finalList.add((Player) entity1);
                });
        if (entity instanceof Player player) finalList.add(player);
        list = finalList.stream().filter(predicate).toList();
        return list;
    }

    public static void sendMessageToNearbyPlayers(int radius, LivingEntity entity, Component message) {
        DrawSphereAndGetEntitiesInIt(entity, radius, false)
                .stream()
                .filter(entity1 -> entity1 instanceof Player)
                .toList()
                .forEach((entity1) -> {
                    if (entity instanceof Player player) {
                        player.sendSystemMessage(message);
                    }
                });
    }

    public static void sendMessageToNearbyPlayersWithSource(int radius, LivingEntity entity, Component message) {
        Component newMessage = Component.literal("<" + entity.getDisplayName().getString() + "> " + message.getString());
        sendMessageToNearbyPlayers(radius, entity, newMessage);
    }

    public static void ParticleSphere(LivingEntity entity, double radius, ParticleOptions type) {
        for(float x = (float) (entity.getX() - (float)radius); x < entity.getX() + (float)radius; ++x) {
            for(float y = (float) (entity.getY() - (float)radius); y < entity.getY() + (float)radius; ++y) {
                for(float z = (float) (entity.getZ() - (float)radius); z < entity.getZ() + (float)radius; ++z) {
                    float cmp = (float) ((float)(radius * radius) - (entity.getX() - x) * (entity.getX() - x) - (entity.getY() - y) * (entity.getY() - y) - (entity.getZ() - z) * (entity.getZ() - z));
                    if (cmp > 0.0F && cmp < 6.1F && entity.level instanceof ServerLevel world) {
                        world.sendParticles(type, (double) x, (double) y, (double) z, 5, 1.0, 1.0, 1.0, 1.0);
                    }
                }
            }
        }
    }

    public static void ParticleCircle(LivingEntity entity, double radius, ParticleOptions type) {
        ParticleCircle(entity, radius, 1, type);
    }

    public static void ParticleCircle(LivingEntity entity, double radius, int amount, ParticleOptions type) {
        for(float x = (float) (entity.getX() - (float)radius); x < entity.getX() + (float)radius; ++x) {
            for(float z = (float) (entity.getZ() - (float)radius); z < entity.getZ() + (float)radius; ++z) {
                float cmp = (float) ((float)(radius * radius) - (entity.getX() - x) * (entity.getX() - x) - (entity.getZ() - z) * (entity.getZ() - z));
                if (cmp > 0.0F && entity.level instanceof ServerLevel world) {
                    for (int i = 0; i < amount; i++) {
                        world.sendParticles(type, (double) x, (double) entity.getY()-1.5, (double) z, 5, 1.0, 1.0, 1.0, 1.0);
                    }
                }
            }
        }
    }

    public static void ParticleCircle(Vec3 pos, Level level, double radius, int amount, ParticleOptions type) {
        for(float x = (float) (pos.x - (float)radius); x < pos.x + (float)radius; ++x) {
            for (float z = (float) (pos.z - (float) radius); z < pos.z + (float) radius; ++z) {
                float cmp = (float) ((float)(radius * radius) - (pos.x - x) * (pos.x - x) - (pos.z - z) * (pos.z - z));
                if (cmp > 0.0F && cmp < 6.1F && level instanceof ServerLevel world) {
                    for (int i = 0; i < amount; ++i) {
                        world.sendParticles(type, (double) x, (double) pos.y - 1.5, (double) z, 5, 1.0, 1.0, 1.0, 1.0);
                    }
                }
            }
        }
    }

    public static void ParticleRing(LivingEntity entity, double radius, ParticleOptions type) {
        ParticleRing(entity, radius, 1, 1, type);
    }

    public static void ParticleRing(LivingEntity entity, double radius, double height, ParticleOptions type) {
        ParticleRing(entity, radius, height, 1, type);
    }

    public static void ParticleRing(LivingEntity entity, double radius, double height, int amount, ParticleOptions type) {
        ParticleRing(entity.position(), entity.level, radius, height, amount, type);
    }

    public static void ParticleRing(Vec3 pos, Level level, double radius, double height, int amount, ParticleOptions type) {
        for(float x = (float) (pos.x - (float)radius); x < pos.x + (float)radius; ++x) {
            for (float y = (float) pos.y; y < pos.y + height; ++y) {
                for (float z = (float) (pos.z - (float) radius); z < pos.z + (float) radius; ++z) {
                    float cmp = (float) ((float)(radius * radius) - (pos.x - x) * (pos.x - x) - (pos.y - y) * (pos.y - y) - (pos.z - z) * (pos.z - z));
                    if (cmp > 0.0F && cmp < 6.1F && level instanceof ServerLevel world) {
                        for (int i = 0; i < amount; ++i) {
                            world.sendParticles(type, (double) x, (double) pos.y - 1.5, (double) z, 5, 1.0, 1.0, 1.0, 1.0);
                        }
                    }
                }
            }
        }
    }
}
