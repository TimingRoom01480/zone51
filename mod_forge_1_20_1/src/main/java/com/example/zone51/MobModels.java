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
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(64, 24).addBox(-4f, -8f, -4f, 8f, 8f, 8f), PartPose.offset(0f, -1f, -3f));
        head.addOrReplaceChild("brow", CubeListBuilder.create().texOffs(82, 60).addBox(-5f, -6f, -5f, 10f, 2f, 2f), PartPose.ZERO);
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
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6f, -12f, -3.5f, 12f, 12f, 7f), PartPose.offset(0f, 12f, 0f));
        body.addOrReplaceChild("spike_a", CubeListBuilder.create().texOffs(68, 19).addBox(-1f, -18f, 3f, 2f, 9f, 2f), PartPose.ZERO);
        body.addOrReplaceChild("spike_b", CubeListBuilder.create().texOffs(76, 19).addBox(-5f, -15f, 3f, 2f, 6f, 2f), PartPose.ZERO);
        body.addOrReplaceChild("spike_c", CubeListBuilder.create().texOffs(84, 19).addBox(3f, -15f, 3f, 2f, 6f, 2f), PartPose.ZERO);
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 19).addBox(-4f, -8f, -4f, 8f, 8f, 8f), PartPose.offset(0f, 0f, 0f));
        head.addOrReplaceChild("horn_l", CubeListBuilder.create().texOffs(92, 19).addBox(-3f, -14f, -1f, 2f, 6f, 2f), PartPose.ZERO);
        head.addOrReplaceChild("horn_r", CubeListBuilder.create().texOffs(100, 19).addBox(1f, -14f, -1f, 2f, 6f, 2f), PartPose.ZERO);
        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(86, 0).addBox(-2f, -2f, -2f, 4f, 13f, 4f), PartPose.offset(-8f, 2f, 0f));
        right_arm.addOrReplaceChild("shard_r", CubeListBuilder.create().texOffs(32, 19).addBox(-3f, -9f, -2f, 5f, 8f, 4f), PartPose.ZERO);
        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(102, 0).addBox(-2f, -2f, -2f, 4f, 13f, 4f), PartPose.offset(8f, 2f, 0f));
        left_arm.addOrReplaceChild("shard_l", CubeListBuilder.create().texOffs(50, 19).addBox(-2f, -9f, -2f, 5f, 8f, 4f), PartPose.ZERO);
        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(38, 0).addBox(-3f, 0f, -3f, 6f, 12f, 6f), PartPose.offset(-3f, 12f, 0f));
        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(62, 0).addBox(-3f, 0f, -3f, 6f, 12f, 6f), PartPose.offset(3f, 12f, 0f));
        return LayerDefinition.create(mesh, 128, 128);
    }

    public static LayerDefinition cardsharp() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4f, -12f, -2f, 8f, 12f, 4f), PartPose.offset(0f, 12f, 0f));
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 0).addBox(-4f, -8f, -4f, 8f, 8f, 8f), PartPose.offset(0f, 0f, 0f));
        head.addOrReplaceChild("brim", CubeListBuilder.create().texOffs(28, 16).addBox(-6f, -9f, -6f, 12f, 1f, 12f), PartPose.ZERO);
        head.addOrReplaceChild("crown", CubeListBuilder.create().texOffs(0, 16).addBox(-3.5f, -16f, -3.5f, 7f, 7f, 7f), PartPose.ZERO);
        head.addOrReplaceChild("band", CubeListBuilder.create().texOffs(76, 16).addBox(-3.6f, -11f, -3.6f, 7.2f, 2f, 7.2f), PartPose.ZERO);
        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(56, 0).addBox(-3f, -2f, -2f, 4f, 12f, 4f), PartPose.offset(-6f, 2f, 0f));
        right_arm.addOrReplaceChild("card_r", CubeListBuilder.create().texOffs(108, 16).addBox(-0.5f, 10f, -4f, 1f, 5f, 4f), PartPose.ZERO);
        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(72, 0).addBox(-1f, -2f, -2f, 4f, 12f, 4f), PartPose.offset(6f, 2f, 0f));
        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(88, 0).addBox(-2f, 0f, -2f, 4f, 12f, 4f), PartPose.offset(-2f, 12f, 0f));
        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(104, 0).addBox(-2f, 0f, -2f, 4f, 12f, 4f), PartPose.offset(2f, 12f, 0f));
        return LayerDefinition.create(mesh, 128, 128);
    }
}
