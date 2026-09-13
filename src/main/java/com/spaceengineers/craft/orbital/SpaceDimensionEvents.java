package com.spaceengineers.craft.orbital;

import com.spaceengineers.craft.SpaceEngineersCraft;
import com.spaceengineers.craft.block.AirVentBlock;
import com.spaceengineers.craft.block.GravityGeneratorBlock;
import com.spaceengineers.craft.entity.PhysicalShipEntity;
import com.spaceengineers.craft.item.SpacesuitItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = SpaceEngineersCraft.MOD_ID)
public class SpaceDimensionEvents {
    public static final ResourceKey<Level> EARTH_ORBIT = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(SpaceEngineersCraft.MOD_ID, "earth_orbit"));
    public static final ResourceKey<Level> LUNA = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(SpaceEngineersCraft.MOD_ID, "luna"));
    public static final ResourceKey<Level> ARES = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(SpaceEngineersCraft.MOD_ID, "ares"));
    public static final ResourceKey<Level> ASTEROID_BELT = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(SpaceEngineersCraft.MOD_ID, "asteroid_belt"));

    public static boolean isSpaceDimension(Level level) {
        ResourceKey<Level> dim = level.dimension();
        return dim.equals(EARTH_ORBIT) || dim.equals(LUNA) || dim.equals(ARES) || dim.equals(ASTEROID_BELT);
    }

    public static boolean isSpaceVacuum(Entity entity) {
        Level level = entity.level();
        if (!isSpaceDimension(level)) {
            // High atmosphere in Overworld (Y >= 500) is also near-vacuum
            if (level.dimension().equals(Level.OVERWORLD) && entity.getY() >= 500) {
                return !AirVentBlock.isPositionPressurized(level, entity.blockPosition());
            }
            return false;
        }

        // Luna, Asteroids, and Earth Orbit have vacuum. Ares has unbreathable thin atmosphere.
        return !AirVentBlock.isPositionPressurized(level, entity.blockPosition());
    }

    public static void transitionShipToOrbit(PhysicalShipEntity ship) {
        if (!(ship.level() instanceof ServerLevel serverLevel)) return;
        ServerLevel orbitLevel = serverLevel.getServer().getLevel(EARTH_ORBIT);
        if (orbitLevel != null) {
            ship.teleportTo(orbitLevel, ship.getX(), 64.0D, ship.getZ(), null, ship.getYRot(), ship.getXRot());
            if (ship.getControllingPassenger() instanceof Player player) {
                player.displayClientMessage(Component.literal("§b§l[ÓRBITA ALCANZADA] §fHas escapado de la gravedad terrestre. Entrando en microgravedad (LEO)."), true);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity living)) {
            return;
        }

        Level level = living.level();

        // 1. Atmosphere Crossing: Overworld Y >= 600 -> Orbit
        if (!level.isClientSide && living instanceof ServerPlayer player) {
            if (level.dimension().equals(Level.OVERWORLD) && player.getY() >= 600) {
                ServerLevel orbitLevel = player.server.getLevel(EARTH_ORBIT);
                if (orbitLevel != null) {
                    player.teleportTo(orbitLevel, player.getX(), 64.0D, player.getZ(), player.getYRot(), player.getXRot());
                    orbitLevel.playSound(null, player.blockPosition(), SoundEvents.AMBIENT_WARPED_FOREST_MOOD.value(), SoundSource.AMBIENT, 1.0F, 0.5F);
                    player.displayClientMessage(Component.literal("§b§l[ÓRBITA ALCANZADA] §fHas escapado de la gravedad terrestre. Entrando en microgravedad."), true);
                    return;
                }
            } else if (level.dimension().equals(EARTH_ORBIT) && player.getY() <= 0) {
                // Re-entry into Overworld
                ServerLevel overworld = player.server.getLevel(Level.OVERWORLD);
                if (overworld != null) {
                    player.teleportTo(overworld, player.getX(), 550.0D, player.getZ(), player.getYRot(), player.getXRot());
                    overworld.playSound(null, player.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.AMBIENT, 1.0F, 0.7F);
                    player.displayClientMessage(Component.literal("§6§l[REENTRADA ATMOSFÉRICA] §fDescendiendo a través de la ionosfera terrestre."), true);
                    return;
                }
            }
        }

        // 2. Microgravity & Low Gravity Physics
        if (isSpaceDimension(level)) {
            BlockPos pos = living.blockPosition();
            boolean hasArtificialGravity = GravityGeneratorBlock.hasArtificialGravity(level, pos);

            if (!hasArtificialGravity && !living.onGround()) {
                Vec3 motion = living.getDeltaMovement();
                ResourceKey<Level> dim = level.dimension();

                if (dim.equals(EARTH_ORBIT) || dim.equals(ASTEROID_BELT)) {
                    // Zero Gravity: counteract standard Minecraft downward gravity
                    living.setDeltaMovement(motion.x * 0.98D, (motion.y + 0.075D) * 0.98D, motion.z * 0.98D);
                    living.resetFallDistance();
                } else if (dim.equals(LUNA)) {
                    // Lunar Gravity: 0.16G
                    living.setDeltaMovement(motion.x, (motion.y + 0.065D), motion.z);
                    living.resetFallDistance();
                } else if (dim.equals(ARES)) {
                    // Ares Gravity: 0.38G
                    living.setDeltaMovement(motion.x, (motion.y + 0.05D), motion.z);
                    living.resetFallDistance();
                }
            }
        }

        // 3. Vacuum Suffocation / Freezing
        if (!level.isClientSide && living.tickCount % 20 == 0) {
            if (isSpaceVacuum(living)) {
                if (living instanceof Player player) {
                    if (!SpacesuitItem.isWearingFullSuit(player)) {
                        // Suffocation and freeze damage
                        player.hurt(level.damageSources().freeze(), 3.0F);
                        player.setTicksFrozen(Math.min(player.getTicksFrozen() + 60, 300));
                        player.displayClientMessage(Component.literal("§c§l[ALERTA DE VACÍO] §f¡Despresurización! Se requiere Traje Espacial EVA."), true);
                    }
                } else {
                    living.hurt(level.damageSources().freeze(), 2.0F);
                }
            }
        }
    }
}
