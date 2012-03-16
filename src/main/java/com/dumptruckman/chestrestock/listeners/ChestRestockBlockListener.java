/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dumptruckman.chestrestock.listeners;

import com.dumptruckman.chestrestock.ChestData;
import com.dumptruckman.chestrestock.ChestRestock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;

/**
 *
 * @author dumptruckman
 */
public class ChestRestockBlockListener implements Listener {

    ChestRestock plugin;

    public ChestRestockBlockListener(ChestRestock plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        Block block = event.getBlock();
        if (block.getType() != Material.CHEST) {
            return;
        }

        ChestData chest = new ChestData(block, plugin);
        if (!chest.isInConfig()) {
            return;
        }

        if (!event.getPlayer().isOp()) {
            event.setCancelled(chest.isIndestructible());
        } else {
            event.setCancelled(false);
        }

        if (!event.isCancelled()) {
            chest.disable();

            plugin.saveConfig();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBurn(BlockBurnEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();
        if (block.getType() != Material.CHEST) {
            return;
        }

        ChestData chest = new ChestData(block, plugin);
        if (!chest.isInConfig()) {
            return;
        }

        event.setCancelled(chest.isIndestructible());

        if (!event.isCancelled()) {
            chest.disable();
            plugin.saveConfig();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();
        if (block.getType() != Material.CHEST) {
            return;
        }

        ChestData chest = new ChestData(block, plugin);
        if (!chest.isInConfig()) {
            return;
        }

        if (!event.getPlayer().isOp()) {
            event.setCancelled(chest.isIndestructible());
        } else {
            event.setCancelled(false);
        }
        if (!event.isCancelled()) {
            chest.disable();
            plugin.saveConfig();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFade(BlockFadeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();
        if (block.getType() != Material.CHEST) {
            return;
        }

        ChestData chest = new ChestData(block, plugin);
        if (!chest.isInConfig()) {
            return;
        }

        event.setCancelled(chest.isIndestructible());

        if (!event.isCancelled()) {
            chest.disable();
            plugin.saveConfig();
        }
    }
}
