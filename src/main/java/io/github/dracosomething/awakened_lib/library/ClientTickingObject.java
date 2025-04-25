package io.github.dracosomething.awakened_lib.library;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;
import java.util.logging.Level;

public abstract class ClientTickingObject {
    private UUID uuid;
    private Level level;
    private Vec3 position;
    private int life;
    private LivingEntity owner;
    private final Timer timer;

    public ClientTickingObject (LivingEntity owner, Vec3 position, Level level, UUID uuid, int life, Timer timer) {
        this.position = position;
        this.level = level;
        this.uuid = uuid;
        this.life = life;
        this.owner = owner;
        this.timer = timer;
    }

    public ClientTickingObject(LivingEntity owner, Vec3 position, Level level, UUID uuid, int life, String name) {
        this.position = position;
        this.level = level;
        this.uuid = uuid;
        this.life = life;
        this.owner = owner;
        this.timer = new Timer(name);
    }

    public ClientTickingObject(LivingEntity owner, Vec3 position, Level level, UUID uuid, int life) {
        this.position = position;
        this.level = level;
        this.uuid = uuid;
        this.life = life;
        this.owner = owner;
        this.timer = new Timer(this.uuid.toString());
    }

    public ClientTickingObject(LivingEntity owner, Vec3 position, Level level, UUID uuid) {
        this.position = position;
        this.level = level;
        this.uuid = uuid;
        this.life = 100;
        this.owner = owner;
        this.timer = new Timer(this.uuid.toString());
    }

    public ClientTickingObject(LivingEntity owner, Vec3 position, Level level, int life) {
        this.position = position;
        this.level = level;
        this.uuid = UUID.randomUUID();
        this.life = life;
        this.owner = owner;
        this.timer = new Timer(this.uuid.toString());
    }

    public ClientTickingObject(LivingEntity owner, Vec3 position, int life) {
        this.position = position;
        if (owner.getLevel() instanceof Level level) {
            this.level = level;
        }
        this.uuid = UUID.randomUUID();
        this.life = life;
        this.owner = owner;
        this.timer = new Timer(this.uuid.toString());
    }

    public ClientTickingObject(Vec3 position, int life, UUID uuid) {
        this.position = position;
        this.level = null;
        this.uuid = uuid;
        this.life = life;
        this.owner = null;
        this.timer = new Timer(this.uuid.toString());
    }

    public ClientTickingObject(LivingEntity owner, int life) {
        this.position = owner.position();
        if (owner.getLevel() instanceof Level level) {
            this.level = level;
        }
        this.uuid = UUID.randomUUID();
        this.life = life;
        this.owner = owner;
        this.timer = new Timer(this.uuid.toString());
    }

    public ClientTickingObject(LivingEntity owner) {
        this.position = owner.position();
        if (owner.getLevel() instanceof Level level) {
            this.level = level;
        }
        this.uuid = UUID.randomUUID();
        this.life = 100;
        this.owner = owner;
        this.timer = new Timer(this.uuid.toString());
    }

    public ClientTickingObject() {
        this.position = null;
        this.level = null;
        this.uuid = null;
        this.life = 0;
        this.owner = null;
        this.timer = new Timer("");
    }

    public void place() {}

    public void update() {}

    public final void tick() {
        if (this.life <= 0) {
            this.discard();
        }
        update();
        --this.life;
    }

    public final void create() {
        level.getCapability(ObjectsCapability.CAPABILITY).ifPresent((cap) -> {
            cap.addObject(this.uuid, this);
        });
        place();
        Task task = new Task() {
            @Override
            public void run() {
                tick();
            }
        };
        this.timer.schedule(task);
    }

    public final void discard() {
        level.getCapability(ObjectsCapability.CAPABILITY).ifPresent((cap) -> {
            cap.removeObject(this.uuid);
        });
        this.life = 0;
        this.uuid = null;
        this.timer.cancel();
        this.owner = null;
        this.level = null;
        this.position = null;
    }

    public CompoundTag serialize(CompoundTag tag) {
        return null;
    }

    public ClientTickingObject deserialize(CompoundTag tag, ClientTickingObject object) {
        return null;
    }

    public final CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        CompoundTag position = new CompoundTag();
        position.putDouble("x", this.position.x);
        position.putDouble("y", this.position.y);
        position.putDouble("z", this.position.z);
        tag.put("position", position);
        ResourceKey<net.minecraft.world.level.Level> dimension = level.dimension();
        tag.putString("level", dimension.location().toString());
        tag.putUUID("uuid", uuid);
        tag.putInt("life", life);
        tag.putUUID("ownerUUID", this.owner.getUUID());
        serialize(tag);
        return tag;
    }

    public final ClientTickingObject fromNBT(CompoundTag tag) {
        CompoundTag vec3 = tag.getCompound("position");
        ClientTickingObject object = new ClientTickingObject(new Vec3(vec3.getDouble("x"), vec3.getDouble("y"), vec3.getDouble("z")),
                tag.getInt("life"),
                tag.hasUUID("uuid") ? tag.getUUID("uuid") : UUID.randomUUID()
        ) {};
        object = deserialize(tag, object);
        return object;
    }

}
