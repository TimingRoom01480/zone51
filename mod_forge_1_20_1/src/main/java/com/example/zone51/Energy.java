package com.example.zone51;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** Энергия, хранящаяся в NBT предмета (костюмы, Сердце БОСС51). */
public final class Energy {
    public static final String KEY = "energy";

    public static int get(ItemStack s) {
        return s.hasTag() ? s.getTag().getInt(KEY) : 0;
    }

    public static void add(ItemStack s, int delta, int max) {
        if (s.isEmpty()) return;
        s.getOrCreateTag().putInt(KEY, Mth.clamp(get(s) + delta, 0, max));
    }

    /** Зарядка из левой руки: незеритовый блок +500, кристалл молний +250, осколок энергии +100. */
    public static void chargeFromOffhand(Player p, ItemStack target, int max) {
        if (target.isEmpty()) return;
        ItemStack off = p.getOffhandItem();
        int gain = 0;
        if (off.is(Items.NETHERITE_BLOCK)) gain = 500;
        else if (off.is(ModItems.LIGHTNING_CRYSTAL.get())) gain = 250;
        else if (off.is(ModItems.ENERGY_SHARD.get())) gain = 100;
        if (gain > 0 && get(target) < max - gain / 2) {
            add(target, gain, max);
            off.shrink(1);
        }
    }
}
