package com.example.zone51;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

/** Сердце БОСС51: Shift+ПКМ меняет способность, ПКМ применяет. Все 10 способностей прокачиваются в древе. */
public class Boss51HeartItem extends Item {
    public static final int MAX = 500;
    private static final String SEL = "power";

    public enum Power {
        LASER(30), PUNCH(15), DRONES(60), QUAKE(40), TELEPORT(25), STORM(80), CHARGE(20), BARRIER(30), ROAR(25), REGEN(35);
        public final int cost;
        Power(int cost) { this.cost = cost; }
        public Component label() { return Component.translatable("heart.zone51." + name().toLowerCase()); }
    }

    public Boss51HeartItem(Properties props) { super(props); }

    private static Power selected(ItemStack s) {
        Power[] v = Power.values();
        return v[Math.floorMod(s.hasTag() ? s.getTag().getInt(SEL) : 0, v.length)];
    }

    public static Power byId(String id) {
        for (Power p : Power.values()) if (p.name().equalsIgnoreCase(id)) return p;
        return null;
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
        boolean ok = perform(p, s, selected(s));
        return ok ? InteractionResultHolder.success(s) : InteractionResultHolder.fail(s);
    }

    /** Применить способность (вызывается и ПКМ, и командой Palladium). */
    public static boolean perform(Player p, ItemStack s, Power pw) {
        int lv = Skills.level(p, "heart_" + pw.name().toLowerCase());
        if (lv < 1) {
            p.displayClientMessage(Component.translatable("message.zone51.skill_locked"), true);
            return false;
        }
        int cost = Math.max(1, (int) (pw.cost * (1 - 0.06 * (lv - 1))));
        if (Energy.get(s) < cost) {
            p.displayClientMessage(Component.translatable("message.zone51.no_energy"), true);
            return false;
        }
        float f = 1.0f + 0.2f * (lv - 1);
        Vec3 look = p.getLookAngle();
        switch (pw) {
            case LASER -> Abilities.laser(p, look, 40, 10f * f);
            case PUNCH -> Abilities.punch(p);
            case DRONES -> Abilities.summonPets(p, lv >= 3 ? 4 : 3);
            case QUAKE -> Abilities.quake(p, 6, 12f * f);
            case TELEPORT -> Abilities.teleportDash(p, 12 + 2 * (lv - 1));
            case STORM -> Abilities.storm(p, 10, 6, 10f * f);
            case CHARGE -> {
                p.setDeltaMovement(look.x * 2.0, 0.2, look.z * 2.0);
                p.hurtMarked = true;
                Abilities.punch(p);
            }
            case BARRIER -> {
                p.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 200, 3));
                p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 160, 1));
            }
            case ROAR -> {
                for (LivingEntity e : p.level().getEntitiesOfClass(LivingEntity.class, p.getBoundingBox().inflate(10), x -> Abilities.isEnemy(p, x))) {
                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
                    e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
                }
                Abilities.quake(p, 8, 4f * f);
            }
            case REGEN -> {
                p.heal(12f * f);
                p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 2));
            }
        }
        Energy.add(s, -cost, MAX);
        p.getCooldowns().addCooldown(s.getItem(), 10);
        p.displayClientMessage(Component.translatable("message.zone51.heart_power", pw.label(), Energy.get(s)), true);
        return true;
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
