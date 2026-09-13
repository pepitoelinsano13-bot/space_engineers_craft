package com.spaceengineers.craft.registry;

import com.spaceengineers.craft.SpaceEngineersCraft;
import com.spaceengineers.craft.menu.SatelliteTerminalMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, SpaceEngineersCraft.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<SatelliteTerminalMenu>> SATELLITE_TERMINAL =
            MENUS.register("satellite_terminal",
                    () -> IMenuTypeExtension.create((windowId, inv, data) -> new SatelliteTerminalMenu(windowId, inv)));
}
