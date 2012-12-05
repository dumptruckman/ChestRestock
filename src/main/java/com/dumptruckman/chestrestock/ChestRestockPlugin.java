package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.api.CRConfig;
import com.dumptruckman.chestrestock.api.CRDefaults;
import com.dumptruckman.chestrestock.api.ChestManager;
import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.plugin.command.HelpCommand;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import loottables.LootConfig;
import loottables.LootTables;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChestRestockPlugin extends AbstractBukkitPlugin implements ChestRestock {

    private final static String CMD_PREFIX = "cr";

    private ChestManager chestManager = null;
    private LootConfig lootConfig = null;
    private Map<String, CRDefaults> defaultsMap = null;
    private File defaultsFolder;
    private File defaultsFile;

    @Override
    protected Properties getNewConfig() throws IOException {
        return new YamlChestRestockConfig(new File(getDataFolder(), "config.yml"));
    }

    @Override
    public String getCommandPrefix() {
        return CMD_PREFIX;
    }

    @Override
    protected boolean useDatabase() {
        return false;
    }

    private CRDefaults newDefaultsConfig(File file) throws IOException {
        return newDefaultsConfig(file, true);
    }

    private CRDefaults newDefaultsConfig(File file, boolean autoDefault) throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            file.getParentFile().mkdirs();
        }
        return new YamlDefaultsConfig(autoDefault, file);
    }

    @Override
    public void onPluginLoad() {
        defaultsFolder = new File(getDataFolder(), "world_defaults");
        defaultsFile = new File(getDataFolder(), "global_defaults.yml");
        defaultsFolder.mkdirs();
        Language.init();
        HelpCommand.addStaticPrefixedKey("");
    }

    @Override
    public void onPluginEnable() {
        initializeConfigObjects();
        if (getDefaults(null) == null) {
            Logging.severe("Cannot continue without global_defaults.yml!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        migrateDefaults();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ChestRestockListener(this), this);
        if (pm.getPlugin("Multiverse-Adventure") != null) {
            pm.registerEvents(new AdventureListener(this), this);
        }
        initializeTasks();
    }

    @Override
    protected void registerCommands() {
        /*
        getCommandHandler().registerCommand(new CreateCommand(this));
        getCommandHandler().registerCommand(new UpdateCommand(this));
        getCommandHandler().registerCommand(new CheckCommand(this));
        getCommandHandler().registerCommand(new DisableCommand(this));
        getCommandHandler().registerCommand(new SetCommand(this));
        getCommandHandler().registerCommand(new RestockCommand(this));
        getCommandHandler().registerCommand(new RestockAllCommand(this));
        getCommandHandler().registerCommand(new DefaultCommand(this));
        getCommandHandler().registerCommand(new DefaultsCommand(this));
        */
    }

    private void initializeConfigObjects() {
        chestManager = new DefaultChestManager(this);
        lootConfig = LootTables.newLootConfig(this, Logging.getLogger());
        defaultsMap = new HashMap<String, CRDefaults>();
        try {
            CRChest.Constants.setMaxInventorySize(config().get(CRConfig.MAX_INVENTORY_SIZE));
        } catch (IllegalArgumentException e) {
            Logging.warning(e.getMessage());
        }
    }

    private void initializeTasks() {
        getServer().getScheduler().cancelTasks(this);
        long ticks = config().get(CRConfig.RESTOCK_TASK) * 20;
        if (ticks > 0) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    getChestManager().pollChests();
                }
            }, ticks, ticks);
        }
    }

    @Override
    protected void onReloadConfig() {
        initializeConfigObjects();
        initializeTasks();
    }


    private void migrateDefaults() {
        if (config().get(CRConfig.NAME) != null) {
            getDefaults(null).set(CRDefaults.NAME, config().get(CRConfig.NAME));
        }
        if (config().get(CRConfig.PRESERVE_SLOTS) != null) {
            getDefaults(null).set(CRDefaults.PRESERVE_SLOTS, config().get(CRConfig.PRESERVE_SLOTS));
        }
        if (config().get(CRConfig.INDESTRUCTIBLE) != null) {
            getDefaults(null).set(CRDefaults.INDESTRUCTIBLE, config().get(CRConfig.INDESTRUCTIBLE));
        }
        if (config().get(CRConfig.PLAYER_LIMIT) != null) {
            getDefaults(null).set(CRDefaults.PLAYER_LIMIT, config().get(CRConfig.PLAYER_LIMIT));
        }
        if (config().get(CRConfig.UNIQUE) != null) {
            getDefaults(null).set(CRDefaults.UNIQUE, config().get(CRConfig.UNIQUE));
        }
        if (config().get(CRConfig.REDSTONE) != null) {
            getDefaults(null).set(CRDefaults.REDSTONE, config().get(CRConfig.REDSTONE));
        }
        if (config().get(CRConfig.ACCEPT_POLL) != null) {
            getDefaults(null).set(CRDefaults.ACCEPT_POLL, config().get(CRConfig.ACCEPT_POLL));
        }
        if (config().get(CRConfig.PERIOD) != null) {
            getDefaults(null).set(CRDefaults.PERIOD, config().get(CRConfig.PERIOD));
        }
        if (config().get(CRConfig.PERIOD_MODE) != null) {
            getDefaults(null).set(CRDefaults.PERIOD_MODE, config().get(CRConfig.PERIOD_MODE));
        }
        if (config().get(CRConfig.RESTOCK_MODE) != null) {
            getDefaults(null).set(CRDefaults.RESTOCK_MODE, config().get(CRConfig.RESTOCK_MODE));
        }
        if (config().get(CRConfig.LOOT_TABLE) != null) {
            getDefaults(null).set(CRDefaults.LOOT_TABLE, config().get(CRConfig.LOOT_TABLE));
        }
        if (config().get(CRConfig.GLOBAL_MESSAGE) != null) {
            getDefaults(null).set(CRDefaults.GLOBAL_MESSAGE, config().get(CRConfig.GLOBAL_MESSAGE));
        }
    }

    @Override
    protected void onPluginDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public ChestManager getChestManager() {
        return chestManager;
    }

    @Override
    public CRDefaults getDefaults(String world) {
        File file;
        if (world != null) {
            file = new File(defaultsFolder, world + ".yml");
        } else {
            file = defaultsFile;
        }

        if (!defaultsMap.containsKey(world)) {
            try {
                if (world == null) {
                    defaultsMap.put(world, newDefaultsConfig(file));
                } else {
                    defaultsMap.put(world, newDefaultsConfig(file, false));
                }
            } catch (IOException e) {
                Logging.warning("Could not create defaults file for world: " + world + "!");
                Logging.warning("Exception: " + e.getMessage());
                world = null;
            }
        }
        return defaultsMap.get(world);
    }

    @Override
    public LootConfig getLootConfig() {
        return lootConfig;
    }

    @Override
    public boolean hasChestManagerLoaded() {
        if (chestManager == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Block getTargetedInventoryHolder(Player player) throws IllegalStateException {
        Block block = player.getTargetBlock(null, 100);
        if (block == null || !(block.getState() instanceof InventoryHolder)) {
            throw new IllegalStateException(getMessager().getMessage(Language.TARGETING));
        }
        return block;
    }

    @Override
    public List<String> dumpVersionInfo() {
        List<String> versionInfo = new LinkedList<String>();
        versionInfo.add("Restock task period: " + config().get(CRConfig.RESTOCK_TASK));
        versionInfo.add("Chests to poll: " + getChestManager().getNumberChestsPolled());
        versionInfo.add("Chests cached: " + getChestManager().getNumberCachedChests());
        return versionInfo;
    }
}
