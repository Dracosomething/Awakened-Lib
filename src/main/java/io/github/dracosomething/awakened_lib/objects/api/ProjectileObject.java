package io.github.dracosomething.awakened_lib.objects.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public abstract class ProjectileObject extends TickingObject {
    private Vec3 deltaMovement;

    public ProjectileObject(ObjectType<?> type) {
        super(type);
    }

    public Vec3 getMovementToShoot(double x, double y, double z, float velocity, float inaccuracy) {
        return (new Vec3(x, y, z)).normalize().add(this.getRandom().triangle(0.0, 0.0172275 * (double) inaccuracy), this.getRandom().triangle(0.0, 0.0172275 * (double) inaccuracy), this.getRandom().triangle(0.0, 0.0172275 * (double) inaccuracy)).scale((double) velocity);
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vec3 = this.getMovementToShoot(x, y, z, velocity, inaccuracy);
        this.setDeltaMovement(vec3);
        this.place();
    }

    public void shoot(Vec3 target, float velocity, float inaccuracy) {
        this.shoot(target.x, target.y, target.z, velocity, inaccuracy);
    }

    public void setDeltaMovement(Vec3 deltaMovement) {
        this.deltaMovement = deltaMovement;
    }

    public double getDeltaX() {
        return this.deltaMovement.x;
    }

    public double getDeltaY() {
        return this.deltaMovement.y;
    }

    public double getDeltaZ() {
        return this.deltaMovement.z;
    }
    public abstract void onPlace();

    public abstract void onRemove();

    public abstract void onCollide(List<Entity> entities);

    public abstract void onFiredTick();

    public final void onTick() {
        if (this.deltaMovement != null && this.deltaMovement.length() > 0) {
            List<Entity> entities = this.getCollidingEntities();
            if (!entities.isEmpty()) {
                this.onCollide(entities);
            }

            Vec3 vec3 = this.deltaMovement;
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;

            double d7 = this.getX() + d5;
            double d2 = this.getY() + d6;
            double d3 = this.getZ() + d1;

            float f = 0.99F;

            this.setDeltaMovement(vec3.scale((double) f));

            this.setPos(d7, d2, d3);
            onFiredTick();
        }
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        CompoundTag deltaMovement = new CompoundTag();
        deltaMovement.putDouble("x", this.getDeltaX());
        deltaMovement.putDouble("y", this.getDeltaY());
        deltaMovement.putDouble("z", this.getDeltaZ());
        tag.put("deltaMovement", deltaMovement);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        CompoundTag deltaMovement = tag.getCompound("deltaMovement");
        double x = deltaMovement.getDouble("x");
        double y = deltaMovement.getDouble("y");
        double z = deltaMovement.getDouble("z");
        this.deltaMovement = new Vec3(x, y, z);
    }
}
