package com.example.zone51;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

/** Общие способности: ими пользуются и боссы, и игрок (Сердце БОСС51, Древняя Сила). */
public final class Abilities {

    /** Телепорт: для игроков через сетевое соединение, для остальных обычный. */
    public static void tp(Entity e, double x, double y, double z) {
        if (e instanceof ServerPlayer sp) {
            sp.connection.teleport(x, y, z, sp.getYRot(), sp.getXRot());
        } else {
            e.teleportTo(x, y, z);
        }
    }

    public static boolean isEnemy(LivingEntity src, LivingEntity t) {
        if (t == src || !t.isAlive()) return false;
        if (src instanceof Player) {
            return t instanceof Enemy || (t instanceof Mob m && m.getTarget() == src);
        }
        if (t instanceof Player pl) return !pl.isCreative() && !pl.isSpectator();
        return t instanceof TamableAnimal ta && ta.isTame();
    }

    public static DamageSource melee(LivingEntity src) {
        return src instanceof Player p ? src.damageSources().playerAttack(p) : src.damageSources().mobAttack(src);
    }

    public static void laser(LivingEntity src, Vec3 dir, double range, float dmg) {
        Level l = src.level();
        Vec3 s = src.getEyePosition();
        Vec3 e = s.add(dir.scale(range));
        BlockHitResult bhr = l.clip(new ClipContext(s, e, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, src));
        if (bhr.getType() != HitResult.Type.MISS) e = bhr.getLocation();
        final Vec3 end = e;
        for (LivingEntity t : l.getEntitiesOfClass(LivingEntity.class, new AABB(s, end).inflate(1.5), x -> isEnemy(src, x))) {
            Optional<Vec3> hit = t.getBoundingBox().inflate(0.4).clip(s, end);
            if (hit.isPresent()) t.hurt(src.damageSources().indirectMagic(src, src), dmg);
        }
        if (l instanceof ServerLevel sl) {
            double len = s.distanceTo(end);
            for (double d = 0; d < len; d += 0.6) {
                Vec3 p = s.add(dir.scale(d));
                sl.sendParticles(ParticleTypes.END_ROD, p.x, p.y, p.z, 1, 0, 0, 0, 0);
            }
        }
    }

    public static void laserStorm(LivingEntity src, float dmg) {
        for (int i = 0; i < 8; i++) {
            double a = i * Math.PI / 4;
            laser(src, new Vec3(Math.cos(a), -0.05, Math.sin(a)), 30, dmg);
        }
    }

    public static void quake(LivingEntity src, double radius, float dmg) {
        Level l = src.level();
        for (LivingEntity t : l.getEntitiesOfClass(LivingEntity.class, src.getBoundingBox().inflate(radius, 2, radius), x -> isEnemy(src, x))) {
            t.hurt(melee(src), dmg);
            Vec3 n = t.position().subtract(src.position()).normalize().scale(1.2);
            t.setDeltaMovement(t.getDeltaMovement().add(n.x, 0.5, n.z));
            t.hurtMarked = true;
        }
        ring(src, radius);
    }

    public static void ringWave(LivingEntity src, double radius, float dmg) {
        quake(src, radius, dmg);
    }

    private static void ring(LivingEntity src, double radius) {
        if (src.level() instanceof ServerLevel sl) {
            for (int i = 0; i < 48; i++) {
                double a = i * Math.PI / 24;
                sl.sendParticles(ParticleTypes.CLOUD, src.getX() + Math.cos(a) * radius * 0.8, src.getY() + 0.2,
                        src.getZ() + Math.sin(a) * radius * 0.8, 1, 0, 0.05, 0, 0.02);
            }
        }
    }

