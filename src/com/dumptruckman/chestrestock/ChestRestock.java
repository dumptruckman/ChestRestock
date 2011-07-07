package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.commands.ChestRestockPluginCommand;
import com.dumptruckman.chestrestock.listeners.ChestRestockBlockListener;
import com.dumptruckman.chestrestock.listeners.ChestRestockEntityListener;
import com.dumptruckman.chestrestock.listeners.ChestRestockPlayerListener;
import java.io.File;
import org.bukkit.util.config.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 *
 * @author dumptruckman
 */
public class ChestRestock extends JavaPlugin {

    private static final Logger logger = Logger.getLogger("Minecraft.ChestRestock");
    private final ChestRestockPlayerListener playerListener = new ChestRestockPlayerListener(this);
    private final ChestRestockBlockListener blockListener = new ChestRestockBlockListener(this);
    private final ChestRestockEntityListener entityListener = new ChestRestockEntityListener(this);
    
    public Configuration config;

    public void onEnable(){
        // Display enable message/version info
        logger.info("ChestRestock " + getDescription().getVersion() + " enabled.");

        // Make the data folders that ChestRestock uses
        getDataFolder().mkdirs();

        // Loads the configuration file
        config = getConfiguration();
        reload(false);

        // Register command executore for main plugin command
        getCommand("chestrestock").setExecutor(new ChestRestockPluginCommand(this));

        // Register event listeners
        this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
        //this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INVENTORY, playerListener, Event.Priority.Normal, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Event.Priority.Highest, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Highest, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_FADE, blockListener, Event.Priority.Highest, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BURN, blockListener, Event.Priority.Highest, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_EXPLODE, entityListener, Event.Priority.Highest, this);

    }

    public void onDisable(){
        config.save();
        logger.info("ChestRestock " + getDescription().getVersion() + " disabled.");
    }

    public void loadConfig() {
        // Creates the file first if non-existant
        if(!(new File(this.getDataFolder(), "config.yml")).exists()) config.save();
        config.load();

        if (config.getProperty("chests") != null) {

        } else {
            config.setProperty("chests", null);
        }
    }

    /*
     * Returns the ChestRestock Data for the chest the player is targetting.
     * If the player is not targetting a chest, returns null.
     */
    public ChestData getTargetedChest(CommandSender sender) {
        Player player = (Player)sender;
        Block block = player.getTargetBlock(null, 60);
        if (block.getType() == Material.CHEST) {
            return new ChestData(block, this);
        } else {
            return null;
        }
    }

    public void reload(boolean notify) {
        loadConfig();

        // Set/Verifies defaults
        if (config.getString("defaults.period") == null) {
            config.setProperty("defaults.period", "900");
        } else {
            try {
                Integer.parseInt(config.getString("defaults.period"));
            } catch (NumberFormatException nfe) {
                logger.warning("ChestRestock: invalid default period: "
                        + config.getString("defaults.period")
                        + "  Setting to default: 900");
                config.setProperty("defaults.period", 900);
            }
        }
        if (config.getString("defaults.periodmode") == null) {
            config.setProperty("defaults.periodmode", "player");
        } else {
            if (!config.getString("defaults.periodmode").equalsIgnoreCase("player")
                    && !config.getString("defaults.periodmode").equalsIgnoreCase("settime")) {
                logger.warning("ChestRestock: invalid default period mode: "
                        + config.getString("defaults.periodmode")
                        + "  Setting to default: player");
                config.setProperty("defaults.periodmode", "player");
            }
        }
        if (config.getString("defaults.restockmode") == null) {
            config.setProperty("defaults.restockmode", "replace");
        } else {
            if (!config.getString("defaults.restockmode").equalsIgnoreCase("replace")
                    && !config.getString("defaults.restockmode").equalsIgnoreCase("add")) {
                logger.warning("ChestRestock: invalid default restock mode: "
                        + config.getString("defaults.restockmode")
                        + "  Setting to default: replace");
                config.setProperty("defaults.restockmode", "replace");
            }
        }
        if (config.getString("defaults.preserveslots") == null) {
            config.setProperty("defaults.preserveslots", "false");
        } else {
            if (!config.getString("defaults.preserveslots").equalsIgnoreCase("false")
                    && !config.getString("defaults.preserveslots").equalsIgnoreCase("true")) {
                logger.warning("ChestRestock: invalid preserve slots setting: "
                        + config.getString("defaults.preserveslots")
                        + "  Setting to default: false");
                config.setProperty("defaults.preserveslots", "false");
            }
        }
        if (config.getString("defaults.indestructible") == null) {
            config.setProperty("defaults.indestructible", "true");
        } else {
            if (!config.getString("defaults.indestructible").equalsIgnoreCase("false")
                    && !config.getString("defaults.indestructible").equalsIgnoreCase("true")) {
                logger.warning("ChestRestock: invalid indestructible setting: "
                        + config.getString("defaults.indestructible")
                        + "  Setting to default: true");
                config.setProperty("defaults.indestructible", "true");
            }
        }
        if (config.getString("defaults.playerlimit") == null) {
            config.setProperty("defaults.playerlimit", "-1");
        } else {
            try {
                int playerlimit = Integer.parseInt(config.getString("defaults.playerlimit"));
                if (playerlimit < -1) {
                    logger.warning("ChestRestock: invalid playerlimit setting: "
                        + config.getString("defaults.playerlimit")
                        + "  Setting to default: -1");
                    config.setProperty("defaults.playerlimit", "-1");
                }
            } catch (NumberFormatException e) {
                logger.warning("ChestRestock: invalid playerlimit setting: "
                        + config.getString("defaults.playerlimit")
                        + "  Setting to default: -1");
                config.setProperty("defaults.playerlimit", "-1");
            }
        }

        config.save();

        if (notify) {
            logger.info("ChestRestock: reloaded configuration/data!");
        }
    }
}
