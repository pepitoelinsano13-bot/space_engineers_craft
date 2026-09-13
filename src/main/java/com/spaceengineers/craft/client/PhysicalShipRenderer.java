package com.spaceengineers.craft.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.spaceengineers.craft.entity.PhysicalShipEntity;
import com.spaceengineers.craft.grid.ShipBlockData;
import com.spaceengineers.craft.registry.ModBlocks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class PhysicalShipRenderer extends EntityRenderer<PhysicalShipEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public PhysicalShipRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(PhysicalShipEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // 6DOF rotation: Yaw, Pitch, and Roll
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getRoll()));

        // Center origin offset
        poseStack.translate(-0.5D, 0.0D, -0.5D);

        if (entity.getShipBlocks().isEmpty()) {
            // Default core rendering
            blockRenderer.renderSingleBlock(ModBlocks.SHIP_CORE.get().defaultBlockState(), poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
        } else {
            // Render every assembled block in its relative position
            for (ShipBlockData b : entity.getShipBlocks()) {
                poseStack.pushPose();
                poseStack.translate(b.relativePos().getX(), b.relativePos().getY(), b.relativePos().getZ());
                blockRenderer.renderSingleBlock(b.state(), poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
                poseStack.popPose();
            }
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PhysicalShipEntity entity) {
        return null;
    }
}
