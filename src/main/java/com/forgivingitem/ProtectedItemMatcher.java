package com.forgivingitem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Locale;

public final class ProtectedItemMatcher {
    private ProtectedItemMatcher() {
    }

    public static boolean matches(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        String[] configuredItems = ForgivingItem.protectedItems;
        if (configuredItems == null) {
            return false;
        }
        for (String configured : configuredItems) {
            if (matches(stack, configured)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matches(ItemStack stack, String configured) {
        if (configured == null) {
            return false;
        }
        String entry = configured.trim().toLowerCase(Locale.ROOT);
        if (entry.isEmpty()) {
            return false;
        }

        String itemName = entry;
        int metadata = stack.getMetadata();
        int separator = entry.indexOf('#');
        if (separator >= 0) {
            itemName = entry.substring(0, separator).trim();
            String metadataText = entry.substring(separator + 1).trim();
            if (!"*".equals(metadataText)) {
                try {
                    if (metadata != Integer.parseInt(metadataText)) {
                        return false;
                    }
                } catch (NumberFormatException ignored) {
                    return false;
                }
            }
        }

        try {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
            return item != null && stack.getItem() == item;
        } catch (RuntimeException ignored) {
            return false;
        }
    }
}
