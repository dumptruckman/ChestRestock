package com.dumptruckman.chestrestock.api;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * The main interface of the ChestRestock plugin.  You can use this for casting Plugin when got from Bukkit's
 * plugin manager.
 */
public interface ChestRestock extends BukkitPlugin<CRConfig>, Plugin {

    /**
     * @return The chest manager for this plugin.  This is where most of the business starts.
     */
    ChestManager getChestManager();

    /**
     * Retrieves the default chest settings for a specified world.  If null is passed in, it will retrieve the
     * global defaults.  World defaults may not contain all values.  For values not contained, null is returned
     * from the get() method.  In this case, global defaults are generally checked as the global defaults
     * contains every value whether or not they are explicitly set.
     *
     * @param world The world to get defaults for or null for global defaults.
     * @return The defaults for the world passed in or global defaults if null passed in.
     */
    CRDefaults getDefaults(String world);

    /**
     * @return the object that contains all data and methods related to random loot tables.
     */
    LootConfig getLootConfig();

    /**
     * @return true if the ChestManager for the plugin has been loaded.  Added to prevent recursive errors!
     */
    boolean hasChestManagerLoaded();

    /**
     * Returns the block the player is targeting if it is an InventoryHolder otherwise, throws IllegalStateException.
     *
     * @param player Player to check target of.
     * @return The block the player is targeting that is an InventoryHolder.
     * @throws IllegalStateException If the targeted block is not an InventoryHolder or the player is not targeting a block.
     */
    Block getTargetedInventoryHolder(Player player) throws IllegalStateException;
}
