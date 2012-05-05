package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.api.CRChest.Constants;
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

    /**
     * The interval at which a task performs restock polling on all chests set to {@link CRChest#ACCEPT_POLL}.
     */
    ConfigEntry<Integer> RESTOCK_TASK = new EntryBuilder<Integer>(Integer.class, "settings.restock_task_interval").def(60)
            .comment("# This is the interval (in seconds) at which a timer task will poll all 'accept_poll' chests to see if it should update.")
            .comment("# This could potentially affect performance if you have LOTS of chests set to accept polling")
            .comment("# To disable this polling feature, set this to 0").build();

    /**
     * A list of worlds for chests to be restocked in when they are reset by Multiverse-Adventure.
     */
    ListConfigEntry<String> RESET_WORLDS = new EntryBuilder<String>(String.class, "settings.restock_chests_reset_worlds")
            .defList(Arrays.asList("hungergames"))
            .comment("# These are worlds that all ChestRestock chests will be restocked in when Multiverse-Adventure resets the worlds.")
            .comment("# Ignore this if you're not using Multiverse-Adventure").buildList();

    /**
     * The maximum size of any inventory.  This is used for inventory mod support.
     */
    ConfigEntry<Integer> MAX_INVENTORY_SIZE = new EntryBuilder<Integer>(Integer.class, "settings.max_inventory_size")
            .def(CRChest.Constants.getMaxInventorySize())
            .comment("# This is the maximum size of any inventory.  The default (54) is the size of a standard double chest.")
            .comment("# You should only adjust this value if you are using mods that allow larger inventories.")
            .validator(new EntryValidator() {
                @Override
                public boolean isValid(Object o) {
                    if (o instanceof String) {
                        try {
                            o = Integer.valueOf((String) o);
                        } catch (NumberFormatException ignore) {
                        }
                    }
                    return o instanceof Integer && ((Integer) o) >= Constants.MIN_MAX_INVENTORY_SIZE;
                }

                @Override
                public Message getInvalidMessage() {
                    return Language.MAX_INVENTORY_SIZE_INVALID;
                }
            })
            .build();

    /**
     * This just adds a comment to a parent node in the config.
     */
    @Deprecated
    ConfigEntry<Null> DEFAULTS = new EntryBuilder<Null>(Null.class, "defaults")
            .comment("# All defaults have been moved to global_defaults.yml.  You may delete this section!").build();


    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    ConfigEntry<Boolean> PRESERVE_SLOTS = new EntryBuilder<Boolean>(Boolean.class, "defaults.preserve_slots").def(true)
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    ConfigEntry<Boolean> INDESTRUCTIBLE = new EntryBuilder<Boolean>(Boolean.class, "defaults.indestructible").def(true)
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    ConfigEntry<Integer> PLAYER_LIMIT = new EntryBuilder<Integer>(Integer.class, "defaults.player_loot_limit").def(-1)
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    ConfigEntry<Boolean> UNIQUE = new EntryBuilder<Boolean>(Boolean.class, "defaults.unique").def(true)
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    ConfigEntry<Boolean> REDSTONE = new EntryBuilder<Boolean>(Boolean.class, "defaults.redstone").def(false)
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    ConfigEntry<Boolean> ACCEPT_POLL = new EntryBuilder<Boolean>(Boolean.class, "defaults.accept_poll").def(false)
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    ConfigEntry<Integer> PERIOD = new EntryBuilder<Integer>(Integer.class, "defaults.period").def(900)
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
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

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
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

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    ConfigEntry<String> NAME = new EntryBuilder<String>(String.class, "defaults.name").def("")
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    ConfigEntry<String> LOOT_TABLE = new EntryBuilder<String>(String.class, "loot_table").def("")
            .comment("# This value has been moved to global_defaults.yml.  You may delete this entry.")
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    ConfigEntry<String> GLOBAL_MESSAGE = new EntryBuilder<String>(String.class, "global_message").def("")
            .comment("# This value has been moved to global_defaults.yml.  You may delete this entry.")
            .deprecated().build();
}
