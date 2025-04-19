package io.github.dracosomething.awakened_lib.library;

import com.google.errorprone.annotations.ForOverride;
import io.github.dracosomething.awakened_lib.world.ClientTickingObjectsSaveData;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public abstract class ClientTickingObject {
    protected Vec3 position;
    protected ServerLevel level;
    private UUID uuid;
    private int life;
    private LivingEntity owner;
    private final Timer timer;

    public ClientTickingObject (LivingEntity owner, Vec3 position, ServerLevel level, UUID uuid, int life, Timer timer) {
        this.position = position;
        this.level = level;
        this.uuid = uuid;
        this.life = life;
        this.owner = owner;
        this.timer = timer;
    }

    public ClientTickingObject(LivingEntity owner, Vec3 position, ServerLevel level, UUID uuid, int life, String name) {
        this.position = position;
        this.level = level;
        this.uuid = uuid;
        this.life = life;
        this.owner = owner;
        this.timer = new Timer(name);
    }

    public ClientTickingObject(LivingEntity owner, Vec3 position, ServerLevel level, UUID uuid, int life) {
        this.position = position;
        this.level = level;
        this.uuid = uuid;
        this.life = life;
        this.owner = owner;
        this.timer = new Timer(this.uuid.toString());
    }

    public ClientTickingObject(LivingEntity owner, Vec3 position, ServerLevel level, UUID uuid) {
        this.position = position;
        this.level = level;
        this.uuid = uuid;
        this.life = 100;
        this.owner = owner;
        this.timer = new Timer(this.uuid.toString());
    }

    public ClientTickingObject(LivingEntity owner, Vec3 position, ServerLevel level, int life) {
        this.position = position;
        this.level = level;
        this.uuid = UUID.randomUUID();
        this.life = life;
        this.owner = owner;
        this.timer = new Timer(this.uuid.toString());
    }

    public ClientTickingObject(LivingEntity owner, Vec3 position, int life) {
        this.position = position;
        if (owner.getLevel() instanceof ServerLevel level) {
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
        if (owner.getLevel() instanceof ServerLevel level) {
            this.level = level;
        }
        this.uuid = UUID.randomUUID();
        this.life = life;
        this.owner = owner;
        this.timer = new Timer(this.uuid.toString());
    }

    public ClientTickingObject(LivingEntity owner) {
        this.position = owner.position();
        if (owner.getLevel() instanceof ServerLevel level) {
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
        ClientTickingObjectsSaveData data = ClientTickingObjectsSaveData.get(this.level);
        data.addObject(this.uuid, this);
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
        ClientTickingObjectsSaveData data = ClientTickingObjectsSaveData.get(this.level);
        data.removeObject(this.uuid);
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
        ResourceKey<Level> dimension = level.dimension();
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
