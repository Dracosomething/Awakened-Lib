package io.github.dracosomething.awakened_lib.objects;

import io.github.dracosomething.awakened_lib.objects.api.ObjectType;
import io.github.dracosomething.awakened_lib.objects.api.ProjectileObject;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class TestProjectile extends ProjectileObject {
    public TestProjectile(ObjectType<?> type) {
        super(type);
    }

    @Override
    public void onPlace() {
        this.setPhysics(true);
        this.setGravity(0.10);
    }

    @Override
    public void onRemove() {

    }

    @Override
    public void onCollide(List<Entity> entities) {
        this.addParticlesAroundSelf(ParticleTypes.EXPLOSION, 0.2);
    }

    @Override
    public void onFiredTick() {
        this.addParticlesOnPos(ParticleTypes.FLAME, 0.5);
    }

    @Override
    public void onCollideBlock() {
        this.addParticlesOnPos(ParticleTypes.FLASH, 0.2);
    }

    @Override
    public void onDefaultTick() {
        this.addParticlesOnPos(ParticleTypes.CAMPFIRE_COSY_SMOKE, 0.5);
    }
}
