package com.spaceengineers.craft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class GravityGeneratorBlock extends Block {
    public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");

    public GravityGeneratorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ENABLED, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ENABLED);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            boolean active = !state.getValue(ENABLED);
            level.setBlock(pos, state.setValue(ENABLED, active), 3);
            player.displayClientMessage(Component.literal("§d[Generador de Gravedad] §7Campo gravitatorio artificial: " + (active ? "§aACTIVADO (1.0G)" : "§cDESACTIVADO (0G)")), true);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static boolean hasArtificialGravity(Level level, BlockPos targetPos) {
        return isPosInGravityField(level, targetPos);
    }

    public static boolean isPosInGravityField(Level level, BlockPos targetPos) {
        int radius = 16;
        BlockPos min = targetPos.offset(-radius, -radius, -radius);
        BlockPos max = targetPos.offset(radius, radius, radius);
        for (BlockPos p : BlockPos.betweenClosed(min, max)) {
            BlockState s = level.getBlockState(p);
            if (s.getBlock() instanceof GravityGeneratorBlock && s.getValue(ENABLED)) {
                return true;
            }
        }
        return false;
    }
}
