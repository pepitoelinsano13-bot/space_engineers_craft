package com.spaceengineers.craft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SolarArrayBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);

    public SolarArrayBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            boolean hasSun = level.canSeeSky(pos.above()) && level.isDay();
            if (hasSun) {
                player.displayClientMessage(Component.literal("§b[Panel Solar] §aGenerando energía solar espacial: §e100% de eficiencia."), true);
            } else {
                player.displayClientMessage(Component.literal("§b[Panel Solar] §cSin radiación solar directa §7(Sombra o noche)."), true);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
