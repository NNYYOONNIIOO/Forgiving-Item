package com.forgivingitem;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public final class ForgivingItemWorldHandler {
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.START || event.world.isRemote) {
            return;
        }

        World world = event.world;
        for (Object object : world.loadedEntityList) {
            if (!(object instanceof EntityItem)) {
                continue;
            }

            EntityItem itemEntity = (EntityItem) object;
            ItemStack stack = itemEntity.getEntityItem();
            if (!ProtectedItemMatcher.matches(stack) || itemEntity.posY > ForgivingItem.protectedHeight) {
                continue;
            }

            double remainingHeight = ForgivingItem.protectedHeight - itemEntity.posY;
            double riseThisTick = Math.min(ForgivingItem.protectedRiseSpeed, remainingHeight);
            itemEntity.motionY = 0.0D;
            itemEntity.moveEntity(0.0D, riseThisTick, 0.0D);
            itemEntity.motionY = 0.03999999910593033D;
        }
    }
}
