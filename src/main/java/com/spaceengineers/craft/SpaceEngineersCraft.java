package com.spaceengineers.craft;

import com.spaceengineers.craft.client.ClientModEvents;
import com.spaceengineers.craft.client.CockpitHudOverlay;
import com.spaceengineers.craft.registry.ModBlocks;
import com.spaceengineers.craft.registry.ModCreativeTabs;
import com.spaceengineers.craft.registry.ModEntities;
import com.spaceengineers.craft.registry.ModItems;
import com.spaceengineers.craft.registry.ModMenus;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SpaceEngineersCraft.MOD_ID)
public class SpaceEngineersCraft {
    public static final String MOD_ID = "space_engineers_craft";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public SpaceEngineersCraft(IEventBus modEventBus) {
        LOGGER.info("Initializing Space Engineers Craft - Redefining Minecraft Physics, Terrain and Space Exploration!");

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(SpaceEngineersCraft::commonSetup);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(SpaceEngineersCraft::registerClientGuiLayers);
            modEventBus.addListener(ClientModEvents::registerScreens);
            modEventBus.addListener(ClientModEvents::registerRenderers);
        }
    }

    public static void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Space Engineers Craft: Common setup completed.");
    }

    public static void registerClientGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "cockpit_hud"),
                new CockpitHudOverlay()
        );
    }
}
