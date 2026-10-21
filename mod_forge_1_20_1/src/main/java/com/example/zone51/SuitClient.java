package com.example.zone51;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/** Клиентская часть: подставляет 3D-модель брони для Древнего Человека и Карточника. */
public final class SuitClient {
    public static IClientItemExtensions create(SuitType suit) {
        final ModelLayerLocation layer = SuitModels2.layerOf(suit);
        return new IClientItemExtensions() {
            private SuitArmorModel model;

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                if (slot == EquipmentSlot.LEGS) return original; // поножи: обычная модель, текстура layer_2
                if (model == null) {
                    model = new SuitArmorModel(Minecraft.getInstance().getEntityModels().bakeLayer(layer));
                }
                return model;
            }
        };
    }
}
