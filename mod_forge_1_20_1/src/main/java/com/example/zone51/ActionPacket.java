package com.example.zone51;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** 0 = следующая способность, 1 = применить способность, 2 = способность костюма. */
public class ActionPacket {
    private final int action;

    public ActionPacket(int action) { this.action = action; }

    public static void encode(ActionPacket m, FriendlyByteBuf b) { b.writeVarInt(m.action); }

    public static ActionPacket decode(FriendlyByteBuf b) { return new ActionPacket(b.readVarInt()); }

    public static void handle(ActionPacket m, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer p = ctx.get().getSender();
            if (p == null) return;
            switch (m.action) {
                case 0 -> AncientPower.next(p);
                case 1 -> AncientPower.use(p);
                case 2 -> Suits.suitAbility(p);
                case 3 -> CardItem.throwFrom(p, net.minecraft.world.InteractionHand.MAIN_HAND);
                case 4 -> Skills.sync(p, true);
                case 5 -> SuitArts.next(p);
                default -> { }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
