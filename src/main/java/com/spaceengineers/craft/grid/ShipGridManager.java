package com.spaceengineers.craft.grid;

import com.spaceengineers.craft.entity.PhysicalShipEntity;
import com.spaceengineers.craft.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class ShipGridManager {
    private static final int MAX_SHIP_BLOCKS = 1024;

    public static boolean assembleShipGrid(Level level, BlockPos corePos, Player player) {
        if (level.isClientSide) return false;

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        List<ShipBlockData> collectedBlocks = new ArrayList<>();

        queue.add(corePos);
        visited.add(corePos);

        while (!queue.isEmpty() && visited.size() <= MAX_SHIP_BLOCKS) {
            BlockPos current = queue.poll();
            BlockState state = level.getBlockState(current);

            if (state.isAir()) continue;

            collectedBlocks.add(new ShipBlockData(current.subtract(corePos), state));

            for (Direction dir : Direction.values()) {
                BlockPos neighbor = current.relative(dir);
                if (!visited.contains(neighbor)) {
                    BlockState neighborState = level.getBlockState(neighbor);
                    if (!neighborState.isAir() && !neighborState.is(Blocks.BEDROCK)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        if (collectedBlocks.isEmpty()) {
            return false;
        }

        // Remove blocks from the world
        for (ShipBlockData b : collectedBlocks) {
            BlockPos worldPos = corePos.offset(b.relativePos());
            level.setBlock(worldPos, Blocks.AIR.defaultBlockState(), 3);
        }

        // Spawn physical ship entity
        PhysicalShipEntity ship = new PhysicalShipEntity(ModEntities.PHYSICAL_SHIP.get(), level);
        ship.setPos(corePos.getX() + 0.5D, corePos.getY(), corePos.getZ() + 0.5D);
        ship.setShipBlocks(collectedBlocks);

        level.addFreshEntity(ship);

        // Mount player to pilot the ship
        if (player != null) {
            player.startRiding(ship, true);
        }

        return true;
    }
}
