package com.dumptruckman.dchest;

import com.dumptruckman.util.io.ConfigIO;
import com.dumptruckman.dchest.commands.DChestPluginCommand;
import com.dumptruckman.dchest.listeners.DChestBlockListener;
import com.dumptruckman.dchest.listeners.DChestEntityListener;
import com.dumptruckman.dchest.listeners.DChestInventoryListener;
import com.dumptruckman.dchest.listeners.DChestPlayerListener;
import com.dumptruckman.util.locale.Language;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.jar.JarFile;
import org.bukkit.util.config.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author dumptruckman
 */
public class DChest extends JavaPlugin {

    public static final Logger logger = Logger.getLogger("Minecraft.dChest");
    public static final String plugname = "dChest";

    //private final DChestPlayerListener playerListener = new DChestPlayerListener(this);
    private final DChestBlockListener blockListener = new DChestBlockListener(this);
    //private final DChestEntityListener entityListener = new DChestEntityListener(this);
    
    //TODO new Config
    public Configuration config;
    public Configuration chestConfig;
    public Configuration chestData;

    private Language lang;
    private Timer timer;

    public void onEnable(){
        // Make the data folders that dChest uses
        getDataFolder().mkdirs();

        // Grab the PluginManager
        final PluginManager pm = getServer().getPluginManager();
        // Loads the configuration file
        reload(false);

        // Start save timer
        timer = new Timer();
        timer.scheduleAtFixedRate(new SaveTimer(this),
                config.getInt("settings.datasavetimer", 300) * 1000,
                config.getInt("settings.datasavetimer", 300) * 1000);

        // Extracts default english language file
        try {
            JarFile jar = new JarFile(DChest.class.getProtectionDomain()
                    .getCodeSource().getLocation().getPath());
            ZipEntry entry = jar.getEntry("english.yml");
            File efile = new File(getDataFolder(), entry.getName());

            InputStream in =
                    new BufferedInputStream(jar.getInputStream(entry));
            OutputStream out =
                    new BufferedOutputStream(new FileOutputStream(efile));
            byte[] buffer = new byte[2048];
            for (;;)  {
                int nBytes = in.read(buffer);
                if (nBytes <= 0) break;
                out.write(buffer, 0, nBytes);
            }
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            logger.warning("Could not extract default language file!");
            if (config.getString("settings.languagefile").equalsIgnoreCase("english.yml")) {
                logger.severe("No alternate language file set!  Disabling " + plugname);
                pm.disablePlugin(this);
                return;
            }
        }

        // Load up language file
        lang = new Language(new File(this.getDataFolder(), config.getString("settings.languagefile")));

        // Register command executor for main plugin command
        getCommand("dchest").setExecutor(new DChestPluginCommand(this));

        if (pm.getPlugin("Spout") == null) {
            logger.info("Cannot find Spout. Disabling dChest");
            return;
        }

        // Register event listeners
        pm.registerEvent(Type.CUSTOM_EVENT, new DChestInventoryListener(this), Priority.Normal, this);
        pm.registerEvent(Type.PLAYER_INTERACT, new DChestPlayerListener(this), Priority.Highest, this);
        pm.registerEvent(Type.BLOCK_DAMAGE, blockListener, Priority.Highest, this);
        pm.registerEvent(Type.BLOCK_BREAK, blockListener, Priority.Highest, this);
        pm.registerEvent(Type.BLOCK_FADE, blockListener, Priority.Highest, this);
        pm.registerEvent(Type.BLOCK_BURN, blockListener, Priority.Highest, this);
        pm.registerEvent(Type.ENTITY_EXPLODE, new DChestEntityListener(this), Priority.Highest, this);

        // Display enable message/version info
        logger.info(plugname + " " + getDescription().getVersion() + " enabled.");
    }

