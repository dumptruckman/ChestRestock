package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.EntryBuilder;
import com.dumptruckman.minecraft.pluginbase.config.EntryValidator;
import com.dumptruckman.minecraft.pluginbase.locale.Message;

/**
 * Interface for interacting with the config of this plugin.
 */
public interface CRConfig extends BaseConfig {
    
    ConfigEntry<Boolean> PRESERVE_SLOTS = new EntryBuilder<Boolean>(Boolean.class, "defaults.preserve_slots").def(true)
            .comment("# When set to true, chests will restock items in the same slots they were set up with.").build();

    ConfigEntry<Boolean> INDESTRUCTIBLE = new EntryBuilder<Boolean>(Boolean.class, "defaults.indestructible").def(true)
            .comment("# When set to true, chests will only be able to broken by players with the correct permission.")
            .build();

    ConfigEntry<Integer> PLAYER_LIMIT = new EntryBuilder<Integer>(Integer.class, "defaults.player_loot_limit").def(-1)
            .comment("# This is the max number of times a chest will restock for each player.  "
                    + "Negative values indicate unlimited restocks.").build();

    ConfigEntry<Boolean> UNIQUE = new EntryBuilder<Boolean>(Boolean.class, "defaults.unique").def(true)
            .comment("# When set to true, chests will give each player a unique version of the chest to prevent "
                    + "loot theft.").build();

    ConfigEntry<Boolean> REDSTONE = new EntryBuilder<Boolean>(Boolean.class, "defaults.redstone").def(false)
            .comment("# When set to true, chests will restock when they receive redstone power.").build();

    ConfigEntry<Boolean> ACCEPT_POLL = new EntryBuilder<Boolean>(Boolean.class, "defaults.accept_poll").def(false)
            .comment("# When set to true, chests will use the restock task poll to determine if the chest should update.")
            .comment("# This may not work as expected with chests that are also 'unique'").build();

    ConfigEntry<Integer> PERIOD = new EntryBuilder<Integer>(Integer.class, "defaults.period").def(900)
            .comment("# This is the max number of times a chest will restock for each player.  "
                    + "Negative values indicate unlimited restocks.").build();
    
    ConfigEntry<String> PERIOD_MODE = new EntryBuilder<String>(String.class, "defaults.period_mode").def("player")
            .comment("# Possible options are 'player' and 'fixed'")
            .comment("# 'player' mode means restock timing will be based on when a player last looted the chest.")
            .comment("# 'fixed' mode means restock timing is based on when a chest is set up.")
            .validator(new EntryValidator() {
                @Override
                public boolean isValid(Object o) {
                    String value = o.toString();
                    return value.equalsIgnoreCase("player") || value.equalsIgnoreCase("fixed");
                }

                @Override
                public Message getInvalidMessage() {
                    return Language.PERIOD_MODE_INVALID;
                }
            }).build();

    ConfigEntry<String> RESTOCK_MODE = new EntryBuilder<String>(String.class, "defaults.restock_mode").def("replace")
            .comment("# Possible options are 'add' and 'replace'")
            .comment("# 'add' mode means restocks will ADD items to the chest.")
            .comment("# 'replace' mode means restocks will REPLACE items in the chest.")
            .validator(new EntryValidator() {
                @Override
                public boolean isValid(Object o) {
                    String value = o.toString();
                    return value.equalsIgnoreCase("add") || value.equalsIgnoreCase("replace");
                }

                @Override
                public Message getInvalidMessage() {
                    return Language.RESTOCK_MODE_INVALID;
                }
            }).build();

    ConfigEntry<String> NAME = new EntryBuilder<String>(String.class, "defaults.name").def("")
            .comment("# The default name for new chests.  This is used for permissions if not left blank (per chest).").build();

    ConfigEntry<String> LOOT_TABLE = new EntryBuilder<String>(String.class, "loot_table").def("")
            .comment("# The default loot table for new chests.  If left blank, chests will only restock with what they're set up with.")
            .comment("# A loot table enables chances for randomized loot.  You must configure a table in the loot_tables.yml file to use this.").build();

    ConfigEntry<String> GLOBAL_MESSAGE = new EntryBuilder<String>(String.class, "global_message").def("")
            .comment("# The default global message for new chests.  This will be broadcast when the chest restocks if not left blank. (per chest)").build();

    ConfigEntry<Integer> RESTOCK_TASK = new EntryBuilder<Integer>(Integer.class, "restock_task_interval").def(60)
            .comment("# This is the interval (in seconds) at which a timer task will poll all 'accept_poll' chests to see if it should update.")
            .comment("# This could potentially affect performance if you have LOTS of chests set to accept polling")
            .comment("# To disable this polling feature, set this to 0").build();
}
