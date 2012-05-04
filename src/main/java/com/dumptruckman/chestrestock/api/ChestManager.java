package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.BlockLocation;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.util.Set;

public interface ChestManager {

    /**
     * Finds an initialized CRChest at the location of the block given.  If the block given is a double chest,
     * it will check to see if the other side of the chest is the one that was used for persistence and return
     * the CRChest associated with that instead.  If ChestRestock has not initialized an inventory holder here
     * this will return null.
     * @deprecated as of release 2.3.  Use {@link #getChest(org.bukkit.block.Block)} instead.
     *
     * @param block The block to check to see if it's set up with ChestRestock.  Must be an InventoryHolder.
     * @param holder The InventoryHolder that represents the block.
     * @return The CRChest represented by the block or null if none configured.
     */
    @Deprecated
    CRChest getChest(Block block, InventoryHolder holder);

    /**
     * Finds an initialized CRChest at the location of the block given.  If the block given is a double chest,
     * it will check to see if the other side of the chest is the one that was used for persistence and return
     * the CRChest associated with that instead.  If ChestRestock has not initialized an inventory holder here
     * this will return null.
     *
     * @param block The block to check to see if it's set up with ChestRestock.  Must be an InventoryHolder.
     * @return The CRChest represented by the block or null if none configured.
     */
    CRChest getChest(Block block);

    /**
     * Returns the block the player is targeting if it is an InventoryHolder otherwise, throws IllegalStateException.
     * @deprecated as of release 2.3.  Use {@link ChestRestock#getTargetedInventoryHolder(org.bukkit.entity.Player)} instead.
     *
     * @param player Player to check target of.
     * @return The block the player is targeting that is an InventoryHolder.
     * @throws IllegalStateException If the targeted block is not an InventoryHolder or the player is not targeting a block.
     */
    @Deprecated
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