    public static void energyExplosion(LivingEntity src, double radius, float dmg) {
        quake(src, radius, dmg);
        if (src.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.EXPLOSION_EMITTER, src.getX(), src.getY() + 1, src.getZ(), 1, 0, 0, 0, 0);
        }
    }

    public static void visualLightning(Level l, Vec3 pos) {
        LightningBolt b = EntityType.LIGHTNING_BOLT.create(l);
        if (b == null) return;
        b.moveTo(pos);
        b.setVisualOnly(true);
        l.addFreshEntity(b);
    }

    public static void storm(LivingEntity src, double radius, int count, float dmg) {
        if (!(src.level() instanceof ServerLevel sl)) return;
        for (int i = 0; i < count; i++) {
            double x = src.getX() + (src.getRandom().nextDouble() * 2 - 1) * radius;
            double z = src.getZ() + (src.getRandom().nextDouble() * 2 - 1) * radius;
            BlockPos top = sl.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(x, src.getY(), z));
            visualLightning(sl, Vec3.atBottomCenterOf(top));
            for (LivingEntity t : sl.getEntitiesOfClass(LivingEntity.class, new AABB(top).inflate(2.5), e -> isEnemy(src, e))) {
                t.hurt(src.damageSources().lightningBolt(), dmg);
            }
        }
    }

    public static void teleportDash(LivingEntity src, double dist) {
        Vec3 s = src.getEyePosition();
        Vec3 d = src.getLookAngle();
        Vec3 e = s.add(d.scale(dist));
        BlockHitResult h = src.level().clip(new ClipContext(s, e, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, src));
        Vec3 dest = h.getType() == HitResult.Type.MISS ? e : h.getLocation().subtract(d.scale(0.6));
        tp(src, dest.x, dest.y - src.getEyeHeight(), dest.z);
        src.resetFallDistance();
    }

    public static void teleportNear(LivingEntity src, LivingEntity target) {
        double a = src.getRandom().nextDouble() * Math.PI * 2;
        tp(src, target.getX() + Math.cos(a) * 3, target.getY(), target.getZ() + Math.sin(a) * 3);
    }

    public static void fireball(LivingEntity src, Vec3 dir, int power) {
        Level l = src.level();
        LargeFireball fb = new LargeFireball(l, src, dir.x, dir.y, dir.z, power);
        fb.setPos(src.getX() + dir.x * 1.5, src.getEyeY() - 0.2 + dir.y * 1.5, src.getZ() + dir.z * 1.5);
        l.addFreshEntity(fb);
    }

    public static void punch(LivingEntity src) {
        Vec3 s = src.getEyePosition();
        Vec3 d = src.getLookAngle();
        AABB box = src.getBoundingBox().expandTowards(d.scale(4)).inflate(1);
        for (LivingEntity t : src.level().getEntitiesOfClass(LivingEntity.class, box, x -> isEnemy(src, x))) {
            Vec3 to = t.getEyePosition().subtract(s);
            if (to.length() < 5 && to.normalize().dot(d) > 0.6) {
                t.hurt(melee(src), 14.0f);
                t.knockback(1.5, -d.x, -d.z);
            }
        }
    }

    /** Союзные «дроны» для игрока: ручные волки на 30 секунд. */
    public static void summonPets(Player p, int n) {
        for (int i = 0; i < n; i++) {
            Wolf w = EntityType.WOLF.create(p.level());
            if (w == null) continue;
            w.moveTo(p.getX() + (i - 1), p.getY(), p.getZ() + 1, p.getYRot(), 0f);
            w.tame(p);
            w.setCustomName(Component.translatable("entity.zone51.drone"));
            w.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0);
            w.getPersistentData().putLong("zone51_expire", p.level().getGameTime() + 600);
            p.level().addFreshEntity(w);
        }
    }

    /** Боевые дроны босса. */
    public static void summonDrones(Mob owner, int n) {
        for (int i = 0; i < n; i++) {
            Vex v = EntityType.VEX.create(owner.level());
            if (v == null) continue;
            v.moveTo(owner.getX() + owner.getRandom().nextInt(5) - 2, owner.getY() + 2, owner.getZ() + owner.getRandom().nextInt(5) - 2, 0f, 0f);
            v.setOwner(owner);
            v.setLimitedLife(20 * 40);
            v.setCustomName(Component.translatable("entity.zone51.drone"));
            owner.level().addFreshEntity(v);
        }
    }
}
