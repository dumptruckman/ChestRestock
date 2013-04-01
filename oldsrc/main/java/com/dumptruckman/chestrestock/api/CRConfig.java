package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.api.CRChest.Constants;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.properties.ListProperty;
import com.dumptruckman.minecraft.pluginbase.properties.NullProperty;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValidator;
import com.dumptruckman.minecraft.pluginbase.properties.SimpleProperty;

import java.util.Arrays;

/**
 * Interface for interacting with the config of this plugin.
 */
public interface CRConfig extends BaseConfig {

    /**
     * The interval at which a task performs restock polling on all chests set to {@link CRChest#ACCEPT_POLL}.
     */
    SimpleProperty<Integer> RESTOCK_TASK = PropertyFactory.newProperty(Integer.class, "settings.restock_task_interval", 60)
            .comment("# This is the interval (in seconds) at which a timer task will poll all 'accept_poll' chests to see if it should update.")
            .comment("# This could potentially affect performance if you have LOTS of chests set to accept polling")
            .comment("# To disable this polling feature, set this to 0").build();

    /**
     * A list of worlds for chests to be restocked in when they are reset by Multiverse-Adventure.
     */
    ListProperty<String> RESET_WORLDS = PropertyFactory.newListProperty(String.class, "settings.restock_chests_reset_worlds", Arrays.asList("hungergames"))
            .comment("# These are worlds that all ChestRestock chests will be restocked in when Multiverse-Adventure resets the worlds.")
            .comment("# Ignore this if you're not using Multiverse-Adventure").build();

    /**
     * The maximum size of any inventory.  This is used for inventory mod support.
     */
    SimpleProperty<Integer> MAX_INVENTORY_SIZE = PropertyFactory.newProperty(Integer.class, "settings.max_inventory_size", CRChest.Constants.getMaxInventorySize())
            .comment("# This is the maximum size of any inventory.  The default (54) is the size of a standard double chest.")
            .comment("# You should only adjust this value if you are using mods that allow larger inventories.")
            .validator(new PropertyValidator<Integer>() {
                @Override
                public boolean isValid(final Integer o) {
                    return o >= Constants.MIN_MAX_INVENTORY_SIZE;
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
    NullProperty DEFAULTS = PropertyFactory.newNullProperty("defaults")
            .comment("# All defaults have been moved to global_defaults.yml.  You may delete this section!").build();


    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    SimpleProperty<Boolean> PRESERVE_SLOTS = PropertyFactory.newProperty(Boolean.class, "defaults.preserve_slots", true)
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    SimpleProperty<Boolean> INDESTRUCTIBLE = PropertyFactory.newProperty(Boolean.class, "defaults.indestructible", true)
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    SimpleProperty<Integer> PLAYER_LIMIT = PropertyFactory.newProperty(Integer.class, "defaults.player_loot_limit", -1)
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    SimpleProperty<Boolean> UNIQUE = PropertyFactory.newProperty(Boolean.class, "defaults.unique", true)
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    SimpleProperty<Boolean> REDSTONE = PropertyFactory.newProperty(Boolean.class, "defaults.redstone", false)
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    SimpleProperty<Boolean> ACCEPT_POLL = PropertyFactory.newProperty(Boolean.class, "defaults.accept_poll", false)
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    SimpleProperty<Integer> PERIOD = PropertyFactory.newProperty(Integer.class, "defaults.period", 900)
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    SimpleProperty<String> PERIOD_MODE = PropertyFactory.newProperty(String.class, "defaults.period_mode", "player")
            .validator(new PropertyValidator<String>() {
                @Override
                public boolean isValid(final String value) {
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
    SimpleProperty<String> RESTOCK_MODE = PropertyFactory.newProperty(String.class, "defaults.restock_mode", "replace")
            .validator(new PropertyValidator<String>() {
                @Override
                public boolean isValid(final String value) {
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
    SimpleProperty<String> NAME = PropertyFactory.newProperty(String.class, "defaults.name", "")
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    SimpleProperty<String> LOOT_TABLE = PropertyFactory.newProperty(String.class, "loot_table", "")
            .comment("# This value has been moved to global_defaults.yml.  You may delete this entry.")
            .deprecated().build();

    /**
     * @deprecated as of release 2.3.  Moved to {@link CRChestOptions}
     */
    @Deprecated
    SimpleProperty<String> GLOBAL_MESSAGE = PropertyFactory.newProperty(String.class, "global_message", "")
            .comment("# This value has been moved to global_defaults.yml.  You may delete this entry.")
            .deprecated().build();
}
