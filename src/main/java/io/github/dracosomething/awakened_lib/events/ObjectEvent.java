package io.github.dracosomething.awakened_lib.events;

import io.github.dracosomething.awakened_lib.objects.api.TickingObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class ObjectEvent extends Event {
    private TickingObject object;

    public ObjectEvent(TickingObject object) {
        this.object = object;
    }

    public TickingObject getObject() {
        return object;
    }

    public static class ObjectTickEvent extends ObjectEvent implements ICancellableEvent {
        public ObjectTickEvent(TickingObject object) {
            super(object);
        }
    }

    public static class ObjectPlaceEvent extends ObjectEvent implements ICancellableEvent {
        private Vec3 pos;
        private int life;

        public ObjectPlaceEvent(TickingObject object, Vec3 pos, int life) {
            super(object);
            this.pos = pos;
            this.life = life;
        }

        public Vec3 getPos() {
            return pos;
        }

        public void setPos(Vec3 pos) {
            this.pos = pos;
        }

        public int getLife() {
            return life;
        }

        public void setLife(int life) {
            this.life = life;
        }
    }

    public static class ObjectRemoveEvent extends ObjectEvent implements ICancellableEvent {
        public ObjectRemoveEvent(TickingObject object) {
            super(object);
        }
    }

    public static class GravityTickEvent extends ObjectTickEvent {
        private double gravity;

        public GravityTickEvent(TickingObject object, double gravity) {
            super(object);
            this.gravity = gravity;
        }

        public double getGravity() {
            return gravity;
        }
    }
}
