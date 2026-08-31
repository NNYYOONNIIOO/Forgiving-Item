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

    @Inject(method = "onUpdate", at = @At("HEAD"))
    private void forgivingItem$protectFromVoid(CallbackInfo callbackInfo) {
        EntityItem itemEntity = (EntityItem) (Object) this;
        ItemStack stack = itemEntity.getEntityItem();
        if (!ProtectedItemMatcher.matches(stack) || itemEntity.posY > ForgivingItem.protectedHeight) {
            return;
        }

        double remainingHeight = ForgivingItem.protectedHeight - itemEntity.posY;
        double riseThisTick = Math.min(ForgivingItem.protectedRiseSpeed, remainingHeight);

        // EntityItem applies gravity later in this update. Compensate for it so
        // vanilla moveEntity performs the actual slow rise and block collision.
        itemEntity.motionY = riseThisTick + ENTITY_GRAVITY;
    }
}
