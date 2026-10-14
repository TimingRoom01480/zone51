package com.example.zone51;

import net.minecraft.core.BlockPos;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Каменный Монстр: после победы появляется алтарь, где даётся «Древняя Сила». */
public class StoneMonster extends AbstractBossMonster {
    private int slamCd = 100;

    public StoneMonster(EntityType<? extends StoneMonster> type, Level level) {
        super(type, level, BossEvent.BossBarColor.BLUE);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0)
                .add(Attributes.ARMOR, 8.0)
                .add(Attributes.ATTACK_DAMAGE, 12.0)
                .add(Attributes.MOVEMENT_SPEED, 0.22)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 40.0);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (getTarget() != null && distanceToSqr(getTarget()) < 64 && --slamCd <= 0) {
            slamCd = 100;
            Abilities.quake(this, 6, 10f);
        }
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!level().isClientSide) {
            BlockPos pos = blockPosition();
            level().setBlockAndUpdate(pos, ModBlocks.ANCIENT_ALTAR.get().defaultBlockState());
            for (Player p : level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(40))) {
                p.getPersistentData().putBoolean(AncientPower.STONE, true);
            }
        }
    }
}
