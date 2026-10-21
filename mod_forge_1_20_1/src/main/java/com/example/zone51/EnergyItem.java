package com.example.zone51;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/** Осколок энергии и кристалл молний: заряжают костюм, Сердце и лазерную пушку из левой руки. */
public class EnergyItem extends Item {
    public final int gain;

    public EnergyItem(int gain, Properties props) {
        super(props);
        this.gain = gain;
    }

    @Override
    public void appendHoverText(ItemStack s, @Nullable Level lv, List<Component> tip, TooltipFlag f) {
        tip.add(Component.translatable("tooltip.zone51.charge", gain).withStyle(ChatFormatting.AQUA));
    }
}
