package com.dumptruckman.chestrestock.api;

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
                    //TODO
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
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
                    //TODO
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }
            }).build();

    ConfigEntry<String> NAME = new EntryBuilder<String>(String.class, "defaults.name").def("")
            .comment("# The default name for new chests.  This is used for permissions if not left blank.").build();
}
