package com.example.zone51;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;

/** Стартовая колода: ПКМ открывает и выдаёт набор карт. */
public class CardDeckItem extends Item {
    public CardDeckItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack deck = player.getItemInHand(hand);
        if (!level.isClientSide) {
            give(player, CardType.BASIC, 16);
            give(player, CardType.FIRE, 4);
            give(player, CardType.ICE, 4);
            give(player, CardType.POISON, 4);
            give(player, CardType.SLOW, 4);
            give(player, CardType.HEAL, 4);
            give(player, CardType.EXPLOSIVE, 2);
            give(player, CardType.SUMMON, 2);
            give(player, CardType.TRAP, 2);
            give(player, CardType.RETURN, 2);
            if (!player.getAbilities().instabuild) deck.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(deck, level.isClientSide());
    }

    private static void give(Player p, CardType t, int n) {
        ItemHandlerHelper.giveItemToPlayer(p, new ItemStack(ModItems.CARDS.get(t).get(), n));
    }
}
