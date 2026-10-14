package com.example.zone51;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class PartsRenderer<T extends Mob> extends MobRenderer<T, HumanoidPartsModel<T>> {
    private final ResourceLocation texture;
    private final float scale;

    public PartsRenderer(EntityRendererProvider.Context ctx, ModelLayerLocation layer, String name, float scale) {
        super(ctx, new HumanoidPartsModel<T>(ctx.bakeLayer(layer)), 0.6f * scale / 2.0f);
        this.texture = new ResourceLocation(Zone51.MODID, "textures/entity/" + name + ".png");
        this.scale = scale;
        final ResourceLocation glow = new ResourceLocation(Zone51.MODID, "textures/entity/" + name + "_glow.png");
        this.addLayer(new EyesLayer<T, HumanoidPartsModel<T>>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(glow);
            }
        });
    }

    @Override
    protected void scale(T entity, PoseStack ps, float partialTick) {
        ps.scale(scale, scale, scale);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return texture;
    }
}
