package com.example.zone51;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

import java.util.function.ToDoubleFunction;

public class ScaledHumanoidRenderer<T extends Mob> extends HumanoidMobRenderer<T, HumanoidModel<T>> {
    private final ResourceLocation texture;
    private final ToDoubleFunction<T> scaleFn;

    public ScaledHumanoidRenderer(EntityRendererProvider.Context ctx, ResourceLocation texture, ToDoubleFunction<T> scaleFn) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
        this.texture = texture;
        this.scaleFn = scaleFn;
    }

    @Override
    protected void scale(T entity, PoseStack poseStack, float partialTick) {
        float s = (float) scaleFn.applyAsDouble(entity);
        poseStack.scale(s, s, s);
        this.shadowRadius = 0.5f * s;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return texture;
    }
}
