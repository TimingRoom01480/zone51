package com.example.zone51;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Zone51.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEvents {
    @SubscribeEvent
    public static void attributes(EntityAttributeCreationEvent e) {
        e.put(ModEntities.BOSS51.get(), Boss51.createAttributes().build());
        e.put(ModEntities.STONE_MONSTER.get(), StoneMonster.createAttributes().build());
        e.put(ModEntities.CRYSTAL_MONSTER.get(), CrystalMonster.createAttributes().build());
        e.put(ModEntities.CARDSHARP.get(), CardsharpBoss.createAttributes().build());
        e.put(ModEntities.CARD_TRADER.get(), CardTrader.createAttributes().build());
    }
}
