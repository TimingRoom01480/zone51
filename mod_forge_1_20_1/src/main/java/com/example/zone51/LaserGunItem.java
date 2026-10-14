package com.example.zone51;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/** Лазерная пушка Зоны 51. Заряжается из левой руки (осколок энергии, кристалл, незеритовый блок). */
public class LaserGunItem extends Item {
    public static final int MAX = 200, COST = 10;

    public LaserGunItem(Properties props) { super(props); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player p, InteractionHand hand) {
        ItemStack s = p.getItemInHand(hand);
        if (level.isClientSide) return InteractionResultHolder.success(s);
        if (Energy.get(s) < COST) {
            p.displayClientMessage(Component.translatable("message.zone51.no_energy"), true);
            return InteractionResultHolder.fail(s);
        }
        Abilities.laser(p, p.getLookAngle(), 45, 9.0f);
        Energy.add(s, -COST, MAX);
        p.getCooldowns().addCooldown(this, 8);
        level.playSound(null, p.getX(), p.getY(), p.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.7f, 1.6f);
        return InteractionResultHolder.success(s);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide && selected && entity instanceof Player p && p.tickCount % 20 == 0) {
            Energy.chargeFromOffhand(p, stack, MAX);
        }
    }

    @Override
    public void appendHoverText(ItemStack s, @Nullable Level lv, List<Component> tip, TooltipFlag f) {
        tip.add(Component.translatable("tooltip.zone51.energy", Energy.get(s), MAX).withStyle(ChatFormatting.AQUA));
    }
}
