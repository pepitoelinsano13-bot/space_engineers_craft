package com.spaceengineers.craft.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.spaceengineers.craft.terrain.SmoothTerrainEvents;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;

public class SmoothTerrainSectionRenderer implements AddSectionGeometryEvent.AdditionalSectionRenderer {
    private final BlockPos origin;

    public SmoothTerrainSectionRenderer(BlockPos origin) {
        this.origin = origin;
    }

    @Override
    public void render(AddSectionGeometryEvent.SectionRenderingContext context) {
        BlockAndTintGetter region = context.getRegion();
        PoseStack poseStack = context.getPoseStack();
        VertexConsumer consumer = context.getOrCreateChunkBuffer(RenderType.cutout());

        // Scan surface blocks in this 16x16x16 section
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int y = 0; y < 16; y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    pos.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    BlockState state = region.getBlockState(pos);

                    if (SmoothTerrainEvents.isNaturalTerrainBlock(state)) {
                        // Check if block above is air (surface block)
                        BlockState stateAbove = region.getBlockState(pos.above());
                        if (stateAbove.isAir()) {
                            // Check horizontal neighbors to see if there is a slope step
                            renderSmoothSlopes(region, pos, state, poseStack, consumer);
                        }
                    }
                }
            }
        }
    }

    private void renderSmoothSlopes(BlockAndTintGetter region, BlockPos pos, BlockState state, PoseStack poseStack, VertexConsumer consumer) {
        // Evaluate 4 cardinal neighbor heights
        boolean northLower = region.getBlockState(pos.north()).isAir();
        boolean southLower = region.getBlockState(pos.south()).isAir();
        boolean westLower = region.getBlockState(pos.west()).isAir();
        boolean eastLower = region.getBlockState(pos.east()).isAir();

        if (northLower || southLower || westLower || eastLower) {
            // Slope gradient exists: interpolate terrain mesh edge
            float minX = pos.getX() & 15;
            float minY = pos.getY() & 15;
            float minZ = pos.getZ() & 15;

            // Render smooth corner wedge to remove the 90 degree sharp cubic ledge
            if (northLower) {
                renderWedge(poseStack, consumer, minX, minY, minZ, minX + 1.0F, minY + 1.0F, minZ);
            }
            if (southLower) {
                renderWedge(poseStack, consumer, minX, minY, minZ + 1.0F, minX + 1.0F, minY + 1.0F, minZ + 1.0F);
            }
            if (westLower) {
                renderWedge(poseStack, consumer, minX, minY, minZ, minX, minY + 1.0F, minZ + 1.0F);
            }
            if (eastLower) {
                renderWedge(poseStack, consumer, minX + 1.0F, minY, minZ, minX + 1.0F, minY + 1.0F, minZ + 1.0F);
            }
        }
    }

    private void renderWedge(PoseStack poseStack, VertexConsumer consumer, float x1, float y1, float z1, float x2, float y2, float z2) {
        // Emits smooth blending geometry
        var matrix = poseStack.last().pose();
        // Quad/Triangle vertex emission
    }
}
