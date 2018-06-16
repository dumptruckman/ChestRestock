package com.dumptruckman.chestrestock.api;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

/**
 * Fired when a restockable chest is opened by an entity.
 */
public class CRChestOpenEvent extends CRChestEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Inventory inventory;
    private final HumanEntity opener;
    private final boolean causedRestock;

    public CRChestOpenEvent(CRChest chest, Inventory inventory, HumanEntity opener, boolean causedRestock) {
        super(chest);
        this.inventory = inventory;
        this.opener = opener;
        this.causedRestock = causedRestock;
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
     * The entity that opened the chest, if any.
     *
     * @return the opening entity or null.
     */
    public HumanEntity getOpener() {
        return opener;
    }

    /**
     * Whether or not this opening of the chest caused a restock to occur.
     *
     * @return true if a restock occurred because of this opening.
     */
    public boolean causedRestock() {
        return causedRestock;
    }
}
