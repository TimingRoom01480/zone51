package com.example.zone51;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/** Модель БОСС51 (сгенерирована скриптом вместе с текстурой): бронированный мутант-киборг. */
public class Boss51Model extends EntityModel<Boss51> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(new ResourceLocation(Zone51.MODID, "boss51"), "main");

    private final ModelPart root, head, body, rightArm, leftArm, rightLeg, leftLeg;

    public Boss51Model(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-7f, -12f, -3.5f, 14f, 12f, 7f), PartPose.offset(0f, 12f, 0f));
        body.addOrReplaceChild("chest_plate", CubeListBuilder.create().texOffs(46, 49).addBox(-5f, -10.5f, -4.5f, 10f, 7f, 1f), PartPose.ZERO);
        body.addOrReplaceChild("pipe_l", CubeListBuilder.create().texOffs(30, 49).addBox(-6f, -11f, 3.5f, 2f, 8f, 2f), PartPose.ZERO);
        body.addOrReplaceChild("pipe_r", CubeListBuilder.create().texOffs(38, 49).addBox(4f, -11f, 3.5f, 2f, 8f, 2f), PartPose.ZERO);
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(86, 0).addBox(-4.5f, -9f, -4.5f, 9f, 9f, 9f), PartPose.offset(0f, 0f, 0f));
        head.addOrReplaceChild("crest", CubeListBuilder.create().texOffs(64, 37).addBox(-1f, -12f, -4f, 2f, 3f, 8f), PartPose.ZERO);
        head.addOrReplaceChild("ear_l", CubeListBuilder.create().texOffs(68, 49).addBox(-6.5f, -6f, -1.5f, 2f, 4f, 3f), PartPose.ZERO);
        head.addOrReplaceChild("ear_r", CubeListBuilder.create().texOffs(78, 49).addBox(4.5f, -6f, -1.5f, 2f, 4f, 3f), PartPose.ZERO);
        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(42, 0).addBox(-2.5f, -2f, -3f, 5f, 13f, 6f), PartPose.offset(-9f, 2f, 0f));
        right_arm.addOrReplaceChild("pad_r", CubeListBuilder.create().texOffs(0, 37).addBox(-4f, -4f, -4.5f, 7f, 3f, 9f), PartPose.ZERO);
        right_arm.addOrReplaceChild("fist_r", CubeListBuilder.create().texOffs(48, 19).addBox(-3.5f, 11f, -3.5f, 7f, 8f, 7f), PartPose.ZERO);
        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(64, 0).addBox(-2.5f, -2f, -3f, 5f, 13f, 6f), PartPose.offset(9f, 2f, 0f));
        left_arm.addOrReplaceChild("pad_l", CubeListBuilder.create().texOffs(32, 37).addBox(-3f, -4f, -4.5f, 7f, 3f, 9f), PartPose.ZERO);
        left_arm.addOrReplaceChild("fist_l", CubeListBuilder.create().texOffs(76, 19).addBox(-3.5f, 11f, -3.5f, 7f, 8f, 7f), PartPose.ZERO);
        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 19).addBox(-3f, 0f, -3f, 6f, 12f, 6f), PartPose.offset(-3.5f, 12f, 0f));
        right_leg.addOrReplaceChild("boot_r", CubeListBuilder.create().texOffs(84, 37).addBox(-3.5f, 9f, -4.5f, 7f, 3f, 8f), PartPose.ZERO);
        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(24, 19).addBox(-3f, 0f, -3f, 6f, 12f, 6f), PartPose.offset(3.5f, 12f, 0f));
        left_leg.addOrReplaceChild("boot_l", CubeListBuilder.create().texOffs(0, 49).addBox(-3.5f, 9f, -4.5f, 7f, 3f, 8f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(Boss51 e, float limbSwing, float limbSwingAmount, float age, float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        head.xRot = headPitch * Mth.DEG_TO_RAD;

        float swing = Mth.cos(limbSwing * 0.5f) * limbSwingAmount;
        rightLeg.xRot = swing * 1.1f;
        leftLeg.xRot = -swing * 1.1f;
        rightArm.xRot = -swing * 0.7f;
        leftArm.xRot = swing * 0.7f;
        rightArm.zRot = 0.08f + Mth.sin(age * 0.09f) * 0.03f;
        leftArm.zRot = -0.08f - Mth.sin(age * 0.09f) * 0.03f;
        body.xRot = Mth.sin(age * 0.08f) * 0.02f;

        if (e.isAggressive()) { // боевая стойка: руки-пушки вперёд
            rightArm.xRot = -1.45f + Mth.sin(age * 0.12f) * 0.05f;
            leftArm.xRot = -1.45f - Mth.sin(age * 0.12f) * 0.05f;
            rightArm.zRot = 0.12f;
            leftArm.zRot = -0.12f;
        }
    }

    @Override
    public void renderToBuffer(PoseStack ps, VertexConsumer vc, int light, int overlay, float r, float g, float b, float a) {
        root.render(ps, vc, light, overlay, r, g, b, a);
    }
}
