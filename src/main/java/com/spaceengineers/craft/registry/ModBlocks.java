package com.spaceengineers.craft.registry;

import com.spaceengineers.craft.SpaceEngineersCraft;
import com.spaceengineers.craft.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SpaceEngineersCraft.MOD_ID);

    // Ship & Grid Cores
    public static final DeferredBlock<ShipCoreBlock> SHIP_CORE = BLOCKS.register("ship_core",
            () -> new ShipCoreBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0F, 12.0F)
                    .sound(SoundType.NETHERITE_BLOCK)));

    public static final DeferredBlock<StationCoreBlock> STATION_CORE = BLOCKS.register("station_core",
            () -> new StationCoreBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(6.0F, 15.0F)
                    .sound(SoundType.NETHERITE_BLOCK)));

    // Cockpit & Flight
    public static final DeferredBlock<CockpitBlock> COCKPIT = BLOCKS.register("cockpit",
            () -> new CockpitBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(4.0F, 10.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    // Thrusters
    public static final DeferredBlock<IonThrusterBlock> ION_THRUSTER = BLOCKS.register("ion_thruster",
            () -> new IonThrusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(3.5F, 8.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> state.getValue(IonThrusterBlock.FIRING) ? 14 : 0)));

    public static final DeferredBlock<HydrogenThrusterBlock> HYDROGEN_THRUSTER = BLOCKS.register("hydrogen_thruster",
            () -> new HydrogenThrusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(4.0F, 10.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> state.getValue(HydrogenThrusterBlock.FIRING) ? 15 : 0)));

    public static final DeferredBlock<AtmosphericThrusterBlock> ATMOSPHERIC_THRUSTER = BLOCKS.register("atmospheric_thruster",
            () -> new AtmosphericThrusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5F, 8.0F)
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<RcsThrusterBlock> RCS_THRUSTER = BLOCKS.register("rcs_thruster",
            () -> new RcsThrusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(2.5F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final DeferredBlock<GyroscopeBlock> GYROSCOPE = BLOCKS.register("gyroscope",
            () -> new GyroscopeBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0F, 12.0F)
                    .sound(SoundType.ANVIL)));

    public static final DeferredBlock<GravityGeneratorBlock> GRAVITY_GENERATOR = BLOCKS.register("gravity_generator",
            () -> new GravityGeneratorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(6.0F, 15.0F)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .lightLevel(state -> 8)));

    // Life Support & Pressurization
    public static final DeferredBlock<AirVentBlock> AIR_VENT = BLOCKS.register("air_vent",
            () -> new AirVentBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final DeferredBlock<HydrogenElectrolyzerBlock> HYDROGEN_ELECTROLYZER = BLOCKS.register("hydrogen_electrolyzer",
            () -> new HydrogenElectrolyzerBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(4.0F, 10.0F)
                    .sound(SoundType.METAL)));

    // Satellites & Space Tech
    public static final DeferredBlock<SatelliteCoreBlock> SATELLITE_CORE = BLOCKS.register("satellite_core",
            () -> new SatelliteCoreBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 8.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final DeferredBlock<SolarArrayBlock> SOLAR_ARRAY = BLOCKS.register("solar_array",
            () -> new SolarArrayBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .strength(2.0F, 4.0F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()));

    public static final DeferredBlock<SatelliteTerminalBlock> SATELLITE_TERMINAL = BLOCKS.register("satellite_terminal",
            () -> new SatelliteTerminalBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(4.0F, 10.0F)
                    .sound(SoundType.METAL)));

    // Planetary Terrain Blocks
    public static final DeferredBlock<Block> LUNAR_REGOLITH = BLOCKS.register("lunar_regolith",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(0.8F, 0.8F)
                    .sound(SoundType.SAND)));

    public static final DeferredBlock<Block> LUNAR_STONE = BLOCKS.register("lunar_stone",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(2.5F, 8.0F)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> ARES_SAND = BLOCKS.register("ares_sand",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .strength(0.8F, 0.8F)
                    .sound(SoundType.SAND)));

    public static final DeferredBlock<Block> ARES_ROCK = BLOCKS.register("ares_rock",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .strength(2.8F, 9.0F)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> ASTEROID_STONE = BLOCKS.register("asteroid_stone",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(3.5F, 12.0F)
                    .sound(SoundType.BASALT)));
}
