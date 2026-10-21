package com.example.zone51;

import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Map;

/** Уровни способностей на клиенте (только клиент). */
public final class ClientSkills {
    public static final Map<String, Integer> LEVELS = new HashMap<>();
    public static int version = 0;

    public static void apply(Map<String, Integer> levels, boolean open) {
        LEVELS.clear();
        LEVELS.putAll(levels);
        version++;
        if (open) Minecraft.getInstance().setScreen(new SkillTreeScreen());
    }

    public static int level(String id) { return LEVELS.getOrDefault(id, 0); }
}
