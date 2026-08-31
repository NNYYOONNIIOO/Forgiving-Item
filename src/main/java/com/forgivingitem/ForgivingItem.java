package com.forgivingitem;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = ForgivingItem.MOD_ID,
        name = ForgivingItem.MOD_NAME,
        version = ForgivingItem.VERSION
)
public final class ForgivingItem {
    public static final String MOD_ID = "forgiving_item";
    public static final String MOD_NAME = "Forgiving Item";
    public static final String VERSION = "1.0.0";

    public static int protectedHeight = 1;
    public static double protectedRiseSpeed = 0.05D;
    public static String[] protectedItems = new String[]{"appliedenergistics2:item.ItemEncodedPattern"};

    private static Configuration configuration;
    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        configuration = new Configuration(event.getSuggestedConfigurationFile());
        loadConfiguration();
        ForgivingItemWorldHandler handler = new ForgivingItemWorldHandler();
        FMLCommonHandler.instance().bus().register(handler);
        MinecraftForge.EVENT_BUS.register(handler);
    }

    private static void loadConfiguration() {
        configuration.load();
        protectedHeight = configuration.getInt(
                "protectedHeight",
                "general",
                1,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                "Configured item entities at or below this Y coordinate are moved upward."
        );
        protectedRiseSpeed = configuration.getFloat(
                "riseSpeed",
                "general",
                0.05F,
                0.001F,
                1.0F,
                "Upward movement in blocks per tick while a protected item is below protectedHeight."
        );
        protectedItems = configuration.getStringList(
                "protectedItems",
                "items",
                new String[]{"appliedenergistics2:item.ItemEncodedPattern"},
                "Registry names to protect. Use modid:item_name or modid:item_name#metadata; use * for every metadata value."
        );
        if (configuration.hasChanged()) {
            configuration.save();
        }
        if (logger != null) {
            logger.info("Loaded {} protected item pattern(s) at Y <= {}", protectedItems.length, protectedHeight);
        }
    }
}
