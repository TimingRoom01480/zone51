package com.example.zone51;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;

public class ModTiers {
    public static final Tier ANCIENT = new ForgeTier(4, 3500, 9.0f, 4.0f, 20,
            Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(ModItems.ENERGY_SHARD.get()));
    public static final Tier ENERGY = new ForgeTier(3, 200, 8.0f, 3.0f, 15,
            BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.EMPTY);
}
