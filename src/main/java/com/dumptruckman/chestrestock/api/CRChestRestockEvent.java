package com.dumptruckman.chestrestock.api;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Fired when a restockable chest is restocked by any means.
 */
public class CRChestRestockEvent extends CRChestEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Inventory inventory;
    private final ItemStack[] restockItems;
    private final LootTable lootTable;

    public CRChestRestockEvent(CRChest chest, Inventory inventory, ItemStack[] restockItems, LootTable lootTable) {
        super(chest);
        this.inventory = inventory;
        this.restockItems = restockItems;
        this.lootTable = lootTable;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * The inventory that is restocked.
     *
     * @return the inventory that is restocked.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * The base set of items used for restocking the chest.
     *
     * @return the base set of items to restock with.
     */
    public ItemStack[] getRestockItems() {
        return restockItems;
    }

    /**
     * The loot table used to generate random items for the chest, if any.
     *
     * @return The loot table used for restocking or null.
     */
    public LootTable getLootTable() {
        return lootTable;
    }
}
