package com.spaceengineers.craft.client;

import com.spaceengineers.craft.menu.SatelliteTerminalMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SatelliteTerminalScreen extends AbstractContainerScreen<SatelliteTerminalMenu> {
    private float radarAngle = 0.0F;

    public SatelliteTerminalScreen(SatelliteTerminalMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 220;
        this.imageHeight = 220;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 10;
        this.titleLabelY = 8;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int left = this.leftPos;
        int top = this.topPos;

        // Terminal dark frame
        guiGraphics.fill(left, top, left + this.imageWidth, top + this.imageHeight, 0xEE0B1015);
        guiGraphics.fill(left + 2, top + 2, left + this.imageWidth - 2, top + this.imageHeight - 2, 0xDD121B24);

        // Terminal header border
        guiGraphics.fill(left + 5, top + 22, left + this.imageWidth - 5, top + 24, 0xFF00AAFF);

        // Orbital Radar Display Area
        int radarCenterX = left + 60;
        int radarCenterY = top + 80;
        int radius = 45;

        // Radar background circle
        guiGraphics.fill(radarCenterX - radius, radarCenterY - radius, radarCenterX + radius, radarCenterY + radius, 0x88002233);

        // Radar sweep line
        this.radarAngle += 2.0F;
        if (this.radarAngle >= 360.0F) {
            this.radarAngle = 0.0F;
        }
        double rad = Math.toRadians(this.radarAngle);
        int sweepX = radarCenterX + (int) (Math.cos(rad) * radius);
        int sweepY = radarCenterY + (int) (Math.sin(rad) * radius);
        guiGraphics.fill(radarCenterX, radarCenterY, sweepX, sweepY, 0xAA00FF88);

        // Center crosshair
        guiGraphics.fill(radarCenterX - radius, radarCenterY, radarCenterX + radius, radarCenterY + 1, 0x4400FFFF);
        guiGraphics.fill(radarCenterX, radarCenterY - radius, radarCenterX, radarCenterY + radius, 0x4400FFFF);

        // Orbit blips (satellites)
        guiGraphics.fill(radarCenterX + 15, radarCenterY - 20, radarCenterX + 18, radarCenterY - 17, 0xFFFFCC00);
        guiGraphics.fill(radarCenterX - 25, radarCenterY + 10, radarCenterX - 22, radarCenterY + 13, 0xFF00FFCC);

        // Telemetry details panel (Right side)
        int panelX = left + 115;
        int panelY = top + 35;
        guiGraphics.drawString(this.font, "§b[RED DE SATÉLITES]", panelX, panelY, 0x00FFFF, false);
        guiGraphics.drawString(this.font, "§7Órbita: §aLEO-400km", panelX, panelY + 14, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, "§7Enlace: §2100% ACTIVO", panelX, panelY + 26, 0x55FF55, false);
        guiGraphics.drawString(this.font, "§7Energía: §e98.4 kW", panelX, panelY + 38, 0xFFFF00, false);
        guiGraphics.drawString(this.font, "§7Cuerpos en rango:", panelX, panelY + 52, 0xAAAAAA, false);
        guiGraphics.drawString(this.font, "§f• Luna (0.16G)", panelX, panelY + 64, 0xDDDDDD, false);
        guiGraphics.drawString(this.font, "§c• Ares (0.38G)", panelX, panelY + 76, 0xFF8888, false);
        guiGraphics.drawString(this.font, "§6• Asteroides", panelX, panelY + 88, 0xFFAA00, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
