package com.example.zone51;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

/** Две шкалы над здоровьем: Древняя Сила (голубая) и энергия костюма (оранжевая). */
public final class EnergyHud {
    public static final IGuiOverlay OVERLAY = (gui, g, partialTick, w, h) -> {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || (!ClientEnergy.hasAp && !ClientEnergy.hasSuit)) return;
        int x = w / 2 - 91;
        int y = h - 64;
        if (ClientEnergy.hasAp) {
            bar(g, mc, x, y, ClientEnergy.ap, AncientPower.MAX, 0xFF55EEFF, Component.translatable("hud.zone51.ancient").getString());
            y += 10;
        }
        if (ClientEnergy.hasSuit) {
            bar(g, mc, x, y, ClientEnergy.suit, Suits.MAX_ENERGY, 0xFFFF9A28, Component.translatable("hud.zone51.suit").getString());
        }
    };

    private static void bar(GuiGraphics g, Minecraft mc, int x, int y, int v, int max, int color, String label) {
        g.fill(x - 1, y - 1, x + 82, y + 7, 0xAA000000);
        g.fill(x, y, x + 81 * Math.min(v, max) / Math.max(1, max), y + 6, color);
        g.drawString(mc.font, label + " " + v + "/" + max, x + 86, y - 1, 0xFFFFFF, true);
    }
}
