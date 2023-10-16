package com.ultreon.mods.chunkyguns.mixin.common;

import com.ultreon.mods.chunkyguns.item.GasMaskItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Shadow @Final public DefaultedList<ItemStack> armor;

    @Redirect(method = "damageArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
    private void damageArmor(ItemStack instance, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
        int index = PlayerInventory.HELMET_SLOTS[0];
        if (!(armor.get(index).getItem() instanceof GasMaskItem)) {
            instance.damage(amount, entity, (player) -> {
                player.sendEquipmentBreakStatus(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, index));
            });
        }
    }
}
