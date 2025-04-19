package io.github.dracosomething.awakened_lib.mobEffects;

import io.github.dracosomething.awakened_lib.helper.skillHelper;
import io.github.dracosomething.awakened_lib.registry.EffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TimeStopCoreEffect extends MobEffect {
    public TimeStopCoreEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity p_19467_, int p_19468_) {
        List<Entity> list = skillHelper.DrawSphereAndGetEntitiesInIt(p_19467_, 160, false);
        for (Entity entity1 : list){
            if(entity1 instanceof LivingEntity living && living != p_19467_) {
                living.addEffect(new MobEffectInstance(EffectRegistry.TIMESTOP.get(), 1, 1, false, false, false));
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int amplifier) {
        return pDuration % 20 == 0;
    }
}
