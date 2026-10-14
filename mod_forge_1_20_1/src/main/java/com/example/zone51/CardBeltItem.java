package com.example.zone51;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;

/** Пояс Карточника (слот Curios: belt). Нужен для метания карт, сам пополняет карты. */
public class CardBeltItem extends Item implements ICurioItem {
    public static final String LAST_HIT = "zone51_last_hit";
    private static final int COMMON_SECONDS = 5;
    private static final int RARE_SECONDS = 15;
    private static final int COMBAT_WINDOW_TICKS = 100;

    public CardBeltItem(Properties props) {
        super(props);
    }

    /** API Curios для 1.20.1: getCuriosInventory возвращает LazyOptional. */
    public static boolean isWearing(LivingEntity entity) {
        return CuriosApi.getCuriosInventory(entity).resolve()
                .map(h -> h.findFirstCurio(ModItems.CARD_BELT.get()).isPresent())
                .orElse(false);
    }

    @Override
    public void curioTick(SlotContext ctx, ItemStack stack) {
        if (!(ctx.entity() instanceof ServerPlayer p) || p.tickCount % 20 != 0) return;

        boolean combat = p.level().getGameTime() - p.getPersistentData().getLong(LAST_HIT) < COMBAT_WINDOW_TICKS;
        CompoundTag tag = stack.getOrCreateTag();

        for (CardType t : CardType.values()) {
            if (!t.consumable) continue; // базовые карты и так бесконечные
            String key = "t_" + t.id;
            ItemStack target = findGrowable(p, ModItems.CARDS.get(t).get());
            if (target == null) { // карт нет в инвентаре или стак уже полный
                tag.putInt(key, 0);
                continue;
            }
            int need = t.rare ? RARE_SECONDS : COMMON_SECONDS;
            if (combat) need = (need + 1) / 2;
            int timer = tag.getInt(key) + 1;
            if (timer >= need) {
                target.grow(1);
                timer = 0;
            }
            tag.putInt(key, timer);
        }
    }

    /** Пополняем только те карты, что у игрока уже есть. */
    private static ItemStack findGrowable(Player p, Item item) {
        List<ItemStack> all = new ArrayList<>(p.getInventory().items);
        all.addAll(p.getInventory().offhand);
        for (ItemStack s : all) {
            if (s.is(item) && s.getCount() < s.getMaxStackSize()) return s;
        }
        return null;
    }
}
