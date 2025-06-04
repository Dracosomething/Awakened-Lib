package io.github.dracosomething.awakened_lib.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SoulBound(
        String systemID,
        boolean canBeDropped,
        int requiredAmount,
        boolean keepsEnchantments,
        int durabilityCost,
        double minimumDurability
) {
    public static final Codec<SoulBound> CODEC = RecordCodecBuilder.create(builder ->
        builder.group(
                Codec.STRING.optionalFieldOf("system_id", "xp").forGetter(SoulBound::systemID),
                Codec.BOOL.optionalFieldOf("can_be_dropped", true).forGetter(SoulBound::canBeDropped),
                Codec.INT.optionalFieldOf("required_amount", 0).forGetter(SoulBound::requiredAmount),
                Codec.BOOL.optionalFieldOf("keeps_enchantments", true).forGetter(SoulBound::keepsEnchantments),
                Codec.INT.optionalFieldOf("durability_cost", 0).forGetter(SoulBound::durabilityCost),
                Codec.DOUBLE.optionalFieldOf("minimum_durability", 0).forGetter(SoulBound::minimumDurability)
        ).apply(builder, SoulBound::new)
    );
}
