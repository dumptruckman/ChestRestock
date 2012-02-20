package com.dumptruckman.chestrestock;

import org.bukkit.inventory.ItemStack;

/**
 *
 * @author dumptruckman
 */
public class ChestItem extends ItemStack {

    private Integer slot;

    public ChestItem(int type) {
        super(type);
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
    public Integer getSlot() {
        return slot;
    }
}
