package io.github.dracosomething.awakened_lib.manaSystem.systems;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.handler.StartUpHandler;

public record RecordManaSystem(String name, double max, double regen, RegenOn on, int regenRate) implements IManaSystem {
    public static final Codec<RecordManaSystem> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.STRING.fieldOf("name").forGetter(RecordManaSystem::getName),
            Codec.DOUBLE.fieldOf("max").forGetter()
    ).apply(builder, RecordManaSystem::new));

    public RecordManaSystem(String name, double max, double regen, RegenOn on, int regenRate) {
        this.name = name;
        this.max = max;
        this.regen = regen;
        this.on = on;
        this.regenRate = regenRate;
        StartUpHandler.getMANAGER().registerSystem(this.name, this);
    }

    public String getName() {
        return name;
    }

    public RegenOn getRegenerator() {
        return this.on;
    }

    public int getRegenRate() {
        return this.regenRate;
    }

    public double getMax() {
        return this.max;
    }

    public double getRegen() {
        return this.regen;
    }
}
