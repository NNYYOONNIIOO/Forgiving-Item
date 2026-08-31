package com.forgivingitem;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public final class ForgivingItemTickHandler {
    @SubscribeEvent
    public void onEntityTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof ItemEntity itemEntity) || itemEntity.level().isClientSide()) {
            return;
        }

        boolean shouldLift = itemEntity.getY() < ForgivingItemConfig.PROTECTION_HEIGHT.get()
                && ProtectedItemMatcher.matches(itemEntity.getItem());
        if (shouldLift) {
            itemEntity.setNoGravity(true);
            Vec3 velocity = itemEntity.getDeltaMovement();
            itemEntity.setDeltaMovement(velocity.x, ForgivingItemConfig.RISE_SPEED.get(), velocity.z);
        } else if (itemEntity.isNoGravity()) {
            itemEntity.setNoGravity(false);
        }
    }
}
