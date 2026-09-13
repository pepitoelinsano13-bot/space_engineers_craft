package com.spaceengineers.craft.client;

import com.spaceengineers.craft.entity.PhysicalShipEntity;
import com.spaceengineers.craft.orbital.SpaceDimensionEvents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class CockpitHudOverlay implements LayeredDraw.Layer {

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.options.hideGui) return;

        Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof PhysicalShipEntity ship)) return;

        Font font = mc.font;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        // 1. Center Reticle & Artificial Horizon
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        guiGraphics.fill(centerX - 15, centerY, centerX - 5, centerY + 1, 0xAA00FFFF);
        guiGraphics.fill(centerX + 5, centerY, centerX + 15, centerY + 1, 0xAA00FFFF);
        guiGraphics.fill(centerX, centerY - 15, centerX + 1, centerY - 5, 0xAA00FFFF);
        guiGraphics.fill(centerX, centerY + 5, centerX + 1, centerY + 15, 0xAA00FFFF);

        // 2. Telemetry Panel (Left)
        Vec3 vel = ship.getDeltaMovement();
        double speedMps = vel.length() * 20.0D;
        double altitude = ship.getY();
        boolean inSpace = SpaceDimensionEvents.isSpaceVacuum(ship);

        int leftX = 16;
        int topY = screenHeight - 110;

        guiGraphics.fill(leftX - 4, topY - 4, leftX + 140, topY + 95, 0x900B132B);
        guiGraphics.renderOutline(leftX - 4, topY - 4, 144, 99, 0xFF48CAE4);

        guiGraphics.drawString(font, "§6§l[NAV TELEMETRY]", leftX, topY, 0xFFFFFF);
        guiGraphics.drawString(font, String.format("§7Velocidad: §a%.1f m/s", speedMps), leftX, topY + 14, 0xFFFFFF);
        guiGraphics.drawString(font, String.format("§7Altitud Y: §e%.0f m", altitude), leftX, topY + 26, 0xFFFFFF);
        guiGraphics.drawString(font, String.format("§7Inclinación: §f%.1f°", ship.getXRot()), leftX, topY + 38, 0xFFFFFF);
        guiGraphics.drawString(font, String.format("§7Rumbo: §f%.1f°", ship.getYRot()), leftX, topY + 50, 0xFFFFFF);
        guiGraphics.drawString(font, inSpace ? "§bAmbiente: §3VACÍO / ÓRBITA" : "§aAmbiente: §2ATMÓSFERA", leftX, topY + 62, 0xFFFFFF);
        guiGraphics.drawString(font, inSpace ? "§7Amortiguador: §aINERCIA 0G" : "§7Fricción: §eATMOSFÉRICA", leftX, topY + 74, 0xFFFFFF);

        // 3. Ship Systems Panel (Right)
        int rightX = screenWidth - 156;
        guiGraphics.fill(rightX - 4, topY - 4, rightX + 140, topY + 95, 0x900B132B);
        guiGraphics.renderOutline(rightX - 4, topY - 4, 144, 99, 0xFF48CAE4);

        guiGraphics.drawString(font, "§3§l[ESTADO DE NAVE]", rightX, topY, 0xFFFFFF);
        guiGraphics.drawString(font, String.format("§7Masa Total: §f%.0f kg", ship.getTotalMass()), rightX, topY + 14, 0xFFFFFF);
        guiGraphics.drawString(font, String.format("§7Empuje Fwd: §b%.0f kN", ship.getForwardThrust()), rightX, topY + 26, 0xFFFFFF);
        guiGraphics.drawString(font, String.format("§7Empuje Vert: §b%.0f kN", ship.getVerticalThrust()), rightX, topY + 38, 0xFFFFFF);
        guiGraphics.drawString(font, String.format("§7Giroscopios: §e%d activos", ship.getGyroCount()), rightX, topY + 50, 0xFFFFFF);
        guiGraphics.drawString(font, String.format("§7Bloques: §a%d partes", ship.getShipBlocks().size()), rightX, topY + 62, 0xFFFFFF);
        guiGraphics.drawString(font, "§c[Shift+Click] Desacoplar", rightX, topY + 74, 0xFFFFFF);

        // 4. Pilot Controls Guide at top center
        String controlsText = "§7[W/S] Acelerar | [A/D] Guiñada | [Espacio/Shift] Elevación";
        int textWidth = font.width(controlsText);
        guiGraphics.drawString(font, controlsText, centerX - textWidth / 2, 12, 0xFFFFFF);
    }
}
