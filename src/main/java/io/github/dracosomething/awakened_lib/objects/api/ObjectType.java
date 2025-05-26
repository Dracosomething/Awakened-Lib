package io.github.dracosomething.awakened_lib.objects.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class ObjectType<T extends TickingObject> {
    private final ObjectBuilder<T> builder;
    private final AABB boundingBox;

//    public ObjectType(Supplier<T> constructor, AABB boundingBox) {
//        this.constructor = constructor;
//        this.boundingBox = boundingBox;
//    }

    public ObjectType(ObjectBuilder<T> builder, AABB boundingBox) {
        this.builder = builder;
        this.boundingBox = boundingBox;
    }

    public T spawn(int life, Level level, Vec3 pos) {
        T object = create(life, level, pos);
        object.place();
        return object;
    }

    public T create(int life, Level level, Vec3 pos) {
        T object = this.builder.create(this);
        object.setPos(pos);
        object.setLevel(level);
        object.setLife(life);
        object.setBoundingBox(this.boundingBox);
        return object;
    }

    public interface ObjectBuilder<T extends TickingObject> {
        T create(ObjectType<T> type);
    }
}
