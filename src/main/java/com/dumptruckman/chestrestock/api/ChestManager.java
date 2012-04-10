package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.BlockLocation;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.util.Set;

public interface ChestManager {
    
    CRChest getChest(Block block, InventoryHolder holder);
    
    Block getTargetedInventoryHolder(Player player) throws IllegalStateException;
    
    CRChest newChest(Block block, InventoryHolder holder);

    Chest getOtherSide(Block chestBlock);
    
    boolean removeChest(BlockLocation location);

    boolean pollingCheckIn(CRChest chest);

    Set<CRChest> getChestsForPolling();

    void pollChests();
}
