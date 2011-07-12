package com.dumptruckman.dchest;

import org.bukkit.inventory.ItemStack;

/**
 *
 * @author dumptruckman
 */
public class ItemData extends ItemStack {

    private Integer slot;

    public ItemData(int type) {
        super(type);
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
    public Integer getSlot() {
        return slot;
    }
}
