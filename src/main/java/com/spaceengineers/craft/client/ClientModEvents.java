package com.spaceengineers.craft.client;

import com.spaceengineers.craft.registry.ModEntities;
import com.spaceengineers.craft.registry.ModMenus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ClientModEvents {

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.SATELLITE_TERMINAL.get(), SatelliteTerminalScreen::new);
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.PHYSICAL_SHIP.get(), PhysicalShipRenderer::new);
        event.registerEntityRenderer(ModEntities.SATELLITE.get(), SatelliteRenderer::new);
    }
}
