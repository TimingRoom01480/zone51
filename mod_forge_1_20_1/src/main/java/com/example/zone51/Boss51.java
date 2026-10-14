package com.example.zone51;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/** БОСС51: 500 HP, три фазы. */
public class Boss51 extends AbstractBossMonster {
    private static final EntityDataAccessor<Boolean> MINI = SynchedEntityData.defineId(Boss51.class, EntityDataSerializers.BOOLEAN);
    private int attackCd = 60;
    private final List<SmallFireball> homing = new ArrayList<>();

    public Boss51(EntityType<? extends Boss51> type, Level level) {
        super(type, level, BossEvent.BossBarColor.RED);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 500.0)
                .add(Attributes.ARMOR, 12.0)
                .add(Attributes.ATTACK_DAMAGE, 14.0)
                .add(Attributes.MOVEMENT_SPEED, 0.28)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(MINI, false);
    }

    public boolean isMini() { return entityData.get(MINI); }

    public void makeMini() {
        entityData.set(MINI, true);
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(60.0);
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0);
        getAttribute(Attributes.ARMOR).setBaseValue(4.0);
        setHealth(60.0f);
        setCustomName(Component.translatable("entity.zone51.boss51_mini"));
    }

    @Override protected boolean showBar() { return !isMini(); }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return isMini() ? super.getDimensions(pose).scale(0.5f) : super.getDimensions(pose);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (MINI.equals(key)) refreshDimensions();
        super.onSyncedDataUpdated(key);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Mini", isMini());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.getBoolean("Mini")) entityData.set(MINI, true);
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return isMini() ? BuiltInLootTables.EMPTY : super.getDefaultLootTable();
    }

    private int phase() {
        float f = getHealth() / getMaxHealth();
        return f > 0.66f ? 1 : f > 0.33f ? 2 : 3;
    }

    @Override
    public boolean doHurtTarget(Entity e) { // «удар кулаком» с отбрасыванием
        boolean r = super.doHurtTarget(e);
        if (r && e instanceof LivingEntity le) {
            le.knockback(1.2, getX() - le.getX(), getZ() - le.getZ());
        }
        return r;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        int phase = isMini() ? 1 : phase();
        if (phase == 3 && tickCount % 200 == 0 && getHealth() < getMaxHealth()) {
            heal(getMaxHealth() * 0.05f); // регенерация: 5% раз в 10 секунд
        }
        LivingEntity t = getTarget();

        homing.removeIf(f -> !f.isAlive());
        if (t != null) {
            for (SmallFireball f : homing) {
                f.setDeltaMovement(t.getEyePosition().subtract(f.position()).normalize().scale(0.6));
            }
        }
        if (t == null || !t.isAlive() || --attackCd > 0) return;
        attackCd = isMini() ? 70 : phase == 1 ? 70 : phase == 2 ? 55 : 40;
        Vec3 dir = t.getEyePosition().subtract(getEyePosition()).normalize();

        if (isMini()) {
            Abilities.laser(this, dir, 25, 5f);
            return;
        }
        int r = random.nextInt(phase == 1 ? 3 : phase == 2 ? 4 : 5);
        if (phase == 1) {
            switch (r) {
                case 0 -> Abilities.laser(this, dir, 40, 8f);
                case 1 -> Abilities.quake(this, 7, 8f);
                default -> drones();
            }
        } else if (phase == 2) {
            switch (r) {
                case 0 -> Abilities.ringWave(this, 10, 10f);
                case 1 -> Abilities.teleportNear(this, t);
                case 2 -> Abilities.storm(this, 10, 5, 9f);
                default -> mutants(t);
            }
        } else {
            switch (r) {
                case 0 -> Abilities.laserStorm(this, 8f);
                case 1 -> Abilities.energyExplosion(this, 8, 16f);
                case 2 -> homingBall(t);
                case 3 -> minis(t);
                default -> Abilities.laser(this, dir, 40, 10f);
            }
        }
    }

    private void drones() {
        int have = level().getEntitiesOfClass(Vex.class, getBoundingBox().inflate(40), v -> v.getOwner() == this).size();
        if (have < 4) Abilities.summonDrones(this, 2 + random.nextInt(2));
    }

    private void mutants(LivingEntity target) {
        for (int i = 0; i < 2 + random.nextInt(2); i++) {
            Zombie z = EntityType.ZOMBIE.create(level());
            if (z == null) continue;
            z.moveTo(getX() + random.nextInt(7) - 3, getY(), getZ() + random.nextInt(7) - 3, 0f, 0f);
            z.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0);
            z.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(7.0);
            z.setHealth(40.0f);
            z.setCustomName(Component.translatable("entity.zone51.mutant"));
            z.setPersistenceRequired();
            z.setTarget(target);
            level().addFreshEntity(z);
        }
    }

    private void minis(LivingEntity target) {
        int have = level().getEntitiesOfClass(Boss51.class, getBoundingBox().inflate(40), b -> b != this && b.isMini()).size();
        if (have >= 2) return;
        Boss51 m = ModEntities.BOSS51.get().create(level());
        if (m == null) return;
        m.moveTo(getX() + random.nextInt(5) - 2, getY(), getZ() + random.nextInt(5) - 2, 0f, 0f);
        m.makeMini();
        m.setTarget(target);
        level().addFreshEntity(m);
    }

    private void homingBall(LivingEntity t) {
        Vec3 d = t.getEyePosition().subtract(getEyePosition()).normalize();
        SmallFireball f = new SmallFireball(level(), this, d.x, d.y, d.z);
        f.setPos(getX() + d.x * 2, getEyeY() + d.y, getZ() + d.z * 2);
        level().addFreshEntity(f);
        homing.add(f);
    }
}
