package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.LootConfig;
import com.dumptruckman.chestrestock.api.LootTable;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

class DefaultLootConfig implements LootConfig {

    private FileConfiguration config;
    private File configFile;

    private Map<String, LootTable> cachedTables = new WeakHashMap<String, LootTable>();

    DefaultLootConfig(ChestRestockPlugin plugin) {
        configFile = new File(plugin.getDataFolder(), "loot_tables.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        String nl = System.getProperty("line.separator");
        /*config.options().header("This is where you define loot tables for your chests to have random loot."
                + nl + "All numbers should be whole numbers!  DO NOT use fractions!");
        try {
            config.save(configFile);
        } catch (IOException e) {
            Logging.severe("Could not save loot_tables.yml!");
            Logging.severe("Reason: " + e.getMessage());
        }*/
    }

    @Override
    public LootTable getLootTable(String name) {
        if (name.isEmpty()) {
            return null;
        }
        if (cachedTables.containsKey(name)) {
            Logging.fine("Got cached table!");
            return cachedTables.get(name);
        }
        ConfigurationSection section = config.getConfigurationSection(name);
        if (section == null) {
            Logging.warning("Could not locate loot table: " + name);
            return null;
        }
        LootTable newTable = new DefaultLootTable(section);
        cachedTables.put(name, newTable);
        Logging.fine("Loaded loot table from config.");
        return newTable;
    }
}
