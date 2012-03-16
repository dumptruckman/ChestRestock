/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.ChestManager;
import com.dumptruckman.chestrestock.api.RestockableChest;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import com.dumptruckman.minecraft.pluginbase.locale.Messager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.SignChangeEvent;

public class ChestRestockListener implements Listener {

    private ChestRestockPlugin plugin;
    private Messager messager;
    private ChestManager chestManager;

    public ChestRestockListener(ChestRestockPlugin plugin) {
        this.plugin = plugin;
        this.messager = plugin.getMessager();
        this.chestManager = plugin.getChestManager();
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void newSign(SignChangeEvent event) {
        boolean found = false;
        for (String line : event.getLines()) {
            if (line.equalsIgnoreCase("[restock]")) {
                found = true;
                break;
            }
        }
        if (!found) {
            return;
        }
        org.bukkit.material.Sign signMat = (org.bukkit.material.Sign) event.getBlock().getState().getData();
        if (signMat.getAttachedFace() == BlockFace.UP || signMat.getAttachedFace() == BlockFace.DOWN) {
            messager.bad(Language.SIGN_NOT_ON_CHEST, event.getPlayer());
            event.setCancelled(true);
            return;
        }
        Block chestBlock = event.getBlock().getRelative(signMat.getAttachedFace());
        if (chestBlock.getType() != Material.CHEST) {
            messager.bad(Language.SIGN_NOT_ON_CHEST, event.getPlayer());
            event.setCancelled(true);
            return;
        }
        Chest chest = (Chest) chestBlock.getState();
        RestockableChest rChest = chestManager.getChest(chest);
        if (rChest == null) {
            if (!Perms.CAN_CREATE.hasPermission(event.getPlayer())) {
                messager.bad(Language.NO_CREATE_PERMISSION, event.getPlayer());
                event.setCancelled(true);
                return;
            }
            rChest = chestManager.newChest(chest, event.getPlayer());
        } else {
            if (!Perms.CAN_EDIT.hasPermission(event.getPlayer())) {
                messager.bad(Language.NO_EDIT_PERMISSION, event.getPlayer());
                event.setCancelled(true);
                return;
            }
        }
        rChest.getMenu().getViews().add(chestManager.newSignView((Sign) event.getBlock().getState()));
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

        }
    }
}
