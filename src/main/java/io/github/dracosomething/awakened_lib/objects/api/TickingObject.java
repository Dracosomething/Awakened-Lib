package io.github.dracosomething.awakened_lib.objects.api;

import io.github.dracosomething.awakened_lib.api.ObjectsAPI;
import io.github.dracosomething.awakened_lib.dataAttachements.ObjectsAttachement;
import io.github.dracosomething.awakened_lib.events.ObjectEvent;
import io.github.dracosomething.awakened_lib.helper.NBTHelper;
import io.github.dracosomething.awakened_lib.util.Task;
import io.github.dracosomething.awakened_lib.util.Timer;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class TickingObject implements Clearable {
    private AtomicInteger counter = new AtomicInteger();
    private final ObjectType<?> type;
    private Timer ticker;
    private RandomSource random;
    private int life;
    private int id;
    private Level level;
    private AABB boundingBox;
    private Vec3 pos;
    private UUID uuid;
    private boolean placed;

    public TickingObject(ObjectType<?> type) {
        this.type = type;
        this.random = RandomSource.create();
        this.id = counter.incrementAndGet();
        this.uuid = UUID.randomUUID();
        this.ticker = new Timer(this.uuid.toString());
        System.out.println("Created new TickingObject instance");
        System.out.println(this.random);
        this.placed = false;
    }

    public abstract void onPlace();

    public abstract void onTick();

    public abstract void onRemove();

    public void clearContent() {
        ObjectsAttachement.removeObject(this.uuid, this, this.getChunk());
        this.ticker.cancel();
        this.life = 0;
        this.level = null;
        this.boundingBox = null;
        this.pos = null;
        this.ticker = null;
        this.random = null;
        this.placed = true;
    }

    public final void place() {
        ObjectEvent.ObjectPlaceEvent event = new ObjectEvent.ObjectPlaceEvent(this, this.pos, this.life);
        if (!NeoForge.EVENT_BUS.post(event).isCanceled()) {
            this.placed = true;
            this.pos = event.getPos();
            this.life = event.getLife();
            ObjectsAttachement.addObject(this.uuid, this, this.getChunk());
            if (!this.placed) {
                onPlace();
            }
            Task tick = new Task() {
                @Override
                public void run() {
                    tick();
                }
            };
            this.ticker.schedule(tick);
        }
    }

    public final void tick() {
        ObjectEvent.ObjectTickEvent event = new ObjectEvent.ObjectTickEvent(this);
        if (!NeoForge.EVENT_BUS.post(event).isCanceled()) {
            if (this.life <= 0) {
                ObjectEvent.ObjectRemoveEvent remove = new ObjectEvent.ObjectRemoveEvent(this);
                if (!NeoForge.EVENT_BUS.post(remove).isCanceled()) {
                    onRemove();
                    this.clearContent();
                }
            }
            this.onTick();
            --this.life;
        }
    }

    public List<Entity> getCollidingEntities() {
        return this.level.getEntitiesOfClass(Entity.class, this.boundingBox, (this::isCollidingWith));
    }

    public boolean isCollidingWith(Entity entity) {
        return entity.getBoundingBox().contains(this.pos) &&
                this.boundingBox.contains(entity.position()) &&
                entity.getBoundingBox().intersects(this.boundingBox) &&
                this.boundingBox.intersects(entity.getBoundingBox());
    }

    protected RandomSource getRandom() {
        return random;
    }

    protected Timer getTicker() {
        return ticker;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setPos(Vec3 pos) {
        this.pos = pos;
    }

    public void setPos(double x, double y, double z) {
        this.pos = new Vec3(x, y, z);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setBoundingBox(AABB boundingBox) {
        this.boundingBox = boundingBox;
    }

    public boolean isCollidingWith(TickingObject object) {
        return this.boundingBox.intersects(object.boundingBox) && this.boundingBox.contains(this.pos);
    }

    public List<TickingObject> getCollidingObjects() {
        return this.getChunk().getData(DataAttachmentRegistry.OBJECTS.get()).getOBJECTS().values().stream().filter((object) -> {
           return object.isCollidingWith(this) && this.isCollidingWith(object);
        }).toList();
    }

    public Level getLevel() {
        return level;
    }

    public AABB getBoundingBox() {
        return this.boundingBox;
    }

    public int getId() {
        return this.id;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public BlockPos blockPosition() {
        return new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ());
    }

    public Vec3 getPos() {
        return pos;
    }

    public boolean shouldBeSaved() {
        return true;
    }

    public boolean isAlwaysTicking() {
        return true;
    }

    public final double getBbHeight() {
        return this.boundingBox.getYsize();
    }

    public final double getBbWidth() {
        return this.boundingBox.getXsize();
    }

    public final double getBbLength() {
        return this.boundingBox.getZsize();
    }

    public final double getX() {
        return this.pos.x();
    }

    public final double getX(double multiplier) {
        return this.pos.x() + (double)this.getBbWidth() * multiplier;
    }

    public final double getRandomX(double randomScale) {
        if (this.random == null) return this.getX();
        return this.getX((2.0 * this.random.nextDouble() - 1.0) * randomScale);
    }

    public final double getY() {
        return this.pos.y();
    }

    public final double getY(double multiplier) {
        return this.getY() + (double)this.getBbHeight() * multiplier;
    }

    public final double getRandomY() {
        if (this.random == null) return this.getY();
        return this.getY(this.random.nextDouble());
    }

    public final double getZ() {
        return this.pos.z();
    }

    public final double getZ(double multiplier) {
        return this.getZ() + (double)this.getBbLength() * multiplier;
    }

    public final double getRandomZ(double randomScale) {
        if (this.random == null) return this.getZ();
        return this.getZ((2.0 * this.random.nextDouble() - 1.0) * randomScale);
    }

    public LevelChunk getChunk() {
        return this.level.getChunkAt(this.blockPosition());
    }

    public void addParticlesOnPos(ParticleOptions particles, double randomScale) {
        if (this.random == null) return;
        double d0 = random.nextGaussian() * 0.02;
        double d1 = random.nextGaussian() * 0.02;
        double d2 = random.nextGaussian() * 0.02;
        this.level.addParticle(particles, this.getRandomX(randomScale) , this.getRandomY(), this.getRandomZ(randomScale), d0, d1, d2);
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particles, this.getRandomX(randomScale) , this.getRandomY(), this.getRandomZ(randomScale), 1, d0, d1, d2, 0.1);
        }
    }

    public void addParticlesAroundSelf(ParticleOptions particles, double randomScale) {
        if (this.random == null) return;
        RandomSource random = RandomSource.create();
        double d0 = random.nextGaussian() * 0.02;
        double d1 = random.nextGaussian() * 0.02;
        double d2 = random.nextGaussian() * 0.02;
        double d3 = random.nextGaussian() * 0.02;
        double d4 = random.nextGaussian() * 0.02;
        double d5 = random.nextGaussian() * 0.02;
        double x0 = this.boundingBox.minX * (2.0 * this.random.nextDouble() - 1.0) * randomScale;
        double x1 = this.boundingBox.maxX * (2.0 * this.random.nextDouble() - 1.0) * randomScale;
        double y0 = this.boundingBox.minY * this.random.nextDouble();
        double y1 = this.boundingBox.maxY * this.random.nextDouble();
        double z0 = this.boundingBox.minZ * (2.0 * this.random.nextDouble() - 1.0) * randomScale;
        double z1 = this.boundingBox.maxZ * (2.0 * this.random.nextDouble() - 1.0) * randomScale;
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particles, x0, y0, z0, 1, d0, d1, d2, 0.1);
            serverLevel.sendParticles(particles, x1, y1, z1, 1, d3, d4, d5, 0.1);
        }
        this.level.addParticle(particles, x0, y0, z0, d0, d1, d2);
        this.level.addParticle(particles, x1, y1, z1, d3, d4, d5);
    }

    public void addAdditionalSaveData(CompoundTag tag) {

    }

    public void readAdditionalSaveData(CompoundTag tag) {

    }

    public void moveTo(Vec3 movement) {
        double x = movement.x;
        double y = movement.y;
        double z = movement.z;
        double d0 = Mth.clamp(x, -3.0E7, 3.0E7);
        double d1 = Mth.clamp(z, -3.0E7, 3.0E7);
        this.setPos(d0, y, d1);
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putString("Location", ObjectsAPI.getRegistry().getKey(this.type).toString());
        tag.putInt("life", this.life);
        tag.putInt("id", this.id);
        ListTag listTag = new ListTag();
        if (this.boundingBox != null) {
            listTag.add(DoubleTag.valueOf(this.boundingBox.minX));
            listTag.add(DoubleTag.valueOf(this.boundingBox.minY));
            listTag.add(DoubleTag.valueOf(this.boundingBox.minZ));
            listTag.add(DoubleTag.valueOf(this.boundingBox.maxX));
            listTag.add(DoubleTag.valueOf(this.boundingBox.maxY));
            listTag.add(DoubleTag.valueOf(this.boundingBox.maxZ));
            tag.put("hitbox", listTag);
        }
        tag.put("pos", NBTHelper.parseVec3(this.pos));
        tag.putUUID("UUID", this.uuid);
        CompoundTag key = NBTHelper.parseResourceKey(this.level.dimension());
        tag.put("level", key);
        tag.putBoolean("placed", this.placed);
        this.addAdditionalSaveData(tag);
        return tag;
    }

    public void load(CompoundTag tag, HolderLookup.Provider provider) {
        this.life = tag.getInt("life");
        this.id = tag.getInt("id");
        ListTag listTag = tag.getList("hitbox", 6);
        this.boundingBox = new AABB(
                listTag.getDouble(1),
                listTag.getDouble(2),
                listTag.getDouble(3),
                listTag.getDouble(4),
                listTag.getDouble(5),
                listTag.getDouble(6)
        );
        CompoundTag pos = tag.getCompound("pos");
        this.pos = new Vec3(pos.getDouble("x"), pos.getDouble("y"), pos.getDouble("z"));
        if (tag.hasUUID("UUID")) {
            this.uuid = tag.getUUID("UUID");
        } else {
            this.uuid = UUID.randomUUID();
        }
        this.placed = tag.getBoolean("placed");
        CompoundTag locationTag = tag.getCompound("level").getCompound("location");
        ResourceLocation location = NBTHelper.parseTagToLocation(locationTag);
        ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, location);
        Level level = provider.lookupOrThrow(Registries.DIMENSION).getOrThrow(key).value();
        this.level = level;
        this.readAdditionalSaveData(tag);
    }
}
