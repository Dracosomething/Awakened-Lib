package io.github.dracosomething.awakened_lib.ability;

import io.github.dracosomething.awakened_lib.api.ability.AbilityAPI;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
        this.ability = ability;
    }

    public ResourceLocation getSkillId() {
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
        tag.putString("ability", this.getSkillId().toString());
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
}
