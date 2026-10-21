package com.example.zone51;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

/** Реализация активных способностей всех костюмов. Уровень способности из древа даёт +20% мощности за уровень. */
public final class SuitArts {
    public static final String SEL = "zone51_suit_sel";
    private static final CardType[] POOL = {CardType.BASIC, CardType.EXPLOSIVE, CardType.FIRE, CardType.ICE, CardType.POISON, CardType.SLOW, CardType.TRAP};

    private static void fx(ServerPlayer p, MobEffect e, int amp, int ticks) { p.addEffect(new MobEffectInstance(e, ticks, amp, false, true)); }
    private static List<LivingEntity> enemies(ServerPlayer p, double r) {
        return p.level().getEntitiesOfClass(LivingEntity.class, p.getBoundingBox().inflate(r), e -> Abilities.isEnemy(p, e));
    }
    private static void particles(ServerPlayer p, net.minecraft.core.particles.ParticleOptions t, int n) {
        if (p.level() instanceof ServerLevel sl) sl.sendParticles(t, p.getX(), p.getY() + 1, p.getZ(), n, 1.2, 0.8, 1.2, 0.02);
    }
    private static LivingEntity aimTarget(ServerPlayer p, Vec3 look, double range) {
        LivingEntity best = null;
        double bd = 1e9;
        AABB box = p.getBoundingBox().expandTowards(look.scale(range)).inflate(2);
        for (LivingEntity e : p.level().getEntitiesOfClass(LivingEntity.class, box, x -> Abilities.isEnemy(p, x))) {
            Vec3 to = e.getEyePosition().subtract(p.getEyePosition());
            if (to.normalize().dot(look) > 0.85 && to.length() < bd) { bd = to.length(); best = e; }
        }
        return best;
    }
    private static Vec3 aimPoint(ServerPlayer p, Vec3 look, double range) {
        Vec3 s = p.getEyePosition(), e = s.add(look.scale(range));
        BlockHitResult h = p.level().clip(new ClipContext(s, e, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, p));
        return h.getType() == HitResult.Type.MISS ? e : h.getLocation();
    }

    /** Следующая способность надетого костюма (клавиша J без Palladium). */
    public static void next(ServerPlayer p) {
        for (SuitType s : SuitType.values()) {
            if (!Suits.full(p, s)) continue;
            CompoundTag d = p.getPersistentData();
            SuitAbility cur = SuitAbility.byId(d.getString(SEL));
            SuitAbility n = (cur == null || cur.suit != s) ? SuitAbility.first(s) : cur.next();
            d.putString(SEL, n.id());
            p.displayClientMessage(Component.translatable("message.zone51.suit_selected", Component.translatable("suitability.zone51." + n.id())), true);
            return;
        }
        p.displayClientMessage(Component.translatable("message.zone51.no_suit_ability"), true);
    }

    /** Применить выбранную способность костюма (клавиша G без Palladium). */
    public static void useSelected(ServerPlayer p) {
        for (SuitType s : SuitType.values()) {
            if (!Suits.full(p, s)) continue;
            SuitAbility cur = SuitAbility.byId(p.getPersistentData().getString(SEL));
            cast(p, (cur == null || cur.suit != s) ? SuitAbility.first(s) : cur);
            return;
        }
        p.displayClientMessage(Component.translatable("message.zone51.no_suit_ability"), true);
    }

