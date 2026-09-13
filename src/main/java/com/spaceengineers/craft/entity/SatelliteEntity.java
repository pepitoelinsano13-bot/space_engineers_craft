package com.spaceengineers.craft.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SatelliteEntity extends Entity {
    private int scanTimer = 0;
    private float rotationAngle = 0.0F;

    public SatelliteEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.scanTimer = compound.getInt("ScanTimer");
        this.rotationAngle = compound.getFloat("RotationAngle");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("ScanTimer", this.scanTimer);
        compound.putFloat("RotationAngle", this.rotationAngle);
    }

    @Override
    public void tick() {
        super.tick();

        // Slow orbital rotation
        this.rotationAngle += 1.0F;
        if (this.rotationAngle >= 360.0F) {
            this.rotationAngle = 0.0F;
        }
        this.setYRot(this.rotationAngle);

        // Orbital hover: maintain altitude or slow drift
        Vec3 delta = this.getDeltaMovement();
        this.setDeltaMovement(delta.x * 0.9D, delta.y * 0.9D, delta.z * 0.9D);
        this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());

        // Emit telemetry antenna signal pulses
        if (this.level().isClientSide) {
            if (this.tickCount % 20 == 0) {
                this.level().addParticle(ParticleTypes.ELECTRIC_SPARK,
                        this.getX(), this.getY() + 1.2D, this.getZ(),
                        0.0D, 0.05D, 0.0D);
            }
        } else {
            scanTimer++;
            if (scanTimer >= 100) {
                scanTimer = 0;
                // Periodic telemetry pulse
            }
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }
}
