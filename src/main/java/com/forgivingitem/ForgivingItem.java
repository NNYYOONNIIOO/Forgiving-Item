package com.forgivingitem;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

@Mod(ForgivingItem.MOD_ID)
public final class ForgivingItem {
    public static final String MOD_ID = "forgiving_item";

    public ForgivingItem(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, ForgivingItemConfig.SPEC);
        NeoForge.EVENT_BUS.register(new ForgivingItemTickHandler());
    }
}
