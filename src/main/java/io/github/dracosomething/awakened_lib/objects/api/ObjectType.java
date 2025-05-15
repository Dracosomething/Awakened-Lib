package io.github.dracosomething.awakened_lib.objects.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class ObjectType<T extends TickingObject> {
    private final Function<ObjectType<T>, T> constructor;
    private final AABB boundingBox;

//    public ObjectType(Supplier<T> constructor, AABB boundingBox) {
//        this.constructor = constructor;
//        this.boundingBox = boundingBox;
//    }

    public ObjectType(Function<ObjectType<T>, T> aNew, AABB boundingBox) {
        this.constructor = aNew;
        this.boundingBox = boundingBox;
    }

    public T spawn(int life, Level level, Vec3 pos) {
        T object = create(life, level, pos);
        object.place();
        return object;
    }

    public T create(int life, Level level, Vec3 pos) {
        T object = this.constructor.apply(this);
        object.setPos(pos);
        object.setLevel(level);
        object.setLife(life);
        object.setBoundingBox(this.boundingBox);
        return object;
    }
}
