package com.example.zone51;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SkillSyncPacket {
    private final boolean open;
    private final Map<String, Integer> levels;

    public SkillSyncPacket(boolean open, Map<String, Integer> levels) { this.open = open; this.levels = levels; }

    public static void encode(SkillSyncPacket m, FriendlyByteBuf b) {
        b.writeBoolean(m.open);
        b.writeVarInt(m.levels.size());
        m.levels.forEach((k, v) -> { b.writeUtf(k); b.writeVarInt(v); });
    }

    public static SkillSyncPacket decode(FriendlyByteBuf b) {
        boolean open = b.readBoolean();
        int n = b.readVarInt();
        Map<String, Integer> m = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) m.put(b.readUtf(), b.readVarInt());
        return new SkillSyncPacket(open, m);
    }

    public static void handle(SkillSyncPacket m, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientSkills.apply(m.levels, m.open)));
        ctx.get().setPacketHandled(true);
    }
}
