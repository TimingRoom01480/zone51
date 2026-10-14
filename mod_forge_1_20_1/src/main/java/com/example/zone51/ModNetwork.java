package com.example.zone51;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Zone51.MODID, "main"), () -> VERSION, VERSION::equals, VERSION::equals);

    public static void init() {
        CHANNEL.registerMessage(0, ActionPacket.class, ActionPacket::encode, ActionPacket::decode, ActionPacket::handle);
    }
}
