package io.github.dracosomething.awakened_lib.objects.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class ProjectileObject extends TickingObject {
    private Vec3 deltaMovement;
    private float yRot;
    private float xRot;
    private float yRotO;
    private float xRotO;
    private double gravity;

    public ProjectileObject(ObjectType<?> type) {
        super(type);
    }

    public Vec3 getMovementToShoot(double x, double y, double z, float velocity, float inaccuracy) {
        return (new Vec3(x, y, z)).normalize().add(this.getRandom().triangle(0.0, 0.0172275 * (double) inaccuracy), this.getRandom().triangle(0.0, 0.0172275 * (double) inaccuracy), this.getRandom().triangle(0.0, 0.0172275 * (double) inaccuracy)).scale((double) velocity);
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vec3 = this.getMovementToShoot(x, y, z, velocity, inaccuracy);
        this.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 180.0 / 3.1415927410125732));
        this.setXRot((float)(Mth.atan2(vec3.y, d0) * 180.0 / 3.1415927410125732));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        this.gravity = 0.10;
        this.place();
    }

    public void shootFromRotation(float xRot, float yRot, float zRot, float velocity, float inaccuracy) {
        float f = -Mth.sin(yRot * 0.017453292F) * Mth.cos(xRot * 0.017453292F);
        float f1 = -Mth.sin((xRot + zRot) * 0.017453292F);
        float f2 = Mth.cos(yRot * 0.017453292F) * Mth.cos(xRot * 0.017453292F);
        this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
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

    public float getXRot() {
        return xRot;
    }

    public float getYRot() {
        return yRot;
    }

    public void setXRot(float xRot) {
        this.xRot = xRot;
    }

    public void setYRot(float yRot) {
        this.yRot = yRot;
    }

    public abstract void onPlace();

    public abstract void onRemove();

    public abstract void onCollide(List<Entity> entities);

    public abstract void onFiredTick();

    public final void onTick() {
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
        if (this.deltaMovement != null && this.deltaMovement.length() > 0) {
            Vec3 vec3 = this.deltaMovement;
            if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
                double d0 = vec3.horizontalDistance();
                this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 180.0F / (float)Math.PI));
                this.setXRot((float)(Mth.atan2(vec3.y, d0) * 180.0F / (float)Math.PI));
                System.out.println(this.yRot);
                System.out.println(this.xRot);
                this.yRotO = this.getYRot();
                this.xRotO = this.getXRot();
            }

            List<Entity> entities = this.getCollidingEntities();
            if (!entities.isEmpty()) {
                this.onCollide(entities);
            }

            vec3 = this.deltaMovement;
            double x = vec3.x;
            double y = vec3.y;
            double z = vec3.z;

            double dX = this.getX() + x;
            double dY = this.getY() + y;
            double dZ = this.getZ() + z;
            double DistanceH = vec3.horizontalDistance();

            this.setYRot((float) (Mth.atan2(x, z) * 180.0F / (float) Math.PI));
            this.setXRot((float) (Mth.atan2(y, DistanceH) * 180.0F / (float) Math.PI));

            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));

            float f = 0.99F;

            this.setDeltaMovement(vec3.scale((double) f));
            this.setDeltaMovement(this.deltaMovement.add(0.0, -this.gravity, 0.0));

            this.setPos(dX, dY, dZ);
            onFiredTick();
        }
    }

    protected float lerpRotation(float currentRotation, float targetRotation) {
        while(targetRotation - currentRotation < -180.0F) {
            currentRotation -= 360.0F;
        }

        while(targetRotation - currentRotation >= 180.0F) {
            currentRotation += 360.0F;
        }

        return Mth.lerp(0.2F, currentRotation, targetRotation);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        CompoundTag deltaMovement = new CompoundTag();
        deltaMovement.putDouble("x", this.getDeltaX());
        deltaMovement.putDouble("y", this.getDeltaY());
        deltaMovement.putDouble("z", this.getDeltaZ());
        tag.put("deltaMovement", deltaMovement);
        tag.putFloat("xRot", this.xRot);
        tag.putFloat("yRot", this.yRot);
        tag.putFloat("xRotO", this.xRotO);
        tag.putFloat("yRotO", this.yRotO);
        tag.putDouble("gravity", this.gravity);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        CompoundTag deltaMovement = tag.getCompound("deltaMovement");
        double x = deltaMovement.getDouble("x");
        double y = deltaMovement.getDouble("y");
        double z = deltaMovement.getDouble("z");
        this.deltaMovement = new Vec3(x, y, z);
        this.xRot = tag.getFloat("xRot");
        this.yRot = tag.getFloat("yRot");
        this.xRotO = tag.getFloat("xRotO");
        this.yRotO = tag.getFloat("yRotO");
        this.gravity = tag.getDouble("gravity");
    }
}
