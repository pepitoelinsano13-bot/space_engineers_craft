package com.spaceengineers.craft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RcsThrusterBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty FIRING = BooleanProperty.create("firing");
    private static final VoxelShape SHAPE = Block.box(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D);

    public RcsThrusterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(FIRING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FIRING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(FIRING)) {
            Direction dir = state.getValue(FACING).getOpposite();
            double x = pos.getX() + 0.5D + dir.getStepX() * 0.4D;
            double y = pos.getY() + 0.5D + dir.getStepY() * 0.4D;
            double z = pos.getZ() + 0.5D + dir.getStepZ() * 0.4D;
            level.addParticle(ParticleTypes.FIREWORK, x, y, z,
                    dir.getStepX() * 0.1D, dir.getStepY() * 0.1D, dir.getStepZ() * 0.1D);
        }
    }
}
