package com.example.zone51;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Новые способности Древней Силы (страницы 3-5): движение, контроль и призыв. */
public final class AncientArts {
    private static final String SLAM = "zone51_slam_until";

    private static void fx(ServerPlayer p, net.minecraft.world.effect.MobEffect e, int amp, int ticks) {
        p.addEffect(new MobEffectInstance(e, ticks, amp, false, true));
    }

    private static List<LivingEntity> enemies(ServerPlayer p, double r) {
        return p.level().getEntitiesOfClass(LivingEntity.class, p.getBoundingBox().inflate(r), e -> Abilities.isEnemy(p, e));
    }

    public static void cast(ServerPlayer p, AncientPower.Ability a, boolean staff, int atk, int def) {
        Level l = p.level();
        Vec3 look = p.getLookAngle();
        float dm = (1.0f + 0.25f * atk) * (staff ? 1.3f : 1.0f);
        switch (a) {
            case BEAM -> Abilities.laser(p, look, 60, 14f * dm);
            case SPEED -> { fx(p, MobEffects.MOVEMENT_SPEED, 2 + def / 2, 300); fx(p, MobEffects.DIG_SPEED, 1, 300); }
            case LEAP -> {
                p.setDeltaMovement(look.x * 0.7, 1.5, look.z * 0.7);
                p.hurtMarked = true;
                fx(p, MobEffects.SLOW_FALLING, 0, 120);
            }
            case SLAM -> {
                p.setDeltaMovement(0, -2.6, 0);
                p.hurtMarked = true;
                p.getPersistentData().putLong(SLAM, l.getGameTime() + 80);
            }
            case PHASE -> { fx(p, MobEffects.INVISIBILITY, 0, 100); fx(p, MobEffects.DAMAGE_RESISTANCE, 1, 100); }
            case NIGHT -> {
                fx(p, MobEffects.NIGHT_VISION, 0, 900);
                for (LivingEntity e : enemies(p, 24)) e.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0, true, false));
            }
            case PULL -> {
                for (LivingEntity e : enemies(p, 12)) {
                    Vec3 to = p.position().subtract(e.position()).normalize().scale(0.9);
                    e.setDeltaMovement(to.x, 0.25, to.z);
                    e.hurtMarked = true;
                }
            }
            case WAVE -> Abilities.quake(p, 8, 5f * dm);
            case FREEZE -> {
                for (LivingEntity e : enemies(p, 8)) {
                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3));
                    e.setTicksFrozen(e.getTicksRequiredToFreeze() + 100);
                    e.hurt(p.damageSources().playerAttack(p), 3f * dm);
                }
            }
            case STUN -> {
                for (LivingEntity e : enemies(p, 7)) {
                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 6));
                    e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
                }
            }
            case CHAIN -> chain(p, look, 5, 8f * dm);
            case GUARDIAN -> {
                IronGolem g = EntityType.IRON_GOLEM.create(l);
                if (g != null) {
                    g.moveTo(p.getX() + look.x * 2, p.getY(), p.getZ() + look.z * 2, p.getYRot(), 0f);
                    g.setPlayerCreated(true);
                    g.setCustomName(Component.translatable("entity.zone51.guardian"));
                    g.getPersistentData().putLong("zone51_expire", l.getGameTime() + 600);
                    l.addFreshEntity(g);
                }
            }
            case BERSERK -> {
                fx(p, MobEffects.DAMAGE_BOOST, 2, 200);
                fx(p, MobEffects.MOVEMENT_SPEED, 1, 200);
                fx(p, MobEffects.HUNGER, 1, 200);
            }
            case BARRAGE -> {
                for (int i = -2; i <= 2; i++) Abilities.fireball(p, look.yRot((float) (i * 0.18)), 1);
            }
            case METEOR -> meteors(p, look);
            case ABSORB -> {
                fx(p, MobEffects.ABSORPTION, 3, 200);
                fx(p, MobEffects.REGENERATION, 1, 200);
                fx(p, MobEffects.DAMAGE_RESISTANCE, 0, 200);
            }
            default -> { }
        }
    }

    private static void chain(ServerPlayer p, Vec3 look, int jumps, float dmg) {
        Level l = p.level();
        LivingEntity cur = null;
        double best = 1e9;
        AABB box = p.getBoundingBox().expandTowards(look.scale(16)).inflate(2);
        for (LivingEntity e : l.getEntitiesOfClass(LivingEntity.class, box, x -> Abilities.isEnemy(p, x))) {
            Vec3 to = e.getEyePosition().subtract(p.getEyePosition());
            if (to.normalize().dot(look) > 0.8 && to.length() < best) { best = to.length(); cur = e; }
        }
        Set<LivingEntity> hit = new HashSet<>();
        for (int i = 0; i < jumps && cur != null; i++) {
            hit.add(cur);
            cur.hurt(p.damageSources().playerAttack(p), dmg);
            Abilities.visualLightning(l, cur.position());
            LivingEntity next = null;
            double nb = 1e9;
            for (LivingEntity e : l.getEntitiesOfClass(LivingEntity.class, cur.getBoundingBox().inflate(6),
                    x -> Abilities.isEnemy(p, x) && !hit.contains(x))) {
                double d = e.distanceToSqr(cur);
                if (d < nb) { nb = d; next = e; }
            }
            cur = next;
        }
    }

    private static void meteors(ServerPlayer p, Vec3 look) {
        Level l = p.level();
        Vec3 s = p.getEyePosition();
        Vec3 e = s.add(look.scale(40));
        BlockHitResult h = l.clip(new ClipContext(s, e, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, p));
        Vec3 t = h.getType() == HitResult.Type.MISS ? e : h.getLocation();
        for (int i = 0; i < 8; i++) {
            double ox = (p.getRandom().nextDouble() - 0.5) * 12;
            double oz = (p.getRandom().nextDouble() - 0.5) * 12;
            LargeFireball fb = new LargeFireball(l, p, 0, -1, 0, 2);
            fb.setPos(t.x + ox, t.y + 28 + p.getRandom().nextInt(8), t.z + oz);
            l.addFreshEntity(fb);
        }
    }

    /** Каждый тик: приземление после «Удара о землю». */
    public static void tickFast(ServerPlayer p, long t) {
        long until = p.getPersistentData().getLong(SLAM);
        if (until <= 0) return;
        if (p.onGround() && t < until - 3) {
            Abilities.quake(p, 7, 16f);
            p.getPersistentData().putLong(SLAM, 0);
        } else if (t >= until) {
            p.getPersistentData().putLong(SLAM, 0);
        }
    }
}
