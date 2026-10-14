package com.example.zone51;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Zone51.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    private static ResourceLocation tex(String name) {
        return new ResourceLocation(Zone51.MODID, "textures/entity/" + name + ".png");
    }

    @SubscribeEvent
    public static void renderers(EntityRenderersEvent.RegisterRenderers e) {
        e.registerEntityRenderer(ModEntities.THROWN_CARD.get(), ThrownItemRenderer::new);
        e.registerEntityRenderer(ModEntities.BOSS51.get(), Boss51Renderer::new);
        e.registerEntityRenderer(ModEntities.STONE_MONSTER.get(),
                c -> new PartsRenderer<StoneMonster>(c, MobModels.STONE, "stone_monster", 2.0f));
        e.registerEntityRenderer(ModEntities.CRYSTAL_MONSTER.get(),
                c -> new PartsRenderer<CrystalMonster>(c, MobModels.CRYSTAL, "crystal_monster", 1.9f));
        e.registerEntityRenderer(ModEntities.CARD_TRADER.get(),
                c -> new ScaledHumanoidRenderer<CardTrader>(c, tex("card_trader"), b -> 1.0));
        e.registerEntityRenderer(ModEntities.CARDSHARP.get(),
                c -> new PartsRenderer<CardsharpBoss>(c, MobModels.CARDSHARP, "cardsharp", 1.0f));
    }

    @SubscribeEvent
    public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e) {
        e.registerLayerDefinition(Boss51Model.LAYER, Boss51Model::createBodyLayer);
        e.registerLayerDefinition(MobModels.STONE, MobModels::stone);
        e.registerLayerDefinition(MobModels.CRYSTAL, MobModels::crystal);
        e.registerLayerDefinition(MobModels.CARDSHARP, MobModels::cardsharp);
    }

    @SubscribeEvent
    public static void keys(RegisterKeyMappingsEvent e) {
        e.register(ClientKeys.NEXT);
        e.register(ClientKeys.USE);
        e.register(ClientKeys.SUIT);
    }
}
