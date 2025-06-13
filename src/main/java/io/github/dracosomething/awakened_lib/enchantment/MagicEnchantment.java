package io.github.dracosomething.awakened_lib.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record MagicEnchantment(
        String systemId,
        LevelBasedValue max
) {
    public static Codec<MagicEnchantment> CODEC =
        RecordCodecBuilder.create(
                builder -> builder.group(
                                Codec.STRING.optionalFieldOf("system_id", "xp").forGetter(MagicEnchantment::systemId),
                                LevelBasedValue.CODEC.optionalFieldOf("max", new LevelBasedValue.Constant(100)).forGetter(MagicEnchantment::max)
                        )
                        .apply(builder, MagicEnchantment::new)
        );

    public IManaSystem getSystem() {
        IManaSystem system = StartUpHandler.getMANAGER().get(this.systemId);
        if (system != null) {
            return system;
        }
        return StartUpHandler.DEFAULT;
    }
}
