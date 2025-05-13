package io.github.dracosomething.awakened_lib.library;

import io.github.dracosomething.awakened_lib.capability.ObjectsCapability;
import io.github.dracosomething.awakened_lib.capability.ObjectsProvider;
import io.github.dracosomething.awakened_lib.events.ObjectEvent;
import io.github.dracosomething.awakened_lib.helper.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.EventBus;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public abstract class TickingObject implements Clearable {
    private AtomicInteger counter = new AtomicInteger();
    private int life;
    private int id;
    private Level level;
    private AABB boundingBox;
    private BlockPos pos;
    private UUID uuid;
    private Timer ticker;

    public TickingObject(int life, Level level, AABB aabb, BlockPos pos) {
        this.life = life;
        this.level = level;
        this.boundingBox = aabb;
        this.pos = pos;
        this.uuid = UUID.randomUUID();
        this.ticker = new Timer(this.uuid.toString());
        this.id = counter.incrementAndGet();
    }

    public void onPlace() {}

    public void onTick() {}

    public void onRemove() {}

    public void clearContent() {
        ObjectsCapability.removeObject(this.uuid, this, this.level);
        this.ticker.cancel();
        this.life = 0;
        this.level = null;
        this.boundingBox = null;
        this.pos = null;
        this.ticker = null;
    }

    public final void place() {
        ObjectEvent.ObjectPlaceEvent event = new ObjectEvent.ObjectPlaceEvent(this, this.pos, this.life);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            ObjectsCapability.addObject(this.uuid, this, this.level);
            onPlace();
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
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            if (this.life <= 0) {
                ObjectEvent.ObjectRemoveEvent remove = new ObjectEvent.ObjectRemoveEvent(this);
                if (!MinecraftForge.EVENT_BUS.post(remove)) {
                    onRemove();
                    this.clearContent();
                }
            }
            this.onTick();
            --this.life;
        }
    }

    public boolean isColidingWith(TickingObject object) {
        return this.boundingBox.intersects(object.boundingBox);
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
        return this.pos;
    }

    public boolean shouldBeSaved() {
        return true;
    }

    public boolean isAlwaysTicking() {
        return true;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("life", this.life);
        tag.putInt("id", this.id);
        ListTag listTag = new ListTag();
        listTag.add(DoubleTag.valueOf(this.boundingBox.minX));
        listTag.add(DoubleTag.valueOf(this.boundingBox.minY));
        listTag.add(DoubleTag.valueOf(this.boundingBox.minZ));
        listTag.add(DoubleTag.valueOf(this.boundingBox.maxX));
        listTag.add(DoubleTag.valueOf(this.boundingBox.maxY));
        listTag.add(DoubleTag.valueOf(this.boundingBox.maxZ));
        tag.put("hitbox", listTag);
        tag.putLong("pos", this.pos.asLong());
        tag.putUUID("UUID", this.uuid);
        CompoundTag key = NBTHelper.parseResourceKey(this.level.dimension());
        tag.put("level", key);
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
        this.pos = BlockPos.of(tag.getLong("pos"));
        if (tag.hasUUID("UUID")) {
            this.uuid = tag.getUUID("UUID");
        } else {
            this.uuid = UUID.randomUUID();
        }
        CompoundTag locationTag = tag.getCompound("level").getCompound("location");
        ResourceLocation location = NBTHelper.parseTagToLocation(locationTag);
        ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, location);
        Level level = provider.lookupOrThrow(Registries.DIMENSION).getOrThrow(key).get();
        this.level = level;
    }
}
