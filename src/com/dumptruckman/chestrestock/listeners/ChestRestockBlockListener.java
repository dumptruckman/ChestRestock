/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dumptruckman.chestrestock.listeners;

import com.dumptruckman.chestrestock.ChestData;
import com.dumptruckman.chestrestock.ChestRestock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockListener;

/**
 *
 * @author dumptruckman
 */
public class ChestRestockBlockListener extends BlockListener {

    ChestRestock plugin;

    public ChestRestockBlockListener(ChestRestock plugin) {
        this.plugin = plugin;
    }

    @Override
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
            plugin.config.save();
        }
    }
}
