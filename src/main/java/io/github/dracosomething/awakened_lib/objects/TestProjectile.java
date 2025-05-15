package io.github.dracosomething.awakened_lib.objects;

import io.github.dracosomething.awakened_lib.objects.api.ObjectType;
import io.github.dracosomething.awakened_lib.objects.api.ProjectileObject;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class TestProjectile extends ProjectileObject {
    public TestProjectile(ObjectType<?> type) {
        super(type);
    }

    @Override
    public void onPlace() {

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
        this.addParticlesAroundSelf(ParticleTypes.EXPLOSION, 0.2);
        this.addParticlesAroundSelf(ParticleTypes.CAMPFIRE_COSY_SMOKE, 0.5);
    }
}
