package com.example.zone51;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

/** Общая модель с частями head/body/right_arm/left_arm/right_leg/left_leg и простой анимацией. */
public class HumanoidPartsModel<T extends Mob> extends EntityModel<T> {
    private final ModelPart root, head, body, rightArm, leftArm, rightLeg, leftLeg;

    public HumanoidPartsModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    @Override
    public void setupAnim(T e, float limbSwing, float limbSwingAmount, float age, float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        head.xRot = headPitch * Mth.DEG_TO_RAD;
        float swing = Mth.cos(limbSwing * 0.5f) * limbSwingAmount;
        rightLeg.xRot = swing * 1.0f;
        leftLeg.xRot = -swing * 1.0f;
        rightArm.xRot = -swing * 0.7f;
        leftArm.xRot = swing * 0.7f;
        rightArm.zRot = 0.06f + Mth.sin(age * 0.09f) * 0.03f;
        leftArm.zRot = -0.06f - Mth.sin(age * 0.09f) * 0.03f;
        body.xRot = Mth.sin(age * 0.08f) * 0.02f;
        if (e.isAggressive()) {
            rightArm.xRot = -1.4f + Mth.sin(age * 0.12f) * 0.05f;
            leftArm.xRot = -1.4f - Mth.sin(age * 0.12f) * 0.05f;
        }
    }

    @Override
    public void renderToBuffer(PoseStack ps, VertexConsumer vc, int light, int overlay, float r, float g, float b, float a) {
        root.render(ps, vc, light, overlay, r, g, b, a);
    }
}
