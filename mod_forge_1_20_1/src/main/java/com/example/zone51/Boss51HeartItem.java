package com.example.zone51;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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

/** Сердце БОСС51: Shift+ПКМ меняет способность, ПКМ применяет. Заряжается из левой руки. */
public class Boss51HeartItem extends Item {
    public static final int MAX = 500;
    private static final String SEL = "power";

    public enum Power {
        LASER(30), PUNCH(15), DRONES(60), QUAKE(40), TELEPORT(25), STORM(80);
        public final int cost;
        Power(int cost) { this.cost = cost; }
        public Component label() { return Component.translatable("heart.zone51." + name().toLowerCase()); }
    }

    public Boss51HeartItem(Properties props) { super(props); }

    private static Power selected(ItemStack s) {
        Power[] v = Power.values();
        return v[Math.floorMod(s.hasTag() ? s.getTag().getInt(SEL) : 0, v.length)];
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player p, InteractionHand hand) {
        ItemStack s = p.getItemInHand(hand);
        if (level.isClientSide) return InteractionResultHolder.success(s);

        if (p.isShiftKeyDown()) {
            s.getOrCreateTag().putInt(SEL, (selected(s).ordinal() + 1) % Power.values().length);
            p.displayClientMessage(Component.translatable("message.zone51.heart_power", selected(s).label(), Energy.get(s)), true);
            return InteractionResultHolder.success(s);
        }
        Power pw = selected(s);
        if (Energy.get(s) < pw.cost) {
            p.displayClientMessage(Component.translatable("message.zone51.no_energy"), true);
            return InteractionResultHolder.fail(s);
        }
        switch (pw) {
            case LASER -> Abilities.laser(p, p.getLookAngle(), 40, 10f);
            case PUNCH -> Abilities.punch(p);
            case DRONES -> Abilities.summonPets(p, 3);
            case QUAKE -> Abilities.quake(p, 6, 12f);
            case TELEPORT -> Abilities.teleportDash(p, 12);
            case STORM -> Abilities.storm(p, 10, 6, 10f);
        }
        Energy.add(s, -pw.cost, MAX);
        p.getCooldowns().addCooldown(this, 10);
        p.displayClientMessage(Component.translatable("message.zone51.heart_power", pw.label(), Energy.get(s)), true);
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
        tip.add(selected(s).label().copy().withStyle(ChatFormatting.GRAY));
    }
}
