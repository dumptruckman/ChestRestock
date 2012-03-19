/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.ChestManager;
import com.dumptruckman.chestrestock.api.RestockableChest;
import com.dumptruckman.minecraft.pluginbase.locale.Messager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;

public class ChestRestockListener implements Listener {

    private ChestRestockPlugin plugin;
    private Messager messager;
    private ChestManager chestManager;

    public ChestRestockListener(ChestRestockPlugin plugin) {
        this.plugin = plugin;
        this.messager = plugin.getMessager();
        this.chestManager = plugin.getChestManager();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), null)) {
            event.setCancelled(true);
        }
    }
    
    private boolean chestBreak(Block block, Player player) {
        if (block.getType() != Material.CHEST) {
            return false;
        }
        Chest chest = (Chest) block.getState();
        RestockableChest rChest = chestManager.getChest(chest);
        if (rChest == null) {
            return false;
        }
        if (rChest.get(RestockableChest.INDESTRUCTIBLE)) {
            return true;
        } else {
            return false;
        }
    }
}
