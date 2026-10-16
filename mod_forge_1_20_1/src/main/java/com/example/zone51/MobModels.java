package com.example.zone51;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

/** Модели Каменного Монстра, Кристального Монстра и Карточника (сгенерированы вместе с текстурами). */
public class MobModels {
    public static final ModelLayerLocation STONE = loc("stone_monster");
    public static final ModelLayerLocation CRYSTAL = loc("crystal_monster");
    public static final ModelLayerLocation CARDSHARP = loc("cardsharp");

    private static ModelLayerLocation loc(String n) {
        return new ModelLayerLocation(new ResourceLocation(Zone51.MODID, n), "main");
    }

    public static LayerDefinition stone() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(48, 0).addBox(-8f, -13f, -5f, 16f, 13f, 10f), PartPose.offset(0f, 12f, 0f));
        body.addOrReplaceChild("rock_a", CubeListBuilder.create().texOffs(32, 60).addBox(-5f, -16f, 3f, 9f, 5f, 5f), PartPose.ZERO);
        body.addOrReplaceChild("rock_b", CubeListBuilder.create().texOffs(60, 60).addBox(1f, -15f, -6f, 6f, 4f, 5f), PartPose.ZERO);
        body.addOrReplaceChild("rock_c", CubeListBuilder.create().texOffs(82, 60).addBox(-7f, -15f, -3f, 5f, 3f, 6f), PartPose.ZERO);
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(64, 24).addBox(-4f, -8f, -4f, 8f, 8f, 8f), PartPose.offset(0f, -1f, -3f));
        head.addOrReplaceChild("brow", CubeListBuilder.create().texOffs(104, 60).addBox(-5f, -6f, -5f, 10f, 2f, 2f), PartPose.ZERO);
        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 0).addBox(-3f, -3f, -3f, 6f, 18f, 6f), PartPose.offset(-11f, 1f, 0f));
        right_arm.addOrReplaceChild("pad_r", CubeListBuilder.create().texOffs(0, 44).addBox(-6f, -6f, -5f, 10f, 6f, 10f), PartPose.ZERO);
        right_arm.addOrReplaceChild("fist_r", CubeListBuilder.create().texOffs(80, 44).addBox(-4f, 14f, -4f, 8f, 7f, 8f), PartPose.ZERO);
        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(24, 0).addBox(-3f, -3f, -3f, 6f, 18f, 6f), PartPose.offset(11f, 1f, 0f));
        left_arm.addOrReplaceChild("pad_l", CubeListBuilder.create().texOffs(40, 44).addBox(-4f, -6f, -5f, 10f, 6f, 10f), PartPose.ZERO);
        left_arm.addOrReplaceChild("fist_l", CubeListBuilder.create().texOffs(0, 60).addBox(-4f, 14f, -4f, 8f, 7f, 8f), PartPose.ZERO);
        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 24).addBox(-4f, 0f, -4f, 8f, 12f, 8f), PartPose.offset(-4.5f, 12f, 0f));
        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(32, 24).addBox(-4f, 0f, -4f, 8f, 12f, 8f), PartPose.offset(4.5f, 12f, 0f));
        return LayerDefinition.create(mesh, 128, 128);
    }

    public static LayerDefinition crystal() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.5f, -12f, -4f, 13f, 12f, 8f), PartPose.offset(0f, 12f, 0f));
        body.addOrReplaceChild("band_b", CubeListBuilder.create().texOffs(24, 39).addBox(-6.8f, -6f, -4.3f, 13.6f, 1.5f, 8.6f), PartPose.ZERO);
        body.addOrReplaceChild("core", CubeListBuilder.create().texOffs(24, 61).addBox(-2.5f, -11f, -4.6f, 5f, 6f, 1f), PartPose.ZERO);
        body.addOrReplaceChild("cr_a", CubeListBuilder.create().texOffs(60, 20).addBox(-1.5f, -20f, 1.5f, 3f, 10f, 3f), PartPose.ZERO);
        body.addOrReplaceChild("cr_b", CubeListBuilder.create().texOffs(70, 39).addBox(-6f, -17f, 2f, 3f, 7f, 3f), PartPose.ZERO);
        body.addOrReplaceChild("cr_c", CubeListBuilder.create().texOffs(82, 39).addBox(3f, -17f, 2f, 3f, 7f, 3f), PartPose.ZERO);
        body.addOrReplaceChild("cr_d", CubeListBuilder.create().texOffs(36, 61).addBox(-4f, -14f, 3.5f, 2f, 5f, 2f), PartPose.ZERO);
        body.addOrReplaceChild("cr_e", CubeListBuilder.create().texOffs(44, 61).addBox(2f, -14f, 3.5f, 2f, 5f, 2f), PartPose.ZERO);
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 20).addBox(-4f, -8f, -4f, 8f, 8f, 8f), PartPose.offset(0f, 0f, 0f));
        head.addOrReplaceChild("horn_l", CubeListBuilder.create().texOffs(8, 61).addBox(-3.5f, -14f, -1f, 2f, 6f, 2f), PartPose.ZERO);
        head.addOrReplaceChild("horn_r", CubeListBuilder.create().texOffs(16, 61).addBox(1.5f, -14f, -1f, 2f, 6f, 2f), PartPose.ZERO);
        head.addOrReplaceChild("horn_c", CubeListBuilder.create().texOffs(94, 39).addBox(-1f, -16f, -2.5f, 2f, 8f, 2f), PartPose.ZERO);
        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(42, 0).addBox(-3f, -2f, -3f, 6f, 14f, 6f), PartPose.offset(-9.5f, 2f, 0f));
        right_arm.addOrReplaceChild("band_ra", CubeListBuilder.create().texOffs(64, 51).addBox(-3.3f, 3f, -3.3f, 6.6f, 1.5f, 6.6f), PartPose.ZERO);
        right_arm.addOrReplaceChild("sh_r1", CubeListBuilder.create().texOffs(0, 39).addBox(-3f, -10f, -2f, 3f, 9f, 3f), PartPose.ZERO);
        right_arm.addOrReplaceChild("sh_r2", CubeListBuilder.create().texOffs(92, 51).addBox(0f, -8f, -1f, 2f, 7f, 2f), PartPose.ZERO);
        right_arm.addOrReplaceChild("fist_r", CubeListBuilder.create().texOffs(72, 20).addBox(-3.5f, 12f, -3.5f, 7f, 6f, 7f), PartPose.ZERO);
        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(66, 0).addBox(-3f, -2f, -3f, 6f, 14f, 6f), PartPose.offset(9.5f, 2f, 0f));
        left_arm.addOrReplaceChild("band_la", CubeListBuilder.create().texOffs(100, 51).addBox(-3.3f, 3f, -3.3f, 6.6f, 1.5f, 6.6f), PartPose.ZERO);
        left_arm.addOrReplaceChild("sh_l1", CubeListBuilder.create().texOffs(12, 39).addBox(0f, -10f, -2f, 3f, 9f, 3f), PartPose.ZERO);
        left_arm.addOrReplaceChild("sh_l2", CubeListBuilder.create().texOffs(0, 61).addBox(-2f, -8f, -1f, 2f, 7f, 2f), PartPose.ZERO);
        left_arm.addOrReplaceChild("fist_l", CubeListBuilder.create().texOffs(100, 20).addBox(-3.5f, 12f, -3.5f, 7f, 6f, 7f), PartPose.ZERO);
        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(90, 0).addBox(-3.5f, 0f, -3.5f, 7f, 12f, 7f), PartPose.offset(-4f, 12f, 0f));
        right_leg.addOrReplaceChild("band_rl", CubeListBuilder.create().texOffs(0, 51).addBox(-3.8f, 8.5f, -3.8f, 7.6f, 1.5f, 7.6f), PartPose.ZERO);
        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 20).addBox(-3.5f, 0f, -3.5f, 7f, 12f, 7f), PartPose.offset(4f, 12f, 0f));
        left_leg.addOrReplaceChild("band_ll", CubeListBuilder.create().texOffs(32, 51).addBox(-3.8f, 8.5f, -3.8f, 7.6f, 1.5f, 7.6f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 128);
    }

    public static LayerDefinition cardsharp() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4f, -12f, -2f, 8f, 12f, 4f), PartPose.offset(0f, 12f, 0f));
        body.addOrReplaceChild("coat", CubeListBuilder.create().texOffs(28, 16).addBox(-4.5f, 0f, -2.5f, 9f, 8f, 5f), PartPose.ZERO);
        body.addOrReplaceChild("collar", CubeListBuilder.create().texOffs(0, 40).addBox(-4.5f, -12.5f, -2.5f, 9f, 2f, 5f), PartPose.ZERO);
        body.addOrReplaceChild("buckle", CubeListBuilder.create().texOffs(96, 40).addBox(-1.5f, -3.5f, -2.4f, 3f, 2f, 0.6f), PartPose.ZERO);
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 0).addBox(-4f, -8f, -4f, 8f, 8f, 8f), PartPose.offset(0f, 0f, 0f));
        head.addOrReplaceChild("brim", CubeListBuilder.create().texOffs(56, 16).addBox(-6f, -9f, -6f, 12f, 1f, 12f), PartPose.ZERO);
        head.addOrReplaceChild("crown", CubeListBuilder.create().texOffs(0, 16).addBox(-3.5f, -16f, -3.5f, 7f, 7f, 7f), PartPose.ZERO);
        head.addOrReplaceChild("band", CubeListBuilder.create().texOffs(0, 30).addBox(-3.6f, -11f, -3.6f, 7.2f, 2f, 7.2f), PartPose.ZERO);
        head.addOrReplaceChild("gog_l", CubeListBuilder.create().texOffs(76, 40).addBox(-3.5f, -11f, -6.6f, 3f, 2f, 1.2f), PartPose.ZERO);
        head.addOrReplaceChild("gog_r", CubeListBuilder.create().texOffs(86, 40).addBox(0.5f, -11f, -6.6f, 3f, 2f, 1.2f), PartPose.ZERO);
        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(56, 0).addBox(-3f, -2f, -2f, 4f, 12f, 4f), PartPose.offset(-6f, 2f, 0f));
        right_arm.addOrReplaceChild("cuff_r", CubeListBuilder.create().texOffs(76, 30).addBox(-3.3f, 6f, -2.3f, 4.6f, 4f, 4.6f), PartPose.ZERO);
        right_arm.addOrReplaceChild("card_r", CubeListBuilder.create().texOffs(96, 30).addBox(-0.5f, 10f, -4f, 1f, 5f, 4f), PartPose.ZERO);
        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(72, 0).addBox(-1f, -2f, -2f, 4f, 12f, 4f), PartPose.offset(6f, 2f, 0f));
        left_arm.addOrReplaceChild("cuff_l", CubeListBuilder.create().texOffs(106, 30).addBox(-1.3f, 6f, -2.3f, 4.6f, 4f, 4.6f), PartPose.ZERO);
        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(88, 0).addBox(-2f, 0f, -2f, 4f, 12f, 4f), PartPose.offset(-2f, 12f, 0f));
        right_leg.addOrReplaceChild("boot_r", CubeListBuilder.create().texOffs(32, 30).addBox(-2.5f, 8.5f, -2.5f, 5f, 3.5f, 5.5f), PartPose.ZERO);
        right_leg.addOrReplaceChild("trim_r", CubeListBuilder.create().texOffs(28, 40).addBox(-2.7f, 8f, -2.7f, 5.4f, 1f, 5.9f), PartPose.ZERO);
        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(104, 0).addBox(-2f, 0f, -2f, 4f, 12f, 4f), PartPose.offset(2f, 12f, 0f));
        left_leg.addOrReplaceChild("boot_l", CubeListBuilder.create().texOffs(54, 30).addBox(-2.5f, 8.5f, -2.5f, 5f, 3.5f, 5.5f), PartPose.ZERO);
        left_leg.addOrReplaceChild("trim_l", CubeListBuilder.create().texOffs(52, 40).addBox(-2.7f, 8f, -2.7f, 5.4f, 1f, 5.9f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 128);
    }
}
