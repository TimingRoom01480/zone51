package com.example.zone51;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CardItem extends Item {
    public final CardType type;

    public CardItem(CardType type, Properties props) {
        super(props);
        this.type = type;
    }

    /** Бросок карты (только сервер). Вызывается и по ПКМ, и по ЛКМ. */
    public static boolean throwFrom(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof CardItem ci)) return false;
        if (player.getCooldowns().isOnCooldown(ci)) return false;
        Level level = player.level();

        if (!CardBeltItem.isWearing(player)) {
            player.displayClientMessage(Component.translatable("message.zone51.need_belt"), true);
            return false;
        }
        if (ci.type == CardType.HEAL && player.isShiftKeyDown()) {
            player.heal(6.0f); // Shift: вылечить себя
        } else {
            ThrownCard card = new ThrownCard(level, player, stack);
            float speed = Suits.full(player, SuitType.CARDSHARP) ? 3.2f : 2.0f; // костюм: карты летят дальше
            card.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, speed, 0.3f);
            level.addFreshEntity(card);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.6f, 1.4f);
        if (ci.type.consumable && !player.getAbilities().instabuild) stack.shrink(1);
        player.getCooldowns().addCooldown(ci, 8);
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return CardBeltItem.isWearing(player) ? InteractionResultHolder.success(stack) : InteractionResultHolder.fail(stack);
        }
        return throwFrom(player, hand) ? InteractionResultHolder.success(stack) : InteractionResultHolder.fail(stack);
    }
}
