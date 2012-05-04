package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.api.CRDefaults;
import com.dumptruckman.chestrestock.api.ChestManager;
import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.util.Perms;
import com.dumptruckman.minecraft.pluginbase.locale.Messager;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.RedstoneTorch;

import java.util.List;

public class ChestRestockListener implements Listener {

    private ChestRestock plugin;
    private Messager messager;
    private ChestManager chestManager;

    public ChestRestockListener(ChestRestock plugin) {
        this.plugin = plugin;
        this.messager = plugin.getMessager();
        this.chestManager = plugin.getChestManager();
    }

    @EventHandler
    public void redstonePower(BlockRedstoneEvent event) {
        if (event.getNewCurrent() <= 0) {
            return;
        }
        boolean torch = false;
        if (event.getBlock().getState().getData() instanceof RedstoneTorch) {
            torch = true;
        }
        Block block = getNearbyInventoryHolder(event.getBlock(), torch);
        if (block == null) {
            return;
        }
        InventoryHolder holder = (InventoryHolder) block.getState();
        CRChest rChest = chestManager.getChest(block);
        if (rChest == null) {
            Logging.finest("chest not configured");
            return;
        }
        if (rChest.get(CRChest.REDSTONE)) {
            // This will force a restock of physical and virtual inventories.
            Logging.finer("Chest restocking due to redstone power!");
            rChest.restockAllInventories();
        } else {
            Logging.finer("Checking if chest needs restocking due to redstone power!");
            // This will restock the physical inventory if it has been an appropriate amount of time.
            rChest.openInventory(null);
        }
    }

    private Block getNearbyInventoryHolder(Block block, boolean torch) {
        Block nearBlock = block.getRelative(BlockFace.EAST);
        if (nearBlock.getState() instanceof InventoryHolder) {
            return nearBlock;
        }
        nearBlock = block.getRelative(BlockFace.NORTH);
        if (nearBlock.getState() instanceof InventoryHolder) {
            return nearBlock;
        }
        nearBlock = block.getRelative(BlockFace.SOUTH);
        if (nearBlock.getState() instanceof InventoryHolder) {
            return nearBlock;
        }
        nearBlock = block.getRelative(BlockFace.WEST);
        if (nearBlock.getState() instanceof InventoryHolder) {
            return nearBlock;
        }
        nearBlock = block.getRelative(BlockFace.UP);
        if (nearBlock.getState() instanceof InventoryHolder) {
            return nearBlock;
        }
        if (torch) {
            nearBlock = nearBlock.getRelative(BlockFace.UP);
            if (nearBlock.getState() instanceof InventoryHolder) {
                return nearBlock;
            }
        }
        return null;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void inventoryOpen(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = event.getClickedBlock();
        if (!(block.getState() instanceof InventoryHolder)) {
            return;
        }
        CRChest rChest = chestManager.getChest(block);
        if (rChest == null) {
            if (plugin.getDefaults(block.getWorld().getName()).get(CRDefaults.AUTO_CREATE)) {
                rChest = chestManager.createChest(block);
            }
            if (rChest == null) {
                Logging.finest("chest not configured");
                return;
            }
        }
        event.setCancelled(true);
        rChest.openInventory(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockBurn(BlockBurnEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockIgnite(BlockIgniteEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockFade(BlockFadeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void pistonExtend(BlockPistonExtendEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void pistonRetract(BlockPistonRetractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void entityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        List<Block> blocks = event.blockList();
        for (Block block : blocks) {
            if (chestBreak(block, null)) {
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void entityChangeBlock(EntityChangeBlockEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockDamage(BlockDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (chestBreak(event.getBlock(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockBreak(BlockBreakEvent event) {
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
        CRChest rChest = chestManager.getChest(block);
        if (rChest == null) {
            return false;
        }
        if (rChest.get(CRChest.INDESTRUCTIBLE)) {
            if (player != null) {
                if (!rChest.get(CRChest.NAME).isEmpty()) {
                    if (Perms.CAN_BREAK.specific(rChest.get(CRChest.NAME)).hasPermission(player)) {
                        return false;
                    }
                } else {
                    if (Perms.CAN_BREAK_ANY.hasPermission(player)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
