package com.spaceengineers.craft.grid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record ShipBlockData(BlockPos relativePos, BlockState state) {
    public int getStateId() {
        return Block.getId(state);
    }

    public static BlockState fromStateId(int id) {
        return Block.stateById(id);
    }
}
