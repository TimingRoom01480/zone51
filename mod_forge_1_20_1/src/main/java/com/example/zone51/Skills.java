package com.example.zone51;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Древо способностей: каждая способность прокачивается до 5 уровня за уровни опыта.
 * Первые способности веток открыты, остальные нужно изучить после родительской.
 * Цена следующего уровня = номер уровня * глубина узла в древе.
 */
public final class Skills {
    public record Node(String id, String tab, String nameKey, String parent, int depth, boolean starter) { }

    public static final int MAX = 5;
    public static final String PREFIX = "zone51_sk_";
    public static final String[] TABS = {"ancient", "guard", "lab", "exo", "mutant", "ancient_man", "cardsharp", "heart"};
    public static final List<Node> ALL = new ArrayList<>();
    public static final Map<String, Node> BY_ID = new HashMap<>();

    private static void add(String id, String tab, String key, String parent) {
        Node pn = parent == null ? null : BY_ID.get(parent);
        Node n = new Node(id, tab, key, parent, pn == null ? 1 : pn.depth() + 1, parent == null);
        ALL.add(n);
        BY_ID.put(id, n);
    }

    static {
        String[][] ap = {{"flight", null}, {"fireball", null}, {"shield", null}, {"teleport", null}, {"strike", "fireball"}, {"beam", "fireball"},
                {"barrage", "beam"}, {"storm", "strike"}, {"chain", "storm"}, {"explosion", "storm"}, {"meteor", "explosion"}, {"sword", "strike"},
                {"heal", "shield"}, {"absorb", "heal"}, {"guardian", "absorb"}, {"phase", "shield"}, {"speed", "teleport"}, {"leap", "speed"},
                {"slam", "leap"}, {"night", "speed"}, {"pull", "teleport"}, {"wave", "pull"}, {"freeze", "wave"}, {"stun", "freeze"}, {"berserk", "strike"}};
        for (String[] r : ap) add("ap_" + r[0], "ancient", "ability.zone51." + r[0], r[1] == null ? null : "ap_" + r[1]);
        for (SuitType s : SuitType.values()) {
            String prev = null;
            for (SuitAbility a : SuitAbility.values()) {
                if (a.suit != s) continue;
                add("su_" + a.id(), s.id, "suitability.zone51." + a.id(), prev);
                prev = "su_" + a.id();
            }
        }
        String[][] heart = {{"laser", null}, {"punch", "laser"}, {"drones", "punch"}, {"quake", "punch"}, {"teleport", "laser"}, {"storm", "quake"},
                {"charge", "teleport"}, {"barrier", "drones"}, {"roar", "quake"}, {"regen", "barrier"}};
        for (String[] r : heart) add("heart_" + r[0], "heart", "heart.zone51." + r[0], r[1] == null ? null : "heart_" + r[1]);
    }

    public static List<Node> byTab(String tab) {
        List<Node> out = new ArrayList<>();
        for (Node n : ALL) if (n.tab().equals(tab)) out.add(n);
        return out;
    }

    public static int level(Player p, String id) {
        Node n = BY_ID.get(id);
        int v = p.getPersistentData().getInt(PREFIX + id);
        return (n != null && n.starter()) ? Math.max(1, v) : v;
    }

    public static int cost(Node n, int nextLevel) {
        return (n.starter() && nextLevel == 1) ? 0 : nextLevel * n.depth();
    }

    /** Возвращает ключ сообщения. */
    public static String upgrade(ServerPlayer p, String id) {
        Node n = BY_ID.get(id);
        if (n == null) return null;
        int lv = level(p, id);
        if (lv >= MAX) return "message.zone51.skill_max";
        if (n.parent() != null && level(p, n.parent()) < 1) return "message.zone51.skill_parent";
        int cost = cost(n, lv + 1);
        if (!p.isCreative() && p.experienceLevel < cost) return "message.zone51.skill_xp";
        if (!p.isCreative()) p.giveExperienceLevels(-cost);
        p.getPersistentData().putInt(PREFIX + id, lv + 1);
        return "message.zone51.skill_up";
    }

    public static void sync(ServerPlayer p, boolean open) {
        Map<String, Integer> m = new LinkedHashMap<>();
        for (Node n : ALL) m.put(n.id(), level(p, n.id()));
        ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> p), new SkillSyncPacket(open, m));
    }
}
