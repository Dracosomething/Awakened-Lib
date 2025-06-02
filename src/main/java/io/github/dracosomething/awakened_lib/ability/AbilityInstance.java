package io.github.dracosomething.awakened_lib.ability;

import io.github.dracosomething.awakened_lib.api.ability.AbilityAPI;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public class AbilityInstance {
    private final Ability ability;
    private int mode = 1;
    private int cooldown;
    private CompoundTag tag;
    private boolean toggled;
    private boolean dirty = false;

    public AbilityInstance(Ability ability) {
        this.ability = ability;}

    public Ability getAbility() {
        return ability;
    }

    public ResourceLocation getAbilityId() {
        return AbilityAPI.getRegistry().getKey(this.ability);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbilityInstance that = (AbilityInstance) o;
        return mode == that.mode &&
                cooldown == that.cooldown &&
                toggled == that.toggled &&
                dirty == that.dirty &&
                Objects.equals(ability, that.ability) &&
                Objects.equals(tag, that.tag);
    }

    public int hashCode() {
        return Objects.hash(ability, mode, cooldown, tag, toggled, dirty);
    }

    @ApiStatus.NonExtendable
    public final CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("ability", this.getAbilityId().toString());
        this.serialize(tag);
        return tag;
    }

    public CompoundTag serialize(CompoundTag tag) {
        tag.putInt("Mode", this.mode);
        tag.putInt("Cooldown", this.cooldown);
        tag.putBoolean("Toggled", this.toggled);
        if (this.tag != null) {
            tag.put("tag", this.tag.copy());
        }

        return tag;
    }

    public void deserialize(CompoundTag tag) {
        this.mode = tag.getInt("Mode");
        this.cooldown = tag.getInt("Cooldown");
        this.toggled = tag.getBoolean("Toggled");
        if (tag.contains("tag", 10)) {
            this.tag = tag.getCompound("tag");
        }

    }

    @ApiStatus.NonExtendable
    public static AbilityInstance fromNBT(CompoundTag tag) {
        ResourceLocation location = ResourceLocation.tryParse(tag.getString("ability"));
        AbilityInstance instance = Objects.requireNonNull(AbilityAPI.getRegistry().get(location)).createDefaultInstance();
        instance.deserialize(tag);
        return instance;
    }

    public AbilityInstance clone() {
        AbilityInstance clone = new AbilityInstance(this.ability);
        clone.dirty = this.dirty;
        clone.serialize(this.toNBT());
        return clone;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty() {
        this.dirty = true;
    }

    public void resetDirty() {
        this.dirty = false;
    }

    public void onLearn(LivingEntity entity) {
        this.ability.onLearn(this, entity);
    }

    public void onToggle(LivingEntity entity, boolean toggled) {
        this.ability.onToggle(this, entity, toggled);
    }

    public void onPressed(LivingEntity entity) {
        this.ability.onPressed(this, entity);
    }

    public boolean onHeld(LivingEntity entity, int heldTicks) {
        return this.ability.onHeld(this, entity, heldTicks);
    }

    public void onRelease(LivingEntity entity, int heldTicks) {
        this.ability.onRelease(this, entity, heldTicks);
    }

    public void onScroll(LivingEntity entity, double delta) {
        this.ability.onScroll(this, entity, delta);
    }

    public void onRightClickBlock(BlockHitResult result, PlayerInteractEvent.RightClickBlock event) {
        this.ability.onRightClickBlock(this, result, event);
    }

    public void onLeftClickBlock(BlockHitResult result, PlayerInteractEvent.LeftClickBlock event) {
        this.ability.onLeftClickBlock(this, result, event);
    }

    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        this.ability.onRightClickEmpty(this, event);
    }

    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        this.ability.onLeftClickEmpty(this, event);
    }

    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        this.ability.onRightClickItem(this, event);
    }

    public void onInteractEntity(PlayerInteractEvent.EntityInteractSpecific event) {
        this.ability.onInteractEntity(this, event);
    }

}
