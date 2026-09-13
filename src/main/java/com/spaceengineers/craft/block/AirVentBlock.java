package com.spaceengineers.craft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

public class AirVentBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty PRESSURIZED = BooleanProperty.create("pressurized");

    public AirVentBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PRESSURIZED, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PRESSURIZED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            boolean pressurized = state.getValue(PRESSURIZED);
            player.displayClientMessage(Component.literal("§b[Ventilación y Soporte Vital] §7Estado de la sala: " + (pressurized ? "§aHERMÉTICA / PRESURIZADA (O2 100%)" : "§cDESPRESURIZADA (Brecha detectada)")), true);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static boolean isPosPressurized(Level level, BlockPos targetPos) {
        return isPositionPressurized(level, targetPos);
    }

    public static boolean isPositionPressurized(Level level, BlockPos targetPos) {
        int radius = 12;
        BlockPos min = targetPos.offset(-radius, -radius, -radius);
        BlockPos max = targetPos.offset(radius, radius, radius);
        for (BlockPos p : BlockPos.betweenClosed(min, max)) {
            BlockState s = level.getBlockState(p);
            if (s.getBlock() instanceof AirVentBlock && s.getValue(PRESSURIZED)) {
                return true;
            }
        }
        return false;
    }
}
