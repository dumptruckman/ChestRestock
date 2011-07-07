package com.dumptruckman.chestrestock.listeners;

import com.dumptruckman.chestrestock.ChestData;
import com.dumptruckman.chestrestock.ChestRestock;
import java.util.Date;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author dumptruckman
 */
public class ChestRestockPlayerListener extends PlayerListener {

    ChestRestock plugin;

    public ChestRestockPlayerListener(ChestRestock plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        super.onPlayerInteract(event);

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

        Long accesstime = new Date().getTime() / 1000;
        if (accesstime < (chest.getLastRestockTime() +
                Integer.parseInt(chest.getPeriod()))) {
            return;
        }

        int missedperiods = 1;
        if (chest.getPeriodMode().equalsIgnoreCase("player")) {
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

        if (chest.getRestockMode().equalsIgnoreCase("replace")) {
            chest.getChest().getInventory().clear();
        }


        for (int i = 0; i < missedperiods; i++) {
            chest.restock();
        }
        plugin.config.save();
    }
}
