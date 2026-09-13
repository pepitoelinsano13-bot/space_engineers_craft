package com.spaceengineers.craft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class GyroscopeBlock extends Block {
    public GyroscopeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            player.displayClientMessage(Component.literal("§e[Giroscopio] §7Estabilizador de inercia y control de cabeceo/guiñada activo. Par torsional: §a50,000 Nm"), true);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
