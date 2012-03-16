package com.dumptruckman.chestrestock.api;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.SimpleConfigEntry;

/**
 * Interface for interacting with the config of this plugin.
 */
public interface CRConfig extends BaseConfig {
    
    ConfigEntry<Boolean> PRESERVE_SLOTS = new SimpleConfigEntry<Boolean>(Boolean.class, "defaults.preserve_slots", true,
            "# When set to true, chests will restock items in the same slots they were set up with.");

    ConfigEntry<Boolean> INDESTRUCTIBLE = new SimpleConfigEntry<Boolean>(Boolean.class, "defaults.indestructible", true,
            "# When set to true, chests will only be able to broken by players with the correct permission.");

    ConfigEntry<Integer> PLAYER_LIMIT = new SimpleConfigEntry<Integer>(Integer.class, "defaults.player_loot_limit", -1,
            "# This is the max number of times a chest will restock for each player.  "
                    + "Negative values indicate unlimited restocks.");

    ConfigEntry<Boolean> UNIQUE = new SimpleConfigEntry<Boolean>(Boolean.class, "defaults.unique", true,
            "# When set to true, chests will give each player a unique version of the chest to prevent loot theft.");

    ConfigEntry<Integer> PERIOD = new SimpleConfigEntry<Integer>(Integer.class, "defaults.period", 900,
            "# This is the max number of times a chest will restock for each player.  "
                    + "Negative values indicate unlimited restocks.");
    
    ConfigEntry<String> PERIOD_MODE = new SimpleConfigEntry<String>(String.class, "defaults.period_mode", "player",
            "# Possible options are 'player' and 'fixed'",
            "# 'player' mode means restock timing will be based on when a player last looted the chest.",
            "# 'fixed' mode means restock timing is based on when a chest is set up.") {
        @Override
        public boolean isValid(Object obj) {
            String value = obj.toString();
            return value.equalsIgnoreCase("player") || value.equalsIgnoreCase("fixed");
        }
    };

    ConfigEntry<String> RESTOCK_MODE = new SimpleConfigEntry<String>(String.class, "defaults.restock_mode", "replace",
            "# Possible options are 'add' and 'replace'",
            "# 'add' mode means restocks will ADD items to the chest.",
            "# 'replace' mode means restocks will REPLACE items in the chest.") {
        @Override
        public boolean isValid(Object obj) {
            String value = obj.toString();
            return value.equalsIgnoreCase("add") || value.equalsIgnoreCase("replace");
        }
    };
}
