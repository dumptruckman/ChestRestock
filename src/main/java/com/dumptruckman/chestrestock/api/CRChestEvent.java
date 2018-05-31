package com.dumptruckman.chestrestock.api;

import org.bukkit.event.Event;

/**
 * ChestRestock base event for events related to a restockable chest.
 */
public abstract class CRChestEvent extends Event {

    private final CRChest chest;

    protected CRChestEvent(CRChest chest) {
        this.chest = chest;
    }

    /**
     * Returns the {@link CRChest} assocated with this event.
     *
     * @return the {@link CRChest} assocated with this event.
     */
    public CRChest getChest() {
        return chest;
    }
}
