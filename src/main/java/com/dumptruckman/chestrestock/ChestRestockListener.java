package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.api.ChestManager;
import com.dumptruckman.chestrestock.util.Perms;
import com.dumptruckman.minecraft.pluginbase.locale.Messager;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public class ChestRestockListener implements Listener {

    private ChestRestockPlugin plugin;
    private Messager messager;
    private ChestManager chestManager;

    public ChestRestockListener(ChestRestockPlugin plugin) {
        this.plugin = plugin;
        this.messager = plugin.getMessager();
        this.chestManager = plugin.getChestManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void inventoryOpen(InventoryOpenEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Block block = event.getPlayer().getTargetBlock(null, 100);
        if (!(block.getState() instanceof InventoryHolder)) {
            return;
        }
        InventoryHolder holder = (InventoryHolder) block.getState();
        if (!event.getInventory().getHolder().equals(holder)) {
            Logging.finest("player opened inventory not related to target block");
            return;
        }
        CRChest rChest = chestManager.getChest(block, holder);
        if (rChest == null) {
            Logging.finest("chest not configured");
            return;
        }
        event.setCancelled(true);
        rChest.openInventory(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBurn(BlockBurnEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFade(BlockFadeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    
    private boolean chestBreak(Block block, Player player) {
        if (!(block.getState() instanceof InventoryHolder)) {
            return false;
        }
        InventoryHolder invHolder = (InventoryHolder) block.getState();
        CRChest rChest = chestManager.getChest(block, invHolder);
        if (rChest == null) {
            return false;
        }
        if (rChest.get(CRChest.INDESTRUCTIBLE)) {
            if (player != null) {
                if (Perms.CAN_BREAK.hasPermission(player)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
