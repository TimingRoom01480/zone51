package com.example.zone51;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

/** Бонусы полных комплектов костюмов. */
public final class Suits {
    public static final int MAX_ENERGY = 1000;
    public static final String VORTEX = "zone51_vortex_until", SUIT_CD = "zone51_suit_cd";
    private static final EquipmentSlot[] SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public static boolean full(Player p, SuitType s) {
        for (EquipmentSlot slot : SLOTS) {
            if (!(p.getItemBySlot(slot).getItem() instanceof SuitItem si) || si.suit != s) return false;
        }
        return true;
    }

    public static ItemStack chest(Player p) { return p.getItemBySlot(EquipmentSlot.CHEST); }
    public static int energy(Player p) { return Energy.get(chest(p)); }
    public static void addEnergy(Player p, int d) { Energy.add(chest(p), d, MAX_ENERGY); }

    private static void fx(Player p, MobEffect e, int amp, int dur) {
        p.addEffect(new MobEffectInstance(e, dur, amp, true, false, false));
    }

    /** Раз в 10 тиков. */
    public static void tick(ServerPlayer p, long t) {
        Level l = p.level();
        boolean lowHp = p.getHealth() < p.getMaxHealth() * 0.3f;

        if (full(p, SuitType.GUARD)) {
            fx(p, MobEffects.NIGHT_VISION, 0, 260);
            fx(p, MobEffects.MOVEMENT_SPEED, 0, 60);
            if (t % 20 == 0) { // рация: подсветка мобов
                for (Monster m : l.getEntitiesOfClass(Monster.class, p.getBoundingBox().inflate(15))) {
                    m.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40, 0, true, false));
                }
            }
        }
        if (full(p, SuitType.LAB)) {
            if (t % 80 == 0 && p.getHealth() < p.getMaxHealth()) p.heal(1.0f);
            if (t % 20 == 0) analysis(p);
        }
        if (full(p, SuitType.EXO)) {
            Energy.chargeFromOffhand(p, chest(p), MAX_ENERGY);
            if (energy(p) > 0) {
                fx(p, MobEffects.MOVEMENT_SPEED, 1, 60);
                fx(p, MobEffects.JUMP, 2, 60);
                fx(p, MobEffects.DAMAGE_RESISTANCE, 0, 60);
                if (t % 20 == 0) addEnergy(p, -1);
            }
        }
        if (full(p, SuitType.MUTANT)) {
            fx(p, MobEffects.DAMAGE_BOOST, lowHp ? 2 : 0, 60);
            fx(p, MobEffects.DAMAGE_RESISTANCE, 0, 60);
            if (lowHp) fx(p, MobEffects.DIG_SPEED, 1, 60);
            fx(p, MobEffects.HUNGER, 0, 60); // минус комплекта
        }
        if (full(p, SuitType.ANCIENT)) {
            Energy.chargeFromOffhand(p, chest(p), MAX_ENERGY);
            if (energy(p) > 0) {
                fx(p, MobEffects.NIGHT_VISION, 0, 260);
                fx(p, MobEffects.MOVEMENT_SPEED, p.isSprinting() ? 2 : 0, 60);
                fx(p, MobEffects.DAMAGE_RESISTANCE, 0, 60);
                fx(p, MobEffects.JUMP, 1, 60);
                if (t % 20 == 0) addEnergy(p, -1);
                if (t % 40 == 0) { // сканер жизни: подсветка сквозь стены
                    for (LivingEntity m : l.getEntitiesOfClass(LivingEntity.class, p.getBoundingBox().inflate(20),
                            x -> x != p && !(x instanceof Player))) {
                        m.addEffect(new MobEffectInstance(MobEffects.GLOWING, 50, 0, true, false));
                    }
                }
            }
        }
    }

    /** Каждый тик: «Карточный вихрь». */
    public static void tickFast(ServerPlayer p, long t) {
        long until = p.getPersistentData().getLong(VORTEX);
        if (t >= until || t % 5 != 0) return;
        ServerLevel sl = p.serverLevel();
        double a = t * 0.5;
        for (int i = 0; i < 6; i++) {
            double ang = a + i * Math.PI / 3;
            sl.sendParticles(ParticleTypes.CRIT, p.getX() + Math.cos(ang) * 3, p.getY() + 1, p.getZ() + Math.sin(ang) * 3, 2, 0.1, 0.3, 0.1, 0.02);
        }
        for (LivingEntity m : sl.getEntitiesOfClass(LivingEntity.class, p.getBoundingBox().inflate(4), x -> Abilities.isEnemy(p, x))) {
            m.hurt(p.damageSources().playerAttack(p), 3.0f);
        }
    }

    private static void analysis(ServerPlayer p) {
        Vec3 s = p.getEyePosition();
        Vec3 look = p.getLookAngle();
        Vec3 e = s.add(look.scale(20));
        EntityHitResult r = ProjectileUtil.getEntityHitResult(p.level(), p, s, e,
                p.getBoundingBox().expandTowards(look.scale(20)).inflate(1),
                x -> x instanceof LivingEntity && x.isAlive() && x != p);
        if (r != null && r.getEntity() instanceof LivingEntity le) {
            p.displayClientMessage(Component.literal(le.getDisplayName().getString() + ": "
                    + (int) le.getHealth() + "/" + (int) le.getMaxHealth()), true);
        }
    }

    /** Клавиша X: способность надетого костюма. */
    public static void suitAbility(ServerPlayer p) {
        SuitArts.useSelected(p);
    }
}
