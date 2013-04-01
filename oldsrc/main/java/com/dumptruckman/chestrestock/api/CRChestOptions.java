package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValidator;
import com.dumptruckman.minecraft.pluginbase.properties.SimpleProperty;

/**
 * These class contains all of the properties of a CRChest.
 */
public interface CRChestOptions extends Properties {

    /**
     * Whether or not the chest options for this chest are actually enabled.
     */
    SimpleProperty<Boolean> ENABLED = PropertyFactory.newProperty(Boolean.class, "enabled", true)
            .comment("# When set to true, the chest functions as a chest for this plugin.  Very rarely would you manually set this to false.")
            .comment("# When set to false, the chest will behave exactly as a normal chest!")
            .description(Language.ENABLED_DESC).build();

    /**
     * The name of the chest.
     */
    SimpleProperty<String> NAME = PropertyFactory.newProperty(String.class, "name", "")
            .comment("# The default name for new chests.  This is used for permissions if not left blank (per chest).")
            .description(Language.NAME_DESC).build();

    /**
     * Whether or not the chest preserve slot locations when restocking.
     */
    SimpleProperty<Boolean> PRESERVE_SLOTS = PropertyFactory.newProperty(Boolean.class, "preserve_slots", true)
            .comment("# When set to true, chests will restock items in the same slots they were set up with.")
            .description(Language.PRESERVE_SLOTS_DESC).build();

    /**
     * Whether or not the chest is indestructible without permissions.
     */
    SimpleProperty<Boolean> INDESTRUCTIBLE = PropertyFactory.newProperty(Boolean.class, "indestructible", true)
            .comment("# When set to true, chests will only be able to broken by players with the correct permission.")
            .description(Language.INDESTRUCTIBLE_DESC).build();

    /**
     * The maximum number of times the chest will restock for each single player.
     */
    SimpleProperty<Integer> PLAYER_LIMIT = PropertyFactory.newProperty(Integer.class, "player_limit", -1)
            .comment("# This is the max number of times a chest will restock for each player.  Negative values indicate unlimited restocks.")
            .description(Language.PLAYER_LIMIT_DESC).build();

    /**
     * Whether or not the chest has a unique inventory per player.
     */
    SimpleProperty<Boolean> UNIQUE = PropertyFactory.newProperty(Boolean.class, "unique", true)
            .comment("# When set to true, chests will give each player a unique version of the chest to prevent loot theft.")
            .comment("# These inventories will NOT persist through server restarts/reloads.")
            .comment("# It's possible these inventories will be wiped when the chest is not in use for a while.  This is dependent on your JVM Garbage Collection settings.")
            .description(Language.UNIQUE_DESC).build();

    /**
     * Whether or not the chest will be restocked by redstone power.
     */
    SimpleProperty<Boolean> REDSTONE = PropertyFactory.newProperty(Boolean.class, "redstone", false)
            .comment("# When set to true, chests will restock when they receive redstone power.")
            .description(Language.REDSTONE_DESC).build();

    /**
     * Whether or not the ChestRestock polling task will check if this chest needs restocking.
     */
    SimpleProperty<Boolean> ACCEPT_POLL = PropertyFactory.newProperty(Boolean.class, "accept_poll", false)
            .comment("# When set to true, chests will use the restock task poll to determine if the chest should update.")
            .comment("# This may not work as expected with chests that are also 'unique'")
            .description(Language.ACCEPT_POLL_DESC).build();

    /**
     * The period at which the chest restocks.
     */
    SimpleProperty<Integer> PERIOD = PropertyFactory.newProperty(Integer.class, "period", 900)
            .comment("# This is the amount of time a chest requires before it will restock it's inventory. (In seconds)")
            .comment("# You may set this to 0 to disable timed restocking.")
            .description(Language.PERIOD_DESC).build();

    /**
     * "player" period_mode for {@link #PERIOD_MODE}.
     */
    String PERIOD_MODE_PLAYER = "player";
    /**
     * "fixed" period_mode for {@link #PERIOD_MODE}.
     */
    String PERIOD_MODE_FIXED = "fixed";
    /**
     * The way in which the period of this chest works.  Whether it is fixed or based on when a player last opened
     * the chest and caused a restock.
     */
    SimpleProperty<String> PERIOD_MODE = PropertyFactory.newProperty(String.class, "period_mode", PERIOD_MODE_PLAYER)
            .comment("# Possible options are 'player' and 'fixed'")
            .comment("# 'player' mode means restock timing will be based on when a player last looted the chest.")
            .comment("# 'fixed' mode means restock timing is based on when a chest is set up.")
            .validator(new PropertyValidator<String>() {
                @Override
                public boolean isValid(final String value) {
                    return value.equalsIgnoreCase(PERIOD_MODE_PLAYER) || value.equalsIgnoreCase(PERIOD_MODE_FIXED);
                }

                @Override
                public Message getInvalidMessage() {
                    return Language.PERIOD_MODE_INVALID;
                }
            }).description(Language.PERIOD_MODE_DESC).build();

    /**
     * "replace" restock_mode for {@link #RESTOCK_MODE}.
     */
    String RESTOCK_MODE_REPLACE = "replace";
    /**
     * "add" restock_mode for {@link #RESTOCK_MODE}.
     */
    String RESTOCK_MODE_ADD = "add";
    /**
     * Whether the chests adds items when restocking or replaces them.
     */
    SimpleProperty<String> RESTOCK_MODE = PropertyFactory.newProperty(String.class, "restock_mode", RESTOCK_MODE_REPLACE)
            .comment("# Possible options are 'add' and 'replace'")
            .comment("# 'add' mode means restocks will ADD items to the chest.")
            .comment("# 'replace' mode means restocks will REPLACE items in the chest.")
            .validator(new PropertyValidator<String>() {
                @Override
                public boolean isValid(final String value) {
                    return value.equalsIgnoreCase(RESTOCK_MODE_ADD) || value.equalsIgnoreCase(RESTOCK_MODE_REPLACE);
                }

                @Override
                public Message getInvalidMessage() {
                    return Language.RESTOCK_MODE_INVALID;
                }
            }).description(Language.RESTOCK_MODE_DESC).build();

    /**
     * The random loot table to use.
     */
    SimpleProperty<String> LOOT_TABLE = PropertyFactory.newProperty(String.class, "loot_table", "")
            .comment("# The default loot table for new chests.  If left blank, chests will only restock with what they're set up with.")
            .comment("# A loot table enables chances for randomized loot.  You must configure a table in the loot_tables.yml file to use this.")
            .description(Language.LOOT_TABLE_DESC).build();

    /**
     * The global message for the chest that is triggered when it restocks.
     */
    SimpleProperty<String> GLOBAL_MESSAGE = PropertyFactory.newProperty(String.class, "global_message", "")
            .comment("# The default global message for new chests.  This will be broadcast when the chest restocks if not left blank. (per chest)")
            .description(Language.GLOBAL_MESSAGE_DESC).build();

    /**
     * Whether or not the chest will only restock if the inventory is empty.
     */
    SimpleProperty<Boolean> ONLY_RESTOCK_EMPTY = PropertyFactory.newProperty(Boolean.class, "only_restock_empty", false)
            .comment("# When set to true, only empty chests will be restocked.")
            .description(Language.ONLY_RESTOCK_EMPTY_DESC).build();
}
