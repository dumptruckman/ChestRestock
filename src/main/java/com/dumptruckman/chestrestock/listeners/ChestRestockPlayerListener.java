package com.dumptruckman.chestrestock.listeners;

import com.dumptruckman.chestrestock.ChestData;
import com.dumptruckman.chestrestock.ChestRestock;
import java.util.Date;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author dumptruckman
 */
public class ChestRestockPlayerListener implements Listener {

    ChestRestock plugin;

    public ChestRestockPlayerListener(ChestRestock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if (event.getClickedBlock().getType() != Material.CHEST) {
            return;
        }

        ChestData chest = new ChestData(event.getClickedBlock(), plugin);
        if (!chest.isInConfig()) {
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

        Long accesstime = new Date().getTime() / 1000;
        if (accesstime < (chest.getLastRestockTime() +
                Integer.parseInt(chest.getPeriod()))) {
            return;
        }

        event.setCancelled(true);

        int missedperiods = 1;
        if (chest.getPeriodMode() != null && chest.getPeriodMode().equalsIgnoreCase("player")) {
            missedperiods = (int)Math.floor((new Long(accesstime).doubleValue()
                    - new Long(chest.getLastRestockTime()).doubleValue())
                    / Integer.parseInt(chest.getPeriod()));
            chest.setRestockTime(accesstime);
        } else if (chest.getPeriodMode().equalsIgnoreCase("settime")) {
            chest.setRestockTime(new Double(Math.floor((
                    new Long(accesstime).doubleValue()
                    - new Long(chest.getLastRestockTime()).doubleValue())
                    / Integer.parseInt(chest.getPeriod()))
                    * Integer.parseInt(chest.getPeriod())).longValue()
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
        plugin.saveConfig();
    }

    //@Override
    //public void onInventoryOpen(PlayerInventoryEvent event) {
    //    super.onInventoryOpen(event);
    //
    //}
}
