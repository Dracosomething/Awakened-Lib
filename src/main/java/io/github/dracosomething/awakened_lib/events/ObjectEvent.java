package io.github.dracosomething.awakened_lib.events;

import io.github.dracosomething.awakened_lib.library.TickingObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class ObjectEvent extends Event {
    private TickingObject object;

    public ObjectEvent(TickingObject object) {
        this.object = object;
    }

    public TickingObject getObject() {
        return object;
    }

    @Cancelable
    public static class ObjectTickEvent extends ObjectEvent {
        public ObjectTickEvent(TickingObject object) {
            super(object);
        }
    }

    @Cancelable
    public static class ObjectPlaceEvent extends ObjectEvent {
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

    @Cancelable
    public static class ObjectRemoveEvent extends ObjectEvent {
        public ObjectRemoveEvent(TickingObject object) {
            super(object);
        }
    }
}
