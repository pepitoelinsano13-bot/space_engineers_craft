package com.spaceengineers.craft.item;

import com.spaceengineers.craft.block.ShipCoreBlock;
import com.spaceengineers.craft.grid.ShipGridManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class WelderToolItem extends Item {

    public WelderToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof ShipCoreBlock) {
            if (!level.isClientSide() && player != null) {
                // Compilar la estructura conectada en una nave física
                boolean success = ShipGridManager.assembleShipGrid(level, pos, player);
                if (success) {
                    level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0F, 1.2F);
                    level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.5F);
                    player.displayClientMessage(Component.literal("§6[Space Engineers] §a¡Rejilla de nave espacial compilada con éxito!"), true);
                    return InteractionResult.SUCCESS;
                } else {
                    player.displayClientMessage(Component.literal("§c[Space Engineers] §7Error al compilar la nave: estructura vacía o demasiado grande."), true);
                    return InteractionResult.FAIL;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        } else {
            // Inspección de integridad de bloque y rejilla
            if (level.isClientSide() && player != null) {
                float hardness = state.getDestroySpeed(level, pos);
                player.displayClientMessage(Component.literal("§b[Soldador] §7Componente: §f" + state.getBlock().getName().getString() + " §7| Integridad: §a100% §7| Dureza: §e" + hardness), true);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal("§6[Herramienta de Construcción]"));
        tooltipComponents.add(Component.literal("§7• Clic derecho en §eShip Core§7: Compila la estructura en una nave física."));
        tooltipComponents.add(Component.literal("§7• Clic derecho en bloques: Muestra integridad y estado de la rejilla."));
    }
}
