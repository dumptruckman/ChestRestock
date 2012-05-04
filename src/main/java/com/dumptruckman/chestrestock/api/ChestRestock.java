package com.dumptruckman.chestrestock.api;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import org.bukkit.plugin.Plugin;

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

    boolean defaultsExistForWorld(String world);

    LootConfig getLootConfig();

    boolean hasChestManagerLoaded();
}
