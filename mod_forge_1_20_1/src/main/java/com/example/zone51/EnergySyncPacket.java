package com.example.zone51;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Сервер сообщает клиенту энергию Древней Силы и костюма для шкалы на экране. */
public class EnergySyncPacket {
    private final boolean hasAp, hasSuit;
    private final int ap, suit;

    public EnergySyncPacket(boolean hasAp, int ap, boolean hasSuit, int suit) {
        this.hasAp = hasAp; this.ap = ap; this.hasSuit = hasSuit; this.suit = suit;
    }

    public static void encode(EnergySyncPacket m, FriendlyByteBuf b) {
        b.writeBoolean(m.hasAp); b.writeVarInt(m.ap); b.writeBoolean(m.hasSuit); b.writeVarInt(m.suit);
    }

    public static EnergySyncPacket decode(FriendlyByteBuf b) {
        return new EnergySyncPacket(b.readBoolean(), b.readVarInt(), b.readBoolean(), b.readVarInt());
    }

    public static void handle(EnergySyncPacket m, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientEnergy.set(m.hasAp, m.ap, m.hasSuit, m.suit)));
        ctx.get().setPacketHandled(true);
    }
}
