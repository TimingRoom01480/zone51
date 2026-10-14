package com.example.zone51;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

/** Карта: ПКМ показывает координаты ближайшей структуры (многоразовая). */
public class DungeonMapItem extends Item {
    private final TagKey<Structure> target;

    public DungeonMapItem(String structureTag, Properties props) {
        super(props);
        this.target = TagKey.create(Registries.STRUCTURE, new ResourceLocation(Zone51.MODID, structureTag));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player p, InteractionHand hand) {
        ItemStack s = p.getItemInHand(hand);
        if (level instanceof ServerLevel sl) {
            BlockPos pos = sl.findNearestMapStructure(target, p.blockPosition(), 50, false);
            if (pos == null) {
                p.displayClientMessage(Component.translatable("message.zone51.map_none"), false);
            } else {
                int d = (int) Math.sqrt(pos.distSqr(p.blockPosition()));
                p.displayClientMessage(Component.translatable("message.zone51.map_found", pos.getX(), pos.getZ(), d), false);
            }
            p.getCooldowns().addCooldown(this, 40);
        }
        return InteractionResultHolder.sidedSuccess(s, level.isClientSide());
    }
}
