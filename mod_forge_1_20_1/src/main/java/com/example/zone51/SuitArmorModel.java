package com.example.zone51;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

/** Модель брони с дополнительными 3D-деталями (текстура 64x64). */
public class SuitArmorModel extends HumanoidModel<LivingEntity> {
    public SuitArmorModel(ModelPart root) {
        super(root);
    }
}
