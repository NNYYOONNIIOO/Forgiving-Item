package com.forgivingitem;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(ForgivingItem.MOD_ID)
public final class ForgivingItem {
    public static final String MOD_ID = "forgiving_item";

    public ForgivingItem() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ForgivingItemConfig.SPEC);
        MinecraftForge.EVENT_BUS.register(new ForgivingItemTickHandler());
    }
}
