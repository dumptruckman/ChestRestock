package com.dumptruckman.dchest.listeners;

import com.dumptruckman.dchest.ChestData;
import com.dumptruckman.dchest.DChest;
import java.util.Date;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkitcontrib.player.ContribPlayer;


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
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Discard irrelevant events
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (event.getClickedBlock().getType() != Material.CHEST) {
            return;
        }

        ChestData chest = new ChestData(event.getClickedBlock(), plugin);
        if (!chest.isInConfig()) {
            // Discard event if it's not a configured chest
            return;
        }

        Integer timesrestockedforplayer = chest.getPlayerRestockCount(event.getPlayer().getName());
        if (timesrestockedforplayer != null) {
            if (chest.getPlayerLimit() != -1) {
                if (timesrestockedforplayer >= chest.getPlayerLimit()) {
                    if (!event.getPlayer().isOp()) {
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
                return;
            }
        } else {
            // General access times
            if (accesstime < (chest.getLastRestockTime() +
                    Integer.parseInt(chest.getPeriod()))) {
                return;
            }
        }

        // Take over the event
        event.setCancelled(true);

        ContribPlayer player = (ContribPlayer)event.getPlayer();
        player.openInventoryWindow(chest.getInventory(chest.isDouble()), chest.getLocation());

        long missedperiods = 1;
        if (chest.getPeriodMode().equalsIgnoreCase("player")) {
            if (chest.isUnique()) {
                missedperiods = (accesstime - chest.getLastRestockTime())
                        / Integer.parseInt(chest.getPeriod());
            }
            missedperiods = (accesstime - chest.getLastRestockTime())
                    / Integer.parseInt(chest.getPeriod());
            chest.setRestockTime(accesstime);
        } else if (chest.getPeriodMode().equalsIgnoreCase("settime")) {
            chest.setRestockTime((((accesstime - chest.getLastRestockTime())
                    / Integer.parseInt(chest.getPeriod()))
                    * Integer.parseInt(chest.getPeriod()))
                    + chest.getLastRestockTime());
        }

        ItemStack[] oldchestcontents = chest.getChest().getInventory().getContents();
        
        if (chest.getRestockMode().equalsIgnoreCase("replace")) {
            chest.getChest().getInventory().clear();
        }

        if (chest.getRestockMode().equalsIgnoreCase("add")) {
            for (int i = 0; i < missedperiods; i++) {
                chest.restock();
            }
        } else {
            chest.restock();
        }

        plugin.getServer().getPluginManager().callEvent(new PlayerInventoryEvent(event.getPlayer(), chest.getChest().getInventory()));

        chest.getChest().getInventory().setContents(oldchestcontents);

        if (missedperiods != 0) {
            timesrestockedforplayer++;
            chest.setPlayerRestockCount(event.getPlayer().getName(), timesrestockedforplayer);
        }
        plugin.saveConfigs();
    }
}
