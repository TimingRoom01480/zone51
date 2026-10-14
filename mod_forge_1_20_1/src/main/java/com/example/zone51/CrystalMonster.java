package com.example.zone51;

import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/** Кристальный Монстр: бьёт молниями. С него падают доспехи древнего человека. */
public class CrystalMonster extends AbstractBossMonster {
    private int stormCd = 160;

    public CrystalMonster(EntityType<? extends CrystalMonster> type, Level level) {
        super(type, level, BossEvent.BossBarColor.PURPLE);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 260.0)
                .add(Attributes.ARMOR, 8.0)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.FOLLOW_RANGE, 40.0);
    }

    @Override
    public boolean doHurtTarget(Entity e) {
        boolean r = super.doHurtTarget(e);
        if (r && e instanceof LivingEntity le && random.nextFloat() < 0.3f) {
            Abilities.visualLightning(level(), le.position());
            le.hurt(damageSources().lightningBolt(), 5.0f);
        }
        return r;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (getTarget() != null && --stormCd <= 0) {
            stormCd = 160;
            Abilities.storm(this, 8, 3, 6f);
        }
    }
}
