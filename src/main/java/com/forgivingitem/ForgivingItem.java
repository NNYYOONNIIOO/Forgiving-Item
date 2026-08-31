package com.forgivingitem;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(
        modid = ForgivingItem.MOD_ID,
        name = ForgivingItem.MOD_NAME,
        version = ForgivingItem.VERSION,
        acceptedMinecraftVersions = "[1.12.2]",
        dependencies = "required-after:mixinbooter"
)
@Mod.EventBusSubscriber(modid = ForgivingItem.MOD_ID)
public final class ForgivingItem {
    public static final String MOD_ID = "forgiving_item";
    public static final String MOD_NAME = "Forgiving Item";
    public static final String VERSION = "1.0.0";

    public static int protectedHeight = 1;
    public static double protectedRiseSpeed = 0.05D;
    public static String[] protectedItems = new String[]{"appliedenergistics2:encoded_pattern"};

    private static Configuration configuration;
    private static File configurationFile;
    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        configurationFile = event.getSuggestedConfigurationFile();
        configuration = new Configuration(configurationFile);
        loadConfiguration();
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (MOD_ID.equals(event.getModID()) && configuration != null) {
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        configuration.load();
        protectedHeight = configuration.getInt(
                "protectedHeight",
                "general",
                1,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                "Item entities at or below this Y coordinate are protected from falling into the void."
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
                new String[]{"appliedenergistics2:encoded_pattern"},
                "Registry names of item stacks to protect. Use modid:item_name or modid:item_name#metadata; use * as metadata to match every variant."
        );
        if (configuration.hasChanged()) {
            configuration.save();
        }
        if (logger != null) {
            logger.info("Loaded {} protected item pattern(s) at Y <= {}", protectedItems.length, protectedHeight);
        }
    }
}
