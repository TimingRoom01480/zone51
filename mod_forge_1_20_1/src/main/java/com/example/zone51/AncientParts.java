package com.example.zone51;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

/** Пассивные бонусы частей Древнего Оружия, пока они лежат в инвентаре. */
public final class AncientParts {
    private static final UUID U_HANDLE = UUID.fromString("5a0f3b1e-1111-4a1b-9c01-000000000001");
    private static final UUID U_BLADE = UUID.fromString("5a0f3b1e-1111-4a1b-9c01-000000000002");
    private static final UUID U_GUARD = UUID.fromString("5a0f3b1e-1111-4a1b-9c01-000000000003");

    public static boolean has(Player p, Item item) {
        for (ItemStack s : p.getInventory().items) if (s.is(item)) return true;
        for (ItemStack s : p.getInventory().offhand) if (s.is(item)) return true;
        return false;
    }

    private static void toggle(Player p, Attribute attr, UUID id, String name, double v, AttributeModifier.Operation op, boolean want) {
        AttributeInstance i = p.getAttribute(attr);
        if (i == null) return;
        AttributeModifier m = i.getModifier(id);
        if (want && m == null) i.addTransientModifier(new AttributeModifier(id, name, v, op));
        else if (!want && m != null) i.removeModifier(id);
    }

    public static void tick(ServerPlayer p, long t) {
        if (t % 20 != 0) return;
        toggle(p, Attributes.ATTACK_SPEED, U_HANDLE, "ancient_handle", 0.15, AttributeModifier.Operation.MULTIPLY_BASE, has(p, ModItems.ANCIENT_HANDLE.get()));
        toggle(p, Attributes.ATTACK_DAMAGE, U_BLADE, "ancient_blade", 2.0, AttributeModifier.Operation.ADDITION, has(p, ModItems.ANCIENT_BLADE.get()));
        toggle(p, Attributes.KNOCKBACK_RESISTANCE, U_GUARD, "ancient_guard", 0.3, AttributeModifier.Operation.ADDITION, has(p, ModItems.ANCIENT_GUARD.get()));
        if (has(p, ModItems.ANCIENT_POMMEL.get()) && p.getHealth() < p.getMaxHealth() * 0.4f) {
            p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0, true, false));
        }
        if (has(p, ModItems.ANCIENT_CRYSTAL.get())) {
            p.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 0, true, false));
        }
    }
}
