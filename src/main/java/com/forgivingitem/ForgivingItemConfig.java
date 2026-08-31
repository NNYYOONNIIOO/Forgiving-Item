package com.forgivingitem;

import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ForgivingItemConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.DoubleValue PROTECTION_HEIGHT;
    public static final ForgeConfigSpec.DoubleValue RISE_SPEED;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> PROTECTED_ITEMS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Forgiving Item settings").push("forgiving_item");

        PROTECTION_HEIGHT = builder
                .comment("Configured items below this Y level are lifted instead of falling into the void.")
                .defineInRange("height", -63.0D, -3.2E7D, 3.2E7D);

        RISE_SPEED = builder
                .comment("Vertical movement per tick while a configured item is being lifted.")
                .defineInRange("rise_speed", 0.05D, 0.001D, 1.0D);

        PROTECTED_ITEMS = builder
                .comment(
                        "Item registry IDs to protect. Applied Energistics 2 is optional; unknown IDs are simply ignored.",
                        "The legacy 1.7.10 ID and modern Applied Energistics 2 IDs are accepted.")
                .defineListAllowEmpty(
                        "items",
                        List.of(
                                "ae2:crafting_pattern",
                                "ae2:processing_pattern",
                                "ae2:smithing_table_pattern",
                                "ae2:stonecutting_pattern"),
                        value -> value instanceof String && !((String) value).isBlank());

        builder.pop();
        SPEC = builder.build();
    }

    private ForgivingItemConfig() {
    }
}