    public static void cast(ServerPlayer p, SuitAbility a) {
        if (a == null) return;
        if (!Suits.full(p, a.suit)) { p.displayClientMessage(Component.translatable("message.zone51.need_suit"), true); return; }
        int lv = Skills.level(p, "su_" + a.id());
        if (lv < 1) { p.displayClientMessage(Component.translatable("message.zone51.skill_locked"), true); return; }
        long t = p.level().getGameTime();
        CompoundTag d = p.getPersistentData();
        if (t < d.getLong(Suits.SUIT_CD)) return;
        int cost = a.energy == 0 ? 0 : Math.max(1, (int) (a.energy * (1 - 0.06 * (lv - 1))));
        if (cost > 0) {
            if (Suits.energy(p) < cost) { p.displayClientMessage(Component.translatable("message.zone51.no_energy"), true); return; }
            Suits.addEnergy(p, -cost);
        }
        d.putLong(Suits.SUIT_CD, t + Math.min(a.cd, 40));
        float f = 1.0f + 0.2f * (lv - 1);
        Level l = p.level();
        Vec3 look = p.getLookAngle();
        int up = lv - 1;
        switch (a) {
            // ---- Охранник ----
            case GUARD_FLASH -> {
                for (LivingEntity e : enemies(p, 8)) {
                    e.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, (int) (80 * f), 0));
                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
                }
                particles(p, ParticleTypes.FLASH, 3);
            }
            case GUARD_SCAN -> {
                fx(p, MobEffects.NIGHT_VISION, 0, 600);
                for (Monster m : l.getEntitiesOfClass(Monster.class, p.getBoundingBox().inflate(30))) m.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0, true, false));
            }
            case GUARD_SMOKE -> {
                fx(p, MobEffects.INVISIBILITY, 0, (int) (100 * f));
                for (LivingEntity e : enemies(p, 6)) e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                particles(p, ParticleTypes.LARGE_SMOKE, 60);
            }
            case GUARD_TASER -> {
                LivingEntity t2 = aimTarget(p, look, 20);
                if (t2 != null) {
                    t2.hurt(p.damageSources().playerAttack(p), 6f * f);
                    t2.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 6));
                    Abilities.visualLightning(l, t2.position());
                }
            }
            case GUARD_BACKUP -> AncientArts.cast(p, AncientPower.Ability.GUARDIAN, false, up, up);
            case GUARD_SPRINT -> fx(p, MobEffects.MOVEMENT_SPEED, 3, (int) (160 * f));
            // ---- Лаборатория ----
            case LAB_ANTIDOTE -> {
                for (MobEffectInstance e : new ArrayList<>(p.getActiveEffects())) {
                    if (e.getEffect().getCategory() == MobEffectCategory.HARMFUL) p.removeEffect(e.getEffect());
                }
                p.heal(4f * f);
                fx(p, MobEffects.REGENERATION, 1, 100);
            }
            case LAB_ACID -> {
                for (LivingEntity e : enemies(p, 6)) {
                    Vec3 to = e.position().subtract(p.position()).normalize();
                    if (to.dot(new Vec3(look.x, 0, look.z).normalize()) > 0.3) {
                        e.addEffect(new MobEffectInstance(MobEffects.POISON, 120, 1));
                        e.hurt(p.damageSources().playerAttack(p), 4f * f);
                    }
                }
                particles(p, ParticleTypes.ITEM_SLIME, 30);
            }
            case LAB_PULSE -> {
                for (LivingEntity e : enemies(p, 24)) {
                    e.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0, true, false));
                    e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
                }
            }
            case LAB_STIM -> {
                fx(p, MobEffects.DAMAGE_BOOST, 1, (int) (400 * f)); fx(p, MobEffects.MOVEMENT_SPEED, 1, (int) (400 * f)); fx(p, MobEffects.DIG_SPEED, 1, (int) (400 * f));
            }
            case LAB_CRYO -> AncientArts.cast(p, AncientPower.Ability.FREEZE, false, up, up);
            case LAB_FIELD -> AncientArts.cast(p, AncientPower.Ability.WAVE, false, up, up);
            // ---- Экзоскелет ----
            case EXO_JET -> { p.setDeltaMovement(look.x * 0.8, 1.1 + 0.1 * up, look.z * 0.8); p.hurtMarked = true; }
            case EXO_ROCKET -> { p.setDeltaMovement(look.x * (2.2 + 0.2 * up), 0.3, look.z * (2.2 + 0.2 * up)); p.hurtMarked = true; }
            case EXO_MISSILES -> { for (int i = -1; i <= 1; i++) Abilities.fireball(p, look.yRot((float) (i * 0.12)), 1); }
            case EXO_SHOCK -> Abilities.quake(p, 7, 12f * f);
            case EXO_OVERCHARGE -> { fx(p, MobEffects.DAMAGE_BOOST, 1, 300); fx(p, MobEffects.MOVEMENT_SPEED, 2, 300); fx(p, MobEffects.DIG_SPEED, 2, 300); }
            case EXO_BARRIER -> { fx(p, MobEffects.ABSORPTION, 4, 200); fx(p, MobEffects.DAMAGE_RESISTANCE, 1, 100); }
            case EXO_HOVER -> { fx(p, MobEffects.LEVITATION, 0, 40); fx(p, MobEffects.SLOW_FALLING, 0, 200); }
            // ---- Мутант ----
            case MUTANT_RAGE -> { fx(p, MobEffects.DAMAGE_BOOST, 2, (int) (200 * f)); fx(p, MobEffects.DAMAGE_RESISTANCE, 1, (int) (200 * f)); }
            case MUTANT_POUND -> AncientArts.cast(p, AncientPower.Ability.SLAM, false, up, up);
            case MUTANT_HOWL -> {
                l.playSound(null, p.blockPosition(), SoundEvents.RAVAGER_ROAR, SoundSource.PLAYERS, 1.2f, 0.8f);
                for (LivingEntity e : enemies(p, 10)) {
                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
                    e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
                }
            }
            case MUTANT_REGEN -> { p.heal(10f * f); fx(p, MobEffects.REGENERATION, 2, 100); }
            case MUTANT_BOUND -> { p.setDeltaMovement(look.x * 1.2, 0.9, look.z * 1.2); p.hurtMarked = true; }
            case MUTANT_FRENZY -> {
                for (LivingEntity e : enemies(p, 4)) {
                    e.hurt(p.damageSources().playerAttack(p), 6f * f);
                    e.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0));
                }
                particles(p, ParticleTypes.SWEEP_ATTACK, 6);
            }
            // ---- Древний человек ----
            case ANCIENT_DASH -> Abilities.teleportDash(p, 8 + 2 * up);
            case ANCIENT_ELECTRO -> Abilities.storm(p, 8, 6, 8f * f);
            case ANCIENT_ARC -> AncientArts.cast(p, AncientPower.Ability.CHAIN, false, up, up);
            case ANCIENT_AEGIS -> { fx(p, MobEffects.ABSORPTION, 2 + up / 2, 200); fx(p, MobEffects.DAMAGE_RESISTANCE, 0, 200); }
            case ANCIENT_SENSE -> AncientArts.cast(p, AncientPower.Ability.NIGHT, false, up, up);
            case ANCIENT_OVERLOAD -> Abilities.energyExplosion(p, 6, 14f * f);
            // ---- Карточник ----
            case CARDSHARP_VORTEX -> d.putLong(Suits.VORTEX, t + (long) (100 * f));
            case CARDSHARP_VOLLEY -> {
                for (int i = -2; i <= 2; i++) {
                    ThrownCard c = new ThrownCard(l, p, new ItemStack(ModItems.CARDS.get(POOL[p.getRandom().nextInt(POOL.length)]).get()));
                    c.shootFromRotation(p, p.getXRot(), p.getYRot() + i * 8, 0f, 2.5f, 0.5f);
                    l.addFreshEntity(c);
                }
            }
            case CARDSHARP_TRAP -> {
                Vec3 pt = aimPoint(p, look, 20);
                for (LivingEntity e : l.getEntitiesOfClass(LivingEntity.class, new AABB(pt, pt).inflate(4), x -> Abilities.isEnemy(p, x))) {
                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 6));
                }
                if (l instanceof ServerLevel sl) sl.sendParticles(ParticleTypes.CLOUD, pt.x, pt.y + 0.3, pt.z, 40, 2, 0.1, 2, 0.01);
            }
            case CARDSHARP_JOKER -> {
                switch (p.getRandom().nextInt(3)) {
                    case 0 -> p.heal(8f * f);
                    case 1 -> { fx(p, MobEffects.DAMAGE_BOOST, 1, 300); fx(p, MobEffects.MOVEMENT_SPEED, 1, 300); }
                    default -> { for (LivingEntity e : enemies(p, 8)) e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 5)); }
                }
            }
            case CARDSHARP_SHUFFLE -> {
                for (CardType ct : CardType.values()) if (ct.consumable) ItemHandlerHelper.giveItemToPlayer(p, new ItemStack(ModItems.CARDS.get(ct).get(), 3));
            }
            case CARDSHARP_ROYAL -> { Abilities.energyExplosion(p, 8, 18f * f); particles(p, ParticleTypes.CRIT, 80); }
        }
    }
}
