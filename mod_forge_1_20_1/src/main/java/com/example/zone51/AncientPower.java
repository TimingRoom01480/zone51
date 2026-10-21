package com.example.zone51;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;

/** Способность «Древняя Сила»: энергия, выбор и применение способностей, проклятие. */
public final class AncientPower {
    public static final String HAS = "zone51_ap", ENERGY = "zone51_ap_energy", SEL = "zone51_ap_sel",
            CURSE = "zone51_ap_curse", STONE = "zone51_stone_defeated", FLY = "zone51_ap_fly",
            STRIKE = "zone51_ap_strike", CD = "zone51_ap_cd", SHIELD_CD = "zone51_shield_cd", OFF = "zone51_ap_off";
    public static final String[] KEEP = {HAS, OFF, ENERGY, SEL, STONE, "zone51_tree_flight", "zone51_tree_attack", "zone51_tree_defense"};
    public static final int MAX = 100;

    public enum Ability {
        FLIGHT(0), FIREBALL(10), SHIELD(20), TELEPORT(15), STRIKE(10),
        STORM(40), EXPLOSION(35), SWORD(25), HEAL(30), BEAM(20),
        SPEED(20), LEAP(10), SLAM(25), PHASE(30), NIGHT(10),
        PULL(20), WAVE(15), FREEZE(25), STUN(25), CHAIN(30),
        GUARDIAN(40), BERSERK(30), BARRAGE(35), METEOR(60), ABSORB(25);
        public final int cost;
        Ability(int cost) { this.cost = cost; }
        public Component label() { return Component.translatable("ability.zone51." + name().toLowerCase()); }
    }

    /** Сила выдана и сейчас включена (её можно выключать и включать у алтаря сколько угодно раз). */
    public static boolean has(Player p) {
        CompoundTag d = p.getPersistentData();
        return d.getBoolean(HAS) && !d.getBoolean(OFF);
    }

    public static Ability fromName(String n) {
        for (Ability a : Ability.values()) if (a.name().equalsIgnoreCase(n)) return a;
        return null;
    }

    public static void setFlight(ServerPlayer p, boolean on) {
        if (!has(p)) return;
        if (p.getPersistentData().getBoolean(FLY) != on) toggleFlight(p);
    }

