package com.example.zone51;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/** Радиоактивные отходы. Иммунитет даёт полный Лабораторный костюм. */
public class RadioactiveBlock extends Block {
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);

    public RadioactiveBlock(Properties props) { super(props); }

    @Override
    public VoxelShape getShape(BlockState s, BlockGetter g, BlockPos p, CollisionContext c) { return SHAPE; }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
        if (level.isClientSide || !(e instanceof LivingEntity le) || e.tickCount % 20 != 0) return;
        if (le instanceof AbstractBossMonster) return;
        if (le instanceof Player p && Suits.full(p, SuitType.LAB)) return;
        le.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));
        le.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
    }
}
