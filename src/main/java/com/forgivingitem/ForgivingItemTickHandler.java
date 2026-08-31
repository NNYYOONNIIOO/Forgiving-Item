package com.forgivingitem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ForgivingItemTickHandler {
    private static final String ACTIVE_KEY = ForgivingItem.MOD_ID + ".active";
    private static final String PREVIOUS_NO_GRAVITY_KEY = ForgivingItem.MOD_ID + ".previous_no_gravity";

    @SubscribeEvent
    public void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !(event.level instanceof ServerLevel serverLevel)) {
            return;
        }

        for (Entity entity : serverLevel.getAllEntities()) {
            if (entity instanceof ItemEntity itemEntity) {
                updateItem(itemEntity);
            }
        }
    }

    private static void updateItem(ItemEntity itemEntity) {
        CompoundTag persistentData = itemEntity.getPersistentData();
        boolean shouldLift = itemEntity.getY() < ForgivingItemConfig.PROTECTION_HEIGHT.get()
                && ProtectedItemMatcher.matches(itemEntity.getItem());

        if (shouldLift) {
            if (!persistentData.getBoolean(ACTIVE_KEY)) {
                persistentData.putBoolean(ACTIVE_KEY, true);
                persistentData.putBoolean(PREVIOUS_NO_GRAVITY_KEY, itemEntity.isNoGravity());
            }

            itemEntity.setNoGravity(true);
            itemEntity.setDeltaMovement(
                    itemEntity.getDeltaMovement().x,
                    ForgivingItemConfig.RISE_SPEED.get(),
                    itemEntity.getDeltaMovement().z);
        } else if (persistentData.getBoolean(ACTIVE_KEY)) {
            itemEntity.setNoGravity(persistentData.getBoolean(PREVIOUS_NO_GRAVITY_KEY));
            persistentData.remove(ACTIVE_KEY);
            persistentData.remove(PREVIOUS_NO_GRAVITY_KEY);
        }
    }
}
