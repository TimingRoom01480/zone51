package com.example.zone51;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class SuitItem extends ArmorItem {
    public final SuitType suit;

    public SuitItem(SuitType suit, Type type, Properties props) {
        super(suit, type, props);
        this.suit = suit;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tip, TooltipFlag flag) {
        if (suit.energy && getType() == Type.CHESTPLATE) {
            tip.add(Component.translatable("tooltip.zone51.energy", Energy.get(stack), Suits.MAX_ENERGY)
                    .withStyle(ChatFormatting.AQUA));
        }
    }
}
