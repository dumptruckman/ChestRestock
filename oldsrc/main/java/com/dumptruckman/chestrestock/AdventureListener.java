package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.CRConfig;
import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.onarandombox.MultiverseAdventure.event.MVAResetFinishedEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AdventureListener implements Listener {

    private ChestRestock plugin;

    public AdventureListener(ChestRestock plugin) {
        this.plugin = plugin;
    }

    /**
     * @param event The Multiverse-Adventure event to handle when a world has finished resetting.
     */
    @EventHandler
    public void worldReset(MVAResetFinishedEvent event) {
        World world = Bukkit.getWorld(event.getWorld());
        if (world != null && plugin.config().get(CRConfig.RESET_WORLDS).contains(world.getName())) {
            Logging.info("Restocking all chests for reset world.. This may take a moment");
            plugin.getChestManager().restockAllChests(world, null);
        }
    }
}
