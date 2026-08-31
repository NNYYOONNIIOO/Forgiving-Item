package com.forgivingitem;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ProtectedItemMatcher {
    private ProtectedItemMatcher() {
    }

    public static boolean matches(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        String actualId = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        for (String configuredId : ForgivingItemConfig.PROTECTED_ITEMS.get()) {
            if (configuredId != null && actualId.equalsIgnoreCase(configuredId.trim())) {
                return true;
            }
        }
        return false;
    }
}
