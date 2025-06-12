package io.github.dracosomething.awakened_lib.manaSystem.data.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.data.chunk.ChunkManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystemHolder;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Objects;

public class ItemManaHolder {
    public static final Codec<ItemManaHolder> CODEC = RecordCodecBuilder.create(
            builder -> builder.group(
                    Codec.STRING.fieldOf("systemID").forGetter(ItemManaHolder::getSystemID),
                    Codec.DOUBLE.fieldOf("current").forGetter(ItemManaHolder::getCurrent)
            ).apply(builder, ItemManaHolder::new)
    );
    public static final StreamCodec<ByteBuf, ItemManaHolder> BASIC_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ItemManaHolder::getSystemID,
            ByteBufCodecs.DOUBLE, ItemManaHolder::getCurrent,
            ItemManaHolder::new
    );

    private final ManaSystemHolder holder;
    private double max;
    private double current;
    private final String systemID;

    public ItemManaHolder(IManaSystem system) {
        this.holder = new ManaSystemHolder(system);
        this.current = 0;
        this.systemID = system.getName();
    }

    public ItemManaHolder(String id, double current) {
        this.systemID = id;
        this.current = current;
        this.holder = new ManaSystemHolder(StartUpHandler.getMANAGER().get(id));
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        if (this.max > this.getSystem().getMax()) {
            this.max = this.getSystem().getMax();
        } else {
            this.max = max;
        }
    }

    public void tick(ItemStack stack, Entity entity) {
        if (this.max > this.getSystem().getMax())
            this.max = this.getSystem().getMax();
        if (this.getCurrent() < this.getMax()) {
            double rate = holder.getSystem().getRegen();
            switch (holder.getSystem().getRegenerator()) {
                case PLAYER -> {
                    EntityManaHolder entityHolder = entity.getData(DataAttachmentRegistry.getEntity(holder.getSystem()));
                    if (entityHolder.getCurrent() <= 0) {
                        double newCurr = entityHolder.getCurrent() - rate;
                        double thisCurr = this.getCurrent() + rate;
                        double diff;
                        if (thisCurr >= this.getMax()) {
                            diff = thisCurr - this.getMax();
                            thisCurr = this.getMax();
                            newCurr = entityHolder.getCurrent() + diff;
                        }
                        if (newCurr < 0) {
                            newCurr = 0;
                            thisCurr = this.getCurrent() - entityHolder.getCurrent();
                        }
                        this.setCurrent(thisCurr);
                        entityHolder.setCurrent(newCurr);
                        entityHolder.sync(entity);
                    }
                }
                case CHUNK -> {
                    BlockPos pos = entity.blockPosition();
                    Level level = entity.level();
                    ChunkAccess chunk = level.getChunkAt(pos);
                    ChunkManaHolder chunkHolder = chunk.getData(DataAttachmentRegistry.getChunk(holder.getSystem()));
                    double cost = chunkHolder.getISystem().getRegen() * chunkHolder.getMultiplier();
                    if (chunkHolder.getCurrent() >= cost) {
                        chunkHolder.setCurrent(chunkHolder.getCurrent() - cost);
                        double newCurr = this.getCurrent() + cost;
                        if (newCurr >= this.getMax()) {
                            newCurr = this.getMax();
                        }
                        this.setCurrent(newCurr);
                        chunkHolder.sync(chunk);
                    }
                }
            }
        }
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        if (current < 0)
            this.current = 0;
        else if (current >= this.getMax())
            this.current = this.getMax();
        else
            this.current = current;
    }

    public boolean canSetCurrent(double cost) {
        return this.current >= cost;
    }

    public boolean updateCurrent(double cost) {
        if (canSetCurrent(cost)) {
            this.setCurrent(this.getCurrent() - cost);
            return true;
        }
        return false;
    }

    public ManaSystemHolder getHolder() {
        return holder;
    }

    public String getSystemID() {
        return systemID;
    }

    public IManaSystem getSystem() {
        return this.getHolder().getSystem();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemManaHolder that = (ItemManaHolder) o;
        return Double.compare(current, that.current) == 0 && Objects.equals(holder, that.holder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(holder, current);
    }
}
