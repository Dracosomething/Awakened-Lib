package io.github.dracosomething.awakened_lib.api.ability;

import io.github.dracosomething.awakened_lib.ability.AbilityInstance;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public interface IAbility {
    public IManaSystem getSystem();

    public int getMaxHeldTime(AbilityInstance instance, LivingEntity entity);

    public int getModes(AbilityInstance instance);

    public int getCooldown(AbilityInstance instance);

    public boolean canLearn(LivingEntity entity);

    public boolean canTick(AbilityInstance instance, LivingEntity entity);

    public boolean canBeToggled(AbilityInstance instance, LivingEntity entity);

    public boolean canIgnoreCooldown(AbilityInstance instance, LivingEntity entity);

    public void onLearn(AbilityInstance instance, LivingEntity entity);

    public void onToggle(AbilityInstance instance, LivingEntity entity, boolean toggled);

    public void onPressed(AbilityInstance instance, LivingEntity entity);

    public boolean onHeld(AbilityInstance instance, LivingEntity entity, int heldTicks);

    public void onRelease(AbilityInstance instance, LivingEntity entity, int heldTicks);

    public void onScroll(AbilityInstance instance, LivingEntity entity, double delta);

    public void onRightClickBlock(AbilityInstance instance, BlockHitResult result, PlayerInteractEvent.RightClickBlock event);

    public void onLeftClickBlock(AbilityInstance instance, BlockHitResult result, PlayerInteractEvent.LeftClickBlock event);

    public void onRightClickEmpty(AbilityInstance instance, PlayerInteractEvent.RightClickEmpty event);

    public void onLeftClickEmpty(AbilityInstance instance, PlayerInteractEvent.LeftClickEmpty event);

    public void onRightClickItem(AbilityInstance instance, PlayerInteractEvent.RightClickItem event);

    public void onInteractEntity(AbilityInstance instance, PlayerInteractEvent.EntityInteractSpecific event);

}
