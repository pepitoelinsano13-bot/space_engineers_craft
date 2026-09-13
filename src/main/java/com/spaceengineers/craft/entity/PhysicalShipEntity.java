package com.spaceengineers.craft.entity;

import com.spaceengineers.craft.block.*;
import com.spaceengineers.craft.grid.ShipBlockData;
import com.spaceengineers.craft.orbital.SpaceDimensionEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PhysicalShipEntity extends Entity {
    private static final EntityDataAccessor<Float> ROLL =
            SynchedEntityData.defineId(PhysicalShipEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> TOTAL_MASS =
            SynchedEntityData.defineId(PhysicalShipEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> FORWARD_THRUST =
            SynchedEntityData.defineId(PhysicalShipEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> VERTICAL_THRUST =
            SynchedEntityData.defineId(PhysicalShipEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> GYRO_COUNT =
            SynchedEntityData.defineId(PhysicalShipEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BLOCK_COUNT =
            SynchedEntityData.defineId(PhysicalShipEntity.class, EntityDataSerializers.INT);

    private final List<ShipBlockData> shipBlocks = new ArrayList<>();
    private BlockPos cockpitRelativePos = BlockPos.ZERO;

    public PhysicalShipEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ROLL, 0.0F);
        builder.define(TOTAL_MASS, 1000.0F);
        builder.define(FORWARD_THRUST, 500.0F);
        builder.define(VERTICAL_THRUST, 500.0F);
        builder.define(GYRO_COUNT, 1);
        builder.define(BLOCK_COUNT, 1);
    }

    public List<ShipBlockData> getShipBlocks() {
        return this.shipBlocks;
    }

    public void setShipBlocks(List<ShipBlockData> blocks) {
        this.shipBlocks.clear();
        this.shipBlocks.addAll(blocks);
        this.entityData.set(BLOCK_COUNT, blocks.size());

        float mass = 0.0F;
        float fThrust = 0.0F;
        float vThrust = 0.0F;
        int gyros = 0;

        int minX = 0, minY = 0, minZ = 0;
        int maxX = 0, maxY = 0, maxZ = 0;

        for (ShipBlockData b : blocks) {
            BlockPos p = b.relativePos();
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            minZ = Math.min(minZ, p.getZ());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
            maxZ = Math.max(maxZ, p.getZ());

            BlockState s = b.state();
            mass += 50.0F; // Base mass per block

            if (s.getBlock() instanceof CockpitBlock) {
                this.cockpitRelativePos = p;
            } else if (s.getBlock() instanceof IonThrusterBlock) {
                fThrust += 300.0F;
                vThrust += 150.0F;
                mass += 100.0F;
            } else if (s.getBlock() instanceof HydrogenThrusterBlock) {
                fThrust += 750.0F;
                vThrust += 400.0F;
                mass += 150.0F;
            } else if (s.getBlock() instanceof AtmosphericThrusterBlock) {
                fThrust += 400.0F;
                vThrust += 250.0F;
                mass += 120.0F;
            } else if (s.getBlock() instanceof GyroscopeBlock) {
                gyros++;
                mass += 200.0F;
            }
        }

        this.entityData.set(TOTAL_MASS, Math.max(100.0F, mass));
        this.entityData.set(FORWARD_THRUST, Math.max(150.0F, fThrust));
        this.entityData.set(VERTICAL_THRUST, Math.max(100.0F, vThrust));
        this.entityData.set(GYRO_COUNT, Math.max(1, gyros));

        // Adjust dimensions
        float sizeX = Math.max(1.5F, (maxX - minX + 1));
        float sizeY = Math.max(1.5F, (maxY - minY + 1));
        float sizeZ = Math.max(1.5F, (maxZ - minZ + 1));
        this.setBoundingBox(new AABB(
                getX() - sizeX / 2.0, getY(), getZ() - sizeZ / 2.0,
                getX() + sizeX / 2.0, getY() + sizeY, getZ() + sizeZ / 2.0
        ));
    }

    public float getRoll() {
        return this.entityData.get(ROLL);
    }

    public void setRoll(float roll) {
        this.entityData.set(ROLL, roll);
    }

    public float getTotalMass() {
        return this.entityData.get(TOTAL_MASS);
    }

    public float getForwardThrust() {
        return this.entityData.get(FORWARD_THRUST);
    }

    public float getVerticalThrust() {
        return this.entityData.get(VERTICAL_THRUST);
    }

    public int getGyroCount() {
        return this.entityData.get(GYRO_COUNT);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!this.level().isClientSide) {
            if (player.isShiftKeyDown()) {
                // Disassemble ship back into world grid
                disassembleToWorld(player);
                return InteractionResult.SUCCESS;
            } else if (this.getPassengers().isEmpty()) {
                player.startRiding(this);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    public void disassembleToWorld(Player player) {
        BlockPos basePos = this.blockPosition();
        Level level = this.level();

        for (ShipBlockData b : this.shipBlocks) {
            BlockPos targetPos = basePos.offset(b.relativePos());
            level.setBlock(targetPos, b.state(), 3);
        }

        level.playSound(null, basePos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0F, 0.8F);
        player.displayClientMessage(Component.literal("§6[Space Engineers] §aNave desacoplada y anclada a la rejilla del mundo."), true);
        this.discard();
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity passenger = this.getFirstPassenger();
        return passenger instanceof LivingEntity living ? living : null;
    }

    @Override
    public void tick() {
        super.tick();

        LivingEntity controller = this.getControllingPassenger();

        if (controller instanceof Player player) {
            // Control orientation from player
            float targetYaw = player.getYRot();
            float targetPitch = player.getXRot();

            // Smooth gyro rotation
            float turnSpeed = Math.min(12.0F, 3.0F + getGyroCount() * 1.5F);
            this.setYRot(Mth.rotLerp(turnSpeed * 0.1F, this.getYRot(), targetYaw));
            this.setXRot(Mth.rotLerp(turnSpeed * 0.1F, this.getXRot(), targetPitch));

            // Thruster acceleration
            float forwardInput = player.zza; // W/S
            float strafeInput = player.xxa;  // A/D
            float upInput = player.isShiftKeyDown() ? -1.0F : 0.0F;

            Vec3 lookVec = this.getLookAngle();
            Vec3 upVec = new Vec3(0, 1, 0);
            Vec3 rightVec = lookVec.cross(upVec).normalize();

            float accel = (getForwardThrust() / getTotalMass()) * 0.06F;
            Vec3 thrustVec = Vec3.ZERO;

            if (Math.abs(forwardInput) > 0.01F) {
                thrustVec = thrustVec.add(lookVec.scale(forwardInput * accel));
            }
            if (Math.abs(strafeInput) > 0.01F) {
                thrustVec = thrustVec.add(rightVec.scale(strafeInput * accel * 0.6));
            }
            if (Math.abs(upInput) > 0.01F) {
                thrustVec = thrustVec.add(upVec.scale(upInput * accel * 0.8));
            }

            Vec3 motion = this.getDeltaMovement().add(thrustVec);

            // Inertia & drag calculation
            boolean inSpace = SpaceDimensionEvents.isSpaceVacuum(this);
            if (inSpace) {
                // In space: zero air drag! Linear inertia dampeners if no input
                if (thrustVec.lengthSqr() < 0.0001) {
                    motion = motion.scale(0.995); // Minimal cosmic drag
                }
            } else {
                // Atmospheric drag and gravity
                motion = motion.scale(0.96);
                motion = motion.add(0, -0.012, 0); // Gentle atmospheric gravity
            }

            this.setDeltaMovement(motion);

            // Particles when thrusting
            if (this.level().isClientSide && thrustVec.lengthSqr() > 0.001) {
                spawnThrusterParticles(forwardInput, upInput);
            }
        } else {
            // Unmanned drift
            boolean inSpace = SpaceDimensionEvents.isSpaceVacuum(this);
            Vec3 motion = this.getDeltaMovement();
            if (!inSpace) {
                motion = motion.scale(0.95).add(0, -0.03, 0);
            } else {
                motion = motion.scale(0.998);
            }
            this.setDeltaMovement(motion);
        }

        this.move(MoverType.SELF, this.getDeltaMovement());

        // Atmospheric escape: Flight past Y=600 in Overworld transitions to Orbit
        if (!this.level().isClientSide && this.getY() >= 600.0D) {
            SpaceDimensionEvents.transitionShipToOrbit(this);
        }
    }

    private void spawnThrusterParticles(float forward, float up) {
        Vec3 back = this.getLookAngle().reverse().scale(1.2);
        double px = this.getX() + back.x;
        double py = this.getY() + 0.5 + back.y;
        double pz = this.getZ() + back.z;

        if (forward > 0) {
            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, px, py, pz,
                    back.x * 0.3, back.y * 0.3, back.z * 0.3);
            this.level().addParticle(ParticleTypes.SMOKE, px, py, pz, 0, 0, 0);
        }
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            double posX = this.getX() + this.cockpitRelativePos.getX();
            double posY = this.getY() + this.cockpitRelativePos.getY() + 0.2D;
            double posZ = this.getZ() + this.cockpitRelativePos.getZ();
            moveFunction.accept(passenger, posX, posY, posZ);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(ROLL, tag.getFloat("Roll"));
        this.entityData.set(TOTAL_MASS, tag.getFloat("Mass"));
        this.entityData.set(FORWARD_THRUST, tag.getFloat("ForwardThrust"));
        this.entityData.set(VERTICAL_THRUST, tag.getFloat("VerticalThrust"));
        this.entityData.set(GYRO_COUNT, tag.getInt("GyroCount"));

        if (tag.contains("CockpitPos")) {
            long cPos = tag.getLong("CockpitPos");
            this.cockpitRelativePos = BlockPos.of(cPos);
        }

        this.shipBlocks.clear();
        if (tag.contains("Blocks", Tag.TAG_LIST)) {
            ListTag list = tag.getList("Blocks", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag bTag = list.getCompound(i);
                BlockPos relPos = new BlockPos(bTag.getInt("x"), bTag.getInt("y"), bTag.getInt("z"));
                int stateId = bTag.getInt("state");
                BlockState state = ShipBlockData.fromStateId(stateId);
                if (state != null && !state.isAir()) {
                    this.shipBlocks.add(new ShipBlockData(relPos, state));
                }
            }
        }
        this.entityData.set(BLOCK_COUNT, this.shipBlocks.size());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Roll", getRoll());
        tag.putFloat("Mass", getTotalMass());
        tag.putFloat("ForwardThrust", getForwardThrust());
        tag.putFloat("VerticalThrust", getVerticalThrust());
        tag.putInt("GyroCount", getGyroCount());
        tag.putLong("CockpitPos", this.cockpitRelativePos.asLong());

        ListTag list = new ListTag();
        for (ShipBlockData b : this.shipBlocks) {
            CompoundTag bTag = new CompoundTag();
            bTag.putInt("x", b.relativePos().getX());
            bTag.putInt("y", b.relativePos().getY());
            bTag.putInt("z", b.relativePos().getZ());
            bTag.putInt("state", b.getStateId());
            list.add(bTag);
        }
        tag.put("Blocks", list);
    }
}
