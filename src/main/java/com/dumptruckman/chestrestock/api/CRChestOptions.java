package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.config.Config;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.EntryBuilder;
import com.dumptruckman.minecraft.pluginbase.config.EntryValidator;
import com.dumptruckman.minecraft.pluginbase.locale.Message;

/**
 * These class contains all of the properties of a CRChest.
 */
public interface CRChestOptions extends Config {

    /**
     * The name of the chest.
     */
    ConfigEntry<String> NAME = new EntryBuilder<String>(String.class, "name").def("")
            .comment("# The default name for new chests.  This is used for permissions if not left blank (per chest).")
            .description(Language.NAME_DESC).stringSerializer().build();

    /**
     * Whether or not the chest preserve slot locations when restocking.
     */
    ConfigEntry<Boolean> PRESERVE_SLOTS = new EntryBuilder<Boolean>(Boolean.class, "preserve_slots").def(true)
            .comment("# When set to true, chests will restock items in the same slots they were set up with.")
            .description(Language.PRESERVE_SLOTS_DESC).stringSerializer().build();

    /**
     * Whether or not the chest is indestructible without permissions.
     */
    ConfigEntry<Boolean> INDESTRUCTIBLE = new EntryBuilder<Boolean>(Boolean.class, "indestructible").def(true)
            .comment("# When set to true, chests will only be able to broken by players with the correct permission.")
            .description(Language.INDESTRUCTIBLE_DESC).stringSerializer().build();

    /**
     * The maximum number of times the chest will restock for each single player.
     */
    ConfigEntry<Integer> PLAYER_LIMIT = new EntryBuilder<Integer>(Integer.class, "player_limit").def(-1)
            .comment("# This is the max number of times a chest will restock for each player.  Negative values indicate unlimited restocks.")
            .description(Language.PLAYER_LIMIT_DESC).stringSerializer().build();

    /**
     * Whether or not the chest has a unique inventory per player.
     */
    ConfigEntry<Boolean> UNIQUE = new EntryBuilder<Boolean>(Boolean.class, "unique").def(true)
            .comment("# When set to true, chests will give each player a unique version of the chest to prevent loot theft.")
            .description(Language.UNIQUE_DESC).stringSerializer().build();

    /**
     * Whether or not the chest will be restocked by redstone power.
     */
    ConfigEntry<Boolean> REDSTONE = new EntryBuilder<Boolean>(Boolean.class, "redstone").def(false)
            .comment("# When set to true, chests will restock when they receive redstone power.")
            .description(Language.REDSTONE_DESC).stringSerializer().build();

    /**
     * Whether or not the ChestRestock polling task will check if this chest needs restocking.
     */
    ConfigEntry<Boolean> ACCEPT_POLL = new EntryBuilder<Boolean>(Boolean.class, "accept_poll").def(false)
            .comment("# When set to true, chests will use the restock task poll to determine if the chest should update.")
            .comment("# This may not work as expected with chests that are also 'unique'")
            .description(Language.ACCEPT_POLL_DESC).stringSerializer().build();

    /**
     * The period at which the chest restocks.
     */
    ConfigEntry<Integer> PERIOD = new EntryBuilder<Integer>(Integer.class, "period").def(900)
            .comment("# This is the amount of time a chest requires before it will restock it's inventory. (In seconds)")
            .comment("# You may set this to 0 to disable timed restocking.")
            .description(Language.PERIOD_DESC).stringSerializer().build();

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
    ConfigEntry<String> PERIOD_MODE = new EntryBuilder<String>(String.class, "period_mode").def(PERIOD_MODE_PLAYER)
            .comment("# Possible options are 'player' and 'fixed'")
            .comment("# 'player' mode means restock timing will be based on when a player last looted the chest.")
            .comment("# 'fixed' mode means restock timing is based on when a chest is set up.")
            .validator(new EntryValidator() {
                @Override
                public boolean isValid(Object o) {
                    String value = o.toString();
                    return value.equalsIgnoreCase(PERIOD_MODE_PLAYER) || value.equalsIgnoreCase(PERIOD_MODE_FIXED);
                }

                @Override
                public Message getInvalidMessage() {
                    return Language.PERIOD_MODE_INVALID;
                }
            }).description(Language.PERIOD_MODE_DESC).stringSerializer().build();

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
    ConfigEntry<String> RESTOCK_MODE = new EntryBuilder<String>(String.class, "restock_mode").def(RESTOCK_MODE_REPLACE)
            .comment("# Possible options are 'add' and 'replace'")
            .comment("# 'add' mode means restocks will ADD items to the chest.")
            .comment("# 'replace' mode means restocks will REPLACE items in the chest.")
            .validator(new EntryValidator() {
                @Override
                public boolean isValid(Object o) {
                    String value = o.toString();
                    return value.equalsIgnoreCase(RESTOCK_MODE_ADD) || value.equalsIgnoreCase(RESTOCK_MODE_REPLACE);
                }

                @Override
                public Message getInvalidMessage() {
                    return Language.RESTOCK_MODE_INVALID;
                }
            }).description(Language.RESTOCK_MODE_DESC).stringSerializer().build();

    /**
     * The random loot table to use.
     */
    ConfigEntry<String> LOOT_TABLE = new EntryBuilder<String>(String.class, "loot_table").def("")
            .comment("# The default loot table for new chests.  If left blank, chests will only restock with what they're set up with.")
            .comment("# A loot table enables chances for randomized loot.  You must configure a table in the loot_tables.yml file to use this.")
            .description(Language.LOOT_TABLE_DESC).stringSerializer().build();

    /**
     * The global message for the chest that is triggered when it restocks.
     */
    ConfigEntry<String> GLOBAL_MESSAGE = new EntryBuilder<String>(String.class, "global_message").def("")
            .comment("# The default global message for new chests.  This will be broadcast when the chest restocks if not left blank. (per chest)")
            .description(Language.GLOBAL_MESSAGE_DESC).stringSerializer().build();

    /**
     * Whether or not the chest will only restock if the inventory is empty.
     */
    ConfigEntry<Boolean> ONLY_RESTOCK_EMPTY = new EntryBuilder<Boolean>(Boolean.class, "only_restock_empty").def(false)
            .comment("# When set to true, only empty chests will be restocked.")
            .description(Language.ONLY_RESTOCK_EMPTY_DESC).stringSerializer().build();
}