    public void onDisable(){
        timer.cancel();
        saveFiles();
        logger.info(plugname + " " + getDescription().getVersion() + " disabled.");
    }
    public static boolean hasPerm(Player player, String perm){
        if(player.hasPermission(perm)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void saveConfig() {
        new ConfigIO(config).save();
    }

    public void saveChestData() {
        new ConfigIO(chestData).save();
    }

    public void saveChestConfig() {
        new ConfigIO(chestConfig).save();
    }

    public void saveConfigs() {
        saveConfig();
        saveChestConfig();
    }

    public void saveFiles() {
        saveConfig();
        saveChestData();
        saveChestConfig();
    }

    /*
     * Returns the dChest Data for the chest the player is targetting.
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
        config = new ConfigIO(new File(this.getDataFolder(), "config.yml")).load();
        chestConfig = new ConfigIO(new File(this.getDataFolder(), "chestconfig.yml")).load();
        if (!notify) {
            chestData = new ConfigIO(new File(this.getDataFolder(), "chestdata.yml")).load();
        }

        // Set/Verifies defaults
        if (config.getString("defaults.period") == null) {
            config.setProperty("defaults.period", "900");
        } else {
            try {
                Integer.parseInt(config.getString("defaults.period"));
            } catch (NumberFormatException nfe) {
                logger.warning("[" + plugname + "] invalid default period: "
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
                logger.warning("[" + plugname + "] invalid default period mode: "
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
                logger.warning("[" + plugname + "] invalid default restock mode: "
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
                logger.warning("[" + plugname + "] invalid preserve slots setting: "
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
                logger.warning("[" + plugname + "] invalid indestructible setting: "
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
                    logger.warning("[" + plugname + "] invalid playerlimit setting: "
                        + config.getString("defaults.playerlimit")
                        + "  Setting to default: -1");
                    config.setProperty("defaults.playerlimit", "-1");
                }
            } catch (NumberFormatException e) {
                logger.warning("[" + plugname + "] invalid playerlimit setting: "
                        + config.getString("defaults.playerlimit")
                        + "  Setting to default: -1");
                config.setProperty("defaults.playerlimit", "-1");
            }
        }
        if (config.getString("defaults.unique") == null) {
            config.setProperty("defaults.unique", "true");
        } else {
            if (!config.getString("defaults.unique").equalsIgnoreCase("false")
                    && !config.getString("defaults.unique").equalsIgnoreCase("true")) {
                logger.warning("[" + plugname + "] invalid unique setting: "
                        + config.getString("defaults.unique")
                        + "  Setting to default: true");
                config.setProperty("defaults.unique", "true");
            }
        }
        if (config.getString("settings.languagefile") == null) {
            config.setProperty("settings.languagefile", "english.yml");
        }
        if (config.getString("settings.datasavetimer") == null) {
            config.setProperty("settings.datasavetimer", 300);
        }

        convertOldConfig();

        saveFiles();

        if (notify) {
            logger.info("[" + plugname + "] reloaded configuration/data!");
        }
    }

    public void sendMessage(String path, CommandSender sender, String... args) {
        lang.sendMessage(lang.lang(path, args), sender);
    }

    public void convertOldConfig() {
        if (config.getKeys().contains("chests")) {
            logger.info("[" + plugname + "] Converting old data");
            List<String> worlds = config.getKeys("chests");
            if (worlds == null) return;
            for (int i = 0; i < worlds.size(); i++) {
                List<String> chests = config.getKeys("chests." + worlds.get(i));
                if (chests == null) break;
                for (int j = 0; j < chests.size(); j++) {
                    String chest = worlds.get(i) + "." + chests.get(i) + ".";
                    try {
                        chestConfig.setProperty(chest + "preserveslots", config.getString("chests." + chest + "preserveslots"));
                        config.removeProperty("chests." + chest + "preserveslots");
                    } catch (NullPointerException e) { }
                    try {
                        chestConfig.setProperty(chest + "playerlimit", config.getProperty("chests." + chest + "playerlimit"));
                        config.removeProperty("chests." + chest + "playerlimit");
                    } catch (NullPointerException e) { }
                    try {
                        chestData.setProperty(chest + "lastrestock", config.getProperty("chests." + chest + "lastrestock"));
                        config.removeProperty("chests." + chest + "lastrestock");
                    } catch (NullPointerException e) { }
                    try {
                        chestConfig.setProperty(chest + "items", config.getProperty("chests." + chest + "items"));
                        config.removeProperty("chests." + chest + "items");
                    } catch (NullPointerException e) { }
                    try {
                        chestConfig.setProperty(chest + "restockmode", config.getProperty("chests." + chest + "restockmode"));
                        config.removeProperty("chests." + chest + "restockmode");
                    } catch (NullPointerException e) { }
                    try {
                        chestConfig.setProperty(chest + "period", config.getProperty("chests." + chest + "period"));
                        config.removeProperty("chests." + chest + "period");
                    } catch (NullPointerException e) { }
                    try {
                        chestConfig.setProperty(chest + "indestructible", config.getProperty("chests." + chest + "indestructible"));
                        config.removeProperty("chests." + chest + "indestructible");
                    } catch (NullPointerException e) { }
                    try {
                        chestConfig.setProperty(chest + "periodmode", config.getProperty("chests." + chest + "periodmode"));
                        config.removeProperty("chests." + chest + "periodmode");
                    } catch (NullPointerException e) { }
                    try {
                        chestData.setProperty(chest + "players", config.getProperty("chests." + chest + "players"));
                        config.removeProperty("chests." + chest + "players");
                    } catch (NullPointerException e) { }
                    try {
                        chestConfig.setProperty(chest + "name", config.getProperty("chests." + chest + "name"));
                        config.removeProperty("chests." + chest + "name");
                    } catch (NullPointerException e) { }
                }
            }
        }
    }

    public void restockInventory(Inventory inventory, ChestData chest, List<ItemData> items) {
        //Inventory inventory = chest.getFullInventory();
        if (chest.getRestockMode().equalsIgnoreCase("replace")) {
            inventory.clear();
        }
        for (int i = 0; i < items.size(); i++) {
            if (chest.getPreserveSlots().equalsIgnoreCase("true")) {
                if (chest.getRestockMode().equalsIgnoreCase("add")) {
                    if (items.get(i).getType().equals(inventory
                            .getItem(items.get(i).getSlot()).getType()) &&
                            items.get(i).getDurability() == inventory
                            .getItem(items.get(i).getSlot()).getDurability()) {
                        int newamount = items.get(i).getAmount() + inventory
                            .getItem(items.get(i).getSlot()).getAmount();
                        if (newamount > 64) newamount = 64;
                        items.get(i).setAmount(newamount);
                    }
                }
                inventory.setItem(items.get(i).getSlot(), items.get(i));
            } else {
                inventory.addItem(items.get(i));
            }
        }
        //return inventory;
    }

    public Inventory getInventoryWithItems(Inventory inventory, List<ItemData> items) {
        for (int i = 0; i < items.size(); i++) {
            inventory.setItem(items.get(i).getSlot(), items.get(i));
        }
        return inventory;
    }
}
