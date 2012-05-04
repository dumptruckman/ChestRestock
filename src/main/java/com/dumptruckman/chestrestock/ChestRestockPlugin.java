package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.CRConfig;
import com.dumptruckman.chestrestock.api.CRDefaults;
import com.dumptruckman.chestrestock.api.ChestManager;
import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.api.LootConfig;
import com.dumptruckman.chestrestock.command.CheckCommand;
import com.dumptruckman.chestrestock.command.CreateCommand;
import com.dumptruckman.chestrestock.command.DefaultCommand;
import com.dumptruckman.chestrestock.command.DefaultsCommand;
import com.dumptruckman.chestrestock.command.DisableCommand;
import com.dumptruckman.chestrestock.command.RestockAllCommand;
import com.dumptruckman.chestrestock.command.RestockCommand;
import com.dumptruckman.chestrestock.command.SetCommand;
import com.dumptruckman.chestrestock.command.UpdateCommand;
import com.dumptruckman.chestrestock.util.CommentedConfig;
import com.dumptruckman.chestrestock.util.DefaultsConfig;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.plugin.command.HelpCommand;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChestRestockPlugin extends AbstractBukkitPlugin<CRConfig> implements ChestRestock {

    private final List<String> cmdPrefixes = Arrays.asList("cr");

    private ChestManager chestManager = null;
    private LootConfig lootConfig = null;
    private Map<String, CRDefaults> defaultsMap = null;
    private File defaultsFolder;
    private File defaultsFile;

    @Override
    protected CRConfig newConfigInstance() throws IOException {
        return new CommentedConfig(this, true, new File(getDataFolder(), "config.yml"), CRConfig.class);
    }

    private CRDefaults newDefaultsConfig(File file) throws IOException {
        return newDefaultsConfig(file, true);
    }

    private CRDefaults newDefaultsConfig(File file, boolean autoDefault) throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            file.getParentFile().mkdirs();
        }
        return new DefaultsConfig(this, autoDefault, file);
    }

    @Override
    public void preEnable() {
        defaultsFolder = new File(getDataFolder(), "world_defaults");
        defaultsFile = new File(getDataFolder(), "global_defaults.yml");
        defaultsFolder.mkdirs();
        Language.init();
        HelpCommand.addStaticPrefixedKey("");
    }

    @Override
    public void postEnable() {
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
        getCommandHandler().registerCommand(new CreateCommand(this));
        getCommandHandler().registerCommand(new UpdateCommand(this));
        getCommandHandler().registerCommand(new CheckCommand(this));
        getCommandHandler().registerCommand(new DisableCommand(this));
        getCommandHandler().registerCommand(new SetCommand(this));
        getCommandHandler().registerCommand(new RestockCommand(this));
        getCommandHandler().registerCommand(new RestockAllCommand(this));
        getCommandHandler().registerCommand(new DefaultCommand(this));
        getCommandHandler().registerCommand(new DefaultsCommand(this));
    }

    @Override
    public void preReload() {
        chestManager = null;
        lootConfig = null;
        defaultsMap = null;
    }

    @Override
    public void postReload() {
        long ticks = config().get(CRConfig.RESTOCK_TASK) * 20;
        if (ticks > 0) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    getChestManager().pollChests();
                }
            }, ticks, ticks);
        }
        getLootConfig();
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
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        super.onDisable();
    }

    @Override
    public List<String> getCommandPrefixes() {
        return cmdPrefixes;
    }

    @Override
    public ChestManager getChestManager() {
        if (chestManager == null) {
            chestManager = new DefaultChestManager(this);
        }
        return chestManager;
    }

    @Override
    public CRDefaults getDefaults(String world) {
        if (defaultsMap == null) {
            defaultsMap = new HashMap<String, CRDefaults>();
        }
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
        if (lootConfig == null) {
            lootConfig = new DefaultLootConfig(this);
        }
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
    public List<String> dumpVersionInfo() {
        List<String> versionInfo = new LinkedList<String>();
        versionInfo.add("Restock task period: " + config().get(CRConfig.RESTOCK_TASK));
        versionInfo.add("Chests to poll: " + getChestManager().getNumberChestsPolled());
        versionInfo.add("Chests cached: " + getChestManager().getNumberCachedChests());
        return versionInfo;
    }
}
