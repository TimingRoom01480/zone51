package com.example.zone51;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class Boss51Renderer extends MobRenderer<Boss51, Boss51Model> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Zone51.MODID, "textures/entity/boss51.png");
    private static final ResourceLocation GLOW = new ResourceLocation(Zone51.MODID, "textures/entity/boss51_glow.png");

    public Boss51Renderer(EntityRendererProvider.Context ctx) {
        super(ctx, new Boss51Model(ctx.bakeLayer(Boss51Model.LAYER)), 0.9f);
        // светящиеся глаза, ядро, дула и вены
        this.addLayer(new EyesLayer<Boss51, Boss51Model>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(GLOW);
            }
        });
    }

    @Override
    protected void scale(Boss51 boss, PoseStack ps, float partialTick) {
        float s = boss.isMini() ? 0.95f : 1.9f;
        ps.scale(s, s, s);
        this.shadowRadius = 0.9f * s / 1.9f;
    }

    @Override
    public ResourceLocation getTextureLocation(Boss51 boss) {
        return TEXTURE;
    }
}
