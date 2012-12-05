package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.properties.NullProperty;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory;
import com.dumptruckman.minecraft.pluginbase.properties.SimpleProperty;

/**
 * Relates to the Defaults for new CRChests.  The entries here are not actual chest properties but affect chest
 * creation.
 */
public interface CRDefaults extends CRChestOptions {

    /**
     * This just adds a comment to a parent node in the config.
     */
    NullProperty OTHER = PropertyFactory.newNullProperty("other")
            .comment("# These are options that pertain to chests but are not properties of individual chests.")
            .build();

    /**
     * Whether or not to auto-create CRChests from block based inventories.
     */
    SimpleProperty<Boolean> AUTO_CREATE = PropertyFactory.newProperty(Boolean.class, "other.auto_create", false)
            .description(Language.AUTO_CREATE_DESC)
            .comment("# This will automatically initialize any chest not already managed by ChestRestock with these defaults.")
            .comment("# Essentially the same thing as using \"/cr create\" on every chest encountered")
            .build();

    /**
     * Whether or not to auto-create CRChests from block based inventories.
     */
    SimpleProperty<Boolean> AUTO_CREATE_NEW = PropertyFactory.newProperty(Boolean.class, "other.auto_create_new_chests", false)
            .description(Language.AUTO_CREATE_NEW_DESC)
            .comment("# This will automatically initialize any chest not already managed by ChestRestock with these defaults.")
            .comment("# Essentially the same thing as using \"/cr create\" on every chest encountered")
            .comment("# Requires auto_create to be enabled!")
            .comment("# This basically determines if NEWLY PLACED chests will be enabled or disabled.  When set to false, all NEWLY PLACED chests will act like NORMAL chests.")
            .build();

    /**
     * The random loot table to use on empty chests when initialized with ChestRestock.
     */
    SimpleProperty<String> EMPTY_LOOT_TABLE = PropertyFactory.newProperty(String.class, "other.empty_loot_table", "")
            .description(Language.EMPTY_LOOT_TABLE_DESC)
            .comment("# This is the loot table that will be used when intializing a restock chest that is empty when created.")
            .build();
}
