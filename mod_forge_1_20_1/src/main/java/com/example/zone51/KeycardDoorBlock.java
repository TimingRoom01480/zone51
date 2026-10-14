package com.example.zone51;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Секретная стена: открывается Ключ-картой Зоны 51. */
public class KeycardDoorBlock extends Block {
    public KeycardDoorBlock(Properties props) { super(props); }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player p, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            boolean ok = p.getMainHandItem().is(ModItems.ZONE51_KEYCARD.get()) || p.getOffhandItem().is(ModItems.ZONE51_KEYCARD.get());
            if (ok) {
                level.destroyBlock(pos, false);
                p.displayClientMessage(Component.translatable("message.zone51.keycard_ok"), true);
            } else {
                p.displayClientMessage(Component.translatable("message.zone51.keycard_needed"), true);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
