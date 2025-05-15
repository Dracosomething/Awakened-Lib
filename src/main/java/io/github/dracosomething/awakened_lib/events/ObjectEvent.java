package io.github.dracosomething.awakened_lib.events;

import io.github.dracosomething.awakened_lib.library.TickingObject;
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
        private BlockPos pos;
        private int life;

        public ObjectPlaceEvent(TickingObject object, BlockPos pos, int life) {
            super(object);
            this.pos = pos;
            this.life = life;
        }

        public BlockPos getPos() {
            return pos;
        }

        public void setPos(BlockPos pos) {
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
}
