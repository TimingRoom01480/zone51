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

/** 3D-детали брони: наплечники и шипы Древнего Человека, цилиндр и воротник Карточника. */
public class SuitModels {
    public static final ModelLayerLocation ANCIENT = loc("ancient_man_armor");
    public static final ModelLayerLocation CARDSHARP = loc("cardsharp_armor");

    private static ModelLayerLocation loc(String n) {
        return new ModelLayerLocation(new ResourceLocation(Zone51.MODID, n), "main");
    }

    private static MeshDefinition base() {
        return HumanoidModel.createMesh(new CubeDeformation(1.0f), 0.0f);
    }

    public static LayerDefinition ancient() {
        MeshDefinition mesh = base();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.getChild("head");
        PartDefinition body = root.getChild("body");
        PartDefinition right_arm = root.getChild("right_arm");
        PartDefinition left_arm = root.getChild("left_arm");
        head.addOrReplaceChild("crest", CubeListBuilder.create().texOffs(0, 32).addBox(-1f, -11.5f, -5f, 2f, 3f, 10f), PartPose.ZERO);
        head.addOrReplaceChild("horn_l", CubeListBuilder.create().texOffs(28, 45).addBox(-6.5f, -9f, -1.5f, 2f, 5f, 3f), PartPose.ZERO);
        head.addOrReplaceChild("horn_r", CubeListBuilder.create().texOffs(38, 45).addBox(4.5f, -9f, -1.5f, 2f, 5f, 3f), PartPose.ZERO);
        right_arm.addOrReplaceChild("pad_r", CubeListBuilder.create().texOffs(24, 32).addBox(-6f, -5f, -3.5f, 7f, 3f, 7f), PartPose.ZERO);
        right_arm.addOrReplaceChild("spike_r", CubeListBuilder.create().texOffs(48, 45).addBox(-6.5f, -9.5f, -1.5f, 2f, 5f, 3f), PartPose.ZERO);
        left_arm.addOrReplaceChild("pad_l", CubeListBuilder.create().texOffs(0, 45).addBox(-1f, -5f, -3.5f, 7f, 3f, 7f), PartPose.ZERO);
        left_arm.addOrReplaceChild("spike_l", CubeListBuilder.create().texOffs(0, 55).addBox(4.5f, -9.5f, -1.5f, 2f, 5f, 3f), PartPose.ZERO);
        body.addOrReplaceChild("plate", CubeListBuilder.create().texOffs(40, 55).addBox(-4.5f, 0.5f, -3.5f, 9f, 5f, 1.5f), PartPose.ZERO);
        body.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(10, 55).addBox(-4.5f, 9f, -3f, 9f, 2f, 6f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition cardsharp() {
        MeshDefinition mesh = base();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.getChild("head");
        PartDefinition body = root.getChild("body");
        head.addOrReplaceChild("brim", CubeListBuilder.create().texOffs(0, 46).addBox(-6f, -9.5f, -6f, 12f, 1f, 12f), PartPose.ZERO);
        head.addOrReplaceChild("crown", CubeListBuilder.create().texOffs(0, 32).addBox(-3.5f, -16.5f, -3.5f, 7f, 7f, 7f), PartPose.ZERO);
        head.addOrReplaceChild("band", CubeListBuilder.create().texOffs(0, 59).addBox(-3.6f, -11.5f, -3.6f, 7.2f, 2f, 7.2f), PartPose.ZERO);
        head.addOrReplaceChild("gog_l", CubeListBuilder.create().texOffs(0, 69).addBox(-3f, -11.5f, -4.9f, 2.5f, 1.8f, 1.3f), PartPose.ZERO);
        head.addOrReplaceChild("gog_r", CubeListBuilder.create().texOffs(10, 69).addBox(0.5f, -11.5f, -4.9f, 2.5f, 1.8f, 1.3f), PartPose.ZERO);
        body.addOrReplaceChild("coat", CubeListBuilder.create().texOffs(28, 32).addBox(-5f, 12.5f, -3.25f, 10f, 6.5f, 6.5f), PartPose.ZERO);
        body.addOrReplaceChild("collar", CubeListBuilder.create().texOffs(32, 59).addBox(-5f, -1.5f, -3f, 10f, 2.5f, 6f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 128);
    }
}
