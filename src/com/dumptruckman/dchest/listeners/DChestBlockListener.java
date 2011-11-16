/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dumptruckman.dchest.listeners;

import com.dumptruckman.dchest.ChestData;
import com.dumptruckman.dchest.DChest;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockListener;

/**
 *
 * @author dumptruckman
 */
public class DChestBlockListener extends BlockListener {

    DChest plugin;

    public DChestBlockListener(DChest plugin) {
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
        if (!event.getPlayer().isOp()||plugin.hasPerm(event.getPlayer(),"dChest.break")) {
            event.setCancelled(chest.isIndestructible());
        } else {
            event.setCancelled(false);
        }

        if (!event.isCancelled()) {
            chest.disable();
            
            plugin.saveConfigFile();
        }
    }

    @Override
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
            plugin.saveConfigFile();
        }
    }

    @Override
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
        if (!DChest.hasPerm(event.getPlayer(),"dChest.break")){
            event.setCancelled(chest.isIndestructible());
        }
        else {
            event.setCancelled(false);
        }
        if (!event.isCancelled()) {
            chest.disable();
            plugin.saveConfigFile();
        }
    }

    @Override
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
            plugin.saveConfigFile();
        }
    }
}
