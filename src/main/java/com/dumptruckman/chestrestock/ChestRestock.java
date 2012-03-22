package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.commands.ChestRestockPluginCommand;
import com.dumptruckman.chestrestock.listeners.ChestRestockBlockListener;
import com.dumptruckman.chestrestock.listeners.ChestRestockEntityListener;
import com.dumptruckman.chestrestock.listeners.ChestRestockPlayerListener;
import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
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

    public void onEnable(){
        // Make the data folders that ChestRestock uses
        getDataFolder().mkdirs();

        // Loads the configuration file
        reload(false);

        // Register command executore for main plugin command
        getCommand("chestrestock").setExecutor(new ChestRestockPluginCommand(this));

        // Register event listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(entityListener, this);

        // Display enable message/version info
        logger.info("ChestRestock " + getDescription().getVersion() + " enabled.");
    }

    public void onDisable(){
        saveConfig();
        logger.info("ChestRestock " + getDescription().getVersion() + " disabled.");
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
        reloadConfig();

        // Set/Verifies defaults
        if (getConfig().getString("defaults.period") == null) {
            System.out.println("Setting a default");
            getConfig().set("defaults.period", "900");
        } else {
            try {
                Integer.parseInt(getConfig().getString("defaults.period"));
            } catch (NumberFormatException nfe) {
                logger.warning("ChestRestock: invalid default period: "
                        + getConfig().getString("defaults.period")
                        + "  Setting to default: 900");
                getConfig().set("defaults.period", 900);
            }
        }
        if (getConfig().getString("defaults.periodmode") == null) {
            getConfig().set("defaults.periodmode", "player");
        } else {
            if (!getConfig().getString("defaults.periodmode").equalsIgnoreCase("player")
                    && !getConfig().getString("defaults.periodmode").equalsIgnoreCase("settime")) {
                logger.warning("ChestRestock: invalid default period mode: "
                        + getConfig().getString("defaults.periodmode")
                        + "  Setting to default: player");
                getConfig().set("defaults.periodmode", "player");
            }
        }
        if (getConfig().getString("defaults.restockmode") == null) {
            getConfig().set("defaults.restockmode", "replace");
        } else {
            if (!getConfig().getString("defaults.restockmode").equalsIgnoreCase("replace")
                    && !getConfig().getString("defaults.restockmode").equalsIgnoreCase("add")) {
                logger.warning("ChestRestock: invalid default restock mode: "
                        + getConfig().getString("defaults.restockmode")
                        + "  Setting to default: replace");
                getConfig().set("defaults.restockmode", "replace");
            }
        }
        if (getConfig().getString("defaults.preserveslots") == null) {
            getConfig().set("defaults.preserveslots", "false");
        } else {
            if (!getConfig().getString("defaults.preserveslots").equalsIgnoreCase("false")
                    && !getConfig().getString("defaults.preserveslots").equalsIgnoreCase("true")) {
                logger.warning("ChestRestock: invalid preserve slots setting: "
                        + getConfig().getString("defaults.preserveslots")
                        + "  Setting to default: false");
                getConfig().set("defaults.preserveslots", "false");
            }
        }
        if (getConfig().getString("defaults.indestructible") == null) {
            getConfig().set("defaults.indestructible", "true");
        } else {
            if (!getConfig().getString("defaults.indestructible").equalsIgnoreCase("false")
                    && !getConfig().getString("defaults.indestructible").equalsIgnoreCase("true")) {
                logger.warning("ChestRestock: invalid indestructible setting: "
                        + getConfig().getString("defaults.indestructible")
                        + "  Setting to default: true");
                getConfig().set("defaults.indestructible", "true");
            }
        }
        if (getConfig().getString("defaults.playerlimit") == null) {
            getConfig().set("defaults.playerlimit", "-1");
        } else {
            try {
                int playerlimit = Integer.parseInt(getConfig().getString("defaults.playerlimit"));
                if (playerlimit < -1) {
                    logger.warning("ChestRestock: invalid playerlimit setting: "
                        + getConfig().getString("defaults.playerlimit")
                        + "  Setting to default: -1");
                    getConfig().set("defaults.playerlimit", "-1");
                }
            } catch (NumberFormatException e) {
                logger.warning("ChestRestock: invalid playerlimit setting: "
                        + getConfig().getString("defaults.playerlimit")
                        + "  Setting to default: -1");
                getConfig().set("defaults.playerlimit", "-1");
            }
        }

        saveConfig();

        if (notify) {
            logger.info("ChestRestock: reloaded configuration/data!");
        }
    }
}
