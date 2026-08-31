package com.forgivingitem.mixin;

import com.forgivingitem.ForgivingItem;
import com.forgivingitem.ProtectedItemMatcher;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityItem.class)
public abstract class MixinEntityItem {
    private static final double ENTITY_GRAVITY = 0.03999999910593033D;

    @Inject(method = "func_70071_h_", at = @At("HEAD"), remap = false)
    private void forgivingItem$protectFromVoid(CallbackInfo callbackInfo) {
        EntityItem itemEntity = (EntityItem) (Object) this;
        ItemStack stack = itemEntity.getItem();
        if (!ProtectedItemMatcher.matches(stack)) {
            return;
        }

        if (itemEntity.posY <= ForgivingItem.protectedHeight) {
            double remainingHeight = ForgivingItem.protectedHeight - itemEntity.posY;
            double riseThisTick = Math.min(ForgivingItem.protectedRiseSpeed, remainingHeight);

            // EntityItem applies gravity later in this update. Compensate for it
            // so the actual movement remains the configured upward speed.
            itemEntity.motionY = riseThisTick + ENTITY_GRAVITY;
        }
    }
}
