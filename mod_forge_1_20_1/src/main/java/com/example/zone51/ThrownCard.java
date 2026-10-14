package com.example.zone51;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownCard extends ThrowableItemProjectile {
    public ThrownCard(EntityType<? extends ThrownCard> type, Level level) {
        super(type, level);
    }

    public ThrownCard(Level level, LivingEntity owner, ItemStack stack) {
        super(ModEntities.THROWN_CARD.get(), owner, level);
        ItemStack s = stack.copy();
        s.setCount(1);
        setItem(s);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.CARDS.get(CardType.BASIC).get();
    }

    private CardType type() {
        return getItem().getItem() instanceof CardItem c ? c.type : CardType.BASIC;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (type() != CardType.HEAL) {
            float dmg = (getOwner() instanceof Player pl && Suits.full(pl, SuitType.CARDSHARP)) ? 6.0f : 4.0f;
            result.getEntity().hurt(damageSources().thrown(this, getOwner()), dmg);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (level().isClientSide) return;

        Vec3 p = result.getLocation();
        Entity owner = getOwner();
        LivingEntity target = (result instanceof EntityHitResult e && e.getEntity() instanceof LivingEntity le) ? le : null;

        if (owner instanceof Player op && Suits.full(op, SuitType.CARDSHARP) && type() != CardType.EXPLOSIVE) {
            level().explode(this, p.x, p.y, p.z, 1.0f, Level.ExplosionInteraction.NONE); // костюм: карты взрываются
        }
        switch (type()) {
            case EXPLOSIVE -> level().explode(this, p.x, p.y, p.z, 2.0f, Level.ExplosionInteraction.NONE);
            case FIRE -> {
                if (target != null) target.setSecondsOnFire(6);
            }
            case ICE -> {
                if (target != null) {
                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3));
                    target.setTicksFrozen(target.getTicksRequiredToFreeze() + 60);
                }
            }
            case POISON -> {
                if (target != null) target.addEffect(new MobEffectInstance(MobEffects.POISON, 120, 1));
            }
            case SLOW -> {
                if (target != null) target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
            }
            case TRAP -> trap(p, owner);
            case SUMMON -> summon(p, owner);
            case RETURN -> {
                if (owner != null) {
                    Abilities.tp(owner, p.x, p.y, p.z);
                    owner.resetFallDistance();
                }
            }
            case HEAL -> {
                for (Player pl : level().getEntitiesOfClass(Player.class, new AABB(p, p).inflate(3.0))) {
                    pl.heal(6.0f);
                }
            }
            default -> { }
        }
        discard();
    }

    private void trap(Vec3 p, Entity owner) {
        for (LivingEntity e : level().getEntitiesOfClass(LivingEntity.class, new AABB(p, p).inflate(3.0), x -> x != owner)) {
            e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 6));
        }
        if (level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.CLOUD, p.x, p.y + 0.2, p.z, 20, 1.5, 0.1, 1.5, 0.01);
        }
    }

    private void summon(Vec3 p, Entity owner) {
        Wolf wolf = EntityType.WOLF.create(level());
        if (wolf == null) return;
        wolf.moveTo(p.x, p.y, p.z, random.nextFloat() * 360f, 0f);
        if (owner instanceof Player pl) wolf.tame(pl);
        wolf.setCustomName(Component.translatable("entity.zone51.card_ally"));
        wolf.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0);
        wolf.getPersistentData().putLong("zone51_expire", level().getGameTime() + 600); // 30 секунд
        level().addFreshEntity(wolf);
    }
}
