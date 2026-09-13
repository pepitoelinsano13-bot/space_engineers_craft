package com.spaceengineers.craft.block;

import com.spaceengineers.craft.menu.SatelliteTerminalMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SatelliteTerminalBlock extends Block {

    public SatelliteTerminalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            player.openMenu(new SimpleMenuProvider(
                    (windowId, playerInventory, p) -> new SatelliteTerminalMenu(windowId, playerInventory),
                    Component.literal("Terminal de Satélites Orbitales")
            ));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
