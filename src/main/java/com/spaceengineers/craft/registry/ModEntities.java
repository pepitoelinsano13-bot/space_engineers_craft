package com.spaceengineers.craft.registry;

import com.spaceengineers.craft.SpaceEngineersCraft;
import com.spaceengineers.craft.entity.PhysicalShipEntity;
import com.spaceengineers.craft.entity.SatelliteEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, SpaceEngineersCraft.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<PhysicalShipEntity>> PHYSICAL_SHIP =
            ENTITIES.register("physical_ship",
                    () -> EntityType.Builder.<PhysicalShipEntity>of(PhysicalShipEntity::new, MobCategory.MISC)
                            .sized(2.0F, 2.0F)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build("physical_ship"));

    public static final DeferredHolder<EntityType<?>, EntityType<SatelliteEntity>> SATELLITE =
            ENTITIES.register("satellite",
                    () -> EntityType.Builder.<SatelliteEntity>of(SatelliteEntity::new, MobCategory.MISC)
                            .sized(1.5F, 1.5F)
                            .clientTrackingRange(128)
                            .updateInterval(5)
                            .build("satellite"));
}
