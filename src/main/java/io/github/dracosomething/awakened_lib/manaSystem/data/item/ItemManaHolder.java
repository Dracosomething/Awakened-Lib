package io.github.dracosomething.awakened_lib.manaSystem.data.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.data.chunk.ChunkManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.data.entity.EntityManaHolder;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystemHolder;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import io.github.dracosomething.awakened_lib.registry.dataComponents.dataComponentsRegistry;
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

    public void tick(ItemStack stack, Entity entity) {
        double rate = holder.getSystem().getRegen();
        switch (holder.getSystem().getRegenerator()) {
            case PLAYER -> {
                EntityManaHolder entityHolder = entity.getData(DataAttachmentRegistry.getEntity(holder.getSystem()));
                entityHolder.setCurrent(entityHolder.getCurrent() - rate);
                entityHolder.sync(entity);
                this.setCurrent(this.getCurrent()+rate);
            }
            case CHUNK -> {
                BlockPos pos = entity.blockPosition();
                Level level = entity.level();
                ChunkAccess chunk = level.getChunkAt(pos);
                ChunkManaHolder chunkHolder = chunk.getData(DataAttachmentRegistry.getChunk(holder.getSystem()));
                this.setCurrent(this.getCurrent() + (rate * chunkHolder.getMultiplier()));
            }
        }
    }

    public void setCurrent(double current) {
        if (current < 0)
            this.current = 0;
        else
            this.current = current;
    }

    public double getCurrent() {
        return current;
    }

    public ManaSystemHolder getHolder() {
        return holder;
    }

    public String getSystemID() {
        return systemID;
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
