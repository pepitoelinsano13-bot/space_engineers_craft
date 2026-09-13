package com.spaceengineers.craft.registry;

import com.spaceengineers.craft.SpaceEngineersCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SpaceEngineersCraft.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SPACE_ENGINEERS_TAB = CREATIVE_MODE_TABS.register(
            "space_engineers_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.literal("Space Engineers Craft"))
                    .icon(() -> new ItemStack(ModBlocks.COCKPIT.get()))
                    .displayItems((parameters, output) -> {
                        // Tools & Suits
                        output.accept(ModItems.WELDER_TOOL.get());
                        output.accept(ModItems.OXYGEN_BOTTLE.get());
                        output.accept(ModItems.HYDROGEN_BOTTLE.get());
                        output.accept(ModItems.SPACESUIT_HELMET.get());
                        output.accept(ModItems.SPACESUIT_CHESTPLATE.get());
                        output.accept(ModItems.SPACESUIT_LEGGINGS.get());
                        output.accept(ModItems.SPACESUIT_BOOTS.get());

                        // Ship Components & Tech
                        output.accept(ModBlocks.SHIP_CORE.get());
                        output.accept(ModBlocks.STATION_CORE.get());
                        output.accept(ModBlocks.COCKPIT.get());
                        output.accept(ModBlocks.ION_THRUSTER.get());
                        output.accept(ModBlocks.HYDROGEN_THRUSTER.get());
                        output.accept(ModBlocks.ATMOSPHERIC_THRUSTER.get());
                        output.accept(ModBlocks.RCS_THRUSTER.get());
                        output.accept(ModBlocks.GYROSCOPE.get());
                        output.accept(ModBlocks.GRAVITY_GENERATOR.get());
                        output.accept(ModBlocks.AIR_VENT.get());
                        output.accept(ModBlocks.HYDROGEN_ELECTROLYZER.get());

                        // Satellites & Terminals
                        output.accept(ModBlocks.SATELLITE_CORE.get());
                        output.accept(ModBlocks.SOLAR_ARRAY.get());
                        output.accept(ModBlocks.SATELLITE_TERMINAL.get());

                        // Space Terrain
                        output.accept(ModBlocks.LUNAR_REGOLITH.get());
                        output.accept(ModBlocks.LUNAR_STONE.get());
                        output.accept(ModBlocks.ARES_SAND.get());
                        output.accept(ModBlocks.ARES_ROCK.get());
                        output.accept(ModBlocks.ASTEROID_STONE.get());
                    })
                    .build()
    );
}
