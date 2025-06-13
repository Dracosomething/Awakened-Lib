package io.github.dracosomething.awakened_lib.ability;

import io.github.dracosomething.awakened_lib.api.ability.AbilityAPI;
import io.github.dracosomething.awakened_lib.api.ability.IAbility;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class Ability implements IAbility {
    private final CompoundTag tag = new CompoundTag();
    private final IManaSystem system;

    public Ability(IManaSystem system) {
        this.system = system;
    }

    public ResourceLocation getRegistryName() {
        return AbilityAPI.getRegistry().getKey(this);
    }

    public AbilityInstance createDefaultInstance() {
        return new AbilityInstance(this);
    }

    public MutableComponent getName() {
        ResourceLocation id = this.getRegistryName();
        return id == null ? null : Component.translatable(String.format("%s.skill.%s", id.getNamespace(), id.getPath().replace('/', '.')));
    }

    public ResourceLocation getIcon() {
        ResourceLocation id = this.getRegistryName();
        return id == null ? null : ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "icons/skills/" + id.getPath());
    }

    public ChatFormatting getFormatting() {
        return ChatFormatting.WHITE;
    }

    public MutableComponent getDescription() {
        ResourceLocation id = this.getRegistryName();
        return id == null ? null : Component.translatable(String.format("%s.skill.description.%s", id.getNamespace(), id.getPath().replace('/', '.')));
    }

    public MutableComponent getInstructions() {
        ResourceLocation id = this.getRegistryName();
        return id == null ? null : Component.translatable(String.format("%s.skill.instructions.%s", id.getNamespace(), id.getPath().replace('/', '.')));
    }

    public String getAuthor() {
        return "";
    }

    public CompoundTag getTag() {
        return this.tag;
    }

    public IManaSystem getSystem() {
        return system;
    }

    public double getCost(int mode) {
        return 0.0;
    }

    public double getLearningCost() {
        return 0.0;
    }

    public int getMaxHeldTime(AbilityInstance instance, LivingEntity entity) {
        return 72000;
    }

    public int getModes(AbilityInstance instance) {
        return 1;
    }

    public int getCooldown(AbilityInstance instance) {
        return 0;
    }

    public boolean canLearn(LivingEntity entity) {
        return false;
    }

    public boolean canTick(AbilityInstance instance, LivingEntity entity) {
        return false;
    }

    public boolean canBeToggled(AbilityInstance instance, LivingEntity entity) {
        return false;
    }

    public boolean canIgnoreCooldown(AbilityInstance instance, LivingEntity entity) {
        return false;
    }

    public void onLearn(AbilityInstance instance, LivingEntity entity) {}

    public void onToggle(AbilityInstance instance, LivingEntity entity, boolean toggled) {}

    public void onPressed(AbilityInstance instance, LivingEntity entity) {}

    public boolean onHeld(AbilityInstance instance, LivingEntity entity, int heldTicks) {
        return false;
    }

    public void onRelease(AbilityInstance instance, LivingEntity entity, int heldTicks) {}

    public void onScroll(AbilityInstance instance, LivingEntity entity, double delta) {}

    public void onRightClickBlock(AbilityInstance instance, BlockHitResult result, PlayerInteractEvent.RightClickBlock event) {}

    public void onLeftClickBlock(AbilityInstance instance, BlockHitResult result, PlayerInteractEvent.LeftClickBlock event) {}

    public void onRightClickEmpty(AbilityInstance instance, PlayerInteractEvent.RightClickEmpty event) {}

    public void onLeftClickEmpty(AbilityInstance instance, PlayerInteractEvent.LeftClickEmpty event) {}

    public void onRightClickItem(AbilityInstance instance, PlayerInteractEvent.RightClickItem event) {}

    public void onInteractEntity(AbilityInstance instance, PlayerInteractEvent.EntityInteractSpecific event) {}
}
