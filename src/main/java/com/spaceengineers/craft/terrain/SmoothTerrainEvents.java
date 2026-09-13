package com.spaceengineers.craft.terrain;

import com.spaceengineers.craft.SpaceEngineersCraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = SpaceEngineersCraft.MOD_ID)
public class SmoothTerrainEvents {
    private static final ResourceLocation STEP_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(SpaceEngineersCraft.MOD_ID, "natural_terrain_step");

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        AttributeInstance stepAttribute = player.getAttribute(Attributes.STEP_HEIGHT);
        if (stepAttribute == null) {
            return;
        }

        BlockPos posBelow = player.blockPosition().below();
        BlockState stateBelow = level.getBlockState(posBelow);

        boolean isNaturalTerrain = isNaturalTerrainBlock(stateBelow);

        if (isNaturalTerrain) {
            // Apply smooth slope step-assist (allows smoothly walking up 1.25m slopes without jumping)
            if (!stepAttribute.hasModifier(STEP_MODIFIER_ID)) {
                stepAttribute.addTransientModifier(new AttributeModifier(
                        STEP_MODIFIER_ID,
                        0.65D, // Base step (0.6) + 0.65 = 1.25m smooth slope climbing
                        AttributeModifier.Operation.ADD_VALUE
                ));
            }
        } else {
            if (stepAttribute.hasModifier(STEP_MODIFIER_ID)) {
                stepAttribute.removeModifier(STEP_MODIFIER_ID);
            }
        }
    }

    public static boolean isNaturalTerrainBlock(BlockState state) {
        if (state.isAir()) return false;
        Block b = state.getBlock();
        return b == Blocks.GRASS_BLOCK ||
               b == Blocks.DIRT ||
               b == Blocks.COARSE_DIRT ||
               b == Blocks.ROOTED_DIRT ||
               b == Blocks.MUD ||
               b == Blocks.SAND ||
               b == Blocks.RED_SAND ||
               b == Blocks.GRAVEL ||
               b == Blocks.STONE ||
               b == Blocks.DEEPSLATE ||
               b == Blocks.DIORITE ||
               b == Blocks.ANDESITE ||
               b == Blocks.GRANITE ||
               b == Blocks.TUFF ||
               b == Blocks.SNOW_BLOCK ||
               b == Blocks.POWDER_SNOW ||
               b.getDescriptionId().contains("regolith") ||
               b.getDescriptionId().contains("sand") ||
               b.getDescriptionId().contains("rock") ||
               b.getDescriptionId().contains("stone");
    }
}
