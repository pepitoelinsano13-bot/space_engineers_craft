package com.spaceengineers.craft.block;

import com.spaceengineers.craft.entity.SatelliteEntity;
import com.spaceengineers.craft.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SatelliteCoreBlock extends Block {

    public SatelliteCoreBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            // Deploy satellite into orbit entity if clicked
            if (player.isShiftKeyDown()) {
                SatelliteEntity satellite = new SatelliteEntity(ModEntities.SATELLITE.get(), level);
                satellite.setPos(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
                level.addFreshEntity(satellite);
                level.removeBlock(pos, false);
                level.playSound(null, pos, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.BLOCKS, 1.0F, 1.0F);
                player.displayClientMessage(Component.literal("§6[Satélite] §a¡Satélite de telemetría desplegado y transmitiendo en órbita!"), true);
                return InteractionResult.SUCCESS;
            } else {
                player.displayClientMessage(Component.literal("§6[Satélite] §7Núcleo de Satélite en espera. Agáchate (Shift) y haz clic para desplegar en órbita."), true);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
