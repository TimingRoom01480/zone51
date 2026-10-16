package com.example.zone51;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Zone51.MODID, value = Dist.CLIENT)
public class ClientKeys {
    public static final String CATEGORY = "key.categories.zone51";
    public static final KeyMapping NEXT = new KeyMapping("key.zone51.next_ability", GLFW.GLFW_KEY_H, CATEGORY);
    public static final KeyMapping USE = new KeyMapping("key.zone51.use_ability", GLFW.GLFW_KEY_R, CATEGORY);
    public static final KeyMapping SUIT = new KeyMapping("key.zone51.suit_ability", GLFW.GLFW_KEY_G, CATEGORY);

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        while (NEXT.consumeClick()) ModNetwork.CHANNEL.sendToServer(new ActionPacket(0));
        while (USE.consumeClick()) ModNetwork.CHANNEL.sendToServer(new ActionPacket(1));
        while (SUIT.consumeClick()) ModNetwork.CHANNEL.sendToServer(new ActionPacket(2));
    }

    /** ЛКМ в воздух с картой в руке: бросок (на сервер уходит пакет). */
    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty e) {
        if (e.getEntity().getMainHandItem().getItem() instanceof CardItem) {
            ModNetwork.CHANNEL.sendToServer(new ActionPacket(3));
        }
    }
}