    /** Если установлен Palladium: выдаём или забираем его способность «Древняя Сила» (значки справа внизу). */
    public static void syncPalladium(ServerPlayer p) {
        if (!net.minecraftforge.fml.ModList.get().isLoaded("palladium") || p.getServer() == null) return;
        var server = p.getServer();
        String cmd = "execute as " + p.getUUID() + " run superpower " + (has(p) ? "add" : "remove") + " zone51:ancient_power";
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput(), cmd);
    }

    public static int energy(Player p) {
        CompoundTag t = p.getPersistentData();
        return t.contains(ENERGY) ? t.getInt(ENERGY) : MAX;
    }

    public static void setEnergy(Player p, int v) { p.getPersistentData().putInt(ENERGY, Mth.clamp(v, 0, MAX)); }

    public static Ability selected(Player p) {
        Ability[] a = Ability.values();
        return a[Math.floorMod(p.getPersistentData().getInt(SEL), a.length)];
    }

    private static void msg(Player p, Component c) { p.displayClientMessage(c, true); }

    public static boolean holdingStaff(Player p) { return p.getMainHandItem().is(ModItems.ANCIENT_STAFF.get()); }

    public static void grant(ServerPlayer p) {
        p.getPersistentData().putBoolean(HAS, true);
        p.getPersistentData().putBoolean(OFF, false);
        setEnergy(p, MAX);
        syncPalladium(p);
        msg(p, Component.translatable("message.zone51.power_granted"));
    }

    /**
     * Алтарь работает бесконечно:
     * ПКМ = полная перезарядка и снятие проклятия;
     * Shift+ПКМ пустой рукой = включить/выключить Древнюю Силу;
     * Shift+ПКМ с Каменным ядром = прокачать ветку выбранной способности (ядро не тратится).
     */
    public static void altar(ServerPlayer p) {
        CompoundTag d = p.getPersistentData();
        if (!d.getBoolean(HAS)) {
            if (d.getBoolean(STONE)) grant(p);
            else msg(p, Component.translatable("message.zone51.altar_silent"));
            return;
        }
        if (p.isShiftKeyDown()) {
            if (p.getMainHandItem().is(ModItems.STONE_CORE.get())) { Skills.sync(p, true); return; }
            boolean off = !d.getBoolean(OFF);
            d.putBoolean(OFF, off);
            if (off) disableFlight(p);
            syncPalladium(p);
            msg(p, Component.translatable(off ? "message.zone51.power_off" : "message.zone51.power_on"));
            return;
        }
        d.putInt(CURSE, 0);
        setEnergy(p, MAX);
        // заряжаем всё остальное: костюм, Сердце БОСС51 и лазерную пушку
        ItemStack chest = Suits.chest(p);
        if (chest.getItem() instanceof SuitItem si && si.suit.energy) Energy.add(chest, Suits.MAX_ENERGY, Suits.MAX_ENERGY);
        for (ItemStack st : p.getInventory().items) {
            if (st.getItem() instanceof Boss51HeartItem) Energy.add(st, Boss51HeartItem.MAX, Boss51HeartItem.MAX);
            else if (st.getItem() instanceof LaserGunItem) Energy.add(st, LaserGunItem.MAX, LaserGunItem.MAX);
        }
        msg(p, Component.translatable("message.zone51.altar_recharged"));
    }

    // ---- Древо способностей: три ветки по 3 уровня, прокачка у алтаря (Shift+ПКМ, 3 уровня опыта) ----
    public static String branchOf(Ability a) {
        return switch (a) {
            case FLIGHT -> "flight";
            case SHIELD, TELEPORT, HEAL, SPEED, LEAP, PHASE, NIGHT, GUARDIAN, ABSORB -> "defense";
            default -> "attack";
        };
    }

    public static int lvl(Player p, String branch) { return p.getPersistentData().getInt("zone51_tree_" + branch); }

    private static void upgrade(ServerPlayer p) {
        String b = branchOf(selected(p));
        int l = lvl(p, b);
        Component name = Component.translatable("tree.zone51." + b);
        if (l >= 3) { msg(p, Component.translatable("message.zone51.tree_max", name)); return; }
        if (!p.isCreative() && p.experienceLevel < 3) { msg(p, Component.translatable("message.zone51.tree_xp")); return; }
        if (!p.isCreative()) p.giveExperienceLevels(-3);
        p.getPersistentData().putInt("zone51_tree_" + b, l + 1);
        msg(p, Component.translatable("message.zone51.tree_up", name, l + 1));
    }

    public static void next(ServerPlayer p) {
        if (!has(p)) { msg(p, Component.translatable("message.zone51.no_power")); return; }
        CompoundTag d = p.getPersistentData();
        d.putInt(SEL, (d.getInt(SEL) + 1) % Ability.values().length);
        msg(p, Component.translatable("message.zone51.ability", selected(p).label(), energy(p)));
    }

    private static void disableFlight(ServerPlayer p) {
        p.getPersistentData().putBoolean(FLY, false);
        if (!p.isCreative() && !p.isSpectator()) {
            p.getAbilities().mayfly = false;
            p.getAbilities().flying = false;
        }
        p.onUpdateAbilities();
    }

    private static void toggleFlight(ServerPlayer p) {
        CompoundTag d = p.getPersistentData();
        if (d.getBoolean(FLY)) {
            disableFlight(p);
        } else {
            d.putBoolean(FLY, true);
            p.getAbilities().mayfly = true;
            p.onUpdateAbilities();
        }
        msg(p, Component.translatable(d.getBoolean(FLY) ? "message.zone51.flight_on" : "message.zone51.flight_off"));
    }

    public static void use(ServerPlayer p) {
        useAbility(p, selected(p));
    }

    public static void useAbility(ServerPlayer p, Ability a) {
        if (!has(p)) { msg(p, Component.translatable("message.zone51.no_power")); return; }
        long t = p.level().getGameTime();
        CompoundTag d = p.getPersistentData();
        if (t < d.getLong(CD)) return;
        d.putLong(CD, t + 8);

        boolean staff = holdingStaff(p);
        if (a == Ability.FLIGHT) { toggleFlight(p); return; }

        int lv = Skills.level(p, "ap_" + a.name().toLowerCase());
        if (lv < 1) { msg(p, Component.translatable("message.zone51.skill_locked")); return; }
        int cost = staff ? Math.max(1, a.cost * 3 / 4) : a.cost;
        cost = Math.max(1, (int) (cost * (1 - 0.06 * (lv - 1))));
        if (energy(p) < cost) { msg(p, Component.translatable("message.zone51.no_energy")); return; }

        Vec3 look = p.getLookAngle();
        int atk = lv - 1, def = lv - 1;
        switch (a) {
            case FIREBALL -> Abilities.fireball(p, look, (staff ? 2 : 1) + (atk >= 2 ? 1 : 0));
            case SHIELD -> {
                p.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 400, (staff ? 3 : 1) + def / 2));
                p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, staff ? 1 : 0));
            }
            case STRIKE -> d.putLong(STRIKE, t + 200);
            case STORM -> Abilities.storm(p, 8, 6, (staff ? 12f : 8f) * (1 + 0.25f * atk));
            case TELEPORT -> Abilities.teleportDash(p, 12 + 2 * def);
            case EXPLOSION -> Abilities.energyExplosion(p, 5, (staff ? 18f : 12f) * (1 + 0.25f * atk));
            case SWORD -> {
                ItemStack s = new ItemStack(ModItems.ENERGY_SWORD.get());
                s.getOrCreateTag().putLong("zone51_expire", t + 300);
                ItemHandlerHelper.giveItemToPlayer(p, s);
            }
            case HEAL -> p.heal(8f + 2f * def);
            default -> AncientArts.cast(p, a, staff, atk, def);
        }
        setEnergy(p, energy(p) - cost);

        // Проклятие силы: чем чаще пользуешься, тем сильнее дебаффы, пока не перезарядишься у алтаря
        int curse = d.getInt(CURSE) + 1;
        d.putInt(CURSE, curse);
        if (curse >= 5) p.addEffect(new MobEffectInstance(MobEffects.HUNGER, 300, curse / 5 - 1));
        if (curse >= 10) p.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 300, Math.min(2, curse / 10 - 1)));
        msg(p, Component.translatable("message.zone51.ability", a.label(), energy(p)));
    }

    public static void tick(ServerPlayer p, long t) {
        if (t % 100 == 0 && p.getPersistentData().getBoolean(HAS)) syncPalladium(p);
        if (t % 20 != 0 || !has(p)) return;
        CompoundTag d = p.getPersistentData();
        if (energy(p) < MAX) setEnergy(p, energy(p) + 2);
        if (d.getBoolean(FLY)) {
            if (!p.getAbilities().mayfly) { p.getAbilities().mayfly = true; p.onUpdateAbilities(); }
            if (p.getAbilities().flying) {
                int c = Math.max(1, 3 - lvl(p, "flight") - (holdingStaff(p) ? 1 : 0));
                if (energy(p) < c) {
                    disableFlight(p);
                    msg(p, Component.translatable("message.zone51.no_energy"));
                } else {
                    setEnergy(p, energy(p) - c);
                }
            }
        }
    }
}
