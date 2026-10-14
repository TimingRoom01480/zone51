package com.example.zone51;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MedkitItem extends Item {
    public MedkitItem(Properties props) { super(props); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player p, InteractionHand hand) {
        ItemStack s = p.getItemInHand(hand);
        if (!level.isClientSide) {
            p.heal(10.0f);
            p.removeEffect(MobEffects.POISON);
            p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
            p.getCooldowns().addCooldown(this, 40);
            if (!p.getAbilities().instabuild) s.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(s, level.isClientSide());
    }
}
