package com.dumptruckman.chestrestock.listeners;

import com.dumptruckman.chestrestock.ChestData;
import com.dumptruckman.chestrestock.ChestRestock;
import java.util.Date;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

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


        Integer restockedforplayer = chest.getPlayerRestockCount(event.getPlayer().getName());

        if (restockedforplayer != null) {
            if (chest.getPlayerLimit() != -1) {
                if (restockedforplayer >= chest.getPlayerLimit()) {
                    if (!event.getPlayer().isOp()) {
                        return;
                    }
                }
            }
        } else {
            restockedforplayer = 0;
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


        if (chest.getRestockMode().equalsIgnoreCase("add")) {
            for (int i = 0; i < missedperiods; i++) {
                chest.restock();
            }
        } else {
            chest.restock();
        }
        if (missedperiods != 0) {
            restockedforplayer++;
            chest.setPlayerRestockCount(event.getPlayer().getName(), restockedforplayer);
        }
        plugin.config.save();
    }
}
