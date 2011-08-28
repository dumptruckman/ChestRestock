package com.dumptruckman.dchest.listeners;

import com.dumptruckman.dchest.ChestData;
import com.dumptruckman.dchest.DChest;
import com.dumptruckman.dchest.ItemData;
import java.util.Date;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.Inventory;
import org.getspout.spoutapi.player.SpoutPlayer;



/**
 *
 * @author dumptruckman
 */
public class DChestPlayerListener extends PlayerListener {

    DChest plugin;

    public DChestPlayerListener(DChest plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent evt) {
        // Discard irrelevant events
        if (!evt.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (evt.getClickedBlock().getType() != Material.CHEST) return;

        final ChestData chest = new ChestData(evt.getClickedBlock(), plugin);
        if (!chest.isInConfig()) return; // Discard event if it's not a configured chest
        
        final SpoutPlayer player = (SpoutPlayer)evt.getPlayer();
        evt.setCancelled(true);
        
        final PlayerInteractEvent event = evt;

        
        //Thread inventoryopen = new Thread() {
        //    @Override public void run() {
                Inventory inventory = chest.getInventory(chest.isDouble());
                // Clear the inventory and replace with old items if player is not op
                //OP exclusion from unique?no 
                if (/*!player.isOp() && */chest.isUnique()) {
                    List<ItemData> items = chest.getPlayerItems(player.getName());
                    inventory.clear();
                    // Item data for player not set
                    if (items != null) {
                        inventory = plugin.getInventoryWithItems(inventory, items);
                    }
                }

                // Check to make sure player will still be allowed to have a restock
                Integer timesrestockedforplayer = chest.getPlayerRestockCount(event.getPlayer().getName());
                if (timesrestockedforplayer != null) {
                    if (chest.getPlayerLimit() != -1) {
                        if (timesrestockedforplayer >= chest.getPlayerLimit()) {
                            //TODO Permissions
                            if (!event.getPlayer().isOp()||!plugin.permissionHandler.has(event.getPlayer(), "dChest.infinite")) {
                                // Not time for a restock, just display the inventory as is.
                                player.openInventoryWindow(inventory, chest.getLocation());
                                return;
                            }
                        }
                    }
                } else {
                    timesrestockedforplayer = 0;
                }

                // See if it's been long enough for a restock
                Long accesstime = new Date().getTime() / 1000;
                if (chest.isUnique()) {
                    // Unique access times per player
                    if (accesstime < (chest.getLastPlayerRestockTime(event.getPlayer().getName())
                            + Integer.parseInt(chest.getPeriod()))) {
                        // Not time for a restock, just display the inventory as is.
                        player.openInventoryWindow(inventory, chest.getLocation());
                        return;
                    }
                } else {
                    // General access times
                    if (accesstime < (chest.getLastRestockTime() +
                            Integer.parseInt(chest.getPeriod()))) {
                        // Not time for a restock, just display the inventory as is.
                        player.openInventoryWindow(inventory, chest.getLocation());
                        return;
                    }
                }

                long missedperiods = 1;
                if (chest.getPeriodMode().equalsIgnoreCase("player")) {
                    if (chest.isUnique()) {
                        missedperiods = (accesstime - chest.getLastPlayerRestockTime(player.getName()))
                                / Integer.parseInt(chest.getPeriod());
                    } else {
                        missedperiods = (accesstime - chest.getLastRestockTime())
                                / Integer.parseInt(chest.getPeriod());
                    }
                    chest.setRestockTime(accesstime);
                } else if (chest.getPeriodMode().equalsIgnoreCase("settime")) {
                    chest.setRestockTime((((accesstime - chest.getLastRestockTime())
                            / Integer.parseInt(chest.getPeriod()))
                            * Integer.parseInt(chest.getPeriod()))
                            + chest.getLastRestockTime());
                }
                chest.setPlayerRestockTime(player.getName(), accesstime);

                // Finally, restock the inventory
                if (chest.getRestockMode().equalsIgnoreCase("add")) {
                    for (int i = 0; i < missedperiods; i++) {
                        plugin.restockInventory(inventory, chest, chest.getItems());
                    }
                } else {
                    plugin.restockInventory(inventory, chest, chest.getItems());
                }

                // Finally, show the inventory
                player.openInventoryWindow(inventory, chest.getLocation());

                if (missedperiods != 0) {
                    timesrestockedforplayer++;
                    chest.setPlayerRestockCount(event.getPlayer().getName(), timesrestockedforplayer);
                }
            //}
        //};
        //inventoryopen.start();
        //plugin.saveConfigs();
    }
}
