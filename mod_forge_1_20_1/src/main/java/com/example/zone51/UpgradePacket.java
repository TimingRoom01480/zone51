package com.example.zone51;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Игрок нажал «Изучить / Прокачать» в окне древа. */
public class UpgradePacket {
    private final String id;

    public UpgradePacket(String id) { this.id = id; }

    public static void encode(UpgradePacket m, FriendlyByteBuf b) { b.writeUtf(m.id); }

    public static UpgradePacket decode(FriendlyByteBuf b) { return new UpgradePacket(b.readUtf()); }

    public static void handle(UpgradePacket m, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer p = ctx.get().getSender();
            if (p == null) return;
            String key = Skills.upgrade(p, m.id);
            if (key != null) p.displayClientMessage(Component.translatable(key), true);
            Skills.sync(p, false);
        });
        ctx.get().setPacketHandled(true);
    }
}
