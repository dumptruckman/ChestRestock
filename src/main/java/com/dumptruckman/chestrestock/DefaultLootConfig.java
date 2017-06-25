package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.api.LootConfig;
import com.dumptruckman.chestrestock.api.LootTable;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.WeakHashMap;

class DefaultLootConfig implements LootConfig {

    private FileConfiguration config;

    private File lootFolder;
    private File configFile;

    private Map<String, LootTable> cachedTables = new WeakHashMap<String, LootTable>();

    DefaultLootConfig(ChestRestock plugin) {
        configFile = new File(plugin.getDataFolder(), "loot_tables.yml");
        lootFolder = new File(plugin.getDataFolder(), "loot_tables");
        lootFolder.mkdirs();
        config = YamlConfiguration.loadConfiguration(configFile);
        String nl = System.getProperty("line.separator");
        config.options().header("This is where you define loot tables for your chests to have random loot."
                + nl + "You may also create separate yaml files for each loot table.  Just make sure the file name is the name of the table you want and placed in the loot_tables folder.  example: example_table.yml"
                + nl + "Properties for each section of a table:"
                + nl + "chance - the chance at which the section will be picked (as a fraction: 0.25 == 25%).  default: 1"
                + nl + "rolls - the number of times the section will be considered.  default: 1"
                + nl + "split (true/false) - if true, chance will be used as section weight and only 1 section will be picked.  default: false"
                + nl + "id - the item id (number).  default: none"
                + nl + "data - the item data value (number).  default: none"
                + nl + "amount - the amount of the item.  default: 1"
                + nl + "==================="
                + nl + "enchant - This indicates there is an enchantment for the item selected for this section.  The following values must be defined under enchant:"
                + nl + "name - the name of the enchantment.  default: none.  (possible values: http://jd.bukkit.org/apidocs/org/bukkit/enchantments/Enchantment.html)"
                + nl + "level - the level of the enchantment.  Negative values indicate random level from 1 to -level.  default: 1"
                + nl + "safe - whether or not to only allow safe enchantments.  default: true.  This means only appropriate enchantment/level for the item."
                + nl + "PLEASE NOTE: The enchant section can have all the normal properties but cannot indicate items.  This means, you can do random sets of enchants!"
                + nl + "Refer to loot_example.yml for a complete example!");
        try {
            config.save(configFile);
            YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("loot_example.yml")))
                    .save(new File(plugin.getDataFolder(), "loot_example.yml"));
        } catch (IOException e) {
            Logging.severe("Could not save loot_tables.yml!");
            Logging.severe("Reason: " + e.getMessage());
        }
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
        config = YamlConfiguration.loadConfiguration(configFile);
        ConfigurationSection section = config.getConfigurationSection(name);
        if (section == null) {
            File lootFile = new File(lootFolder, name + ".yml");
            if (lootFile.exists()) {
                section = YamlConfiguration.loadConfiguration(lootFile);
            }
            if (section == null) {
                Logging.warning("Could not locate loot table: " + name);
                return null;
            }
        }
        LootTable newTable = new DefaultLootTable(name, section);
        cachedTables.put(name, newTable);
        Logging.fine("Loaded loot table from config.");
        return newTable;
    }
}
