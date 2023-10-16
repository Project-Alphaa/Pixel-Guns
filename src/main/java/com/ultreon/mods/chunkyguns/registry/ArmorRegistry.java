package com.ultreon.mods.chunkyguns.registry;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public enum ArmorRegistry implements ArmorMaterial {

    ARMORED(
        "armored",
        new DurabilityAmounts(0, 225, 0, 0),
        new ProtectionAmounts(0, 10, 0, 0),
        15,
        SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
        0.0f,
        0.0f,
        Ingredient.empty()
    ),

    GREEN_ARMORED(
            "green_armored",
            new DurabilityAmounts(0, 225, 0, 0),
            new ProtectionAmounts(0, 0, 0, 0),
            0,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            0.0f,
            0.0f,
            Ingredient.empty()
    ),

    HAZARD(
        "hazard",
        new DurabilityAmounts(2400, 0, 0, 0),
        new ProtectionAmounts(0, 0, 0, 0),
        15,
        SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
        0.0f,
        0.0f,
        Ingredient.empty()
    );

    private final String name;
    private final DurabilityAmounts durabilityAmounts;
    private final ProtectionAmounts protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Ingredient ingredient;

    record DurabilityAmounts(int helmet, int chestplate, int leggings, int boots) {}

    record ProtectionAmounts(int helmet, int chestplate, int leggings, int boots) {}


    ArmorRegistry(String name, DurabilityAmounts durabilityAmounts, ProtectionAmounts protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Ingredient ingredient) {
        this.name = name;
        this.durabilityAmounts = durabilityAmounts;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.ingredient = ingredient;
    }

    @Override
    public int getDurability(EquipmentSlot slot) {
        return switch (slot) {
            case FEET -> this.durabilityAmounts.boots;
            case LEGS -> this.durabilityAmounts.leggings;
            case CHEST -> this.durabilityAmounts.chestplate;
            case HEAD -> this.durabilityAmounts.helmet;
            default -> 0;
        };
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return switch (slot) {
            case FEET -> this.protectionAmounts.boots;
            case LEGS -> this.protectionAmounts.leggings;
            case CHEST -> this.protectionAmounts.chestplate;
            case HEAD -> this.protectionAmounts.helmet;
            default -> 0;
        };
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.ingredient;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}