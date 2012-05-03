package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.BlockLocation;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.util.Set;

public interface ChestManager {
    
    CRChest getChest(Block block, InventoryHolder holder);
    
    Block getTargetedInventoryHolder(Player player) throws IllegalStateException;

    @Deprecated
    CRChest newChest(Block block, InventoryHolder holder);

    CRChest createChest(Block block, InventoryHolder holder);

    Chest getOtherSide(Block chestBlock);
    
    boolean removeChest(BlockLocation location);

    boolean pollingCheckIn(CRChest chest);

    void cacheAllChests();

    void cacheChests(String worldName);

    int getNumberChestsPolled();

    int getNumberCachedChests();

    Set<CRChest> getChestsForPolling();

    void pollChests();

    int restockAllChests(World world, String name);
}
