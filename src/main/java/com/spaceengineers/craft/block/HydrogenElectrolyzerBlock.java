package com.spaceengineers.craft.block;

import com.spaceengineers.craft.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class HydrogenElectrolyzerBlock extends Block {

    public HydrogenElectrolyzerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.WATER_BUCKET)) {
            if (!level.isClientSide) {
                if (!player.isCreative()) {
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                }
                // Give 1x Oxygen Bottle and 2x Hydrogen Bottle
                ItemStack oxygen = new ItemStack(ModItems.OXYGEN_BOTTLE.get(), 1);
                ItemStack hydrogen = new ItemStack(ModItems.HYDROGEN_BOTTLE.get(), 2);

                if (!player.getInventory().add(oxygen)) {
                    player.drop(oxygen, false);
                }
                if (!player.getInventory().add(hydrogen)) {
                    player.drop(hydrogen, false);
                }

                level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.5F);
                player.displayClientMessage(Component.literal("§9[Electrolizador de Hidrógeno] §aElectrólisis completada: §f+1 Botella de O2 §7y §6+2 Botellas de H2§7."), true);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
