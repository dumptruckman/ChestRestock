package com.dumptruckman.dchest.listeners;

import com.dumptruckman.dchest.ChestData;
import com.dumptruckman.dchest.DChest;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkitcontrib.event.inventory.InventoryCloseEvent;
import org.bukkitcontrib.event.inventory.InventoryListener;

/**
 *
 * @author dumptruckman
 */
public class DChestInventoryListener extends InventoryListener {

    DChest plugin;
    
    public DChestInventoryListener(DChest plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        //System.out.println("Caught close");
        if (event.getLocation() == null) return;
        Block block = event.getLocation().getBlock();
        if (block.getType() != Material.CHEST) return;
        ChestData chest = new ChestData(block, plugin);
        if (!chest.isInConfig()) return; // Discard event if it's not a configured chest
        if (!chest.isUnique()) return;
        
        chest.setPlayerItems(event.getPlayer().getName(), event.getInventory(), null);
    }
}
