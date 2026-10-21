package com.example.zone51;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

public final class EnergySync {
    /** Два раза в секунду отправляем игроку значения для шкал. */
    public static void tick(ServerPlayer p, long t) {
        if (t % 10 != 0) return;
        boolean hasAp = AncientPower.has(p);
        ItemStack chest = Suits.chest(p);
        boolean suit = chest.getItem() instanceof SuitItem si && si.suit.energy && Suits.full(p, si.suit);
        ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> p),
                new EnergySyncPacket(hasAp, AncientPower.energy(p), suit, suit ? Energy.get(chest) : 0));
    }
}
