package com.spaceengineers.craft.item;

import com.spaceengineers.craft.orbital.SpaceDimensionEvents;
import com.spaceengineers.craft.registry.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class SpacesuitItem extends ArmorItem {

    public SpacesuitItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (!level.isClientSide() && entity instanceof Player player) {
            // Check if player is wearing full spacesuit
            if (isWearingFullSuit(player)) {
                // If in space vacuum and not pressurized
                if (SpaceDimensionEvents.isSpaceVacuum(player)) {
                    // Check if player needs oxygen
                    if (player.getAirSupply() < player.getMaxAirSupply()) {
                        // Consume oxygen from bottle if available
                        consumeOxygenFromInventory(player);
                        player.setAirSupply(player.getMaxAirSupply());
                    }
                }
            }
        }
    }

    public static boolean isWearingFullSuit(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof SpacesuitItem &&
               player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof SpacesuitItem &&
               player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof SpacesuitItem &&
               player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof SpacesuitItem;
    }

    private static void consumeOxygenFromInventory(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(ModItems.OXYGEN_BOTTLE.get())) {
                stack.shrink(1);
                break;
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal("§b[Soporte Vital EVA] §7Protege contra el vacío espacial y temperaturas extremas."));
        tooltipComponents.add(Component.literal("§7» Suministra oxígeno automático si llevas Botellas de Oxígeno."));
    }
}
