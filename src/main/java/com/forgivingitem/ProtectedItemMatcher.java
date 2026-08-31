package com.forgivingitem;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public final class ProtectedItemMatcher {
    private ProtectedItemMatcher() {
    }

    public static boolean matches(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        ResourceLocation registeredId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (registeredId == null) {
            return false;
        }

        String actualId = registeredId.toString();
        for (String configuredId : ForgivingItemConfig.PROTECTED_ITEMS.get()) {
            if (configuredId != null && actualId.equalsIgnoreCase(configuredId.trim())) {
                return true;
            }
        }
        return false;
    }
}
