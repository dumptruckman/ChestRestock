/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dumptruckman.chestrestock;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;

public class ChestRestockListener implements Listener {

    ChestRestockPlugin plugin;

    public ChestRestockListener(ChestRestockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        Block block = event.getBlock();
        if (block.getType() != Material.CHEST) {
            return;
        }

    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();
        if (block.getType() != Material.CHEST) {
            return;
        }

    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();
        if (block.getType() != Material.CHEST) {
            return;
        }

    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();
        if (block.getType() != Material.CHEST) {
            return;
        }
    }
}
