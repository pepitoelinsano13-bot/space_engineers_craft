package com.spaceengineers.craft.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.spaceengineers.craft.entity.SatelliteEntity;
import com.spaceengineers.craft.registry.ModBlocks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SatelliteRenderer extends EntityRenderer<SatelliteEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public SatelliteRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(SatelliteEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));

        poseStack.translate(-0.5D, 0.0D, -0.5D);
        blockRenderer.renderSingleBlock(ModBlocks.SATELLITE_CORE.get().defaultBlockState(), poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);

        // Render solar arrays on both sides
        poseStack.translate(1.0D, 0.0D, 0.0D);
        blockRenderer.renderSingleBlock(ModBlocks.SOLAR_ARRAY.get().defaultBlockState(), poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.translate(-2.0D, 0.0D, 0.0D);
        blockRenderer.renderSingleBlock(ModBlocks.SOLAR_ARRAY.get().defaultBlockState(), poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SatelliteEntity entity) {
        return null;
    }
}
