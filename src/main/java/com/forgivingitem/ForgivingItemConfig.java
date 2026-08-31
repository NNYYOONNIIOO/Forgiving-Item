package com.forgivingitem;

import java.util.List;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ForgivingItemConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.DoubleValue PROTECTION_HEIGHT;
    public static final ModConfigSpec.DoubleValue RISE_SPEED;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> PROTECTED_ITEMS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Forgiving Item settings").push("forgiving_item");

        PROTECTION_HEIGHT = builder
                .comment("Configured items below this Y level are lifted instead of falling into the void.")
                .defineInRange("height", -63.0D, -3.2E7D, 3.2E7D);

        RISE_SPEED = builder
                .comment("Vertical movement per tick while a configured item is being lifted.")
                .defineInRange("rise_speed", 0.05D, 0.001D, 1.0D);

        PROTECTED_ITEMS = builder
                .comment(
                        "Item registry IDs to protect. Applied Energistics 2 is optional; unknown IDs are ignored.",
                        "The list may be edited after the config file is generated.")
                .defineListAllowEmpty(
                        "items",
                        List.of(
                                "ae2:crafting_pattern",
                                "ae2:processing_pattern",
                                "ae2:smithing_table_pattern",
                                "ae2:stonecutting_pattern"),
                        () -> "",
                        value -> value instanceof String && !((String) value).isBlank());

        builder.pop();
        SPEC = builder.build();
    }

    private ForgivingItemConfig() {
    }
}
