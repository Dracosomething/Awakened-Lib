package io.github.dracosomething.awakened_lib.objects;

import io.github.dracosomething.awakened_lib.objects.api.ObjectType;
import io.github.dracosomething.awakened_lib.objects.api.TickingObject;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;

public class TestObject extends TickingObject {
    public TestObject(ObjectType<? extends TestObject> type) {
        super(type);
    }

    public void onPlace() {
        this.addParticlesAroundSelf(ParticleTypes.ASH, 0.2);
    }

    public void onTick() {
        this.addParticlesOnPos(ParticleTypes.CAMPFIRE_COSY_SMOKE, 0.5);
    }

    public void onRemove() {
        this.addParticlesOnPos(ParticleTypes.EGG_CRACK, 0.5);
    }

    @Override
    public void onCollideBlock() {

    }
}
