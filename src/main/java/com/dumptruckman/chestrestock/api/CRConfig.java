package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.EntryBuilder;
import com.dumptruckman.minecraft.pluginbase.config.EntryValidator;
import com.dumptruckman.minecraft.pluginbase.config.ListConfigEntry;
import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.util.Null;

import java.util.Arrays;

/**
 * Interface for interacting with the config of this plugin.
 */
public interface CRConfig extends BaseConfig {

    ConfigEntry<Null> DEFAULTS = new EntryBuilder<Null>(Null.class, "defaults")
            .comment("# All defaults have been moved to global_defaults.yml.  You may delete this section!").build();

    @Deprecated
    ConfigEntry<Boolean> PRESERVE_SLOTS = new EntryBuilder<Boolean>(Boolean.class, "defaults.preserve_slots").def(true)
            .deprecated().build();

    @Deprecated
    ConfigEntry<Boolean> INDESTRUCTIBLE = new EntryBuilder<Boolean>(Boolean.class, "defaults.indestructible").def(true)
            .deprecated().build();

    @Deprecated
    ConfigEntry<Integer> PLAYER_LIMIT = new EntryBuilder<Integer>(Integer.class, "defaults.player_loot_limit").def(-1)
            .deprecated().build();

    @Deprecated
    ConfigEntry<Boolean> UNIQUE = new EntryBuilder<Boolean>(Boolean.class, "defaults.unique").def(true)
            .deprecated().build();

    @Deprecated
    ConfigEntry<Boolean> REDSTONE = new EntryBuilder<Boolean>(Boolean.class, "defaults.redstone").def(false)
            .deprecated().build();

    @Deprecated
    ConfigEntry<Boolean> ACCEPT_POLL = new EntryBuilder<Boolean>(Boolean.class, "defaults.accept_poll").def(false)
            .deprecated().build();

    @Deprecated
    ConfigEntry<Integer> PERIOD = new EntryBuilder<Integer>(Integer.class, "defaults.period").def(900)
            .deprecated().build();

    @Deprecated
    ConfigEntry<String> PERIOD_MODE = new EntryBuilder<String>(String.class, "defaults.period_mode").def("player")
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
            }).deprecated().build();

    @Deprecated
    ConfigEntry<String> RESTOCK_MODE = new EntryBuilder<String>(String.class, "defaults.restock_mode").def("replace")
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
            }).deprecated().build();

    @Deprecated
    ConfigEntry<String> NAME = new EntryBuilder<String>(String.class, "defaults.name").def("")
            .deprecated().build();

    @Deprecated
    ConfigEntry<String> LOOT_TABLE = new EntryBuilder<String>(String.class, "loot_table").def("")
            .comment("# This value has been moved to global_defaults.yml.  You may delete this entry.")
            .deprecated().build();

    @Deprecated
    ConfigEntry<String> GLOBAL_MESSAGE = new EntryBuilder<String>(String.class, "global_message").def("")
            .comment("# This value has been moved to global_defaults.yml.  You may delete this entry.")
            .deprecated().build();

    ConfigEntry<Integer> RESTOCK_TASK = new EntryBuilder<Integer>(Integer.class, "settings.restock_task_interval").def(60)
            .comment("# This is the interval (in seconds) at which a timer task will poll all 'accept_poll' chests to see if it should update.")
            .comment("# This could potentially affect performance if you have LOTS of chests set to accept polling")
            .comment("# To disable this polling feature, set this to 0").build();

    ListConfigEntry<String> RESET_WORLDS = new EntryBuilder<String>(String.class, "settings.restock_chests_reset_worlds")
            .defList(Arrays.asList("hungergames"))
            .comment("# These are worlds that all ChestRestock chests will be restocked in when Multiverse-Adventure resets the worlds.")
            .comment("# Ignore this if you're not using Multiverse-Adventure").buildList();
}
