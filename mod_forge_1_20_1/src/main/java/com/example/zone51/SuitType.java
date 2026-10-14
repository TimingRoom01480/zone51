package com.example.zone51;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public enum SuitType implements ArmorMaterial {
    GUARD("guard", false, 22, new int[]{2, 5, 6, 2}, 12, 1.0f, 0.0f),
    LAB("lab", false, 15, new int[]{1, 3, 4, 1}, 15, 0.0f, 0.0f),
    EXO("exo", true, 37, new int[]{3, 6, 8, 3}, 10, 2.0f, 0.0f),
    MUTANT("mutant", false, 30, new int[]{3, 6, 7, 3}, 9, 1.0f, 0.1f),
    ANCIENT("ancient_man", true, 40, new int[]{4, 7, 9, 4}, 20, 3.0f, 0.1f),
    CARDSHARP("cardsharp", false, 18, new int[]{2, 5, 6, 2}, 18, 0.0f, 0.0f);

    public final String id;
    public final boolean energy;
    private final int mult;
    private final int[] defense; // ботинки, поножи, нагрудник, шлем
    private final int enchant;
    private final float toughness;
    private final float knockback;
    private static final int[] BASE = {13, 15, 16, 11};

    SuitType(String id, boolean energy, int mult, int[] defense, int enchant, float toughness, float knockback) {
        this.id = id;
        this.energy = energy;
        this.mult = mult;
        this.defense = defense;
        this.enchant = enchant;
        this.toughness = toughness;
        this.knockback = knockback;
    }

    private static int idx(ArmorItem.Type t) {
        return switch (t) {
            case BOOTS -> 0;
            case LEGGINGS -> 1;
            case CHESTPLATE -> 2;
            case HELMET -> 3;
        };
    }

    @Override public int getDurabilityForType(ArmorItem.Type t) { return BASE[idx(t)] * mult; }
    @Override public int getDefenseForType(ArmorItem.Type t) { return defense[idx(t)]; }
    @Override public int getEnchantmentValue() { return enchant; }
    @Override public SoundEvent getEquipSound() { return SoundEvents.ARMOR_EQUIP_IRON; }
    @Override public Ingredient getRepairIngredient() { return Ingredient.of(ModItems.ENERGY_SHARD.get()); }
    @Override public String getName() { return Zone51.MODID + ":" + id; }
    @Override public float getToughness() { return toughness; }
    @Override public float getKnockbackResistance() { return knockback; }
}
