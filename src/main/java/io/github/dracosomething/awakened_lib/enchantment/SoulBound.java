package io.github.dracosomething.awakened_lib.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record SoulBound(
        String systemID,
        boolean canBeDropped,
        LevelBasedValue requiredAmount,
        boolean keepsEnchantments,
        LevelBasedValue durabilityCost,
        LevelBasedValue minimumDurability
) {
    public static final Codec<SoulBound> CODEC = RecordCodecBuilder.create(builder ->
        builder.group(
                Codec.STRING.optionalFieldOf("system_id", "xp").forGetter(SoulBound::systemID),
                Codec.BOOL.optionalFieldOf("can_be_dropped", true).forGetter(SoulBound::canBeDropped),
                LevelBasedValue.CODEC.optionalFieldOf("required_amount", new LevelBasedValue.Constant(0)).forGetter(SoulBound::requiredAmount),
                Codec.BOOL.optionalFieldOf("keeps_enchantments", true).forGetter(SoulBound::keepsEnchantments),
                LevelBasedValue.CODEC.optionalFieldOf("durability_cost", new LevelBasedValue.Constant(0)).forGetter(SoulBound::durabilityCost),
                LevelBasedValue.CODEC.optionalFieldOf("minimum_durability", new LevelBasedValue.Constant(0)).forGetter(SoulBound::minimumDurability)
        ).apply(builder, SoulBound::new)
    );
}
