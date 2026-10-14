package com.example.zone51;

import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Карточник: швыряет случайные карты. Роняет Пояс Карточника. */
public class CardsharpBoss extends AbstractBossMonster implements RangedAttackMob {
    private static final CardType[] POOL = {CardType.BASIC, CardType.EXPLOSIVE, CardType.FIRE,
            CardType.ICE, CardType.POISON, CardType.SLOW, CardType.TRAP};

    public CardsharpBoss(EntityType<? extends CardsharpBoss> type, Level level) {
        super(type, level, BossEvent.BossBarColor.YELLOW);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 150.0)
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new RangedAttackGoal(this, 1.0, 25, 14.0f));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 16.0f));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        CardType t = POOL[random.nextInt(POOL.length)];
        ThrownCard c = new ThrownCard(level(), this, new ItemStack(ModItems.CARDS.get(t).get()));
        double dx = target.getX() - getX();
        double dy = target.getEyeY() - c.getY();
        double dz = target.getZ() - getZ();
        c.shoot(dx, dy + Math.sqrt(dx * dx + dz * dz) * 0.1, dz, 1.6f, 4.0f);
        level().addFreshEntity(c);
    }
}
