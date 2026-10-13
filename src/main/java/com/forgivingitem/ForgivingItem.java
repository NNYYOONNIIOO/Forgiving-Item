package com.forgivingitem;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Forgiving Item for Minecraft 1.12.2.
 *
 * <p>This mod is deliberately <b>not</b> annotated with {@code serverSideOnly = true}. In Forge
 * 1.12.2 that flag makes FML refuse to build the mod container at all on a client, which means
 * {@code preInit} never runs and the configuration file is never written. The supported way to
 * ship a server-side mod whose clients do not need it installed is
 * {@code acceptableRemoteVersions = "*"}.</p>
 */
@Mod(
        modid = ForgivingItem.MOD_ID,
        name = ForgivingItem.MOD_NAME,
        version = ForgivingItem.VERSION,
        acceptableRemoteVersions = "*",
        acceptedMinecraftVersions = "[1.12.2]",
        dependencies = "required-after:mixinbooter"
)
public final class ForgivingItem {
    public static final String MOD_ID = "forgiving_item";
    public static final String MOD_NAME = "Forgiving Item";
    public static final String VERSION = "1.0.1";

    static final String DEFAULT_ITEM = "appliedenergistics2:encoded_pattern";
    static final String FALLBACK_CONFIG_NAME = "forgiving_item.cfg";

    public static int protectedHeight = 1;
    public static double protectedRiseSpeed = 0.05D;
    public static String[] protectedItems = new String[]{DEFAULT_ITEM};

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static Configuration configuration;
    private static File configurationFile;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configurationFile = resolveConfigurationFile(event);
        configuration = new Configuration(configurationFile);
        loadConfiguration();
    }

    /**
     * Uses the file suggested by Forge and falls back to {@code config/forgiving_item.cfg} if Forge
     * could not provide one, so that a configuration file is always produced.
     */
    private static File resolveConfigurationFile(FMLPreInitializationEvent event) {
        File suggested = event.getSuggestedConfigurationFile();
        if (suggested != null) {
            return suggested;
        }
        LOGGER.warn("Forge did not suggest a configuration file, falling back to config/{}", FALLBACK_CONFIG_NAME);
        return new File(new File(".", "config"), FALLBACK_CONFIG_NAME).getAbsoluteFile();
    }

    static void loadConfiguration() {
        if (configuration == null || configurationFile == null) {
            LOGGER.warn("Configuration was not initialised, skipping config load.");
            return;
        }

        File parent = configurationFile.getParentFile();
        if (parent != null && !parent.isDirectory() && !parent.mkdirs() && !parent.isDirectory()) {
            LOGGER.error("Could not create the config directory {}", parent.getAbsolutePath());
        }

        boolean fileWasMissing = !configurationFile.isFile();
        try {
            configuration.load();
        } catch (Exception e) {
            LOGGER.error("Could not read {}, falling back to the bundled defaults", configurationFile.getAbsolutePath(), e);
        }

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
                new String[]{DEFAULT_ITEM},
                "Registry names of item stacks to protect. Use modid:item_name or modid:item_name#metadata; use * as metadata to match every variant."
        );

        if (fileWasMissing || configuration.hasChanged() || !configurationFile.isFile() || configurationFile.length() == 0L) {
            try {
                configuration.save();
            } catch (Exception e) {
                LOGGER.error("Could not write {}", configurationFile.getAbsolutePath(), e);
            }
        }

        if (!configurationFile.isFile() || configurationFile.length() == 0L) {
            LOGGER.error("Config file {} is still missing, make sure the config directory is writable.",
                    configurationFile.getAbsolutePath());
        } else {
            LOGGER.info("Loaded {} protected item pattern(s) at Y <= {} from {}",
                    protectedItems.length, protectedHeight, configurationFile.getAbsolutePath());
        }
    }
}
