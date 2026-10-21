package com.example.zone51;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

/** Окно древа способностей: вкладки слева, способности справа, прокачка за уровни опыта. */
public class SkillTreeScreen extends Screen {
    private static final int ROWS = 6;
    private String tab = "ancient";
    private int page = 0;
    private int seen = -1;

    public SkillTreeScreen() {
        super(Component.translatable("screen.zone51.skills"));
    }

    @Override
    protected void init() { rebuild(); }

    @Override
    public void tick() {
        if (seen != ClientSkills.version) rebuild();
    }

    @Override
    public boolean isPauseScreen() { return false; }

    private void rebuild() {
        seen = ClientSkills.version;
        clearWidgets();
        int i = 0;
        for (String t : Skills.TABS) {
            final String tt = t;
            Button b = Button.builder(Component.translatable("tab.zone51." + t), x -> { tab = tt; page = 0; rebuild(); }).bounds(8, 28 + i * 22, 112, 20).build();
            b.active = !t.equals(tab);
            addRenderableWidget(b);
            i++;
        }
        List<Skills.Node> nodes = Skills.byTab(tab);
        int pages = Math.max(1, (nodes.size() + ROWS - 1) / ROWS);
        page = Math.min(page, pages - 1);
        var player = minecraft.player;
        for (int r = 0; r < ROWS; r++) {
            int idx = page * ROWS + r;
            if (idx >= nodes.size()) break;
            Skills.Node n = nodes.get(idx);
            int lv = ClientSkills.level(n.id());
            boolean parentOk = n.parent() == null || ClientSkills.level(n.parent()) >= 1;
            int cost = Skills.cost(n, lv + 1);
            boolean xp = player.isCreative() || player.experienceLevel >= cost;
            String key = lv >= Skills.MAX ? "screen.zone51.max" : (lv == 0 ? "screen.zone51.learn" : "screen.zone51.upgrade");
            Button b = Button.builder(Component.translatable(key), x -> ModNetwork.CHANNEL.sendToServer(new UpgradePacket(n.id())))
                    .bounds(width - 128, 44 + r * 34, 112, 20).build();
            b.active = lv < Skills.MAX && parentOk && xp;
            addRenderableWidget(b);
        }
        final int fp = pages;
        Button prev = Button.builder(Component.literal("<"), x -> { page = Math.max(0, page - 1); rebuild(); }).bounds(130, height - 28, 30, 20).build();
        Button next = Button.builder(Component.literal(">"), x -> { page = Math.min(fp - 1, page + 1); rebuild(); }).bounds(164, height - 28, 30, 20).build();
        prev.active = page > 0;
        next.active = page < pages - 1;
        addRenderableWidget(prev);
        addRenderableWidget(next);
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float pt) {
        renderBackground(g);
        g.drawCenteredString(font, title, width / 2, 10, 0xFFFFFF);
        var player = minecraft.player;
        g.drawString(font, Component.translatable("screen.zone51.xp", player.experienceLevel), 130, height - 22 + 2, 0x80FF20, true);
        g.drawString(font, Component.translatable("screen.zone51.hint"), 200, height - 22 + 2, 0xAAAAAA, false);
        List<Skills.Node> nodes = Skills.byTab(tab);
        for (int r = 0; r < ROWS; r++) {
            int idx = page * ROWS + r;
            if (idx >= nodes.size()) break;
            Skills.Node n = nodes.get(idx);
            int lv = ClientSkills.level(n.id());
            int y = 44 + r * 34;
            boolean parentOk = n.parent() == null || ClientSkills.level(n.parent()) >= 1;
            g.fill(126, y - 4, width - 134, y + 26, 0x66000000);
            g.drawString(font, Component.translatable(n.nameKey()), 132, y, parentOk ? 0xFFFFFF : 0x777777, true);
            StringBuilder stars = new StringBuilder();
            for (int s = 1; s <= Skills.MAX; s++) stars.append(s <= lv ? "★" : "☆");
            g.drawString(font, stars.toString(), 132, y + 12, lv > 0 ? 0xFFD040 : 0x888888, false);
            String info;
            if (lv >= Skills.MAX) info = Component.translatable("screen.zone51.maxed").getString();
            else if (!parentOk) info = Component.translatable("screen.zone51.need_parent", Component.translatable(Skills.BY_ID.get(n.parent()).nameKey())).getString();
            else info = Component.translatable("screen.zone51.cost", Skills.cost(n, lv + 1)).getString();
            g.drawString(font, info, 220, y + 12, 0xA0E0FF, false);
        }
        super.render(g, mx, my, pt);
    }
}
