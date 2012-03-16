package com.dumptruckman.chestrestock.listeners;

import com.dumptruckman.chestrestock.ChestData;
import com.dumptruckman.chestrestock.ChestRestock;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 *
 * @author dumptruckman
 */
public class ChestRestockEntityListener implements Listener {

    ChestRestock plugin;

    public ChestRestockEntityListener(ChestRestock plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
    if (event.isCancelled()) {
            return;
        }
    
        List<Block> blocks = event.blockList();
        List<ChestData> chests = new ArrayList<ChestData>();
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i).getType() == Material.CHEST) {
                ChestData chest = new ChestData(blocks.get(i), plugin);
                if (chest.isInConfig()) {
                    chests.add(chest);
                    if (chest.isIndestructible()) {
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (!event.isCancelled()) {
            for (int i = 0; i < chests.size(); i++) {
                chests.get(i).disable();
            }
            plugin.saveConfig();
        }
    }
}
