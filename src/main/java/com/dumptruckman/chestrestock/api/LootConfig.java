package com.dumptruckman.chestrestock.api;

/**
 * An interface for retrieving a loot table from loot_tables.yml or from an individual loot table file.
 */
public interface LootConfig {

    /**
     * Retrieves the loot table with the specified name.
     *
     * @param name The name of the loot table to retrieve.
     * @return The loot table by that name or null if none found.
     */
    LootTable getLootTable(String name);
}
