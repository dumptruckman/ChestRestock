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

    /**
     * Creates a new CRChest with hard coded defaults and does not persist the chest or update it properly.
     * @deprecated as of release 2.3.  Use {@link #createChest(org.bukkit.block.Block)} instead.
     *
     * @param block The block to set up with ChestRestock.  Must be an InventoryHolder.
     * @param holder The InventoryHolder associated with the block.
     * @return The newly created instance of a CRChest.
     */
    @Deprecated
    CRChest newChest(Block block, InventoryHolder holder);

    /**
     * Creates a new CRChest at the block specified, which must be an InventoryHolder.  This enables the CRChest
     * with all of the defaults for the world the block is in.
     *
     * @param block The block where the CRChest will be.  Must be an InventoryHolder.
     * @return The newly created CRChest.
     */
    CRChest createChest(Block block);

    /**
     * Creates a new CRChest at the block specified, which must be an InventoryHolder.  This enables the CRChest
     * with all of the defaults for the world the block is in.
     * @deprecated as of release 2.3.  Use {@link #createChest(org.bukkit.block.Block)} instead.
     *
     * @param block The block where the CRChest will be.  Must be an InventoryHolder.
     * @param holder The InventoryHolder associated with the block.
     * @return The newly created CRChest.
     */
    @Deprecated
    CRChest createChest(Block block, InventoryHolder holder);

    /**
     * Grabs the other side of a double chest, if the chestBlock IS a double chest.
     *
     * @param chestBlock The chest to check for the other side of.
     * @return The Chest that is the other side of the double chest represented by chestBlock.
     * If chestBlock is not a double chest, this returns null.
     */
    Chest getOtherSide(Block chestBlock);

    /**
     * Disables a CRChest.  This turns it back into a regular ol' InventoryHolder.  This also deletes the file
     * for the chest.
     *
     * @param location The {@link BlockLocation} of the chest to remove.
     * @return True if the chest was deleted or false if none was found to delete.
     */
    boolean removeChest(BlockLocation location);

    /**
     * Adds the CRChest to the set of chests to poll at a regular interval for restocking IF the chest is set to
     * {@link CRChestOptions#ACCEPT_POLL}.  If it is not set to {@link CRChestOptions#ACCEPT_POLL} it will be removed
     * from the polling set.
     *
     * @param chest The CRChest to check in.
     * @return true if chest was added to polling set, false if removed.
     */
    boolean pollingCheckIn(CRChest chest);

    /**
     * Loads all chests from persistence for all loaded worlds and caches them in memory.
     * This is done on startup to make sure all chests set to {@link CRChestOptions#ACCEPT_POLL} are checked in.
     */
    void cacheAllChests();

    /**
     * Loads all chests from persistence for the specified world and caches them in memory.
     *
     * @param worldName The name of world to load chests for.
     */
    void cacheChests(String worldName);

    /**
     * @return The number of chests in the polling set.
     */
    int getNumberChestsPolled();

    /**
     * @return The number of chests cached in memory.
     */
    int getNumberCachedChests();

    /**
     * @deprecated as of release 2.3.  Not intended for public use.
     *
     * @return The set of chests for polling.
     */
    @Deprecated
    Set<CRChest> getChestsForPolling();

    /**
     * Iterates through all chests set to {@link CRChestOptions#ACCEPT_POLL} and restocks them
     * if they are ready to be restocked.  This is performed automatically at an interval specified by
     * {@link CRConfig#RESTOCK_TASK}.
     */
    void pollChests();

    /**
     * Restocks all chests for a given world and given name.
     *
     * @param world The world to restock all chests for.  If null, restocks for ALL worlds.
     * @param name The name of chest to restock.  If null, all names are considered.
     * @return The number of chests that were restocked.
     */
    int restockAllChests(World world, String name);
}
