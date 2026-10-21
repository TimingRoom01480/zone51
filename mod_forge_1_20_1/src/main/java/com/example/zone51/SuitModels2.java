package com.example.zone51;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

/** 3D-детали остальных костюмов: Охранник, Лаборатория, Экзоскелет, Мутант. */
public class SuitModels2 {
    public static final ModelLayerLocation GUARD = loc("guard_armor");
    public static final ModelLayerLocation LAB = loc("lab_armor");
    public static final ModelLayerLocation EXO = loc("exo_armor");
    public static final ModelLayerLocation MUTANT = loc("mutant_armor");

    private static ModelLayerLocation loc(String n) {
        return new ModelLayerLocation(new ResourceLocation(Zone51.MODID, n), "main");
    }

    private static MeshDefinition base() {
        return HumanoidModel.createMesh(new CubeDeformation(1.0f), 0.0f);
    }

    public static ModelLayerLocation layerOf(SuitType s) {
        return switch (s) {
            case GUARD -> GUARD;
            case LAB -> LAB;
            case EXO -> EXO;
            case MUTANT -> MUTANT;
            case ANCIENT -> SuitModels.ANCIENT;
            case CARDSHARP -> SuitModels.CARDSHARP;
        };
    }

    public static LayerDefinition guard() {
        MeshDefinition mesh = base();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.getChild("head");
        PartDefinition body = root.getChild("body");
        PartDefinition right_arm = root.getChild("right_arm");
        PartDefinition left_arm = root.getChild("left_arm");
        PartDefinition right_leg = root.getChild("right_leg");
        PartDefinition left_leg = root.getChild("left_leg");
        head.addOrReplaceChild("helm_rim", CubeListBuilder.create().texOffs(0, 32).addBox(-4.6f, -9.6f, -4.6f, 9.2f, 1.6f, 9.2f), PartPose.ZERO);
        head.addOrReplaceChild("visor", CubeListBuilder.create().texOffs(0, 61).addBox(-4.3f, -6.6f, -5.3f, 8.6f, 2.6f, 1.3f), PartPose.ZERO);
        head.addOrReplaceChild("comm_l", CubeListBuilder.create().texOffs(10, 54).addBox(-5.9f, -5f, -1.5f, 1.6f, 3f, 3f), PartPose.ZERO);
        head.addOrReplaceChild("comm_r", CubeListBuilder.create().texOffs(20, 54).addBox(4.3f, -5f, -1.5f, 1.6f, 3f, 3f), PartPose.ZERO);
        head.addOrReplaceChild("antenna", CubeListBuilder.create().texOffs(30, 54).addBox(3.6f, -14f, 0.4f, 0.9f, 5f, 0.9f), PartPose.ZERO);
        body.addOrReplaceChild("pouch_l", CubeListBuilder.create().texOffs(34, 54).addBox(-3.8f, 6f, -3.5f, 3.2f, 3.2f, 1.5f), PartPose.ZERO);
        body.addOrReplaceChild("pouch_r", CubeListBuilder.create().texOffs(46, 54).addBox(0.6f, 6f, -3.5f, 3.2f, 3.2f, 1.5f), PartPose.ZERO);
        body.addOrReplaceChild("radio", CubeListBuilder.create().texOffs(0, 54).addBox(-5f, 1f, -1f, 1.5f, 3.5f, 2.2f), PartPose.ZERO);
        right_arm.addOrReplaceChild("pad_r", CubeListBuilder.create().texOffs(0, 44).addBox(-4.2f, -4.2f, -3.2f, 6.4f, 2.6f, 6.4f), PartPose.ZERO);
        left_arm.addOrReplaceChild("pad_l", CubeListBuilder.create().texOffs(28, 44).addBox(-2.2f, -4.2f, -3.2f, 6.4f, 2.6f, 6.4f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 96);
    }

    public static LayerDefinition lab() {
        MeshDefinition mesh = base();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.getChild("head");
        PartDefinition body = root.getChild("body");
        PartDefinition right_arm = root.getChild("right_arm");
        PartDefinition left_arm = root.getChild("left_arm");
        PartDefinition right_leg = root.getChild("right_leg");
        PartDefinition left_leg = root.getChild("left_leg");
        head.addOrReplaceChild("strap", CubeListBuilder.create().texOffs(0, 47).addBox(-4.7f, -6.2f, -4.7f, 9.4f, 1.6f, 9.4f), PartPose.ZERO);
        head.addOrReplaceChild("lens_l", CubeListBuilder.create().texOffs(0, 68).addBox(-3.7f, -6.6f, -5.2f, 3.1f, 2.6f, 1.1f), PartPose.ZERO);
        head.addOrReplaceChild("lens_r", CubeListBuilder.create().texOffs(12, 68).addBox(0.6f, -6.6f, -5.2f, 3.1f, 2.6f, 1.1f), PartPose.ZERO);
        head.addOrReplaceChild("filter_l", CubeListBuilder.create().texOffs(24, 68).addBox(-3.9f, -2.2f, -5.5f, 2.6f, 2.6f, 1.7f), PartPose.ZERO);
        head.addOrReplaceChild("filter_r", CubeListBuilder.create().texOffs(34, 68).addBox(1.3f, -2.2f, -5.5f, 2.6f, 2.6f, 1.7f), PartPose.ZERO);
        body.addOrReplaceChild("coat_tail", CubeListBuilder.create().texOffs(0, 32).addBox(-4.6f, 12f, -3.1f, 9.2f, 8f, 6.2f), PartPose.ZERO);
        body.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(24, 59).addBox(-4.5f, 9f, -2.8f, 9f, 1.5f, 5.6f), PartPose.ZERO);
        body.addOrReplaceChild("pocket", CubeListBuilder.create().texOffs(44, 68).addBox(0.8f, 5.5f, -3.4f, 3.2f, 2.6f, 0.7f), PartPose.ZERO);
        right_arm.addOrReplaceChild("cuff_r", CubeListBuilder.create().texOffs(40, 47).addBox(-3.6f, 8.2f, -2.6f, 5.2f, 3f, 5.2f), PartPose.ZERO);
        left_arm.addOrReplaceChild("cuff_l", CubeListBuilder.create().texOffs(0, 59).addBox(-1.6f, 8.2f, -2.6f, 5.2f, 3f, 5.2f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 96);
    }

    public static LayerDefinition exo() {
        MeshDefinition mesh = base();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.getChild("head");
        PartDefinition body = root.getChild("body");
        PartDefinition right_arm = root.getChild("right_arm");
        PartDefinition left_arm = root.getChild("left_arm");
        PartDefinition right_leg = root.getChild("right_leg");
        PartDefinition left_leg = root.getChild("left_leg");
        head.addOrReplaceChild("visor", CubeListBuilder.create().texOffs(38, 77).addBox(-4.4f, -6.2f, -5.2f, 8.8f, 2f, 1.2f), PartPose.ZERO);
        head.addOrReplaceChild("antenna", CubeListBuilder.create().texOffs(24, 77).addBox(-4.8f, -13.5f, -1f, 0.9f, 5.5f, 0.9f), PartPose.ZERO);
        head.addOrReplaceChild("cheek_l", CubeListBuilder.create().texOffs(28, 67).addBox(-5.6f, -4.5f, -2.5f, 1.6f, 4f, 5f), PartPose.ZERO);
        head.addOrReplaceChild("cheek_r", CubeListBuilder.create().texOffs(42, 67).addBox(4f, -4.5f, -2.5f, 1.6f, 4f, 5f), PartPose.ZERO);
        body.addOrReplaceChild("backpack", CubeListBuilder.create().texOffs(0, 32).addBox(-4f, 0.5f, 2.2f, 8f, 9f, 4f), PartPose.ZERO);
        body.addOrReplaceChild("thr_l", CubeListBuilder.create().texOffs(0, 77).addBox(-3.6f, 9f, 3.4f, 2.6f, 5f, 2.6f), PartPose.ZERO);
        body.addOrReplaceChild("thr_r", CubeListBuilder.create().texOffs(12, 77).addBox(1f, 9f, 3.4f, 2.6f, 5f, 2.6f), PartPose.ZERO);
        body.addOrReplaceChild("core", CubeListBuilder.create().texOffs(28, 77).addBox(-1.8f, 3f, -3.5f, 3.6f, 3.6f, 1f), PartPose.ZERO);
        right_arm.addOrReplaceChild("pad_r", CubeListBuilder.create().texOffs(28, 56).addBox(-4.5f, -4.5f, -3.5f, 7f, 3f, 7f), PartPose.ZERO);
        left_arm.addOrReplaceChild("pad_l", CubeListBuilder.create().texOffs(0, 67).addBox(-2.5f, -4.5f, -3.5f, 7f, 3f, 7f), PartPose.ZERO);
        right_arm.addOrReplaceChild("gaunt_r", CubeListBuilder.create().texOffs(24, 32).addBox(-3.8f, 6.5f, -2.8f, 5.6f, 4.5f, 5.6f), PartPose.ZERO);
        left_arm.addOrReplaceChild("gaunt_l", CubeListBuilder.create().texOffs(0, 45).addBox(-1.8f, 6.5f, -2.8f, 5.6f, 4.5f, 5.6f), PartPose.ZERO);
        right_leg.addOrReplaceChild("fin_r", CubeListBuilder.create().texOffs(24, 45).addBox(-3.2f, 8.5f, -3.2f, 6.4f, 3.5f, 6.4f), PartPose.ZERO);
        left_leg.addOrReplaceChild("fin_l", CubeListBuilder.create().texOffs(0, 56).addBox(-3.2f, 8.5f, -3.2f, 6.4f, 3.5f, 6.4f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 96);
    }

    public static LayerDefinition mutant() {
        MeshDefinition mesh = base();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.getChild("head");
        PartDefinition body = root.getChild("body");
        PartDefinition right_arm = root.getChild("right_arm");
        PartDefinition left_arm = root.getChild("left_arm");
        PartDefinition right_leg = root.getChild("right_leg");
        PartDefinition left_leg = root.getChild("left_leg");
        head.addOrReplaceChild("brow", CubeListBuilder.create().texOffs(0, 61).addBox(-4.6f, -8.6f, -5.2f, 9.2f, 1.6f, 1.6f), PartPose.ZERO);
        head.addOrReplaceChild("horn_l", CubeListBuilder.create().texOffs(52, 43).addBox(-5.6f, -11.5f, -1f, 1.6f, 4.5f, 1.6f), PartPose.ZERO);
        head.addOrReplaceChild("horn_r", CubeListBuilder.create().texOffs(0, 54).addBox(4f, -11.5f, -1f, 1.6f, 4.5f, 1.6f), PartPose.ZERO);
        head.addOrReplaceChild("tusk_l", CubeListBuilder.create().texOffs(24, 61).addBox(-3.2f, -1.6f, -5.3f, 1f, 2.2f, 1f), PartPose.ZERO);
        head.addOrReplaceChild("tusk_r", CubeListBuilder.create().texOffs(28, 61).addBox(2.2f, -1.6f, -5.3f, 1f, 2.2f, 1f), PartPose.ZERO);
        body.addOrReplaceChild("spine_a", CubeListBuilder.create().texOffs(34, 54).addBox(-1f, -1.5f, 2.6f, 2f, 3f, 2.2f), PartPose.ZERO);
        body.addOrReplaceChild("spine_b", CubeListBuilder.create().texOffs(8, 54).addBox(-1f, 3f, 2.8f, 2f, 3.5f, 2.2f), PartPose.ZERO);
        body.addOrReplaceChild("spine_c", CubeListBuilder.create().texOffs(44, 54).addBox(-1f, 7.5f, 2.6f, 2f, 3f, 2.2f), PartPose.ZERO);
        body.addOrReplaceChild("rib", CubeListBuilder.create().texOffs(32, 61).addBox(-4.4f, 2.5f, -3.2f, 8.8f, 1f, 1.2f), PartPose.ZERO);
        right_arm.addOrReplaceChild("pad_r", CubeListBuilder.create().texOffs(0, 32).addBox(-4.8f, -4.6f, -3.6f, 7.6f, 3f, 7.2f), PartPose.ZERO);
        left_arm.addOrReplaceChild("pad_l", CubeListBuilder.create().texOffs(32, 32).addBox(-2.8f, -4.6f, -3.6f, 7.6f, 3f, 7.2f), PartPose.ZERO);
        right_arm.addOrReplaceChild("spike_r", CubeListBuilder.create().texOffs(18, 54).addBox(-5.2f, -8.5f, -1f, 2f, 5f, 2f), PartPose.ZERO);
        left_arm.addOrReplaceChild("spike_l", CubeListBuilder.create().texOffs(26, 54).addBox(3.2f, -8.5f, -1f, 2f, 5f, 2f), PartPose.ZERO);
        right_arm.addOrReplaceChild("fist_r", CubeListBuilder.create().texOffs(0, 43).addBox(-4.2f, 8f, -3f, 6.4f, 4.5f, 6f), PartPose.ZERO);
        left_arm.addOrReplaceChild("fist_l", CubeListBuilder.create().texOffs(26, 43).addBox(-2.2f, 8f, -3f, 6.4f, 4.5f, 6f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 96);
    }
}
