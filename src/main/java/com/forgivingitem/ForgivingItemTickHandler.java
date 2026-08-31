package com.forgivingitem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public final class ForgivingItemTickHandler {
    private static final String ACTIVE_KEY = ForgivingItem.MOD_ID + ".active";
    private static final String PREVIOUS_NO_GRAVITY_KEY = ForgivingItem.MOD_ID + ".previous_no_gravity";

    @SubscribeEvent
    public void onEntityTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof ItemEntity itemEntity)
                || !(itemEntity.level() instanceof ServerLevel)) {
            return;
        }

        CompoundTag persistentData = itemEntity.getPersistentData();
        boolean shouldLift = itemEntity.getY() < ForgivingItemConfig.PROTECTION_HEIGHT.get()
                && ProtectedItemMatcher.matches(itemEntity.getItem());

        if (shouldLift) {
            if (!persistentData.getBoolean(ACTIVE_KEY)) {
                persistentData.putBoolean(ACTIVE_KEY, true);
                persistentData.putBoolean(PREVIOUS_NO_GRAVITY_KEY, itemEntity.isNoGravity());
            }

            itemEntity.setNoGravity(true);
            Vec3 velocity = itemEntity.getDeltaMovement();
            itemEntity.setDeltaMovement(
                    velocity.x,
                    ForgivingItemConfig.RISE_SPEED.get(),
                    velocity.z);
        } else if (persistentData.getBoolean(ACTIVE_KEY)) {
            itemEntity.setNoGravity(persistentData.getBoolean(PREVIOUS_NO_GRAVITY_KEY));
            persistentData.remove(ACTIVE_KEY);
            persistentData.remove(PREVIOUS_NO_GRAVITY_KEY);
        }
    }
}
