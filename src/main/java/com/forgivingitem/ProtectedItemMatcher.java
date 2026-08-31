package com.forgivingitem;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Locale;

public final class ProtectedItemMatcher {
    private ProtectedItemMatcher() {
    }

    public static boolean matches(ItemStack stack) {
        if (stack == null || stack.getItem() == null || stack.stackSize <= 0) {
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
        String entry = configured.trim();
        if (entry.length() == 0) {
            return false;
        }

        String itemName = entry;
        int metadata = stack.getItemDamage();
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

        if ("appliedenergistics2:encoded_pattern".equalsIgnoreCase(itemName)) {
            itemName = "appliedenergistics2:item.ItemEncodedPattern";
        }

        int namespaceSeparator = itemName.indexOf(':');
        if (namespaceSeparator <= 0 || namespaceSeparator == itemName.length() - 1) {
            return false;
        }
        String modId = itemName.substring(0, namespaceSeparator).toLowerCase(Locale.ROOT);
        String registryName = itemName.substring(namespaceSeparator + 1);
        try {
            Item configuredItem = GameRegistry.findItem(modId, registryName);
            if (configuredItem == null && registryName.startsWith("item.")) {
                configuredItem = GameRegistry.findItem(modId, registryName.substring("item.".length()));
            }
            return configuredItem != null && stack.getItem() == configuredItem;
        } catch (RuntimeException ignored) {
            return false;
        }
    }
}
